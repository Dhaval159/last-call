package com.example.thelastcall.engine

import com.example.thelastcall.data.*

data class BoardConnectionResult(
    val isValid: Boolean,
    val canonicalRelationship: ReasoningRelationship,
    val label: String,
    val feedbackMessage: String,
    val unlockedDeduction: Deduction? = null,
    val unlockedContradiction: Contradiction? = null
)

object ReasoningEngine {

    fun checkContradiction(id1: String, id2: String, caseDef: CaseDefinition): Contradiction? {
        return caseDef.checkContradictionPair(id1, id2)
    }

    fun checkDeduction(
        id1: String,
        id2: String,
        relationship: ReasoningRelationship,
        caseDef: CaseDefinition
    ): Pair<Deduction?, String?> {
        return caseDef.checkDeductionPair(id1, id2, relationship)
    }

    fun validateBoardConnection(
        id1: String,
        id2: String,
        requestedRelationship: ReasoningRelationship,
        caseDef: CaseDefinition,
        state: CaseState
    ): BoardConnectionResult {
        val clean1 = id1.trim()
        val clean2 = id2.trim()

        if (clean1 == clean2) {
            return BoardConnectionResult(
                isValid = false,
                canonicalRelationship = requestedRelationship,
                label = "INVALID",
                feedbackMessage = "Cannot connect a node to itself."
            )
        }

        // 1. Check for Contradiction
        val contradiction = caseDef.checkContradictionPair(clean1, clean2)
        if (contradiction != null && requestedRelationship == ReasoningRelationship.CONTRADICTS) {
            return BoardConnectionResult(
                isValid = true,
                canonicalRelationship = ReasoningRelationship.CONTRADICTS,
                label = "CONTRADICTS",
                feedbackMessage = "Critical contradiction established: ${contradiction.title}.",
                unlockedContradiction = contradiction
            )
        }

        // 2. Check for Deduction
        val (deduction, _) = caseDef.checkDeductionPair(clean1, clean2, requestedRelationship)
        if (deduction != null) {
            return BoardConnectionResult(
                isValid = true,
                canonicalRelationship = requestedRelationship,
                label = requestedRelationship.displayName.uppercase(),
                feedbackMessage = "Deduction formed: ${deduction.title}.",
                unlockedDeduction = deduction
            )
        }

        // 3. Evidence <-> Suspect
        val ev1 = caseDef.getEvidence(clean1) ?: caseDef.getEvidence(clean2)
        val sus1 = caseDef.getSuspect(clean1) ?: caseDef.getSuspect(clean2)
        if (ev1 != null && sus1 != null) {
            if (ev1.relatedSuspects.contains(sus1.id)) {
                val label = if (state.clearedSuspectIds.contains(sus1.id)) "LOCATES" else "INVOLVES"
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.CONNECTS,
                    label = label,
                    feedbackMessage = "Connection recorded: ${ev1.name} $label ${sus1.name}."
                )
            }
        }

        // 4. Evidence <-> Evidence
        val evA = caseDef.getEvidence(clean1)
        val evB = caseDef.getEvidence(clean2)
        if (evA != null && evB != null) {
            if (evA.relatedEvidence.contains(evB.id) || evB.relatedEvidence.contains(evA.id) ||
                evA.unlocksEvidenceOnInspect.contains(evB.id) || evB.unlocksEvidenceOnInspect.contains(evA.id)
            ) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.SUPPORTS,
                    label = "CORROBORATES",
                    feedbackMessage = "Connection recorded: ${evA.name} corroborates ${evB.name}."
                )
            }
        }

        // 5. Statement <-> Suspect
        val st1 = caseDef.getStatement(clean1) ?: caseDef.getStatement(clean2)
        if (st1 != null && sus1 != null) {
            if (st1.suspectId == sus1.id) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.CONNECTS,
                    label = "TESTIMONY",
                    feedbackMessage = "Connection recorded: Statement belongs to ${sus1.name}."
                )
            }
        }

        // 6. Statement <-> Evidence
        if (st1 != null && ev1 != null) {
            if (st1.relatedEvidenceIds.contains(ev1.id)) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.SUPPORTS,
                    label = "SUPPORTS",
                    feedbackMessage = "Connection recorded: Testimony supported by ${ev1.name}."
                )
            }
        }

        // 7. Evidence <-> Timeline
        val tl1 = caseDef.getTimelineEvent(clean1) ?: caseDef.getTimelineEvent(clean2)
        if (ev1 != null && tl1 != null) {
            if (tl1.requiredEvidenceForUnlock.contains(ev1.id) || tl1.sourceEvidenceId == ev1.id) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.ESTABLISHES,
                    label = "LOCATES",
                    feedbackMessage = "Connection recorded: ${ev1.name} anchors timeline event at ${tl1.time}."
                )
            }
        }

        // 8. Statement <-> Timeline
        if (st1 != null && tl1 != null) {
            if (st1.timestamp == tl1.time || st1.suspectId == tl1.relatedSuspectId) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.SUPPORTS,
                    label = "SUPPORTS",
                    feedbackMessage = "Connection recorded: Statement aligns with timeline anchor at ${tl1.time}."
                )
            }
        }

        // 9. Contradiction <-> Statement / Evidence / Deduction
        val con1 = caseDef.getContradiction(clean1) ?: caseDef.getContradiction(clean2)
        if (con1 != null) {
            if (st1 != null && con1.statementIds.contains(st1.id)) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.CONTRADICTS,
                    label = "CONTRADICTS",
                    feedbackMessage = "Connection recorded: Statement refuted by contradiction."
                )
            }
            if (ev1 != null && con1.evidenceIds.contains(ev1.id)) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.ESTABLISHES,
                    label = "ESTABLISHES",
                    feedbackMessage = "Connection recorded: ${ev1.name} proves contradiction."
                )
            }
        }

        // 10. Deduction <-> Evidence / Contradiction
        val ded1 = caseDef.getDeduction(clean1) ?: caseDef.getDeduction(clean2)
        if (ded1 != null) {
            if (ev1 != null && (ded1.requiredEvidence.contains(ev1.id) || ded1.supportingEvidenceIds.contains(ev1.id))) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.ESTABLISHES,
                    label = "ESTABLISHES",
                    feedbackMessage = "Connection recorded: ${ev1.name} forms deductive pillar."
                )
            }
            if (con1 != null && ded1.requiredContradictions.contains(con1.id)) {
                return BoardConnectionResult(
                    isValid = true,
                    canonicalRelationship = ReasoningRelationship.ESTABLISHES,
                    label = "ESTABLISHES",
                    feedbackMessage = "Connection recorded: Contradiction forms foundation for deduction."
                )
            }
        }

        // Incompatible / Unsupported
        return BoardConnectionResult(
            isValid = false,
            canonicalRelationship = requestedRelationship,
            label = "UNSUPPORTED",
            feedbackMessage = "That connection cannot currently be established from the available evidence."
        )
    }

    fun evaluateAutomaticDeductions(state: CaseState, caseDef: CaseDefinition): Set<String> {
        val unlockedDeductions = state.unlockedDeductionIds.toMutableSet()
        caseDef.deductions.forEach { deduction ->
            val evMet = deduction.requiredEvidence.isEmpty() ||
                    deduction.requiredEvidence.all { state.discoveredEvidenceIds.contains(it) }
            val conMet = deduction.requiredContradictions.isEmpty() ||
                    deduction.requiredContradictions.all { state.unlockedContradictionIds.contains(it) }
            if (evMet && conMet) {
                unlockedDeductions.add(deduction.id)
            }
        }
        return unlockedDeductions
    }
}
