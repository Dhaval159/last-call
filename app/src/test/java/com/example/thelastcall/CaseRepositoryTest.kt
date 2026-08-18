package com.example.thelastcall

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.thelastcall.audio.SoundManager
import com.example.thelastcall.data.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CaseRepositoryTest {

    private lateinit var context: Context
    private lateinit var soundManager: SoundManager
    private lateinit var repository: CaseRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        soundManager = SoundManager(context)
        // Clear prefs before test
        context.getSharedPreferences("the_last_call_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        repository = CaseRepository(context, soundManager)
    }

    @Test
    fun `initial state starts at MAIN_MENU with default settings`() {
        val state = repository.state.value
        assertEquals(Screen.MAIN_MENU, state.currentScreen)
        assertTrue(state.settings.soundEnabled)
        assertTrue(state.settings.hapticsEnabled)
        assertEquals(TextSpeed.NORMAL, state.settings.textSpeed)
    }

    @Test
    fun `startNewCase transitions to CASE_INTRO and IN_PROGRESS`() {
        repository.startNewCase()
        val state = repository.state.value
        assertEquals(Screen.CASE_INTRO, state.currentScreen)
        assertEquals(CaseStatus.IN_PROGRESS, state.caseStatus)

        repository.enterBriefing()
        assertEquals(Screen.BRIEFING, repository.state.value.currentScreen)
    }

    @Test
    fun `inspectHotspot discovers primary and conditional secondary evidence`() {
        repository.startNewCase()
        repository.enterCrimeScene()
        assertEquals(Screen.CRIME_SCENE, repository.state.value.currentScreen)

        // Inspect Phone hotspot
        val phoneHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_phone" }
        val (primary, secondary) = repository.inspectHotspot(phoneHotspot)

        assertNotNull(primary)
        assertEquals("E001", primary?.id)
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E001"))

        // Inspect desk hotspot -> E004, E006
        val deskHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_desk" }
        val (deskPrim, deskSec) = repository.inspectHotspot(deskHotspot)
        assertEquals("E004", deskPrim?.id)
        assertEquals("E006", deskSec?.id)
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E004"))
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E006"))

        // Inspect organizer with E004 already discovered -> unlocks E005 and E019
        val organizerHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_organizer" }
        val (orgPrim, orgSec) = repository.inspectHotspot(organizerHotspot)
        assertEquals("E005", orgPrim?.id)
        assertEquals("E019", orgSec?.id)
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E019"))
    }

    @Test
    fun `interrogation records statements and transit records`() {
        repository.startNewCase()

        // Maya interview
        val qMaya1 = Case001Data.QUESTIONS.first { it.id == "Q_MAYA_1" }
        repository.askQuestion(qMaya1)
        assertTrue(repository.state.value.askedQuestionIds.contains("Q_MAYA_1"))
        assertTrue(repository.state.value.recordedStatementIds.contains("ST001"))
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E011"))

        // Ask Maya departure question -> unlocks transit log E017
        val qMaya3 = Case001Data.QUESTIONS.first { it.id == "Q_MAYA_3" }
        repository.askQuestion(qMaya3)
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E017"))

        // Present E017 to Maya -> clears Maya's alibi
        val outcome = repository.presentEvidence("S001", "E017")
        assertNotNull(outcome.reaction)
        assertTrue(repository.state.value.clearedSuspectIds.contains("S001"))
    }

    @Test
    fun `full investigation and final accusation workflow`() {
        repository.startNewCase()

        // 1. Inspect Crime Scene Hotspots
        Case001Data.CRIME_SCENE_HOTSPOTS.forEach { hotspot ->
            repository.inspectHotspot(hotspot)
        }

        // Detailed inspect E001 (Phone) to reveal call E002 and contact metadata E003
        repository.selectEvidenceForDetail("E001")
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E002"))
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E003"))

        // 2. Interrogate Maya, Victor, Nora, Daniel
        val qMaya3 = Case001Data.QUESTIONS.first { it.id == "Q_MAYA_3" }
        val qVic4 = Case001Data.QUESTIONS.first { it.id == "Q_VIC_4" }
        val qNora3 = Case001Data.QUESTIONS.first { it.id == "Q_NORA_3" }
        val qDan3 = Case001Data.QUESTIONS.first { it.id == "Q_DAN_3" }
        val qDan4 = Case001Data.QUESTIONS.first { it.id == "Q_DAN_4" }

        repository.askQuestion(qMaya3)
        repository.askQuestion(qVic4)
        repository.askQuestion(qNora3)
        repository.askQuestion(qDan3)
        repository.askQuestion(qDan4)

        // Clear suspects with independent evidence
        repository.presentEvidence("S001", "E017")
        repository.presentEvidence("S002", "E015")
        repository.presentEvidence("S003", "E016")

        assertTrue(repository.state.value.clearedSuspectIds.containsAll(listOf("S001", "S002", "S003")))

        // Present Daniel with return footage E018 -> triggers a contradiction CHALLENGE (not an auto-reveal)
        val outcomeDan = repository.presentEvidence("S004", "E018")
        assertNull(outcomeDan.reaction)
        assertNotNull(outcomeDan.challenge)
        assertTrue(repository.state.value.pendingChallengeId == "CHAL_C001")
        assertFalse(repository.state.value.unlockedContradictionIds.contains("C001"))

        // A wrong answer does not establish the contradiction and allows a retry
        val wrongKey = outcomeDan.challenge!!.options.first { !it.isCorrect }.key
        val wrongAttempt = repository.attemptContradictionChallenge(wrongKey)
        assertNotNull(wrongAttempt)
        assertFalse(wrongAttempt!!.accepted)
        assertFalse(repository.state.value.unlockedContradictionIds.contains("C001"))
        assertEquals("CHAL_C001", repository.state.value.pendingChallengeId)

        // The correct explanation establishes C001 and plays the reaction
        val correctKey = outcomeDan.challenge!!.options.first { it.isCorrect }.key
        val correctAttempt = repository.attemptContradictionChallenge(correctKey)
        assertNotNull(correctAttempt)
        assertTrue(correctAttempt!!.accepted)
        assertNotNull(correctAttempt.reaction)
        assertTrue(correctAttempt.reaction!!.isContradiction)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C001"))
        assertNull(repository.state.value.pendingChallengeId)

        // Present Daniel with financial fraud dossier E019
        repository.presentEvidence("S004", "E019")
        assertTrue(repository.state.value.hasDiscoveredMotive)

        // Navigate to final accusation screen
        repository.navigateTo(Screen.FINAL_ACCUSATION)
        assertEquals(Screen.FINAL_ACCUSATION, repository.state.value.currentScreen)

        // 3. Test Wrong Accusation
        val wrongSubmission = AccusationSubmission(
            suspectId = "S002",
            motiveKey = "MOTIVE_CORPORATE",
            weaponKey = "WEAPON_PAPERWEIGHT",
            selectedEvidenceIds = listOf("E015")
        )
        val wrongEval = repository.submitAccusation(wrongSubmission)
        assertFalse(wrongEval.isCorrectCulprit)
        assertEquals(Screen.FINAL_ACCUSATION, repository.state.value.currentScreen) // stays on accusation screen with feedback

        // 4. Test Correct Accusation against Daniel Mercer
        val correctSubmission = AccusationSubmission(
            suspectId = "S004",
            motiveKey = "MOTIVE_FINANCIAL",
            weaponKey = "WEAPON_PAPERWEIGHT",
            selectedEvidenceIds = listOf("E002", "E018", "E019", "E006", "E020")
        )
        val correctEval = repository.submitAccusation(correctSubmission)
        assertTrue(correctEval.isCorrectCulprit)
        assertFalse(correctEval.isPremature)
        assertEquals(Screen.CASE_RESULT, repository.state.value.currentScreen)
        assertTrue(
            repository.state.value.caseStatus == CaseStatus.SOLVED ||
            repository.state.value.caseStatus == CaseStatus.SOLVED_PERFECT
        )
    }

    @Test
    fun `persistence round-trip preserves state across repository instances`() {
        repository.startNewCase()
        val phoneHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_phone" }
        repository.inspectHotspot(phoneHotspot)
        repository.selectSuspectForInterview("S004")

        val stateBefore = repository.state.value
        assertTrue(stateBefore.discoveredEvidenceIds.contains("E001"))
        assertEquals("S004", stateBefore.selectedSuspectId)

        // Create fresh repository reading from shared preferences
        val reloadedRepository = CaseRepository(context, soundManager)
        val stateAfter = reloadedRepository.state.value

        assertEquals(stateBefore.caseStatus, stateAfter.caseStatus)
        assertEquals(stateBefore.currentScreen, stateAfter.currentScreen)
        assertEquals(stateBefore.selectedSuspectId, stateAfter.selectedSuspectId)
        assertTrue(stateAfter.discoveredEvidenceIds.contains("E001"))
    }

    @Test
    fun `testContradiction unlocks valid contradiction and rejects invalid pairing`() {
        repository.startNewCase()

        // Inspect phone to get E002, then ask Daniel questions
        Case001Data.CRIME_SCENE_HOTSPOTS.forEach { repository.inspectHotspot(it) }
        repository.selectEvidenceForDetail("E001") // unlocks E002

        val qDan3 = Case001Data.QUESTIONS.first { it.id == "Q_DAN_3" }
        val qDan4 = Case001Data.QUESTIONS.first { it.id == "Q_DAN_4" }
        repository.askQuestion(qDan3)
        repository.askQuestion(qDan4)

        assertTrue(repository.state.value.recordedStatementIds.contains("ST007"))
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("E018"))

        // Invalid test: ST007 with unrelated evidence E001 (phone) -> should return null
        val invalidResult = repository.testContradiction("ST007", "E001")
        assertNull(invalidResult)
        assertFalse(repository.state.value.unlockedContradictionIds.contains("C001"))

        // Valid test: ST007 ("Claim of no return") with E018 ("Keycard return log 10:20 PM")
        val validResult = repository.testContradiction("ST007", "E018")
        assertNotNull(validResult)
        assertEquals("C001", validResult?.id)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C001"))
    }

    @Test
    fun `testDeduction establishes valid deduction and handles invalid combinations`() {
        repository.startNewCase()

        // Unlock E001 and E002
        Case001Data.CRIME_SCENE_HOTSPOTS.forEach { repository.inspectHotspot(it) }
        repository.selectEvidenceForDetail("E001") // unlocks E002

        // Test establishing D001 (Return Time vs Call Time)
        val (validSuccess, validMessage) = repository.testDeduction("E001", "E002", ReasoningRelationship.ESTABLISHES)
        assertTrue(validSuccess)
        assertTrue(repository.state.value.unlockedDeductionIds.contains("D001"))

        // Test invalid deduction pairing
        val (invalidSuccess, invalidMessage) = repository.testDeduction("E001", "E011", ReasoningRelationship.DISPROVES)
        assertFalse(invalidSuccess)
    }

    @Test
    fun `theory building tracks components, confidence, and accusation readiness`() {
        repository.startNewCase()

        // Initially empty theory
        val initialReadiness = repository.state.value.getCaseReadiness()
        assertFalse(initialReadiness.isReadyForAccusation)
        assertEquals(TheoryConfidence.UNKNOWN, initialReadiness.motiveConfidence)
        assertEquals(TheoryConfidence.UNKNOWN, initialReadiness.opportunityConfidence)
        assertEquals(TheoryConfidence.UNKNOWN, initialReadiness.methodConfidence)

        // Select suspect
        repository.updateTheorySuspect("S004")
        assertEquals("S004", repository.state.value.playerTheory.suspectId)

        // Select motive
        repository.updateTheoryMotive("MOTIVE_FINANCIAL")
        assertEquals("MOTIVE_FINANCIAL", repository.state.value.playerTheory.motiveKey)

        // Select weapon
        repository.updateTheoryWeapon("WEAPON_PAPERWEIGHT")
        assertEquals("WEAPON_PAPERWEIGHT", repository.state.value.playerTheory.weaponKey)

        // Add supporting evidence
        repository.toggleTheorySupportingEvidence("E018")
        repository.toggleTheorySupportingEvidence("E019")
        assertTrue(repository.state.value.playerTheory.supportingEvidenceIds.containsAll(listOf("E018", "E019")))

        // Verify partial theory can be revised without resetting case
        repository.updateTheoryMotive("MOTIVE_FAMILY")
        assertEquals("MOTIVE_FAMILY", repository.state.value.playerTheory.motiveKey)
        repository.updateTheoryMotive("MOTIVE_FINANCIAL")
        assertEquals("MOTIVE_FINANCIAL", repository.state.value.playerTheory.motiveKey)
    }

    @Test
    fun `resetCase clears all reasoning, contradictions, deductions, and theory state`() {
        repository.startNewCase()

        Case001Data.CRIME_SCENE_HOTSPOTS.forEach { repository.inspectHotspot(it) }
        repository.selectEvidenceForDetail("E001") // unlocks E002

        val qDan3 = Case001Data.QUESTIONS.first { it.id == "Q_DAN_3" }
        val qDan4 = Case001Data.QUESTIONS.first { it.id == "Q_DAN_4" }
        repository.askQuestion(qDan3)
        repository.askQuestion(qDan4)
        repository.testContradiction("ST007", "E018")
        repository.updateTheorySuspect("S004")
        repository.updateTheoryMotive("MOTIVE_FINANCIAL")

        assertTrue(repository.state.value.unlockedContradictionIds.isNotEmpty())
        assertNotNull(repository.state.value.playerTheory.suspectId)

        // Reset
        repository.resetCase()

        val resetState = repository.state.value
        assertEquals(CaseStatus.NOT_STARTED, resetState.caseStatus)
        assertEquals(Screen.MAIN_MENU, resetState.currentScreen)
        assertTrue(resetState.discoveredEvidenceIds.isEmpty())
        assertTrue(resetState.recordedStatementIds.isEmpty())
        assertTrue(resetState.unlockedContradictionIds.isEmpty())
        assertTrue(resetState.unlockedDeductionIds.isEmpty())
        assertNull(resetState.playerTheory.suspectId)
        assertNull(resetState.playerTheory.motiveKey)
        assertTrue(resetState.playerTheory.supportingEvidenceIds.isEmpty())
        // Settings preserved
        assertTrue(resetState.settings.soundEnabled)
    }

    @Test
    fun `player notes are added and deleted without affecting investigation data`() {
        repository.startNewCase()

        repository.addPlayerNote("Daniel's departure claim needs verification.")
        repository.addPlayerNote("   ")

        val state = repository.state.value
        assertEquals(1, state.playerNotes.size)
        assertEquals("Daniel's departure claim needs verification.", state.playerNotes.first().text)
        assertTrue(state.activityLog.any { it.kind == ActivityKind.NOTE })

        repository.deletePlayerNote(state.playerNotes.first().id)
        assertTrue(repository.state.value.playerNotes.isEmpty())
    }

    @Test
    fun `contradiction challenge state persists across repository reloads`() {
        repository.startNewCase()

        val outcome = repository.presentEvidence("S004", "E018")
        assertNotNull(outcome.challenge)
        assertTrue(repository.state.value.pendingChallengeId == "CHAL_C001")

        val reloaded = CaseRepository(context, soundManager)
        assertEquals("CHAL_C001", reloaded.state.value.pendingChallengeId)
        assertFalse(reloaded.state.value.unlockedContradictionIds.contains("C001"))

        val correctKey = outcome.challenge!!.options.first { it.isCorrect }.key
        reloaded.attemptContradictionChallenge(correctKey)
        assertTrue(reloaded.state.value.unlockedContradictionIds.contains("C001"))
        assertNull(reloaded.state.value.pendingChallengeId)
    }
}
