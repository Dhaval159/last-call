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
class Case002PlaythroughTest {

    private lateinit var context: Context
    private lateinit var soundManager: SoundManager
    private lateinit var repository: CaseRepository
    private val caseDef: CaseDefinition = Case002Data

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        soundManager = SoundManager(context)
        context.getSharedPreferences("the_last_call_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        repository = CaseRepository(context, soundManager, caseDef)
    }

    @Test
    fun `full canonical playthrough from start to perfect conviction`() {
        // 1. Start Case 002
        repository.startNewCase()
        assertEquals("CASE-002", repository.state.value.caseId)
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
        assertTrue(
            "Primary and secondary hotspot evidence should be discovered",
            repository.state.value.discoveredEvidenceIds.containsAll(
                listOf(
                    "EVD-01", "EVD-02", "EVD-03", "EVD-04", "EVD-06",
                    "EVD-07", "EVD-08", "EVD-09", "EVD-10", "EVD-12",
                    "EVD-13", "EVD-18", "EVD-19", "EVD-20", "EVD-21",
                    "EVD-22", "EVD-23", "EVD-26"
                )
            )
        )

        // Discover autopsy / lab report items by inspecting body evidence
        repository.selectEvidenceForDetail("EVD-01")
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("EVD-05"))
        assertTrue(repository.state.value.discoveredEvidenceIds.contains("EVD-11"))

        // 4. Interrogate all 6 suspects
        caseDef.suspects.forEach { suspect ->
            repository.selectSuspectForInterview(suspect.id)
            val questions = caseDef.questions.filter { it.suspectId == suspect.id }
            questions.forEach { q ->
                val reqMet = q.requiredEvidenceId == null || repository.state.value.discoveredEvidenceIds.contains(q.requiredEvidenceId)
                if (reqMet) {
                    repository.askQuestion(q)
                }
            }
        }
        assertTrue(
            "All suspects interviewed",
            repository.state.value.interviewedSuspectIds.containsAll(
                listOf("SUS-01", "SUS-02", "SUS-03", "SUS-04", "SUS-05", "SUS-06")
            )
        )

        // 5. Present evidence to trigger and resolve contradiction challenges
        // Contradiction 1: David (EVD-13)
        val outcomeDavid13 = repository.presentEvidence("SUS-01", "EVD-13")
        assertNotNull("Challenge should be presented for David EVD-13", outcomeDavid13.challenge)
        val chalDavid = outcomeDavid13.challenge!!
        val correctKeyDavid = chalDavid.options.first { it.isCorrect }.key
        val attemptDavid = repository.attemptContradictionChallenge(correctKeyDavid)
        assertNotNull(attemptDavid)
        assertTrue(attemptDavid!!.accepted)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C001_DAVID_DEPARTURE"))

        // Contradiction 2: Theo (EVD-18)
        val outcomeTheo18 = repository.presentEvidence("SUS-02", "EVD-18")
        assertNotNull("Challenge should be presented for Theo EVD-18", outcomeTheo18.challenge)
        val chalTheo = outcomeTheo18.challenge!!
        val correctKeyTheo = chalTheo.options.first { it.isCorrect }.key
        val attemptTheo = repository.attemptContradictionChallenge(correctKeyTheo)
        assertNotNull(attemptTheo)
        assertTrue(attemptTheo!!.accepted)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C002_THEO_INVENTORY"))

        // Contradiction 3: Priya (EVD-12)
        val outcomePriya12 = repository.presentEvidence("SUS-03", "EVD-12")
        assertNotNull("Challenge should be presented for Priya EVD-12", outcomePriya12.challenge)
        val chalPriya = outcomePriya12.challenge!!
        val correctKeyPriya = chalPriya.options.first { it.isCorrect }.key
        val attemptPriya = repository.attemptContradictionChallenge(correctKeyPriya)
        assertNotNull(attemptPriya)
        assertTrue(attemptPriya!!.accepted)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C003_PRIYA_RETURN"))

        // Contradiction 4: Whit (EVD-17)
        val outcomeWhit17 = repository.presentEvidence("SUS-06", "EVD-17")
        assertNotNull("Challenge should be presented for Whit EVD-17", outcomeWhit17.challenge)
        val chalWhit = outcomeWhit17.challenge!!
        val correctKeyWhit = chalWhit.options.first { it.isCorrect }.key
        val attemptWhit = repository.attemptContradictionChallenge(correctKeyWhit)
        assertNotNull(attemptWhit)
        assertTrue(attemptWhit!!.accepted)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C004_WHIT_ALIBI"))

        // Contradiction 5: Renata (EVD-22) - The Decisive Contradiction
        val outcomeRenata22 = repository.presentEvidence("SUS-04", "EVD-22")
        assertNotNull("Challenge should be presented for Renata EVD-22", outcomeRenata22.challenge)
        val chalRenata = outcomeRenata22.challenge!!
        val correctKeyRenata = chalRenata.options.first { it.isCorrect }.key
        val attemptRenata = repository.attemptContradictionChallenge(correctKeyRenata)
        assertNotNull(attemptRenata)
        assertTrue(attemptRenata!!.accepted)
        assertTrue(repository.state.value.unlockedContradictionIds.contains("C005_RENATA_LOG_CONTRADICTION"))

        // 6. Present clearing evidence to innocent suspects
        repository.presentEvidence("SUS-01", "EVD-15") // Clears David
        repository.presentEvidence("SUS-02", "EVD-19") // Clears Theo
        repository.presentEvidence("SUS-03", "EVD-26") // Clears Priya
        repository.presentEvidence("SUS-05", "EVD-10") // Clears Iris
        repository.presentEvidence("SUS-06", "EVD-17") // Clears Whit
        assertTrue(
            "All 5 innocent suspects should be cleared",
            repository.state.value.clearedSuspectIds.containsAll(
                listOf("SUS-01", "SUS-02", "SUS-03", "SUS-05", "SUS-06")
            )
        )

        // 7. Present motive and scene evidence to Renata
        repository.presentEvidence("SUS-04", "EVD-23")
        assertTrue("Motive should be discovered", repository.state.value.hasDiscoveredMotive)

        // Present rooftop evidence to Renata to reveal tell EVD-24
        repository.presentEvidence("SUS-04", "EVD-08")
        assertTrue("Behavioral tell EVD-24 discovered", repository.state.value.discoveredEvidenceIds.contains("EVD-24"))

        // Ask remaining follow-up questions
        caseDef.questions.filter { it.suspectId == "SUS-04" }.forEach { q ->
            if (repository.state.value.discoveredEvidenceIds.contains(q.requiredEvidenceId ?: "")) {
                repository.askQuestion(q)
            }
        }

        // 8. Verify deductions
        val deductions = repository.state.value.unlockedDeductionIds
        assertTrue("Staged scene deduction formed", deductions.contains("D001_STAGED_SCENE"))
        assertTrue("Rooftop true scene deduction formed", deductions.contains("D002_ROOFTOP_TRUE_SCENE"))
        assertTrue("Alibis cleared deduction formed", deductions.contains("D003_ALIBIS_CLEARED"))
        assertTrue("Dual security systems deduction formed", deductions.contains("D007_DUAL_SECURITY_SYSTEMS"))
        assertTrue("Renata sole culprit deduction formed", deductions.contains("D008_RENATA_SOLE_CULPRIT"))

        // 9. Build Player Theory
        repository.updateTheorySuspect("SUS-04")
        repository.updateTheoryMotive("MOTIVE_HEARTBREAK")
        repository.updateTheoryWeapon("WEAPON_STONE_PLANTER")
        caseDef.culpritSolution.criticalEvidenceIds.forEach {
            repository.toggleTheorySupportingEvidence(it)
        }

        val readiness = repository.state.value.getCaseReadiness(caseDef)
        assertTrue(readiness.hasSuspect)
        assertEquals(TheoryConfidence.ESTABLISHED, readiness.motiveConfidence)
        assertEquals(TheoryConfidence.ESTABLISHED, readiness.opportunityConfidence)
        assertEquals(TheoryConfidence.ESTABLISHED, readiness.methodConfidence)
        assertTrue(readiness.hasContradiction)
        assertTrue(readiness.isReadyForAccusation)

        // 10. Submit Perfect Final Accusation
        val submission = AccusationSubmission(
            suspectId = "SUS-04",
            motiveKey = "MOTIVE_HEARTBREAK",
            weaponKey = "WEAPON_STONE_PLANTER",
            selectedEvidenceIds = listOf("EVD-06", "EVD-07", "EVD-08", "EVD-10", "EVD-21", "EVD-22", "EVD-23", "EVD-24")
        )
        val eval = repository.submitAccusation(submission)
        assertTrue("Correct culprit identified", eval.isCorrectCulprit)
        assertFalse("Accusation is not premature", eval.isPremature)
        assertTrue("Accusation is perfect investigation", eval.isPerfect)
        assertEquals(CaseStatus.SOLVED_PERFECT, eval.resultStatus)
        assertEquals(Screen.CASE_RESULT, repository.state.value.currentScreen)
    }

    @Test
    fun `wrong accusation handles failures and keeps case active for continuation`() {
        repository.startNewCase()
        caseDef.crimeSceneHotspots.forEach { repository.inspectHotspot(it) }

        // Accuse David Ostrow (SUS-01) incorrectly
        val wrongSubmission = AccusationSubmission(
            suspectId = "SUS-01",
            motiveKey = "MOTIVE_FINANCIAL_DEAL",
            weaponKey = "WEAPON_WINE_BOTTLE",
            selectedEvidenceIds = listOf("EVD-01", "EVD-03")
        )

        val eval = repository.submitAccusation(wrongSubmission)
        assertFalse(eval.isCorrectCulprit)
        assertTrue(eval.feedbackMessage.isNotEmpty())
        assertNotEquals(Screen.CASE_RESULT, repository.state.value.currentScreen)
        assertNotEquals(CaseStatus.SOLVED, repository.state.value.caseStatus)

        // Verify player can dismiss feedback and continue
        assertNotNull(repository.state.value.lastAccusationEvaluation)
        repository.clearAccusationFeedback()
        assertNull(repository.state.value.lastAccusationEvaluation)
    }

    @Test
    fun `save and reload preserves mid-game progress accurately`() {
        repository.startNewCase()
        repository.inspectHotspot(caseDef.crimeSceneHotspots.first { it.id == "hotspot_body" })
        repository.selectSuspectForInterview("SUS-01")
        repository.askQuestion(caseDef.questions.first { it.id == "Q_DO_1" })
        repository.updateTheorySuspect("SUS-04")
        repository.updateTheoryMotive("MOTIVE_HEARTBREAK")

        val stateBefore = repository.state.value
        assertEquals("SUS-01", stateBefore.selectedSuspectId)
        assertTrue(stateBefore.discoveredEvidenceIds.contains("EVD-01"))
        assertTrue(stateBefore.askedQuestionIds.contains("Q_DO_1"))
        assertEquals("SUS-04", stateBefore.playerTheory.suspectId)
        assertEquals("MOTIVE_HEARTBREAK", stateBefore.playerTheory.motiveKey)

        // Simulate app restart with new repository
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
        val renata = caseDef.getSuspect("SUS-04")!!

        // Initially Calm
        assertEquals(
            SuspectBehaviorState.CALM,
            renata.getDynamicBehaviorState(
                isCleared = false,
                hasContradictionExposed = false,
                hasMotiveExposed = false,
                askedCount = 0,
                presentedEvidenceCount = 0
            )
        )

        // After asking multiple questions, becomes Defensive
        assertEquals(
            SuspectBehaviorState.DEFENSIVE,
            renata.getDynamicBehaviorState(
                isCleared = false,
                hasContradictionExposed = false,
                hasMotiveExposed = false,
                askedCount = 3,
                presentedEvidenceCount = 0
            )
        )

        // When caught in contradiction, becomes Cornered
        assertEquals(
            SuspectBehaviorState.CORNERED,
            renata.getDynamicBehaviorState(
                isCleared = false,
                hasContradictionExposed = true,
                hasMotiveExposed = false,
                askedCount = 3,
                presentedEvidenceCount = 1
            )
        )

        // Cleared suspect becomes Alibi Verified
        val david = caseDef.getSuspect("SUS-01")!!
        assertEquals(
            SuspectBehaviorState.ALIBI_VERIFIED,
            david.getDynamicBehaviorState(
                isCleared = true,
                hasContradictionExposed = false,
                hasMotiveExposed = false,
                askedCount = 2,
                presentedEvidenceCount = 1
            )
        )
    }
}
