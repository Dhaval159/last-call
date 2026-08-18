package com.example.thelastcall

import com.example.thelastcall.data.Case002Data
import com.example.thelastcall.data.CaseRegistry
import com.example.thelastcall.data.CaseValidator
import org.junit.Assert.*
import org.junit.Test

class Case002DataIntegrityTest {

    @Test
    fun `verify evidence items integrity and count`() {
        assertEquals(26, Case002Data.EVIDENCE_LIST.size)
        val ids = Case002Data.EVIDENCE_LIST.map { it.id }.toSet()
        assertEquals("Evidence IDs must be unique", 26, ids.size)

        // Check required critical evidence items exist
        val criticalItems = Case002Data.EVIDENCE_LIST.filter { it.isCritical }
        assertTrue("Must have critical evidence items", criticalItems.isNotEmpty())
        val criticalIds = criticalItems.map { it.id }.toSet()
        assertTrue(
            "Critical evidence must include key items",
            criticalIds.containsAll(
                listOf(
                    "EVD-05", "EVD-06", "EVD-07", "EVD-08", "EVD-10",
                    "EVD-13", "EVD-21", "EVD-22", "EVD-23"
                )
            )
        )
    }

    @Test
    fun `verify suspects integrity`() {
        assertEquals(6, Case002Data.SUSPECTS.size)
        val suspectIds = Case002Data.SUSPECTS.map { it.id }.toSet()
        assertEquals(
            setOf("SUS-01", "SUS-02", "SUS-03", "SUS-04", "SUS-05", "SUS-06"),
            suspectIds
        )

        // Verify each suspect has name, occupation, relationship, and initials
        Case002Data.SUSPECTS.forEach { suspect ->
            assertTrue("Suspect ${suspect.id} name must not be blank", suspect.name.isNotBlank())
            assertTrue("Suspect ${suspect.id} occupation must not be blank", suspect.occupation.isNotBlank())
            assertTrue("Suspect ${suspect.id} relationship must not be blank", suspect.relationship.isNotBlank())
            assertTrue("Suspect ${suspect.id} initials must not be blank", suspect.initials.isNotBlank())
        }
    }

    @Test
    fun `verify questions link to valid suspects, statements, and evidence`() {
        val suspectIds = Case002Data.SUSPECTS.map { it.id }.toSet()
        val evidenceIds = Case002Data.EVIDENCE_LIST.map { it.id }.toSet()
        val statementIds = Case002Data.STATEMENTS.map { it.id }.toSet()

        Case002Data.QUESTIONS.forEach { question ->
            assertTrue(
                "Question ${question.id} suspect must exist",
                suspectIds.contains(question.suspectId)
            )
            if (question.requiredEvidenceId != null) {
                assertTrue(
                    "Question ${question.id} required evidence ${question.requiredEvidenceId} must exist",
                    evidenceIds.contains(question.requiredEvidenceId)
                )
            }
            if (question.recordedStatementId != null) {
                assertTrue(
                    "Question ${question.id} recorded statement ${question.recordedStatementId} must exist",
                    statementIds.contains(question.recordedStatementId)
                )
            }
            question.unlocksEvidenceIds.forEach { evId ->
                assertTrue(
                    "Question ${question.id} unlocks evidence $evId which must exist",
                    evidenceIds.contains(evId)
                )
            }
        }
    }

    @Test
    fun `verify timeline events link to valid evidence and suspects`() {
        val suspectIds = Case002Data.SUSPECTS.map { it.id }.toSet()
        val evidenceIds = Case002Data.EVIDENCE_LIST.map { it.id }.toSet()

        assertEquals(21, Case002Data.TIMELINE_EVENTS.size)
        Case002Data.TIMELINE_EVENTS.forEach { event ->
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
        val evidenceIds = Case002Data.EVIDENCE_LIST.map { it.id }.toSet()
        val suspectIds = Case002Data.SUSPECTS.map { it.id }.toSet()
        val contradictionIds = Case002Data.CONTRADICTIONS.map { it.id }.toSet()

        Case002Data.CONTRADICTIONS.forEach { contradiction ->
            assertTrue(
                "Contradiction ${contradiction.id} suspect ${contradiction.suspectId} must exist",
                suspectIds.contains(contradiction.suspectId)
            )
            contradiction.evidenceIds.forEach { evId ->
                assertTrue(
                    "Contradiction ${contradiction.id} evidence $evId must exist",
                    evidenceIds.contains(evId)
                )
            }
        }

        Case002Data.DEDUCTIONS.forEach { deduction ->
            deduction.requiredEvidence.forEach { evId ->
                assertTrue(
                    "Deduction ${deduction.id} required evidence $evId must exist",
                    evidenceIds.contains(evId)
                )
            }
            deduction.requiredContradictions.forEach { cId ->
                assertTrue(
                    "Deduction ${deduction.id} required contradiction $cId must exist",
                    contradictionIds.contains(cId)
                )
            }
        }
    }

    @Test
    fun `verify contradiction challenges resolve valid options and correct keys`() {
        val suspectIds = Case002Data.SUSPECTS.map { it.id }.toSet()
        val evidenceIds = Case002Data.EVIDENCE_LIST.map { it.id }.toSet()
        val contradictionIds = Case002Data.CONTRADICTIONS.map { it.id }.toSet()

        assertEquals(5, Case002Data.CONTRADICTION_CHALLENGES.size)
        Case002Data.CONTRADICTION_CHALLENGES.forEach { challenge ->
            assertTrue("Challenge ${challenge.id} suspect must exist", suspectIds.contains(challenge.suspectId))
            assertTrue("Challenge ${challenge.id} evidence must exist", evidenceIds.contains(challenge.evidenceId))
            assertTrue("Challenge ${challenge.id} contradiction must exist", contradictionIds.contains(challenge.contradictionId))
            assertTrue("Challenge ${challenge.id} must have options", challenge.options.isNotEmpty())
            assertTrue("Challenge ${challenge.id} must have exactly one correct option", challenge.options.count { it.isCorrect } == 1)
        }
    }

    @Test
    fun `verify crime scene hotspots valid mappings`() {
        val evidenceIds = Case002Data.EVIDENCE_LIST.map { it.id }.toSet()

        Case002Data.CRIME_SCENE_HOTSPOTS.forEach { hotspot ->
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
        val report = CaseValidator.validate(Case002Data)
        assertTrue("Case 002 validation should have no errors: ${report.errors}", report.isValid)
        assertTrue("Errors list should be empty", report.errors.isEmpty())
    }

    @Test
    fun `verify case registry retrieves case 002 correctly`() {
        val registeredCase = CaseRegistry.getCase("CASE-002")
        assertNotNull(registeredCase)
        assertEquals(Case002Data.id, registeredCase?.id)
        assertEquals(Case002Data.title, registeredCase?.title)
        assertTrue(registeredCase?.isAvailable == true)
        assertFalse(registeredCase?.isPlaceholder == true)
    }
}
