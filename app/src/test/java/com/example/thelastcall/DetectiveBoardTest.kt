package com.example.thelastcall

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.thelastcall.audio.SoundManager
import com.example.thelastcall.data.*
import com.example.thelastcall.engine.ReasoningEngine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DetectiveBoardTest {

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
    fun `fresh case start has empty custom board connections`() {
        val state = repository.state.value
        assertEquals("CASE-001", state.caseId)
        assertTrue(state.customConnections.isEmpty())
        assertTrue(state.discoveredEvidenceIds.isEmpty())
    }

    @Test
    fun `valid connection between evidence and related suspect succeeds`() {
        val caseDef = CaseRegistry.getCase("CASE-001")!!
        val hotspot = caseDef.crimeSceneHotspots.first()
        repository.inspectHotspot(hotspot)

        val evId = hotspot.primaryEvidenceId
        val evidence = caseDef.getEvidence(evId)!!
        val relatedSuspectId = evidence.relatedSuspects.first()

        val result = repository.addBoardConnection(evId, relatedSuspectId, ReasoningRelationship.CONNECTS)
        assertTrue(result.isValid)
        assertTrue(result.label == "INVOLVES" || result.label == "LOCATES")
        assertEquals(1, repository.state.value.customConnections.size)

        val connection = repository.state.value.customConnections.first()
        assertEquals(evId, connection.sourceId)
        assertEquals(relatedSuspectId, connection.targetId)
    }

    @Test
    fun `valid connection between corroborating evidence succeeds`() {
        val caseDef = CaseRegistry.getCase("CASE-001")!!
        val ev1 = caseDef.getEvidence("E001")!!
        val ev2 = caseDef.getEvidence("E002")!!

        val result = repository.addBoardConnection(ev1.id, ev2.id, ReasoningRelationship.SUPPORTS)
        assertTrue(result.isValid)
        assertTrue(result.label == "CORROBORATES" || result.label == "SUPPORTS")
        assertEquals(1, repository.state.value.customConnections.size)
    }

    @Test
    fun `invalid connection returns non-punishing guidance without adding connection`() {
        val result = repository.addBoardConnection("INVALID_A", "INVALID_B", ReasoningRelationship.SUPPORTS)
        assertFalse(result.isValid)
        assertEquals("That connection cannot currently be established from the available evidence.", result.feedbackMessage)
        assertTrue(repository.state.value.customConnections.isEmpty())
    }

    @Test
    fun `connecting node to itself is rejected`() {
        val result = repository.addBoardConnection("E001", "E001", ReasoningRelationship.CONNECTS)
        assertFalse(result.isValid)
        assertEquals("Cannot connect a node to itself.", result.feedbackMessage)
        assertTrue(repository.state.value.customConnections.isEmpty())
    }

    @Test
    fun `removing a board connection updates state`() {
        val caseDef = CaseRegistry.getCase("CASE-001")!!
        val result = repository.addBoardConnection("E001", "E002", ReasoningRelationship.SUPPORTS)
        assertTrue(result.isValid)
        assertEquals(1, repository.state.value.customConnections.size)

        val connId = repository.state.value.customConnections.first().id
        repository.removeBoardConnection(connId)
        assertTrue(repository.state.value.customConnections.isEmpty())
    }

    @Test
    fun `board connections persist across repository reloads`() {
        val caseDef = CaseRegistry.getCase("CASE-001")!!
        val result = repository.addBoardConnection("E001", "E002", ReasoningRelationship.SUPPORTS)
        assertTrue(result.isValid)

        // Reload repository from storage
        val newRepo = CaseRepository(context, soundManager)
        val loadedState = newRepo.state.value
        assertEquals(1, loadedState.customConnections.size)
        assertEquals("E001", loadedState.customConnections.first().sourceId)
        assertEquals("E002", loadedState.customConnections.first().targetId)
    }

    @Test
    fun `case isolation preserves independent connections for Case 001 and Case 002`() {
        // 1. Add connection in Case 001
        val res1 = repository.addBoardConnection("E001", "E002", ReasoningRelationship.SUPPORTS)
        assertTrue(res1.isValid)
        assertEquals(1, repository.state.value.customConnections.size)

        // 2. Switch to Case 002
        repository.loadCase("CASE-002")
        val case002Def = CaseRegistry.getCase("CASE-002")!!
        assertEquals("CASE-002", repository.state.value.caseId)
        assertTrue(repository.state.value.customConnections.isEmpty())

        // 3. Add connection in Case 002
        val c2Ev1 = case002Def.getEvidence("EVD-01")!!
        val c2Ev2 = case002Def.getEvidence("EVD-02")!!
        val res2 = repository.addBoardConnection(c2Ev1.id, c2Ev2.id, ReasoningRelationship.SUPPORTS)
        assertTrue(res2.isValid)
        assertEquals(1, repository.state.value.customConnections.size)
        assertEquals("EVD-01", repository.state.value.customConnections.first().sourceId)

        // 4. Switch back to Case 001
        repository.loadCase("CASE-001")
        assertEquals("CASE-001", repository.state.value.caseId)
        assertEquals(1, repository.state.value.customConnections.size)
        assertEquals("E001", repository.state.value.customConnections.first().sourceId)

        // 5. Switch back to Case 002
        repository.loadCase("CASE-002")
        assertEquals("CASE-002", repository.state.value.caseId)
        assertEquals(1, repository.state.value.customConnections.size)
        assertEquals("EVD-01", repository.state.value.customConnections.first().sourceId)
    }

    @Test
    fun `resetting case clears board connections without affecting other cases`() {
        // Add connections in Case 001 and Case 002
        repository.addBoardConnection("E001", "E002", ReasoningRelationship.SUPPORTS)
        repository.loadCase("CASE-002")
        val case002Def = CaseRegistry.getCase("CASE-002")!!
        val c2Ev1 = case002Def.getEvidence("EVD-01")!!
        val c2Ev2 = case002Def.getEvidence("EVD-02")!!
        repository.addBoardConnection(c2Ev1.id, c2Ev2.id, ReasoningRelationship.SUPPORTS)

        // Reset Case 002
        repository.resetCurrentCase()
        assertTrue(repository.state.value.customConnections.isEmpty())

        // Switch back to Case 001 - its connections remain intact
        repository.loadCase("CASE-001")
        assertEquals(1, repository.state.value.customConnections.size)
    }
}
