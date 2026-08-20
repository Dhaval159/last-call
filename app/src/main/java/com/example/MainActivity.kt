package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thelastcall.audio.SoundManager
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.*
import com.example.thelastcall.ui.screens.*
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.UnresolvedTheme

class MainActivity : ComponentActivity() {

    private lateinit var soundManager: SoundManager
    private lateinit var repository: CaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Validate all registered cases
        val validationReports = CaseValidator.validateAllRegisteredCases()
        validationReports.forEach { report ->
            if (!report.isValid) {
                Log.e("Unresolved", "Case validation failed for ${report.caseId}: ${report.errors}")
            } else {
                Log.d("Unresolved", "Case ${report.caseId} validated successfully.")
            }
        }

        soundManager = SoundManager(applicationContext)
        repository = CaseRepository(applicationContext, soundManager)

        setContent {
            UnresolvedTheme {
                val state by repository.state.collectAsStateWithLifecycle()
                val caseDef = repository.caseDefinition
                var showSettingsDialog by remember { mutableStateOf(false) }

                BackHandler(enabled = state.currentScreen != Screen.MAIN_MENU) {
                    repository.handleAndroidBack()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundDark)
                ) {
                    when (state.currentScreen) {
                        Screen.MAIN_MENU -> {
                            MainMenuScreen(
                                state = state,
                                caseDef = caseDef,
                                registeredCases = CaseRegistry.getAllCases(),
                                getCaseState = { repository.getCaseState(it) },
                                onSelectCase = { caseId -> repository.loadCase(caseId) },
                                onNewCase = { repository.startNewCase() },
                                onContinue = { repository.openCaseHub() },
                                onOpenSettings = { showSettingsDialog = true },
                                onResetCase = { caseId -> repository.resetCase(caseId) }
                            )
                        }

                        Screen.CASE_INTRO -> {
                            CaseIntroScreen(
                                caseDef = caseDef,
                                onProceedToBriefing = { repository.enterBriefing() },
                                onSkip = { repository.enterBriefing() }
                            )
                        }

                        Screen.BRIEFING -> {
                            CaseBriefingScreen(
                                caseDef = caseDef,
                                onEnterScene = { repository.openCaseHub() },
                                onBack = { repository.navigateTo(Screen.MAIN_MENU) }
                            )
                        }

                        Screen.CASE_HUB -> {
                            CaseHubScreen(
                                state = state,
                                caseDef = caseDef,
                                onEnterCrimeScene = { repository.enterCrimeScene() },
                                onOpenCaseFile = { tab ->
                                    repository.openCaseFile(tab)
                                },
                                onOpenDetectiveBoard = { repository.openDetectiveBoard() },
                                onOpenCaseReview = { repository.openCaseReview() },
                                onOpenCommunications = { repository.openCommunications() },
                                onFollowLead = { objective ->
                                    when (objective.leadTarget) {
                                        Screen.CRIME_SCENE -> repository.enterCrimeScene()
                                        Screen.DETECTIVE_BOARD -> repository.openDetectiveBoard()
                                        Screen.FINAL_CASE_REVIEW -> repository.openCaseReview()
                                        Screen.SUSPECT_INTERVIEW -> {
                                            caseDef.suspects.firstOrNull { s ->
                                                !state.interviewedSuspectIds.contains(s.id)
                                            }?.let { repository.selectSuspectForInterview(it.id) }
                                                ?: repository.openCaseFile(CaseFileTab.SUSPECTS)
                                        }
                                        else -> repository.openCaseFile(objective.focusTab ?: CaseFileTab.EVIDENCE)
                                    }
                                },
                                onOpenInvestigationLead = { leadId ->
                                    repository.openLeadInvestigation(leadId)
                                },
                                onNotificationAction = { repository.handleNotificationAction() },
                                onOpenSettings = { showSettingsDialog = true },
                                onDismissNotification = {
                                    repository.dismissNotification()
                                },
                                onBack = { repository.navigateTo(Screen.MAIN_MENU) }
                            )
                        }

                        Screen.INVESTIGATION_LEAD -> {
                            LeadInvestigationScreen(
                                state = state,
                                caseDef = caseDef,
                                onBackToHub = { repository.openCaseHub() },
                                onSelectLead = { leadId -> repository.startLead(leadId) },
                                onFollowObjective = { objective, leadId ->
                                    repository.followLeadObjective(objective, leadId)
                                },
                                onCompleteLead = { leadId ->
                                    repository.completeLead(leadId)
                                }
                            )
                        }

                        Screen.CRIME_SCENE -> {
                            CrimeSceneScreen(
                                state = state,
                                caseDef = caseDef,
                                onInspectHotspot = { hotspot ->
                                    repository.inspectHotspot(hotspot)
                                },
                                onOpenCaseFile = { tab ->
                                    repository.openCaseFile(tab)
                                },
                                onViewEvidenceDetail = { evidenceId ->
                                    repository.selectEvidenceForDetail(evidenceId)
                                },
                                onDismissNotification = {
                                    repository.dismissNotification()
                                },
                                onDismissTutorial = {
                                    repository.dismissTutorial()
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseHub()
                                    }
                                }
                            )
                        }

                        Screen.CASE_FILE -> {
                            CaseFileScreen(
                                state = state,
                                caseDef = caseDef,
                                onTabSelected = { tab ->
                                    repository.setCaseFileTab(tab)
                                },
                                onSelectSuspect = { suspectId ->
                                    repository.selectSuspectForInterview(suspectId)
                                },
                                onOpenPersonProfile = { suspectId ->
                                    repository.openPersonProfile(suspectId)
                                },
                                onSelectEvidence = { evidenceId ->
                                    repository.selectEvidenceForDetail(evidenceId)
                                },
                                onNavigateToAccusation = {
                                    repository.openCaseReview()
                                },
                                onCompareStatement = { statementId ->
                                    repository.selectStatementForReasoning(statementId)
                                },
                                onTestContradiction = { s1, s2 ->
                                    repository.testContradiction(s1, s2)
                                },
                                onTestDeduction = { s1, s2, rel ->
                                    repository.testDeduction(s1, s2, rel)
                                },
                                onSelectSuspectInTheory = { sId ->
                                    repository.updateTheorySuspect(sId)
                                },
                                onSelectMotiveInTheory = { mKey ->
                                    repository.updateTheoryMotive(mKey)
                                },
                                onSelectWeaponInTheory = { wKey ->
                                    repository.updateTheoryWeapon(wKey)
                                },
                                onToggleTheoryEvidence = { eId ->
                                    repository.toggleTheorySupportingEvidence(eId)
                                },
                                onAddNote = { text ->
                                    repository.addPlayerNote(text)
                                },
                                onDeleteNote = { noteId ->
                                    repository.deletePlayerNote(noteId)
                                },
                                onAddConnection = { s1, s2, rel ->
                                    repository.addBoardConnection(s1, s2, rel)
                                },
                                onRemoveConnection = { connectionId ->
                                    repository.removeBoardConnection(connectionId)
                                },
                                onFollowLead = { objective ->
                                    when (objective.leadTarget) {
                                        Screen.CRIME_SCENE -> repository.enterCrimeScene()
                                        Screen.DETECTIVE_BOARD -> repository.openDetectiveBoard()
                                        Screen.FINAL_CASE_REVIEW -> repository.openCaseReview()
                                        Screen.SUSPECT_INTERVIEW -> {
                                            caseDef.suspects.firstOrNull { s ->
                                                !state.interviewedSuspectIds.contains(s.id)
                                            }?.let { repository.selectSuspectForInterview(it.id) }
                                                ?: repository.openCaseFile(CaseFileTab.SUSPECTS)
                                        }
                                        else -> repository.openCaseFile(objective.focusTab ?: CaseFileTab.EVIDENCE)
                                    }
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseHub()
                                    }
                                }
                            )
                        }

                        Screen.DETECTIVE_BOARD -> {
                            DetectiveBoardScreen(
                                state = state,
                                caseDef = caseDef,
                                onOpenCaseFile = { tab ->
                                    repository.openCaseFile(tab)
                                },
                                onSelectEvidence = { evidenceId ->
                                    repository.selectEvidenceForDetail(evidenceId)
                                },
                                onSelectSuspect = { suspectId ->
                                    repository.selectSuspectForInterview(suspectId)
                                },
                                onOpenPersonProfile = { suspectId ->
                                    repository.openPersonProfile(suspectId)
                                },
                                onAddConnection = { s1, s2, rel ->
                                    repository.addBoardConnection(s1, s2, rel)
                                },
                                onRemoveConnection = { connectionId ->
                                    repository.removeBoardConnection(connectionId)
                                },
                                onTestContradiction = { s1, s2 ->
                                    repository.testContradiction(s1, s2)
                                },
                                onTestDeduction = { s1, s2, rel ->
                                    repository.testDeduction(s1, s2, rel)
                                },
                                onDismissNotification = {
                                    repository.dismissNotification()
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseHub()
                                    }
                                }
                            )
                        }

                        Screen.SUSPECT_INTERVIEW -> {
                            val suspectId = state.selectedSuspectId ?: caseDef.suspects.firstOrNull()?.id ?: "S001"
                            SuspectInterviewScreen(
                                suspectId = suspectId,
                                state = state,
                                caseDef = caseDef,
                                onAskQuestion = { question ->
                                    repository.askQuestion(question)
                                },
                                onPresentEvidence = { sId, eId ->
                                    repository.presentEvidence(sId, eId)
                                },
                                onAttemptChallenge = { optionKey ->
                                    repository.attemptContradictionChallenge(optionKey)
                                },
                                onDismissChallenge = {
                                    repository.dismissChallenge()
                                },
                                onOpenCaseFile = {
                                    repository.openCaseFile(CaseFileTab.SUSPECTS)
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseFile(CaseFileTab.SUSPECTS)
                                    }
                                }
                            )
                        }

                        Screen.PERSON_PROFILE -> {
                            val suspectId = state.selectedSuspectId ?: caseDef.suspects.firstOrNull()?.id ?: "S001"
                            PersonProfileScreen(
                                suspectId = suspectId,
                                state = state,
                                caseDef = caseDef,
                                onInterview = { repository.selectSuspectForInterview(it) },
                                onOpenEvidence = { evidenceId ->
                                    repository.selectEvidenceForDetail(evidenceId)
                                },
                                onOpenTimeline = {
                                    repository.openCaseFile(CaseFileTab.TIMELINE)
                                },
                                onOpenStatements = {
                                    repository.openCaseFile(CaseFileTab.STATEMENTS)
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseFile(CaseFileTab.SUSPECTS)
                                    }
                                }
                            )
                        }

                        Screen.COMMUNICATIONS -> {
                            CommunicationsScreen(
                                state = state,
                                caseDef = caseDef,
                                onOpenEvidence = { evidenceId ->
                                    repository.selectEvidenceForDetail(evidenceId)
                                },
                                onOpenContact = { suspectId ->
                                    repository.openPersonProfile(suspectId)
                                },
                                onOpenCaseFile = {
                                    repository.openCaseFile(CaseFileTab.EVIDENCE)
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseHub()
                                    }
                                }
                            )
                        }

                        Screen.EVIDENCE_DETAIL -> {
                            val evidenceId = state.selectedEvidenceId ?: caseDef.evidenceList.firstOrNull()?.id ?: "E001"
                            EvidenceDetailScreen(
                                evidenceId = evidenceId,
                                caseDef = caseDef,
                                state = state,
                                onSelectRelatedEvidence = { relatedId ->
                                    repository.selectEvidenceForDetail(relatedId)
                                },
                                onSelectSuspect = { suspectId ->
                                    repository.selectSuspectForInterview(suspectId)
                                },
                                onBack = {
                                    if (state.leadNavigationContext != null) {
                                        repository.returnFromLeadContext()
                                    } else {
                                        repository.openCaseFile(CaseFileTab.EVIDENCE)
                                    }
                                }
                            )
                        }

                        Screen.FINAL_CASE_REVIEW -> {
                            FinalCaseReviewScreen(
                                state = state,
                                caseDef = caseDef,
                                onProceedToAccusation = {
                                    repository.navigateTo(Screen.FINAL_ACCUSATION)
                                },
                                onReturnToInvestigation = {
                                    repository.openCaseFile(CaseFileTab.THEORY)
                                },
                                onOpenTheory = {
                                    repository.openCaseFile(CaseFileTab.THEORY)
                                },
                                onBack = {
                                    repository.openCaseFile(CaseFileTab.THEORY)
                                }
                            )
                        }

                        Screen.FINAL_ACCUSATION -> {
                            FinalAccusationScreen(
                                state = state,
                                caseDef = caseDef,
                                onSubmitAccusation = { submission ->
                                    repository.submitAccusation(submission)
                                },
                                onDismissFeedback = {
                                    repository.clearAccusationFeedback()
                                },
                                onBack = {
                                    repository.navigateTo(Screen.FINAL_CASE_REVIEW)
                                }
                            )
                        }

                        Screen.CASE_RESULT -> {
                            CaseResultScreen(
                                state = state,
                                caseDef = caseDef,
                                onReviewCaseFile = {
                                    repository.openCaseFile(CaseFileTab.EVIDENCE)
                                },
                                onPlayAgain = {
                                    repository.resetCase()
                                    repository.startNewCase()
                                },
                                onReturnToMainMenu = {
                                    repository.navigateTo(Screen.MAIN_MENU)
                                }
                            )
                        }

                        Screen.SETTINGS -> {
                            showSettingsDialog = true
                        }
                    }

                    if (showSettingsDialog) {
                        SettingsDialog(
                            settings = state.settings,
                            caseDef = caseDef,
                            onUpdateSettings = { sound, haptics, speed, hints ->
                                repository.updateSettings(sound, haptics, speed, hints)
                            },
                            onResetCase = {
                                repository.resetCase()
                            },
                            onDismiss = {
                                showSettingsDialog = false
                            }
                        )
                    }

                    // Investigation Moment Dialog Overlay
                    state.pendingMomentId?.let { momentId ->
                        caseDef.getInvestigationMoment(momentId)?.let { moment ->
                            InvestigationMomentDialog(
                                moment = moment,
                                caseDef = caseDef,
                                onAction = { repository.handleMomentAction(it) },
                                onDismiss = { repository.dismissMoment() }
                            )
                        }
                    }

                    // Evidence Discovery Dialog Overlay
                    state.pendingEvidenceDiscoveryId?.let { evidenceId ->
                        caseDef.getEvidence(evidenceId)?.let { evidence ->
                            EvidenceDiscoveryDialog(
                                evidence = evidence,
                                onExamineInDetail = { repository.selectEvidenceForDetail(it) },
                                onDismiss = { repository.dismissEvidenceDiscovery() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}
