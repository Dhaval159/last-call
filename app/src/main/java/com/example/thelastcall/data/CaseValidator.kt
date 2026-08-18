package com.example.thelastcall.data

data class CaseValidationReport(
    val caseId: String,
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

object CaseValidator {

    fun validate(caseDef: CaseDefinition): CaseValidationReport {
        if (caseDef.isPlaceholder) {
            return CaseValidationReport(
                caseId = caseDef.id,
                isValid = true,
                errors = emptyList(),
                warnings = emptyList()
            )
        }
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // 1. Basic Metadata
        if (caseDef.id.isBlank()) errors.add("Case ID cannot be blank.")
        if (caseDef.title.isBlank()) errors.add("Case title cannot be blank.")
        if (caseDef.victimName.isBlank()) errors.add("Victim name cannot be blank.")
        if (caseDef.location.isBlank()) errors.add("Location cannot be blank.")

        val evidenceIds = caseDef.evidenceList.map { it.id }.toSet()
        val suspectIds = caseDef.suspects.map { it.id }.toSet()
        val questionIds = caseDef.questions.map { it.id }.toSet()
        val statementIds = caseDef.statements.map { it.id }.toSet()
        val timelineIds = caseDef.timelineEvents.map { it.id }.toSet()
        val contradictionIds = caseDef.contradictions.map { it.id }.toSet()
        val deductionIds = caseDef.deductions.map { it.id }.toSet()
        val hotspotIds = caseDef.crimeSceneHotspots.map { it.id }.toSet()
        val objectiveIds = caseDef.objectives.map { it.id }.toSet()

        // 2. Evidence Uniqueness & References
        if (caseDef.evidenceList.size != evidenceIds.size) {
            errors.add("Evidence items contain duplicate IDs.")
        }
        caseDef.evidenceList.forEach { ev ->
            if (ev.name.isBlank()) errors.add("Evidence ${ev.id} has a blank name.")
            ev.relatedSuspects.forEach { sId ->
                if (!suspectIds.contains(sId)) {
                    errors.add("Evidence ${ev.id} references non-existent suspect $sId.")
                }
            }
            ev.relatedEvidence.forEach { otherEvId ->
                if (!evidenceIds.contains(otherEvId)) {
                    errors.add("Evidence ${ev.id} references non-existent related evidence $otherEvId.")
                }
            }
            ev.relatedTimelineEvents.forEach { tId ->
                if (!timelineIds.contains(tId)) {
                    errors.add("Evidence ${ev.id} references non-existent timeline event $tId.")
                }
            }
        }

        // 3. Suspects Uniqueness & References
        if (caseDef.suspects.size != suspectIds.size) {
            errors.add("Suspects contain duplicate IDs.")
        }
        caseDef.suspects.forEach { s ->
            if (s.name.isBlank()) errors.add("Suspect ${s.id} has a blank name.")
            if (s.initials.isBlank()) errors.add("Suspect ${s.id} has blank initials.")
        }

        // 4. Questions References
        if (caseDef.questions.size != questionIds.size) {
            errors.add("Questions contain duplicate IDs.")
        }
        caseDef.questions.forEach { q ->
            if (!suspectIds.contains(q.suspectId)) {
                errors.add("Question ${q.id} references non-existent suspect ${q.suspectId}.")
            }
            if (q.requiredEvidenceId != null && !evidenceIds.contains(q.requiredEvidenceId)) {
                errors.add("Question ${q.id} references non-existent required evidence ${q.requiredEvidenceId}.")
            }
            if (q.requiredStatementId != null && !statementIds.contains(q.requiredStatementId)) {
                errors.add("Question ${q.id} references non-existent required statement ${q.requiredStatementId}.")
            }
            if (q.recordedStatementId != null && !statementIds.contains(q.recordedStatementId)) {
                errors.add("Question ${q.id} references non-existent recorded statement ${q.recordedStatementId}.")
            }
        }

        // 5. Statements References
        if (caseDef.statements.size != statementIds.size) {
            errors.add("Statements contain duplicate IDs.")
        }
        caseDef.statements.forEach { st ->
            if (!suspectIds.contains(st.suspectId)) {
                errors.add("Statement ${st.id} references non-existent suspect ${st.suspectId}.")
            }
            st.relatedEvidenceIds.forEach { evId ->
                if (!evidenceIds.contains(evId)) {
                    errors.add("Statement ${st.id} references non-existent related evidence $evId.")
                }
            }
            if (st.contradictionId != null && !contradictionIds.contains(st.contradictionId)) {
                errors.add("Statement ${st.id} references non-existent contradiction ${st.contradictionId}.")
            }
        }

        // 6. Evidence Reactions References
        caseDef.reactions.forEach { r ->
            if (!suspectIds.contains(r.suspectId)) {
                errors.add("Reaction references non-existent suspect ${r.suspectId}.")
            }
            if (!evidenceIds.contains(r.evidenceId)) {
                errors.add("Reaction for suspect ${r.suspectId} references non-existent evidence ${r.evidenceId}.")
            }
            if (r.triggersContradictionId != null && !contradictionIds.contains(r.triggersContradictionId)) {
                errors.add("Reaction for ${r.suspectId}/${r.evidenceId} triggers non-existent contradiction ${r.triggersContradictionId}.")
            }
            if (r.triggersMotiveId != null && !evidenceIds.contains(r.triggersMotiveId) && caseDef.motives.none { it.key == r.triggersMotiveId }) {
                errors.add("Reaction for ${r.suspectId}/${r.evidenceId} triggers non-existent motive ${r.triggersMotiveId}.")
            }
            r.unlocksQuestionIds.forEach { qId ->
                if (!questionIds.contains(qId)) {
                    errors.add("Reaction for ${r.suspectId}/${r.evidenceId} unlocks non-existent question $qId.")
                }
            }
        }

        // 7. Timeline Events References
        if (caseDef.timelineEvents.size != timelineIds.size) {
            errors.add("Timeline events contain duplicate IDs.")
        }
        caseDef.timelineEvents.forEach { t ->
            if (t.sourceEvidenceId != null && !evidenceIds.contains(t.sourceEvidenceId)) {
                errors.add("Timeline event ${t.id} references non-existent source evidence ${t.sourceEvidenceId}.")
            }
            if (t.relatedSuspectId != null && !suspectIds.contains(t.relatedSuspectId)) {
                errors.add("Timeline event ${t.id} references non-existent suspect ${t.relatedSuspectId}.")
            }
            t.requiredEvidenceForUnlock.forEach { reqEvId ->
                if (!evidenceIds.contains(reqEvId)) {
                    errors.add("Timeline event ${t.id} references non-existent required evidence $reqEvId.")
                }
            }
        }

        // 8. Contradictions References
        if (caseDef.contradictions.size != contradictionIds.size) {
            errors.add("Contradictions contain duplicate IDs.")
        }
        caseDef.contradictions.forEach { c ->
            if (!suspectIds.contains(c.suspectId)) {
                errors.add("Contradiction ${c.id} references non-existent suspect ${c.suspectId}.")
            }
            c.statementIds.forEach { sId ->
                if (!statementIds.contains(sId)) {
                    errors.add("Contradiction ${c.id} references non-existent statement $sId.")
                }
            }
            c.evidenceIds.forEach { evId ->
                if (!evidenceIds.contains(evId)) {
                    errors.add("Contradiction ${c.id} references non-existent evidence $evId.")
                }
            }
        }

        // 9. Deductions References
        if (caseDef.deductions.size != deductionIds.size) {
            errors.add("Deductions contain duplicate IDs.")
        }
        caseDef.deductions.forEach { d ->
            d.requiredEvidence.forEach { evId ->
                if (!evidenceIds.contains(evId)) {
                    errors.add("Deduction ${d.id} references non-existent required evidence $evId.")
                }
            }
            d.supportingEvidenceIds.forEach { evId ->
                if (!evidenceIds.contains(evId)) {
                    errors.add("Deduction ${d.id} references non-existent supporting evidence $evId.")
                }
            }
            d.requiredContradictions.forEach { cId ->
                if (!contradictionIds.contains(cId)) {
                    errors.add("Deduction ${d.id} references non-existent required contradiction $cId.")
                }
            }
        }

        // 10. Crime Scene Hotspots
        if (caseDef.crimeSceneHotspots.size != hotspotIds.size) {
            errors.add("Hotspots contain duplicate IDs.")
        }
        caseDef.crimeSceneHotspots.forEach { h ->
            if (!evidenceIds.contains(h.primaryEvidenceId)) {
                errors.add("Hotspot ${h.id} references non-existent primary evidence ${h.primaryEvidenceId}.")
            }
            if (h.secondaryEvidenceId != null && !evidenceIds.contains(h.secondaryEvidenceId)) {
                errors.add("Hotspot ${h.id} references non-existent secondary evidence ${h.secondaryEvidenceId}.")
            }
            if (h.requiredEvidenceForSecondary != null && !evidenceIds.contains(h.requiredEvidenceForSecondary)) {
                errors.add("Hotspot ${h.id} references non-existent required evidence ${h.requiredEvidenceForSecondary}.")
            }
        }

        // 11. Culprit Solution
        val sol = caseDef.culpritSolution
        if (!suspectIds.contains(sol.culpritSuspectId)) {
            errors.add("Culprit solution references non-existent suspect ${sol.culpritSuspectId}.")
        }
        sol.requiredContradictionIds.forEach { cId ->
            if (!contradictionIds.contains(cId)) {
                errors.add("Culprit solution references non-existent required contradiction $cId.")
            }
        }
        sol.requiredMotiveEvidenceIds.forEach { evId ->
            if (!evidenceIds.contains(evId)) {
                errors.add("Culprit solution references non-existent required motive evidence $evId.")
            }
        }
        sol.requiredTimeAnchorEvidenceIds.forEach { evId ->
            if (!evidenceIds.contains(evId)) {
                errors.add("Culprit solution references non-existent required time anchor evidence $evId.")
            }
        }
        sol.criticalEvidenceIds.forEach { evId ->
            if (!evidenceIds.contains(evId)) {
                errors.add("Culprit solution references non-existent critical evidence $evId.")
            }
        }
        sol.clearedSuspectIdsForPerfect.forEach { sId ->
            if (!suspectIds.contains(sId)) {
                errors.add("Culprit solution references non-existent cleared suspect $sId.")
            }
        }

        // Warnings for loose ends
        if (caseDef.motives.none { it.key == sol.correctMotiveKey }) {
            warnings.add("Correct motive key ${sol.correctMotiveKey} not found in case motives list.")
        }
        if (caseDef.weapons.none { it.key == sol.correctWeaponKey }) {
            warnings.add("Correct weapon key ${sol.correctWeaponKey} not found in case weapons list.")
        }

        return CaseValidationReport(
            caseId = caseDef.id,
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }

    fun validateAllRegisteredCases(): List<CaseValidationReport> {
        return CaseRegistry.getAllCases().map { validate(it) }
    }
}
