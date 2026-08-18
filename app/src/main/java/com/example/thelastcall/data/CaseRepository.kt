package com.example.thelastcall.data

import android.content.Context
import android.content.SharedPreferences
import com.example.thelastcall.audio.SoundManager
import com.example.thelastcall.engine.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject

class CaseRepository(
    private val context: Context,
    private val soundManager: SoundManager,
    initialCaseDefinition: CaseDefinition? = null
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("the_last_call_prefs", Context.MODE_PRIVATE)

    private var _caseDefinition: CaseDefinition = initialCaseDefinition ?: CaseRegistry.getDefaultCase()
    val caseDefinition: CaseDefinition get() = _caseDefinition

    private val _state = MutableStateFlow(loadInitialState())
    val state: StateFlow<CaseState> = _state.asStateFlow()

    init {
        // Initial auto-evaluation
        evaluateState(playSound = false)
    }

    private fun getSaveKey(caseId: String): String = "case_state_$caseId"

    private fun loadInitialState(): CaseState {
        val currentKey = getSaveKey(_caseDefinition.id)
        var savedJson = prefs.getString(currentKey, null)
        
        // Migration check for legacy save key
        if (savedJson.isNullOrEmpty() && (_caseDefinition.id == "CASE-001" || _caseDefinition.id == "CASE_001")) {
            savedJson = prefs.getString("case_state_v1", null)
        }

        val settings = loadSettings()
        if (savedJson.isNullOrEmpty()) {
            return CaseState(
                caseId = _caseDefinition.id,
                unlockedTimelineEventIds = _caseDefinition.initialUnlockedTimelineIds,
                settings = settings
            )
        }
        return try {
            val json = JSONObject(savedJson)
            val caseId = json.optString("caseId", _caseDefinition.id)
            val caseStatusStr = json.optString("caseStatus", CaseStatus.NOT_STARTED.name)
            val caseStatus = try { CaseStatus.valueOf(caseStatusStr) } catch (_: Exception) { CaseStatus.NOT_STARTED }
            val screenStr = json.optString("currentScreen", Screen.MAIN_MENU.name)
            val screen = try { Screen.valueOf(screenStr) } catch (_: Exception) { Screen.MAIN_MENU }
            val tabStr = json.optString("caseFileTab", CaseFileTab.EVIDENCE.name)
            val tab = try { CaseFileTab.valueOf(tabStr) } catch (_: Exception) { CaseFileTab.EVIDENCE }

            val suspectId = if (json.has("selectedSuspectId") && !json.isNull("selectedSuspectId")) json.optString("selectedSuspectId", "").takeIf { it.isNotEmpty() } else null
            val evidenceId = if (json.has("selectedEvidenceId") && !json.isNull("selectedEvidenceId")) json.optString("selectedEvidenceId", "").takeIf { it.isNotEmpty() } else null
            val statementId = if (json.has("selectedStatementId") && !json.isNull("selectedStatementId")) json.optString("selectedStatementId", "").takeIf { it.isNotEmpty() } else null

            // Load Player Theory
            val theoryObj = json.optJSONObject("playerTheory")
            val playerTheory = if (theoryObj != null) {
                PlayerTheory(
                    suspectId = theoryObj.optString("suspectId", "").takeIf { it.isNotEmpty() && it != "null" },
                    motiveKey = theoryObj.optString("motiveKey", "").takeIf { it.isNotEmpty() && it != "null" },
                    weaponKey = theoryObj.optString("weaponKey", "").takeIf { it.isNotEmpty() && it != "null" },
                    opportunityConfirmed = theoryObj.optBoolean("opportunityConfirmed", false),
                    supportingEvidenceIds = jsonArrayToSet(theoryObj.optJSONArray("supportingEvidenceIds")),
                    establishedDeductionIds = jsonArrayToSet(theoryObj.optJSONArray("establishedDeductionIds"))
                )
            } else {
                PlayerTheory()
            }

            val rawTimeline = jsonArrayToSet(json.optJSONArray("unlockedTimelineEventIds"))
            val unlockedTimeline = if (rawTimeline.isEmpty()) _caseDefinition.initialUnlockedTimelineIds else rawTimeline

            CaseState(
                caseId = caseId,
                caseStatus = caseStatus,
                currentScreen = screen,
                caseFileTab = tab,
                selectedSuspectId = suspectId?.takeIf { it.isNotEmpty() && it != "null" },
                selectedEvidenceId = evidenceId?.takeIf { it.isNotEmpty() && it != "null" },
                selectedStatementId = statementId?.takeIf { it.isNotEmpty() && it != "null" },
                discoveredEvidenceIds = jsonArrayToSet(json.optJSONArray("discoveredEvidenceIds")),
                inspectedEvidenceIds = jsonArrayToSet(json.optJSONArray("inspectedEvidenceIds")),
                interviewedSuspectIds = jsonArrayToSet(json.optJSONArray("interviewedSuspectIds")),
                askedQuestionIds = jsonArrayToSet(json.optJSONArray("askedQuestionIds")),
                recordedStatementIds = jsonArrayToSet(json.optJSONArray("recordedStatementIds")),
                clearedSuspectIds = jsonArrayToSet(json.optJSONArray("clearedSuspectIds")),
                presentedEvidenceRecords = jsonToMapOfSets(json.optJSONObject("presentedEvidenceRecords")),
                unlockedTimelineEventIds = unlockedTimeline,
                unlockedDeductionIds = jsonArrayToSet(json.optJSONArray("unlockedDeductionIds")),
                unlockedContradictionIds = jsonArrayToSet(json.optJSONArray("unlockedContradictionIds")),
                completedObjectiveIds = jsonArrayToSet(json.optJSONArray("completedObjectiveIds")),
                inspectedHotspotIds = jsonArrayToSet(json.optJSONArray("inspectedHotspotIds")),
                customConnections = loadCustomConnections(json),
                playerTheory = playerTheory,
                hasDiscoveredMotive = json.optBoolean("hasDiscoveredMotive", false),
                hasDiscoveredOpportunity = json.optBoolean("hasDiscoveredOpportunity", false),
                hasSeenCrimeSceneTutorial = json.optBoolean("hasSeenCrimeSceneTutorial", false),
                playerNotes = loadPlayerNotes(json),
                pendingChallengeId = json.optString("pendingChallengeId", "").takeIf { it.isNotEmpty() },
                activityLog = loadActivityLog(json),
                investigationMinutes = json.optInt("investigationMinutes", 0),
                settings = settings
            )
        } catch (_: Exception) {
            CaseState(
                caseId = _caseDefinition.id,
                unlockedTimelineEventIds = _caseDefinition.initialUnlockedTimelineIds,
                settings = settings
            )
        }
    }

    fun loadCase(caseId: String) {
        val newDef = CaseRegistry.getCase(caseId) ?: return
        _caseDefinition = newDef
        _state.value = loadInitialState()
        evaluateState(playSound = false)
    }

    fun getCaseState(caseId: String): CaseState {
        if (caseId == _caseDefinition.id) {
            return _state.value
        }
        val targetDef = CaseRegistry.getCase(caseId) ?: return CaseState(caseId = caseId)
        val currentKey = getSaveKey(caseId)
        var savedJson = prefs.getString(currentKey, null)
        if (savedJson.isNullOrEmpty() && (caseId == "CASE-001" || caseId == "CASE_001")) {
            savedJson = prefs.getString("case_state_v1", null)
        }
        val settings = _state.value.settings
        if (savedJson.isNullOrEmpty()) {
            return CaseState(
                caseId = targetDef.id,
                unlockedTimelineEventIds = targetDef.initialUnlockedTimelineIds,
                settings = settings
            )
        }
        return try {
            val json = JSONObject(savedJson)
            val savedCaseId = json.optString("caseId", targetDef.id)
            val caseStatusStr = json.optString("caseStatus", CaseStatus.NOT_STARTED.name)
            val caseStatus = try { CaseStatus.valueOf(caseStatusStr) } catch (_: Exception) { CaseStatus.NOT_STARTED }
            val screenStr = json.optString("currentScreen", Screen.MAIN_MENU.name)
            val screen = try { Screen.valueOf(screenStr) } catch (_: Exception) { Screen.MAIN_MENU }
            val tabStr = json.optString("caseFileTab", CaseFileTab.EVIDENCE.name)
            val tab = try { CaseFileTab.valueOf(tabStr) } catch (_: Exception) { CaseFileTab.EVIDENCE }

            val rawTimeline = jsonArrayToSet(json.optJSONArray("unlockedTimelineEventIds"))
            val unlockedTimeline = if (rawTimeline.isEmpty()) targetDef.initialUnlockedTimelineIds else rawTimeline

            CaseState(
                caseId = savedCaseId,
                caseStatus = caseStatus,
                currentScreen = screen,
                caseFileTab = tab,
                discoveredEvidenceIds = jsonArrayToSet(json.optJSONArray("discoveredEvidenceIds")),
                inspectedEvidenceIds = jsonArrayToSet(json.optJSONArray("inspectedEvidenceIds")),
                interviewedSuspectIds = jsonArrayToSet(json.optJSONArray("interviewedSuspectIds")),
                askedQuestionIds = jsonArrayToSet(json.optJSONArray("askedQuestionIds")),
                recordedStatementIds = jsonArrayToSet(json.optJSONArray("recordedStatementIds")),
                clearedSuspectIds = jsonArrayToSet(json.optJSONArray("clearedSuspectIds")),
                unlockedTimelineEventIds = unlockedTimeline,
                unlockedDeductionIds = jsonArrayToSet(json.optJSONArray("unlockedDeductionIds")),
                unlockedContradictionIds = jsonArrayToSet(json.optJSONArray("unlockedContradictionIds")),
                completedObjectiveIds = jsonArrayToSet(json.optJSONArray("completedObjectiveIds")),
                customConnections = loadCustomConnections(json),
                hasDiscoveredMotive = json.optBoolean("hasDiscoveredMotive", false),
                hasDiscoveredOpportunity = json.optBoolean("hasDiscoveredOpportunity", false),
                settings = settings
            )
        } catch (_: Exception) {
            CaseState(
                caseId = targetDef.id,
                unlockedTimelineEventIds = targetDef.initialUnlockedTimelineIds,
                settings = settings
            )
        }
    }

    fun resetCase(caseId: String = _caseDefinition.id) {
        prefs.edit().remove(getSaveKey(caseId)).apply()
        if (caseId == "CASE-001" || caseId == "CASE_001") {
            prefs.edit().remove("case_state_v1").apply()
        }
        if (_caseDefinition.id == caseId) {
            _state.value = CaseState(
                caseId = _caseDefinition.id,
                unlockedTimelineEventIds = _caseDefinition.initialUnlockedTimelineIds,
                settings = _state.value.settings
            )
            evaluateState(playSound = false)
        }
    }

    fun resetCurrentCase() {
        resetCase(_caseDefinition.id)
    }

    private fun loadSettings(): GameSettings {
        val sound = prefs.getBoolean("setting_sound", true)
        val haptics = prefs.getBoolean("setting_haptics", true)
        val speedStr = prefs.getString("setting_speed", TextSpeed.NORMAL.name) ?: TextSpeed.NORMAL.name
        val speed = try { TextSpeed.valueOf(speedStr) } catch (_: Exception) { TextSpeed.NORMAL }
        val hints = prefs.getBoolean("setting_hints", true)
        return GameSettings(sound, haptics, speed, hints)
    }

    fun updateSettings(sound: Boolean, haptics: Boolean, textSpeed: TextSpeed, hints: Boolean) {
        val newSettings = GameSettings(sound, haptics, textSpeed, hints)
        prefs.edit()
            .putBoolean("setting_sound", sound)
            .putBoolean("setting_haptics", haptics)
            .putString("setting_speed", textSpeed.name)
            .putBoolean("setting_hints", hints)
            .apply()
        _state.update { it.copy(settings = newSettings) }
    }

    fun startNewCase() {
        _state.update {
            CaseState(
                caseId = _caseDefinition.id,
                caseStatus = CaseStatus.IN_PROGRESS,
                currentScreen = Screen.CASE_INTRO,
                unlockedTimelineEventIds = _caseDefinition.initialUnlockedTimelineIds,
                settings = it.settings
            )
        }
        evaluateState(playSound = false)
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun enterBriefing() {
        _state.update { it.copy(currentScreen = Screen.BRIEFING) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun enterCrimeScene() {
        _state.update { it.copy(currentScreen = Screen.CRIME_SCENE) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun openCaseReview() {
        _state.update { it.copy(currentScreen = Screen.FINAL_CASE_REVIEW) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun dismissTutorial() {
        _state.update { it.copy(hasSeenCrimeSceneTutorial = true) }
        saveState()
    }

    fun navigateTo(screen: Screen) {
        _state.update { it.copy(currentScreen = screen) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun openCaseFile(tab: CaseFileTab = CaseFileTab.EVIDENCE) {
        _state.update { it.copy(currentScreen = Screen.CASE_FILE, caseFileTab = tab) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun openDetectiveBoard() {
        _state.update { it.copy(currentScreen = Screen.DETECTIVE_BOARD) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun setCaseFileTab(tab: CaseFileTab) {
        _state.update { it.copy(caseFileTab = tab) }
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun selectSuspectForInterview(suspectId: String) {
        _state.update {
            it.copy(
                selectedSuspectId = suspectId,
                currentScreen = Screen.SUSPECT_INTERVIEW,
                interviewedSuspectIds = it.interviewedSuspectIds + suspectId
            )
        }
        evaluateState(playSound = false)
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun selectEvidenceForDetail(evidenceId: String) {
        val extraDiscovered = InvestigationEngine.inspectEvidence(evidenceId, _state.value, _caseDefinition)

        _state.update {
            it.copy(
                selectedEvidenceId = evidenceId,
                currentScreen = Screen.EVIDENCE_DETAIL,
                inspectedEvidenceIds = it.inspectedEvidenceIds + evidenceId,
                discoveredEvidenceIds = it.discoveredEvidenceIds + extraDiscovered
            )
        }
        evaluateState(playSound = false)
        extraDiscovered.forEach { evidenceId ->
            _caseDefinition.getEvidence(evidenceId)?.let { evidence ->
                logActivity(ActivityKind.EVIDENCE, "${evidence.name} secured into Case File.")
            }
        }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun inspectHotspot(hotspot: CrimeSceneHotspot): Pair<EvidenceItem?, EvidenceItem?> {
        val result = InvestigationEngine.inspectHotspot(hotspot, _state.value, _caseDefinition)

        _state.update {
            it.copy(
                discoveredEvidenceIds = result.updatedDiscoveredEvidenceIds,
                inspectedHotspotIds = result.updatedInspectedHotspotIds,
                activeNotification = if (result.newlyDiscoveredEvidence.isNotEmpty()) {
                    GameNotification(
                        title = "New Evidence",
                        message = "${result.newlyDiscoveredEvidence.first().name} added to Case File.",
                        type = NotificationType.EVIDENCE
                    )
                } else it.activeNotification
            )
        }

        if (result.newlyDiscoveredEvidence.isNotEmpty()) {
            result.newlyDiscoveredEvidence.forEach { evidence ->
                logActivity(ActivityKind.EVIDENCE, "${evidence.name} secured at ${evidence.location}.")
            }
            soundManager.playEvidenceDiscovered(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        } else {
            soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        }

        evaluateState(playSound = false)
        saveState()
        return Pair(result.primary, result.secondary)
    }

    fun askQuestion(question: InterviewQuestion) {
        val result = InvestigationEngine.askQuestion(question, _state.value, _caseDefinition)

        _state.update {
            it.copy(
                askedQuestionIds = result.updatedAskedQuestionIds,
                recordedStatementIds = result.updatedRecordedStatementIds,
                discoveredEvidenceIds = result.updatedDiscoveredEvidenceIds
            )
        }

        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        if (result.newlyDiscoveredEvidenceIds.isNotEmpty()) {
            result.newlyDiscoveredEvidenceIds.forEach { evidenceId ->
                _caseDefinition.getEvidence(evidenceId)?.let { evidence ->
                    logActivity(ActivityKind.EVIDENCE, "${evidence.name} secured into Case File.")
                }
            }
        }
        if (result.updatedRecordedStatementIds.size > _state.value.recordedStatementIds.size) {
            logActivity(
                ActivityKind.INTERVIEW,
                "${_caseDefinition.getSuspect(question.suspectId)?.name ?: "Suspect"}'s formal statement recorded into case file."
            )
        }
        evaluateState(playSound = true)
        saveState()
    }

    fun presentEvidence(suspectId: String, evidenceId: String): EvidencePresentationOutcome {
        val result = InvestigationEngine.presentEvidence(suspectId, evidenceId, _state.value, _caseDefinition)
        val challenge = result.pendingChallenge

        _state.update {
            it.copy(
                presentedEvidenceRecords = result.updatedPresentedEvidenceRecords,
                clearedSuspectIds = result.updatedClearedSuspectIds,
                discoveredEvidenceIds = result.updatedDiscoveredEvidenceIds,
                hasDiscoveredMotive = result.hasDiscoveredMotive,
                pendingChallengeId = challenge?.id ?: it.pendingChallengeId,
                activeNotification = when {
                    challenge != null -> GameNotification(
                        title = "Contradiction Challenge",
                        message = "The evidence conflicts with ${_caseDefinition.getSuspect(suspectId)?.name ?: "the suspect"}'s account. Explain the conflict.",
                        type = NotificationType.CONTRADICTION,
                        actionLabel = "Resolve",
                        actionTarget = Screen.SUSPECT_INTERVIEW,
                        actionTab = CaseFileTab.STATEMENTS
                    )
                    result.reaction != null -> GameNotification(
                        title = "Statement Recorded",
                        message = "Statement from ${_caseDefinition.getSuspect(suspectId)?.name ?: "suspect"} added to Case File.",
                        type = NotificationType.INFO,
                        actionLabel = "View",
                        actionTarget = Screen.CASE_FILE,
                        actionTab = CaseFileTab.STATEMENTS
                    )
                    else -> it.activeNotification
                }
            )
        }

        if (challenge != null) {
            soundManager.playContradiction(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        } else if (result.reaction?.triggersMotiveId != null) {
            soundManager.playDeductionFormed(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        } else {
            soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        }

        result.newlyDiscoveredEvidenceIds.forEach { evidenceIdNew ->
            logActivity(ActivityKind.EVIDENCE, "${_caseDefinition.getEvidence(evidenceIdNew)?.name ?: "Evidence"} secured into Case File.")
        }
        if (result.reaction?.clearsSuspectCriticalPeriod == true) {
            logActivity(ActivityKind.CLEARED, "${_caseDefinition.getSuspect(suspectId)?.name ?: "Suspect"}'s alibi verified for the critical incident window.")
        }

        evaluateState(playSound = false)
        saveState()
        return EvidencePresentationOutcome(result.reaction, challenge)
    }

    /** Resolves the active contradiction challenge with the player's selected answer. */
    fun attemptContradictionChallenge(optionKey: String): ChallengeAttemptResult? {
        val challengeId = _state.value.pendingChallengeId ?: return null
        val challenge = _caseDefinition.getContradictionChallenge(challengeId) ?: return null
        val attempt = InvestigationEngine.evaluateContradictionChallenge(challenge, optionKey, _state.value, _caseDefinition)

        if (attempt.accepted) {
            val reaction = attempt.reaction
            val newBonusEvidence = reaction?.unlocksEvidenceIds?.filter {
                !_state.value.discoveredEvidenceIds.contains(it)
            }?.toSet() ?: emptySet()

            _state.update {
                it.copy(
                    pendingChallengeId = null,
                    unlockedContradictionIds = it.unlockedContradictionIds + challenge.contradictionId,
                    discoveredEvidenceIds = it.discoveredEvidenceIds + newBonusEvidence,
                    clearedSuspectIds = if (reaction?.clearsSuspectCriticalPeriod == true) {
                        it.clearedSuspectIds + challenge.suspectId
                    } else it.clearedSuspectIds,
                    activeNotification = GameNotification(
                        title = "Contradiction Established!",
                        message = _caseDefinition.getContradiction(challenge.contradictionId)?.title ?: "Contradiction recorded.",
                        type = NotificationType.CONTRADICTION,
                        actionLabel = "View Reasoning",
                        actionTarget = Screen.CASE_FILE,
                        actionTab = CaseFileTab.DEDUCTIONS
                    )
                )
            }
            newBonusEvidence.forEach { evidenceId ->
                logActivity(ActivityKind.EVIDENCE, "${_caseDefinition.getEvidence(evidenceId)?.name ?: "Evidence"} secured into Case File.")
            }
            logActivity(
                ActivityKind.CONTRADICTION,
                "Contradiction established: ${_caseDefinition.getSuspect(challenge.suspectId)?.name ?: "Suspect"}'s testimony refuted by physical evidence."
            )
            soundManager.playContradiction(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        } else {
            soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        }

        evaluateState(playSound = true)
        saveState()
        return attempt
    }

    /** Dismisses the pending challenge without penalty, allowing a retry. */
    fun dismissChallenge() {
        _state.update { it.copy(pendingChallengeId = null) }
        saveState()
    }

    fun addPlayerNote(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        val note = PlayerNote(
            id = "note_${System.currentTimeMillis()}",
            text = trimmed,
            timestamp = formatInvestigationTime(_state.value.investigationMinutes)
        )
        _state.update { it.copy(playerNotes = it.playerNotes + note) }
        logActivity(ActivityKind.NOTE, "Field observation recorded in investigator logbook.")
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        saveState()
    }

    fun deletePlayerNote(noteId: String) {
        _state.update { it.copy(playerNotes = it.playerNotes.filterNot { p -> p.id == noteId }) }
        saveState()
    }

    fun addBoardConnection(
        sourceId: String,
        targetId: String,
        requestedRelationship: ReasoningRelationship
    ): com.example.thelastcall.engine.BoardConnectionResult {
        val result = ReasoningEngine.validateBoardConnection(
            sourceId,
            targetId,
            requestedRelationship,
            _caseDefinition,
            _state.value
        )
        if (result.isValid) {
            val existing = _state.value.customConnections.find {
                (it.sourceId == sourceId && it.targetId == targetId) || (it.sourceId == targetId && it.targetId == sourceId)
            }
            val newConnection = EvidenceConnection(
                id = existing?.id ?: "conn_${System.currentTimeMillis()}",
                sourceId = sourceId,
                targetId = targetId,
                relationship = result.canonicalRelationship,
                description = result.label
            )
            val updatedConnections = if (existing != null) {
                _state.value.customConnections.map { if (it.id == existing.id) newConnection else it }
            } else {
                _state.value.customConnections + newConnection
            }

            val newContradictions = if (result.unlockedContradiction != null) {
                _state.value.unlockedContradictionIds + result.unlockedContradiction.id
            } else _state.value.unlockedContradictionIds

            val newDeductions = if (result.unlockedDeduction != null) {
                _state.value.unlockedDeductionIds + result.unlockedDeduction.id
            } else _state.value.unlockedDeductionIds

            _state.update {
                it.copy(
                    customConnections = updatedConnections,
                    unlockedContradictionIds = newContradictions,
                    unlockedDeductionIds = newDeductions
                )
            }
            logActivity(ActivityKind.NOTE, "Link recorded on Case Board: $sourceId -> $targetId (${result.label}).")
            soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
            evaluateState(playSound = true)
            saveState()
        }
        return result
    }

    fun removeBoardConnection(connectionId: String) {
        _state.update {
            it.copy(customConnections = it.customConnections.filterNot { c -> c.id == connectionId })
        }
        saveState()
    }

    fun clearBoardConnections() {
        _state.update { it.copy(customConnections = emptyList()) }
        saveState()
    }

    fun openPersonProfile(suspectId: String) {
        _state.update {
            it.copy(
                selectedSuspectId = suspectId,
                currentScreen = Screen.PERSON_PROFILE
            )
        }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun openCommunications() {
        _state.update { it.copy(currentScreen = Screen.COMMUNICATIONS) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    private fun logActivity(kind: ActivityKind, detail: String) {
        _state.update {
            val entry = ActivityLogEntry(
                id = "act_${System.currentTimeMillis()}_${it.activityLog.size}",
                kind = kind,
                detail = detail,
                timestamp = formatInvestigationTime(it.investigationMinutes)
            )
            it.copy(
                activityLog = (listOf(entry) + it.activityLog).take(60),
                investigationMinutes = it.investigationMinutes + 20
            )
        }
    }

    private fun formatInvestigationTime(totalMinutes: Int): String {
        val clamped = totalMinutes.coerceIn(0, 23 * 60 + 59)
        val hour = 7 + clamped / 60
        val minute = clamped % 60
        val displayHour = if (hour >= 24) 23 else hour
        return "DAY 1 • ${displayHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
    }

    private fun evaluateState(playSound: Boolean) {
        val s = _state.value

        // Timeline unlocks
        val newTimeline = InvestigationEngine.evaluateTimelineUnlocks(s, _caseDefinition)

        // Automatic deductions
        val prevDeductions = s.unlockedDeductionIds
        val newDeductions = ReasoningEngine.evaluateAutomaticDeductions(s, _caseDefinition)
        val newlyAddedDeduction = newDeductions.size > prevDeductions.size
        val newlyAddedDeductionTitle = if (newlyAddedDeduction) {
            newDeductions.minus(s.unlockedDeductionIds).firstOrNull()?.let { id -> _caseDefinition.getDeduction(id)?.title }
        } else null

        // Objectives evaluation
        val prevObjectives = s.completedObjectiveIds
        val newObjectives = InvestigationEngine.evaluateObjectives(s.copy(
            unlockedTimelineEventIds = newTimeline,
            unlockedDeductionIds = newDeductions
        ), _caseDefinition)
        val newlyCompletedObjective = newObjectives.size > prevObjectives.size

        // Accusation readiness
        val readiness = AccusationEngine.evaluateReadiness(s.copy(
            unlockedTimelineEventIds = newTimeline,
            unlockedDeductionIds = newDeductions,
            completedObjectiveIds = newObjectives
        ), _caseDefinition)

        val caseStatus = if (s.caseStatus == CaseStatus.NOT_STARTED) {
            CaseStatus.IN_PROGRESS
        } else if (s.caseStatus == CaseStatus.IN_PROGRESS && readiness.isReadyForAccusation) {
            CaseStatus.READY_FOR_ACCUSATION
        } else {
            s.caseStatus
        }

        _state.update {
            it.copy(
                unlockedTimelineEventIds = newTimeline,
                unlockedDeductionIds = newDeductions,
                completedObjectiveIds = newObjectives,
                caseStatus = caseStatus,
                activeNotification = when {
                    it.activeNotification != null -> it.activeNotification
                    newlyAddedDeductionTitle != null -> GameNotification(
                        title = "New Deduction",
                        message = "$newlyAddedDeductionTitle added to Case File.",
                        type = NotificationType.DEDUCTION,
                        actionLabel = "View Reasoning",
                        actionTarget = Screen.CASE_FILE,
                        actionTab = CaseFileTab.DEDUCTIONS
                    )
                    newlyCompletedObjective -> {
                        val completedNow = newObjectives.minus(prevObjectives).firstOrNull()
                        val newLead = completedNow?.let { completedId ->
                            _caseDefinition.objectives.firstOrNull { it.id == completedId }
                        }
                        val nextLead = _caseDefinition.getCurrentLead(s.copy(completedObjectiveIds = newObjectives))
                        GameNotification(
                            title = "New Lead",
                            message = newLead?.let { "Follow up on: ${it.title}" } ?: "Investigation milestone recorded in Case File.",
                            type = NotificationType.LEAD,
                            actionLabel = nextLead?.leadActionLabel ?: "Investigate",
                            actionTarget = nextLead?.leadTarget ?: Screen.CASE_HUB,
                            actionTab = nextLead?.focusTab
                        )
                    }
                    else -> it.activeNotification
                }
            )
        }

        if (newlyAddedDeduction) {
            newDeductions.minus(s.unlockedDeductionIds).firstOrNull()?.let { id ->
                _caseDefinition.getDeduction(id)?.let { d ->
                    logActivity(ActivityKind.DEDUCTION, "Deduction formed: ${d.title}.")
                }
            }
        }
        if (newTimeline.size > s.unlockedTimelineEventIds.size) {
            logActivity(ActivityKind.TIMELINE, "Incident timeline reconstructed with verified chronological records.")
        }
        if (newlyCompletedObjective) {
            newObjectives.minus(prevObjectives).firstOrNull()?.let { id ->
                _caseDefinition.objectives.firstOrNull { it.id == id }?.let { lead ->
                    logActivity(ActivityKind.LEAD, "Investigative lead pursued: ${lead.title}.")
                }
            }
        }

        if (newlyAddedDeduction && playSound) {
            soundManager.playDeductionFormed(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        }
    }

    fun submitAccusation(submission: AccusationSubmission): AccusationEvaluation {
        val s = _state.value
        val evaluation = AccusationEngine.evaluateAccusation(submission, s, _caseDefinition)

        if (evaluation.isCorrectCulprit && !evaluation.isPremature) {
            _state.update {
                it.copy(
                    caseStatus = evaluation.resultStatus,
                    currentScreen = Screen.CASE_RESULT,
                    lastAccusationSubmission = submission,
                    lastAccusationEvaluation = evaluation
                )
            }
            evaluateState(playSound = false)
            soundManager.playCaseSolved(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        } else {
            _state.update {
                it.copy(
                    lastAccusationSubmission = submission,
                    lastAccusationEvaluation = evaluation
                )
            }
            soundManager.playContradiction(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        }

        saveState()
        return evaluation
    }

    fun dismissNotification() {
        _state.update { it.copy(activeNotification = null) }
    }

    /** Routes the active notification's action button to its intended screen. */
    fun handleNotificationAction() {
        val notif = _state.value.activeNotification ?: return
        val target = notif.actionTarget
        val tab = notif.actionTab
        when (target) {
            null -> Unit
            Screen.CASE_FILE -> openCaseFile(tab ?: CaseFileTab.EVIDENCE)
            Screen.DETECTIVE_BOARD -> openDetectiveBoard()
            Screen.CRIME_SCENE -> enterCrimeScene()
            Screen.SUSPECT_INTERVIEW -> _state.value.selectedSuspectId?.let { selectSuspectForInterview(it) }
            Screen.COMMUNICATIONS -> openCommunications()
            Screen.FINAL_CASE_REVIEW -> openCaseReview()
            else -> openCaseHub()
        }
        dismissNotification()
    }

    fun clearAccusationFeedback() {
        _state.update { it.copy(lastAccusationEvaluation = null) }
    }

    fun resetCase() {
        val settings = _state.value.settings
        prefs.edit()
            .remove(getSaveKey(_caseDefinition.id))
            .remove("case_state_v1")
            .apply()
        _state.value = CaseState(
            caseId = _caseDefinition.id,
            unlockedTimelineEventIds = _caseDefinition.initialUnlockedTimelineIds,
            settings = settings
        )
        soundManager.playUiClick(settings.soundEnabled, settings.hapticsEnabled)
    }

    fun openCaseHub() {
        _state.update { it.copy(currentScreen = Screen.CASE_HUB) }
        saveState()
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
    }

    fun handleAndroidBack(): Boolean {
        val current = _state.value.currentScreen
        when (current) {
            Screen.MAIN_MENU -> return false // let system handle exit
            Screen.CASE_INTRO -> navigateTo(Screen.MAIN_MENU)
            Screen.BRIEFING -> navigateTo(Screen.MAIN_MENU)
            Screen.CASE_HUB -> navigateTo(Screen.MAIN_MENU)
            Screen.CRIME_SCENE -> openCaseHub()
            Screen.CASE_FILE -> openCaseHub()
            Screen.DETECTIVE_BOARD -> openCaseHub()
            Screen.SUSPECT_INTERVIEW -> openCaseFile(CaseFileTab.SUSPECTS)
            Screen.EVIDENCE_DETAIL -> openCaseFile(CaseFileTab.EVIDENCE)
            Screen.PERSON_PROFILE -> openCaseFile(CaseFileTab.SUSPECTS)
            Screen.COMMUNICATIONS -> openCaseHub()
            Screen.FINAL_CASE_REVIEW -> openCaseFile(CaseFileTab.THEORY)
            Screen.FINAL_ACCUSATION -> navigateTo(Screen.FINAL_CASE_REVIEW)
            Screen.CASE_RESULT -> navigateTo(Screen.MAIN_MENU)
            Screen.SETTINGS -> navigateTo(Screen.MAIN_MENU)
        }
        return true
    }

    fun selectStatementForReasoning(statementId: String) {
        _state.update {
            it.copy(
                selectedStatementId = statementId,
                caseFileTab = CaseFileTab.DEDUCTIONS,
                currentScreen = Screen.CASE_FILE
            )
        }
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        saveState()
    }

    fun testContradiction(source1Id: String, source2Id: String): Contradiction? {
        val contradiction = ReasoningEngine.checkContradiction(source1Id, source2Id, _caseDefinition)
        if (contradiction != null) {
            val newlyDiscovered = !_state.value.unlockedContradictionIds.contains(contradiction.id)
            _state.update {
                it.copy(
                    unlockedContradictionIds = it.unlockedContradictionIds + contradiction.id,
                    activeNotification = GameNotification(
                        title = "Contradiction Established!",
                        message = contradiction.title,
                        type = NotificationType.CONTRADICTION,
                        actionLabel = "View Reasoning",
                        actionTarget = Screen.CASE_FILE,
                        actionTab = CaseFileTab.DEDUCTIONS
                    )
                )
            }
            if (newlyDiscovered) {
                soundManager.playContradiction(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
                logActivity(ActivityKind.CONTRADICTION, "${_caseDefinition.getSuspect(contradiction.suspectId)?.name ?: "Suspect"}'s statement contradicts verified evidence.")
            }
            evaluateState(playSound = false)
            saveState()
            return contradiction
        } else {
            soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
            return null
        }
    }

    fun testDeduction(source1Id: String, source2Id: String, relationship: ReasoningRelationship): Pair<Boolean, String> {
        val (deduction, customMsg) = ReasoningEngine.checkDeduction(source1Id, source2Id, relationship, _caseDefinition)
        if (deduction != null) {
            val isNew = !_state.value.unlockedDeductionIds.contains(deduction.id)
            _state.update {
                it.copy(
                    unlockedDeductionIds = it.unlockedDeductionIds + deduction.id,
                    activeNotification = GameNotification(
                        title = "Deduction Formed!",
                        message = deduction.title,
                        type = NotificationType.DEDUCTION
                    )
                )
            }
            if (isNew) {
                soundManager.playDeductionFormed(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
            }
            evaluateState(playSound = false)
            saveState()
            return Pair(true, deduction.reasoning)
        } else if (customMsg != null) {
            soundManager.playDeductionFormed(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
            return Pair(true, customMsg)
        } else {
            soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
            return Pair(false, "These pieces of information do not establish a solid connection yet. Review the timeline and suspect alibis.")
        }
    }

    fun updatePlayerTheory(theory: PlayerTheory) {
        _state.update { it.copy(playerTheory = theory) }
        saveState()
    }

    fun updateTheorySuspect(suspectId: String?) {
        _state.update {
            it.copy(playerTheory = it.playerTheory.copy(suspectId = suspectId))
        }
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        saveState()
    }

    fun updateTheoryMotive(motiveKey: String?) {
        _state.update {
            it.copy(playerTheory = it.playerTheory.copy(motiveKey = motiveKey))
        }
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        saveState()
    }

    fun updateTheoryWeapon(weaponKey: String?) {
        _state.update {
            it.copy(playerTheory = it.playerTheory.copy(weaponKey = weaponKey))
        }
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        saveState()
    }

    fun toggleTheorySupportingEvidence(evidenceId: String) {
        val current = _state.value.playerTheory.supportingEvidenceIds
        val updated = if (current.contains(evidenceId)) current - evidenceId else current + evidenceId
        _state.update {
            it.copy(playerTheory = it.playerTheory.copy(supportingEvidenceIds = updated))
        }
        soundManager.playUiClick(_state.value.settings.soundEnabled, _state.value.settings.hapticsEnabled)
        saveState()
    }

    private fun saveState() {
        try {
            val s = _state.value
            val json = JSONObject().apply {
                put("schemaVersion", 1)
                put("caseId", s.caseId)
                put("caseStatus", s.caseStatus.name)
                put("currentScreen", s.currentScreen.name)
                put("caseFileTab", s.caseFileTab.name)
                put("selectedSuspectId", s.selectedSuspectId ?: JSONObject.NULL)
                put("selectedEvidenceId", s.selectedEvidenceId ?: JSONObject.NULL)
                put("selectedStatementId", s.selectedStatementId ?: JSONObject.NULL)
                put("discoveredEvidenceIds", setToJsonArray(s.discoveredEvidenceIds))
                put("inspectedEvidenceIds", setToJsonArray(s.inspectedEvidenceIds))
                put("interviewedSuspectIds", setToJsonArray(s.interviewedSuspectIds))
                put("askedQuestionIds", setToJsonArray(s.askedQuestionIds))
                put("recordedStatementIds", setToJsonArray(s.recordedStatementIds))
                put("clearedSuspectIds", setToJsonArray(s.clearedSuspectIds))
                put("presentedEvidenceRecords", mapOfSetsToJson(s.presentedEvidenceRecords))
                put("unlockedTimelineEventIds", setToJsonArray(s.unlockedTimelineEventIds))
                put("unlockedDeductionIds", setToJsonArray(s.unlockedDeductionIds))
                put("unlockedContradictionIds", setToJsonArray(s.unlockedContradictionIds))
                put("completedObjectiveIds", setToJsonArray(s.completedObjectiveIds))
                put("inspectedHotspotIds", setToJsonArray(s.inspectedHotspotIds))
                put("hasDiscoveredMotive", s.hasDiscoveredMotive)
                put("hasDiscoveredOpportunity", s.hasDiscoveredOpportunity)
                put("hasSeenCrimeSceneTutorial", s.hasSeenCrimeSceneTutorial)
                put("pendingChallengeId", s.pendingChallengeId ?: JSONObject.NULL)
                put("investigationMinutes", s.investigationMinutes)
                put("playerNotes", playerNotesToJson(s.playerNotes))
                put("activityLog", activityLogToJson(s.activityLog))
                put("customConnections", customConnectionsToJson(s.customConnections))

                // Save Player Theory
                val theoryObj = JSONObject().apply {
                    put("suspectId", s.playerTheory.suspectId ?: JSONObject.NULL)
                    put("motiveKey", s.playerTheory.motiveKey ?: JSONObject.NULL)
                    put("weaponKey", s.playerTheory.weaponKey ?: JSONObject.NULL)
                    put("opportunityConfirmed", s.playerTheory.opportunityConfirmed)
                    put("supportingEvidenceIds", setToJsonArray(s.playerTheory.supportingEvidenceIds))
                    put("establishedDeductionIds", setToJsonArray(s.playerTheory.establishedDeductionIds))
                }
                put("playerTheory", theoryObj)
            }
            prefs.edit().putString(getSaveKey(_caseDefinition.id), json.toString()).apply()
        } catch (_: Exception) {}
    }

    private fun playerNotesToJson(notes: List<PlayerNote>): JSONArray {
        val array = JSONArray()
        notes.forEach { note ->
            val obj = JSONObject().apply {
                put("id", note.id)
                put("text", note.text)
                put("timestamp", note.timestamp)
            }
            array.put(obj)
        }
        return array
    }

    private fun loadPlayerNotes(json: JSONObject): List<PlayerNote> {
        val array = json.optJSONArray("playerNotes") ?: return emptyList()
        val notes = mutableListOf<PlayerNote>()
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            notes.add(
                PlayerNote(
                    id = obj.optString("id", "note_$i"),
                    text = obj.optString("text", ""),
                    timestamp = obj.optString("timestamp", "")
                )
            )
        }
        return notes
    }

    private fun activityLogToJson(entries: List<ActivityLogEntry>): JSONArray {
        val array = JSONArray()
        entries.forEach { entry ->
            val obj = JSONObject().apply {
                put("id", entry.id)
                put("kind", entry.kind.name)
                put("detail", entry.detail)
                put("timestamp", entry.timestamp)
            }
            array.put(obj)
        }
        return array
    }

    private fun loadActivityLog(json: JSONObject): List<ActivityLogEntry> {
        val array = json.optJSONArray("activityLog") ?: return emptyList()
        val entries = mutableListOf<ActivityLogEntry>()
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            val kind = try { ActivityKind.valueOf(obj.optString("kind", ActivityKind.EVIDENCE.name)) } catch (_: Exception) { ActivityKind.EVIDENCE }
            entries.add(
                ActivityLogEntry(
                    id = obj.optString("id", "act_$i"),
                    kind = kind,
                    detail = obj.optString("detail", ""),
                    timestamp = obj.optString("timestamp", "")
                )
            )
        }
        return entries
    }

    private fun customConnectionsToJson(connections: List<EvidenceConnection>): JSONArray {
        val array = JSONArray()
        connections.forEach { conn ->
            val obj = JSONObject().apply {
                put("id", conn.id)
                put("sourceId", conn.sourceId)
                put("targetId", conn.targetId)
                put("relationship", conn.relationship.name)
                put("description", conn.description)
            }
            array.put(obj)
        }
        return array
    }

    private fun loadCustomConnections(json: JSONObject): List<EvidenceConnection> {
        val array = json.optJSONArray("customConnections") ?: return emptyList()
        val list = mutableListOf<EvidenceConnection>()
        for (i in 0 until array.length()) {
            val obj = array.optJSONObject(i) ?: continue
            val relStr = obj.optString("relationship", ReasoningRelationship.CONNECTS.name)
            val rel = try { ReasoningRelationship.valueOf(relStr) } catch (_: Exception) { ReasoningRelationship.CONNECTS }
            list.add(
                EvidenceConnection(
                    id = obj.optString("id", "conn_$i"),
                    sourceId = obj.optString("sourceId", ""),
                    targetId = obj.optString("targetId", ""),
                    relationship = rel,
                    description = obj.optString("description", "")
                )
            )
        }
        return list
    }

    private fun setToJsonArray(set: Set<String>): JSONArray {
        val array = JSONArray()
        set.forEach { array.put(it) }
        return array
    }

    private fun jsonArrayToSet(array: JSONArray?): Set<String> {
        if (array == null) return emptySet()
        val set = mutableSetOf<String>()
        for (i in 0 until array.length()) {
            set.add(array.getString(i))
        }
        return set
    }

    private fun mapOfSetsToJson(map: Map<String, Set<String>>): JSONObject {
        val obj = JSONObject()
        map.forEach { (k, v) ->
            obj.put(k, setToJsonArray(v))
        }
        return obj
    }

    private fun jsonToMapOfSets(obj: JSONObject?): Map<String, Set<String>> {
        if (obj == null) return emptyMap()
        val map = mutableMapOf<String, Set<String>>()
        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = jsonArrayToSet(obj.optJSONArray(key))
        }
        return map
    }
}
