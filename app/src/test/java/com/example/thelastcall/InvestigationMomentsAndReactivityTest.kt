package com.example.thelastcall

import com.example.thelastcall.data.*
import com.example.thelastcall.engine.InvestigationEngine
import com.example.thelastcall.engine.ReasoningEngine
import org.junit.Assert.*
import org.junit.Test

class InvestigationMomentsAndReactivityTest {

    @Test
    fun `test investigation moments trigger on evidence discovery`() {
        val caseDef = Case002Data
        var state = CaseState(caseId = "CASE-002")

        // Initially no moments triggered
        val initialMoments = InvestigationEngine.evaluateInvestigationMoments(state, caseDef)
        assertTrue(initialMoments.isEmpty())

        // Discover EVD-05 (Autopsy)
        state = state.copy(discoveredEvidenceIds = setOf("EVD-05"))
        val momentsAfterEVD05 = InvestigationEngine.evaluateInvestigationMoments(state, caseDef)
        assertEquals(1, momentsAfterEVD05.size)
        assertEquals("MOMENT_002_01_CELLAR_DISCREPANCY", momentsAfterEVD05.first().id)

        // Once marked as seen, it should not be returned again
        state = state.copy(seenMomentIds = setOf("MOMENT_002_01_CELLAR_DISCREPANCY"))
        val momentsAfterSeen = InvestigationEngine.evaluateInvestigationMoments(state, caseDef)
        assertTrue(momentsAfterSeen.isEmpty())
    }

    @Test
    fun `test rooftop forensic connection moment triggers with both evidence items`() {
        val caseDef = Case002Data
        var state = CaseState(caseId = "CASE-002", discoveredEvidenceIds = setOf("EVD-06"))

        // Only EVD-06 discovered -> rooftop moment not triggered yet
        val moments1 = InvestigationEngine.evaluateInvestigationMoments(state, caseDef)
        assertTrue(moments1.none { it.id == "MOMENT_002_03_ROOFTOP_CONNECTION" })

        // Discover EVD-08 (Rooftop Planter) -> moment triggers
        state = state.copy(discoveredEvidenceIds = setOf("EVD-06", "EVD-08"))
        val moments2 = InvestigationEngine.evaluateInvestigationMoments(state, caseDef)
        val rooftopMoment = moments2.find { it.id == "MOMENT_002_03_ROOFTOP_CONNECTION" }
        assertNotNull(rooftopMoment)
        assertTrue(rooftopMoment?.isMajorBreakthrough == true)
    }

    @Test
    fun `test suspect dynamic greeting changes based on behavior state`() {
        val caseDef = Case002Data
        val suspect = caseDef.getSuspect("SUS-01") ?: error("Suspect not found")

        // 1. Initial State -> CALM
        val stateCalm = CaseState(caseId = "CASE-002")
        val greetingCalm = suspect.getDynamicGreeting(stateCalm, caseDef)
        assertTrue("Calm greeting expected", greetingCalm.contains("ready to answer your questions"))

        // 2. Presented evidence -> NERVOUS
        val stateNervous = CaseState(
            caseId = "CASE-002",
            presentedEvidenceRecords = mapOf("SUS-01" to setOf("EVD-13"))
        )
        val greetingNervous = suspect.getDynamicGreeting(stateNervous, caseDef)
        assertTrue("Nervous greeting expected", greetingNervous.contains("telling you everything I can"))

        // 3. Contradiction exposed -> CORNERED
        val stateCornered = CaseState(
            caseId = "CASE-002",
            unlockedContradictionIds = setOf("C001_DAVID_DEPARTURE")
        )
        val greetingCornered = suspect.getDynamicGreeting(stateCornered, caseDef)
        assertTrue("Cornered greeting expected", greetingCornered.contains("don't know what you're talking about"))

        // 4. Cleared -> ALIBI_VERIFIED
        val stateCleared = CaseState(
            caseId = "CASE-002",
            clearedSuspectIds = setOf("SUS-01")
        )
        val greetingCleared = suspect.getDynamicGreeting(stateCleared, caseDef)
        assertTrue("Cleared greeting expected", greetingCleared.contains("alibi is verified"))
    }

    @Test
    fun `test automatic deduction formed on forensic evidence synthesis`() {
        val caseDef = Case002Data
        val state = CaseState(caseId = "CASE-002", discoveredEvidenceIds = setOf("EVD-07", "EVD-08"))
        val deductions = ReasoningEngine.evaluateAutomaticDeductions(state, caseDef)
        assertTrue("D002_ROOFTOP_TRUE_SCENE must be automatically formed", deductions.contains("D002_ROOFTOP_TRUE_SCENE"))
    }

    @Test
    fun `test optional lead is unlocked when prerequisite evidence is discovered`() {
        val caseDef = Case002Data
        val optionalLead = caseDef.leads.find { it.id == "LEAD_002_OPTIONAL_TITAN_DEAL" }
        assertNotNull(optionalLead)
        assertTrue(optionalLead?.isOptional == true)

        val stateBefore = CaseState(caseId = "CASE-002")
        assertEquals(LeadStatus.LOCKED, optionalLead?.getStatus(stateBefore, caseDef))

        val stateAfter = CaseState(caseId = "CASE-002", discoveredEvidenceIds = setOf("EVD-16"))
        assertEquals(LeadStatus.OPTIONAL, optionalLead?.getStatus(stateAfter, caseDef))
    }
}
