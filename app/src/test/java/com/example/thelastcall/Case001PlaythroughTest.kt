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
class Case001PlaythroughTest {

    private lateinit var context: Context
    private lateinit var soundManager: SoundManager
    private lateinit var repository: CaseRepository
    private val caseDef: CaseDefinition = Case001Data

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        soundManager = SoundManager(context)
        context.getSharedPreferences("the_last_call_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        repository = CaseRepository(context, soundManager, caseDef)
    }

    @Test
    fun `full canonical playthrough from start to perfect conviction`() {
        // 1. Start Case
        repository.startNewCase()
        assertEquals(CaseStatus.IN_PROGRESS, repository.state.value.caseStatus)
        assertEquals(Screen.CASE_INTRO, repository.state.value.currentScreen)

        // 2. Enter Briefing & Crime Scene
        repository.enterBriefing()
        assertEquals(Screen.BRIEFING, repository.state.value.currentScreen)
        repository.enterCrimeScene()
        assertEquals(Screen.CRIME_SCENE, repository.state.value.currentScreen)

        // 3. Inspect all crime scene hotspots
        caseDef.crimeSceneHotspots.forEach { hotspot ->
            repository.inspectHotspot(hotspot)
        }
        assertEquals(caseDef.crimeSceneHotspots.size, repository.state.value.inspectedHotspotIds.size)
        assertTrue(repository.state.value.discoveredEvidenceIds.containsAll(listOf("E001", "E004", "E005", "E006", "E007", "E008", "E009", "E010", "E019")))

        // Inspect Phone to uncover call details
        repository.selectEvidenceForDetail("E001")
        assertTrue(repository.state.value.discoveredEvidenceIds.containsAll(listOf("E002", "E003")))

        // 4. Interrogate all 4 suspects
        caseDef.suspects.forEach { suspect ->
            repository.selectSuspectForInterview(suspect.id)
            val questions = caseDef.questions.filter { it.suspectId == suspect.id }
            questions.forEach { q ->
                // Check if question requirements are met
                val reqMet = q.requiredEvidenceId == null || repository.state.value.discoveredEvidenceIds.contains(q.requiredEvidenceId)
                if (reqMet) {
                    repository.askQuestion(q)
                }
            }
        }
        assertTrue(repository.state.value.interviewedSuspectIds.containsAll(listOf("S001", "S002", "S003", "S004")))

        // 5. Present clearing evidence to Maya, Victor, Nora
        repository.presentEvidence("S001", "E017")
        repository.presentEvidence("S002", "E015")
        repository.presentEvidence("S003", "E016")
        assertTrue(repository.state.value.clearedSuspectIds.containsAll(listOf("S001", "S002", "S003")))

        // 6. Present Daniel with return footage E018 -> contradiction CHALLENGE, then resolve it
        val outcomeDan18 = repository.presentEvidence("S004", "E018")
        assertNull(outcomeDan18.reaction)
        assertNotNull(outcomeDan18.challenge)
        assertFalse(repository.state.value.unlockedContradictionIds.contains("C001"))
        val challenge = outcomeDan18.challenge!!
        val correctKey = challenge.options.first { it.isCorrect }.key
        val attempt = repository.attemptContradictionChallenge(correctKey)
        assertNotNull(attempt)
        assertTrue(attempt!!.accepted)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C001"))

        repository.presentEvidence("S004", "E019")
        assertTrue(repository.state.value.hasDiscoveredMotive)

        // 7. Verify all deductions auto-form or are verified
        val deductions = repository.state.value.unlockedDeductionIds
        assertTrue(deductions.contains("D001")) // Alive at 10:42 PM
        assertTrue(deductions.contains("D002")) // Alibis clear Maya, Victor, Nora
        assertTrue(deductions.contains("D004")) // Daniel lied about departure
        assertTrue(deductions.contains("D005")) // Financial embezzlement motive
        assertTrue(deductions.contains("D008")) // Daniel Mercer is sole culprit

        // 8. Build Theory
        val readinessBeforeSuspect = repository.state.value.getCaseReadiness(caseDef)
        assertEquals(TheoryConfidence.ESTABLISHED, readinessBeforeSuspect.motiveConfidence)
        assertEquals(TheoryConfidence.ESTABLISHED, readinessBeforeSuspect.opportunityConfidence)
        assertEquals(TheoryConfidence.ESTABLISHED, readinessBeforeSuspect.methodConfidence)
        assertFalse(readinessBeforeSuspect.hasSuspect)
        assertFalse(readinessBeforeSuspect.isReadyForAccusation)

        // 9. Designate Suspect and Formulate Theory
        repository.updateTheorySuspect("S004")
        repository.updateTheoryMotive("MOTIVE_FINANCIAL")
        repository.updateTheoryWeapon("WEAPON_PAPERWEIGHT")
        caseDef.culpritSolution.criticalEvidenceIds.forEach {
            repository.toggleTheorySupportingEvidence(it)
        }

        val readinessAfterTheory = repository.state.value.getCaseReadiness(caseDef)
        assertTrue(readinessAfterTheory.hasSuspect)
        assertTrue(readinessAfterTheory.isReadyForAccusation)

        // 10. Submit Perfect Accusation
        val submission = AccusationSubmission(
            suspectId = "S004",
            motiveKey = "MOTIVE_FINANCIAL",
            weaponKey = "WEAPON_PAPERWEIGHT",
            selectedEvidenceIds = listOf("E002", "E006", "E008", "E018", "E019", "E020")
        )
        val eval = repository.submitAccusation(submission)
        assertTrue(eval.isCorrectCulprit)
        assertFalse(eval.isPremature)
        assertTrue(eval.isPerfect)
        assertEquals(CaseStatus.SOLVED_PERFECT, eval.resultStatus)
        assertEquals(Screen.CASE_RESULT, repository.state.value.currentScreen)
    }

    @Test
    fun `wrong accusation handles failures and keeps case active for continuation`() {
        repository.startNewCase()
        caseDef.crimeSceneHotspots.forEach { repository.inspectHotspot(it) }
        repository.selectEvidenceForDetail("E001")

        // Intentionally accuse Maya Voss (S001)
        val wrongSubmission = AccusationSubmission(
            suspectId = "S001",
            motiveKey = "MOTIVE_FAMILY",
            weaponKey = "WEAPON_GLASS",
            selectedEvidenceIds = listOf("E007")
        )

        val eval = repository.submitAccusation(wrongSubmission)
        assertFalse(eval.isCorrectCulprit)
        assertTrue(eval.feedbackMessage.isNotEmpty())
        assertNotEquals(Screen.CASE_RESULT, repository.state.value.currentScreen)
        assertNotEquals(CaseStatus.SOLVED, repository.state.value.caseStatus)
        assertNotEquals(CaseStatus.SOLVED_PERFECT, repository.state.value.caseStatus)

        // Verify state is preserved and player can dismiss feedback and continue
        assertNotNull(repository.state.value.lastAccusationEvaluation)
        repository.clearAccusationFeedback()
        assertNull(repository.state.value.lastAccusationEvaluation)
    }

    @Test
    fun `save and reload preserves mid-game progress accurately`() {
        repository.startNewCase()
        repository.inspectHotspot(caseDef.crimeSceneHotspots.first { it.id == "hotspot_desk" })
        repository.selectSuspectForInterview("S002")
        repository.askQuestion(caseDef.questions.first { it.id == "Q_VIC_1" })
        repository.updateTheorySuspect("S004")
        repository.updateTheoryMotive("MOTIVE_FINANCIAL")

        val stateBefore = repository.state.value
        assertEquals("S002", stateBefore.selectedSuspectId)
        assertTrue(stateBefore.discoveredEvidenceIds.contains("E004"))
        assertTrue(stateBefore.askedQuestionIds.contains("Q_VIC_1"))
        assertEquals("S004", stateBefore.playerTheory.suspectId)
        assertEquals("MOTIVE_FINANCIAL", stateBefore.playerTheory.motiveKey)

        // Instantiate new repository to simulate app restart
        val freshRepo = CaseRepository(context, soundManager, caseDef)
        val stateAfter = freshRepo.state.value

        assertEquals(stateBefore.caseId, stateAfter.caseId)
        assertEquals(stateBefore.caseStatus, stateAfter.caseStatus)
        assertEquals(stateBefore.discoveredEvidenceIds, stateAfter.discoveredEvidenceIds)
        assertEquals(stateBefore.askedQuestionIds, stateAfter.askedQuestionIds)
        assertEquals(stateBefore.playerTheory.suspectId, stateAfter.playerTheory.suspectId)
        assertEquals(stateBefore.playerTheory.motiveKey, stateAfter.playerTheory.motiveKey)
    }

    @Test
    fun `reactive suspect behavior transitions dynamically through interrogation`() {
        repository.startNewCase()
        val daniel = caseDef.getSuspect("S004")!!

        // Initially Calm
        var state = repository.state.value
        assertEquals(
            SuspectBehaviorState.CALM,
            daniel.getDynamicBehaviorState(
                isCleared = false,
                hasContradictionExposed = false,
                hasMotiveExposed = false,
                askedCount = 0,
                presentedEvidenceCount = 0
            )
        )

        // After multiple questions or evidence presentation, becomes Defensive
        assertEquals(
            SuspectBehaviorState.DEFENSIVE,
            daniel.getDynamicBehaviorState(
                isCleared = false,
                hasContradictionExposed = false,
                hasMotiveExposed = false,
                askedCount = 3,
                presentedEvidenceCount = 0
            )
        )

        // When presented with contradiction evidence, becomes Cornered
        assertEquals(
            SuspectBehaviorState.CORNERED,
            daniel.getDynamicBehaviorState(
                isCleared = false,
                hasContradictionExposed = true,
                hasMotiveExposed = false,
                askedCount = 3,
                presentedEvidenceCount = 1
            )
        )

        // Cleared suspect becomes Alibi Verified
        val victor = caseDef.getSuspect("S002")!!
        assertEquals(
            SuspectBehaviorState.ALIBI_VERIFIED,
            victor.getDynamicBehaviorState(
                isCleared = true,
                hasContradictionExposed = false,
                hasMotiveExposed = false,
                askedCount = 2,
                presentedEvidenceCount = 1
            )
        )
    }
}

