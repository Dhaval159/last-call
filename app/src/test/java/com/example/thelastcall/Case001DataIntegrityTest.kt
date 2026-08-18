package com.example.thelastcall

import com.example.thelastcall.data.Case001Data
import org.junit.Assert.*
import org.junit.Test

class Case001DataIntegrityTest {

    @Test
    fun `verify evidence items integrity and count`() {
        assertEquals(20, Case001Data.EVIDENCE_LIST.size)
        val ids = Case001Data.EVIDENCE_LIST.map { it.id }.toSet()
        assertEquals("Evidence IDs must be unique", 20, ids.size)

        // Check required critical evidence items exist
        val criticalItems = Case001Data.EVIDENCE_LIST.filter { it.isCritical }
        assertTrue("Must have critical evidence items", criticalItems.isNotEmpty())
        val criticalIds = criticalItems.map { it.id }.toSet()
        assertTrue(criticalIds.containsAll(listOf("E002", "E006", "E014", "E018", "E019", "E020")))
    }

    @Test
    fun `verify suspects integrity`() {
        assertEquals(4, Case001Data.SUSPECTS.size)
        val suspectIds = Case001Data.SUSPECTS.map { it.id }.toSet()
        assertEquals(setOf("S001", "S002", "S003", "S004"), suspectIds)

        // Verify each suspect has name and description
        Case001Data.SUSPECTS.forEach { suspect ->
            assertTrue(suspect.name.isNotBlank())
            assertTrue(suspect.occupation.isNotBlank())
            assertTrue(suspect.relationship.isNotBlank())
            assertTrue(suspect.initials.isNotBlank())
        }
    }

    @Test
    fun `verify questions link to valid suspects and evidence`() {
        val suspectIds = Case001Data.SUSPECTS.map { it.id }.toSet()
        val evidenceIds = Case001Data.EVIDENCE_LIST.map { it.id }.toSet()

        Case001Data.QUESTIONS.forEach { question ->
            assertTrue("Question ${question.id} suspect must exist", suspectIds.contains(question.suspectId))
            if (question.requiredEvidenceId != null) {
                assertTrue(
                    "Question ${question.id} required evidence ${question.requiredEvidenceId} must exist",
                    evidenceIds.contains(question.requiredEvidenceId)
                )
            }
        }
    }

    @Test
    fun `verify timeline events link to valid evidence and suspects`() {
        val suspectIds = Case001Data.SUSPECTS.map { it.id }.toSet()
        val evidenceIds = Case001Data.EVIDENCE_LIST.map { it.id }.toSet()

        assertEquals(10, Case001Data.TIMELINE_EVENTS.size)
        Case001Data.TIMELINE_EVENTS.forEach { event ->
            if (event.relatedSuspectId != null) {
                assertTrue(
                    "Timeline ${event.id} suspect ${event.relatedSuspectId} must exist",
                    suspectIds.contains(event.relatedSuspectId)
                )
            }
            if (event.sourceEvidenceId != null) {
                assertTrue(
                    "Timeline ${event.id} source evidence ${event.sourceEvidenceId} must exist",
                    evidenceIds.contains(event.sourceEvidenceId)
                )
            }
            event.requiredEvidenceForUnlock.forEach { req ->
                assertTrue(
                    "Timeline ${event.id} required evidence $req must exist",
                    evidenceIds.contains(req)
                )
            }
        }
    }

    @Test
    fun `verify deductions and contradictions have valid dependencies`() {
        val evidenceIds = Case001Data.EVIDENCE_LIST.map { it.id }.toSet()

        Case001Data.CONTRADICTIONS.forEach { contradiction ->
            contradiction.evidenceIds.forEach { evId ->
                assertTrue(
                    "Contradiction ${contradiction.id} evidence $evId must exist",
                    evidenceIds.contains(evId)
                )
            }
        }

        Case001Data.DEDUCTIONS.forEach { deduction ->
            deduction.requiredEvidence.forEach { evId ->
                assertTrue(
                    "Deduction ${deduction.id} required evidence $evId must exist",
                    evidenceIds.contains(evId)
                )
            }
        }
    }

    @Test
    fun `verify crime scene hotspots valid mappings`() {
        val evidenceIds = Case001Data.EVIDENCE_LIST.map { it.id }.toSet()

        Case001Data.CRIME_SCENE_HOTSPOTS.forEach { hotspot ->
            assertTrue(
                "Hotspot ${hotspot.id} primary evidence ${hotspot.primaryEvidenceId} must exist",
                evidenceIds.contains(hotspot.primaryEvidenceId)
            )
            if (hotspot.secondaryEvidenceId != null) {
                assertTrue(
                    "Hotspot ${hotspot.id} secondary evidence ${hotspot.secondaryEvidenceId} must exist",
                    evidenceIds.contains(hotspot.secondaryEvidenceId)
                )
            }
        }
    }

    @Test
    fun `verify case passes CaseValidator without errors`() {
        val report = com.example.thelastcall.data.CaseValidator.validate(Case001Data)
        assertTrue("Case 001 validation should have no errors: ${report.errors}", report.isValid)
        assertTrue("Errors should be empty", report.errors.isEmpty())
    }

    @Test
    fun `verify case registry retrieves case 001 correctly`() {
        val registryCase = com.example.thelastcall.data.CaseRegistry.getDefaultCase()
        assertEquals(Case001Data.id, registryCase.id)
        assertEquals(Case001Data.title, registryCase.title)
    }
}
