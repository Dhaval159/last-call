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
class InvestigationLeadSystemTest {

    private lateinit var context: Context
    private lateinit var soundManager: SoundManager
    private lateinit var repository: CaseRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        soundManager = SoundManager(context)
        context.getSharedPreferences("the_last_call_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        CaseRegistry.resetRegistry()
        repository = CaseRepository(context, soundManager)
    }

    @Test
    fun `case validator validates all lead structures in both cases without errors`() {
        val reports = CaseValidator.validateAllRegisteredCases()
        assertEquals(2, reports.size)
        reports.forEach { report ->
            assertTrue("Validation failed for ${report.caseId}: ${report.errors}", report.isValid)
            assertTrue("Errors present for ${report.caseId}: ${report.errors}", report.errors.isEmpty())
        }

        val case002 = CaseRegistry.getCase("CASE-002")!!
        assertTrue(case002.leads.isNotEmpty())
        assertEquals("LEAD_002_01_CELLAR_SCENE", case002.leads.first().id)
        assertEquals("THE SCENE IN THE CELLAR", case002.leads.first().title)
        assertEquals(5, case002.leads.first().objectives.size)
    }

    @Test
    fun `initial state of case 002 has Lead 01 available and active`() {
        repository.loadCase("CASE-002")
        val state = repository.state.value
        val caseDef = repository.caseDefinition

        val currentLead = caseDef.getCurrentInvestigationLead(state)
        assertNotNull(currentLead)
        assertEquals("LEAD_002_01_CELLAR_SCENE", currentLead?.id)
        assertEquals(LeadStatus.ACTIVE, currentLead?.getStatus(state, caseDef))
        assertEquals(0, state.completedLeadIds.size)
        assertEquals(0, state.completedLeadObjectiveIds.size)
    }

    @Test
    fun `openLeadInvestigation navigates to INVESTIGATION_LEAD screen`() {
        repository.loadCase("CASE-002")
        repository.openLeadInvestigation()

        val state = repository.state.value
        assertEquals(Screen.INVESTIGATION_LEAD, state.currentScreen)
        assertEquals("LEAD_002_01_CELLAR_SCENE", state.activeLeadId)
    }

    @Test
    fun `following a lead objective sets contextual navigation and navigates to target`() {
        repository.loadCase("CASE-002")
        val caseDef = repository.caseDefinition
        val lead = caseDef.getLead("LEAD_002_01_CELLAR_SCENE")!!
        val objective = lead.objectives.first() // Search Cellar -> CrimeScene

        repository.followLeadObjective(objective, lead.id)

        val state = repository.state.value
        assertEquals(Screen.CRIME_SCENE, state.currentScreen)
        assertNotNull(state.leadNavigationContext)
        assertEquals(lead.id, state.leadNavigationContext?.sourceLeadId)
        assertEquals(objective.id, state.leadNavigationContext?.sourceObjectiveId)

        // Returning from lead context returns to INVESTIGATION_LEAD screen
        repository.returnFromLeadContext()
        val returnedState = repository.state.value
        assertEquals(Screen.INVESTIGATION_LEAD, returnedState.currentScreen)
        assertNull(returnedState.leadNavigationContext)
    }

    @Test
    fun `completing vertical slice objectives completes Lead 01 and unlocks Lead 02`() {
        repository.loadCase("CASE-002")
        val caseDef = repository.caseDefinition
        val lead01 = caseDef.getLead("LEAD_002_01_CELLAR_SCENE")!!
        val lead02 = caseDef.getLead("LEAD_002_02_TIMELINES_AND_DEPARTURES")!!

        // Initially Lead 02 is locked
        assertEquals(LeadStatus.LOCKED, lead02.getStatus(repository.state.value, caseDef))

        // Step 1: Discover Cellar Evidence EVD-01, EVD-02, EVD-03
        caseDef.getHotspot("hotspot_body")?.let { repository.inspectHotspot(it) } // EVD-01
        caseDef.getHotspot("hotspot_body")?.let { repository.inspectHotspot(it) } // EVD-02
        caseDef.getHotspot("hotspot_wine_case")?.let { repository.inspectHotspot(it) } // EVD-03

        // Step 2: Examine Body (EVD-05)
        repository.selectEvidenceForDetail("EVD-01") // unlocks EVD-05

        // Step 3: Interview Theo (Q_TM_1)
        repository.selectSuspectForInterview("SUS-02")
        caseDef.getQuestion("Q_TM_1")?.let { repository.askQuestion(it) }

        // Step 4: Check Dock Door (EVD-04)
        caseDef.getHotspot("hotspot_dock_door")?.let { repository.inspectHotspot(it) }

        // Step 5: Form Staged Scene Deduction (D001_STAGED_SCENE)
        repository.testDeduction("EVD-01", "EVD-05", ReasoningRelationship.ESTABLISHES)

        val stateAfterDeduction = repository.state.value

        // Lead 01 should be completed
        assertTrue("Lead 01 should be completed", lead01.isCompleted(stateAfterDeduction, caseDef))
        assertTrue("Completed lead IDs should contain Lead 01", stateAfterDeduction.completedLeadIds.contains(lead01.id))

        // Lead 02 should now be unlocked and available
        assertTrue("Lead 02 should be unlocked", lead02.isUnlocked(stateAfterDeduction, caseDef))
        assertEquals(LeadStatus.ACTIVE, lead02.getStatus(stateAfterDeduction, caseDef))

        // Next current investigation lead should now be Lead 02
        val nextActiveLead = caseDef.getCurrentInvestigationLead(stateAfterDeduction)
        assertEquals("LEAD_002_02_TIMELINES_AND_DEPARTURES", nextActiveLead?.id)
    }

    @Test
    fun `major breakthrough lead triggers breakthrough log entry and notification`() {
        repository.loadCase("CASE-002")
        val caseDef = repository.caseDefinition

        // Manually complete lead 03 (Rooftop Forensics) which is a major breakthrough
        val lead03 = caseDef.getLead("LEAD_002_03_ROOFTOP_FORENSICS")!!
        assertTrue(lead03.isMajorBreakthrough)

        repository.completeLead(lead03.id)

        val state = repository.state.value
        assertTrue(state.completedLeadIds.contains(lead03.id))
        assertTrue(state.activityLog.any { it.detail.contains("INVESTIGATION BREAKTHROUGH") })
    }

    @Test
    fun `lead state persists across repository reloads`() {
        repository.loadCase("CASE-002")
        val caseDef = repository.caseDefinition

        // Advance progress on Lead 01
        caseDef.getHotspot("hotspot_body")?.let { repository.inspectHotspot(it) }
        caseDef.getHotspot("hotspot_body")?.let { repository.inspectHotspot(it) }
        caseDef.getHotspot("hotspot_wine_case")?.let { repository.inspectHotspot(it) }
        repository.startLead("LEAD_002_01_CELLAR_SCENE")

        val stateBefore = repository.state.value
        assertTrue(stateBefore.completedLeadObjectiveIds.contains("OBJ_002_01_SEARCH_CELLAR"))
        assertEquals("LEAD_002_01_CELLAR_SCENE", stateBefore.activeLeadId)

        // Create new repository instance simulating app restart
        val newRepo = CaseRepository(context, soundManager)
        newRepo.loadCase("CASE-002")
        val reloadedState = newRepo.state.value

        assertEquals(stateBefore.activeLeadId, reloadedState.activeLeadId)
        assertTrue(reloadedState.completedLeadObjectiveIds.contains("OBJ_002_01_SEARCH_CELLAR"))
        assertEquals(stateBefore.completedLeadIds, reloadedState.completedLeadIds)
    }

    @Test
    fun `resetting a case resets all lead state cleanly`() {
        repository.loadCase("CASE-002")
        val caseDef = repository.caseDefinition

        caseDef.getHotspot("hotspot_body")?.let { repository.inspectHotspot(it) }
        repository.completeLead("LEAD_002_01_CELLAR_SCENE")

        assertTrue(repository.state.value.completedLeadIds.contains("LEAD_002_01_CELLAR_SCENE"))

        repository.resetCase("CASE-002")
        val resetState = repository.state.value

        assertEquals(0, resetState.completedLeadIds.size)
        assertEquals(0, resetState.completedLeadObjectiveIds.size)
        assertEquals(CaseStatus.NOT_STARTED, resetState.caseStatus)
    }

    @Test
    fun `case isolation preserves independent lead states between Case 001 and Case 002`() {
        // Play Case 001
        repository.loadCase("CASE-001")
        val case001Def = repository.caseDefinition
        case001Def.getHotspot("hotspot_phone")?.let { repository.inspectHotspot(it) } // E001
        case001Def.getHotspot("hotspot_desk")?.let { repository.inspectHotspot(it) } // E004
        case001Def.getHotspot("hotspot_glass")?.let { repository.inspectHotspot(it) } // E007
        case001Def.getHotspot("hotspot_paperweight")?.let { repository.inspectHotspot(it) } // E008

        val case001State = repository.state.value
        assertTrue(case001State.completedLeadObjectiveIds.contains("OBJ_001_01_SEARCH_DESK"))

        // Switch to Case 002
        repository.loadCase("CASE-002")
        val case002State = repository.state.value
        assertEquals(0, case002State.completedLeadObjectiveIds.size)
        assertEquals(0, case002State.completedLeadIds.size)
        assertFalse(case002State.completedLeadObjectiveIds.contains("OBJ_001_01_SEARCH_DESK"))

        // Switch back to Case 001
        repository.loadCase("CASE-001")
        val case001StateReloaded = repository.state.value
        assertTrue(case001StateReloaded.completedLeadObjectiveIds.contains("OBJ_001_01_SEARCH_DESK"))
    }
}
