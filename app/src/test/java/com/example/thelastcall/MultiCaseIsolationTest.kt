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
class MultiCaseIsolationTest {

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
    fun `case registry properly initializes case 001 and case 002`() {
        val allCases = CaseRegistry.getAllCases()
        assertEquals(2, allCases.size)

        val case001 = CaseRegistry.getCase("CASE-001")
        assertNotNull(case001)
        assertEquals("Last Call", case001?.title)
        assertTrue(case001?.isAvailable == true)
        assertFalse(case001?.isPlaceholder == true)

        val case002 = CaseRegistry.getCase("CASE-002")
        assertNotNull(case002)
        assertEquals("The Last Round", case002?.title)
        assertTrue(case002?.isAvailable == true)
        assertFalse(case002?.isPlaceholder == true)

        val availableCases = CaseRegistry.getAvailableCases()
        assertEquals(2, availableCases.size)
        assertEquals(listOf("CASE-001", "CASE-002"), availableCases.map { it.id })
    }

    @Test
    fun `case validator validates all registered cases successfully`() {
        val reports = CaseValidator.validateAllRegisteredCases()
        assertEquals(2, reports.size)
        assertTrue(reports.all { it.isValid })
        assertTrue(reports.all { it.errors.isEmpty() })
    }

    @Test
    fun `case 001 progress does not leak into case 002 state`() {
        // Start Case 001 and discover clues
        repository.startNewCase()
        val phoneHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_phone" }
        repository.inspectHotspot(phoneHotspot)
        repository.addPlayerNote("Hypothesis: Call was placed at 10:42 PM.")

        val state001 = repository.state.value
        assertEquals("CASE-001", state001.caseId)
        assertTrue(state001.discoveredEvidenceIds.contains("E001"))
        assertEquals(1, state001.playerNotes.size)

        // Inspect Case 002 state via getCaseState without switching
        val state002NonActive = repository.getCaseState("CASE-002")
        assertEquals("CASE-002", state002NonActive.caseId)
        assertTrue(state002NonActive.discoveredEvidenceIds.isEmpty())
        assertTrue(state002NonActive.playerNotes.isEmpty())

        // Active state remains Case 001
        assertEquals("CASE-001", repository.state.value.caseId)

        // Switch to Case 002
        repository.loadCase("CASE-002")
        val state002Active = repository.state.value
        assertEquals("CASE-002", state002Active.caseId)
        assertTrue(state002Active.discoveredEvidenceIds.isEmpty())
        assertTrue(state002Active.playerNotes.isEmpty())

        // Switch back to Case 001
        repository.loadCase("CASE-001")
        val state001Reloaded = repository.state.value
        assertEquals("CASE-001", state001Reloaded.caseId)
        assertTrue(state001Reloaded.discoveredEvidenceIds.contains("E001"))
        assertEquals(1, state001Reloaded.playerNotes.size)
        assertEquals("Hypothesis: Call was placed at 10:42 PM.", state001Reloaded.playerNotes[0].text)
    }

    @Test
    fun `resetting case 001 only resets case 001 and preserves settings and other cases`() {
        // Change settings
        repository.updateSettings(sound = false, haptics = false, textSpeed = TextSpeed.FAST, hints = false)

        // Start and make progress in Case 001
        repository.startNewCase()
        val phoneHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_phone" }
        repository.inspectHotspot(phoneHotspot)
        assertTrue(repository.state.value.discoveredEvidenceIds.isNotEmpty())

        // Reset Case 001
        repository.resetCase("CASE-001")

        val stateAfterReset = repository.state.value
        assertEquals("CASE-001", stateAfterReset.caseId)
        assertTrue(stateAfterReset.discoveredEvidenceIds.isEmpty())

        // Settings are untouched
        assertFalse(stateAfterReset.settings.soundEnabled)
        assertFalse(stateAfterReset.settings.hapticsEnabled)
        assertEquals(TextSpeed.FAST, stateAfterReset.settings.textSpeed)
        assertFalse(stateAfterReset.settings.hintsEnabled)
    }

    @Test
    fun `bidirectional case progression maintains complete isolation`() {
        // Start and make progress in Case 002
        repository.loadCase("CASE-002")
        repository.startNewCase()
        val cellarHotspot = Case002Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_body" }
        repository.inspectHotspot(cellarHotspot)
        repository.addPlayerNote("Case 002 Note: Staged cellar body.")

        val state002 = repository.state.value
        assertEquals("CASE-002", state002.caseId)
        assertTrue(state002.discoveredEvidenceIds.contains("EVD-01"))
        assertEquals(1, state002.playerNotes.size)

        // Switch to Case 001 and make progress in Case 001
        repository.loadCase("CASE-001")
        repository.startNewCase()
        val deskHotspot = Case001Data.CRIME_SCENE_HOTSPOTS.first { it.id == "hotspot_desk" }
        repository.inspectHotspot(deskHotspot)
        repository.addPlayerNote("Case 001 Note: Desk papers found.")

        val state001 = repository.state.value
        assertEquals("CASE-001", state001.caseId)
        assertTrue(state001.discoveredEvidenceIds.contains("E004"))
        assertFalse(state001.discoveredEvidenceIds.contains("EVD-01"))
        assertEquals(1, state001.playerNotes.size)
        assertEquals("Case 001 Note: Desk papers found.", state001.playerNotes[0].text)

        // Switch back to Case 002 and verify Case 002 is untouched
        repository.loadCase("CASE-002")
        val state002Restored = repository.state.value
        assertEquals("CASE-002", state002Restored.caseId)
        assertTrue(state002Restored.discoveredEvidenceIds.contains("EVD-01"))
        assertFalse(state002Restored.discoveredEvidenceIds.contains("E004"))
        assertEquals(1, state002Restored.playerNotes.size)
        assertEquals("Case 002 Note: Staged cellar body.", state002Restored.playerNotes[0].text)
    }
}
