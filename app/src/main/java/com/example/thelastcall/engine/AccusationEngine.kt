package com.example.thelastcall.engine

import com.example.thelastcall.data.*

object AccusationEngine {

    fun evaluateReadiness(state: CaseState, caseDef: CaseDefinition): CaseReadiness {
        val sol = caseDef.culpritSolution

        val hasSuspect = state.playerTheory.suspectId != null

        val hasMotiveEvidence = sol.requiredMotiveEvidenceIds.any { state.discoveredEvidenceIds.contains(it) } || state.hasDiscoveredMotive
        val motiveConfidence = when {
            hasMotiveEvidence -> TheoryConfidence.ESTABLISHED
            state.discoveredEvidenceIds.any { evId -> caseDef.getEvidence(evId)?.category == EvidenceCategory.DOCUMENTARY } -> TheoryConfidence.SUPPORTED
            state.recordedStatementIds.isNotEmpty() -> TheoryConfidence.SUGGESTED
            else -> TheoryConfidence.UNKNOWN
        }

        val hasRequiredContradiction = sol.requiredContradictionIds.isEmpty() ||
                sol.requiredContradictionIds.all { state.unlockedContradictionIds.contains(it) }
        val hasTimeAnchor = sol.requiredTimeAnchorEvidenceIds.isEmpty() ||
                sol.requiredTimeAnchorEvidenceIds.all { state.discoveredEvidenceIds.contains(it) }

        val opportunityConfidence = when {
            hasRequiredContradiction && hasTimeAnchor -> TheoryConfidence.ESTABLISHED
            hasTimeAnchor || sol.requiredTimeAnchorEvidenceIds.any { state.discoveredEvidenceIds.contains(it) } -> TheoryConfidence.SUPPORTED
            state.recordedStatementIds.isNotEmpty() -> TheoryConfidence.SUGGESTED
            else -> TheoryConfidence.UNKNOWN
        }

        val weaponFound = state.discoveredEvidenceIds.any { evId ->
            val ev = caseDef.getEvidence(evId)
            ev?.iconType == "weapon" || ev?.category == EvidenceCategory.PHYSICAL && ev.isCritical
        }

        val methodConfidence = when {
            weaponFound && (state.unlockedDeductionIds.isNotEmpty() || state.discoveredEvidenceIds.size >= 6) -> TheoryConfidence.ESTABLISHED
            weaponFound -> TheoryConfidence.SUPPORTED
            else -> TheoryConfidence.UNKNOWN
        }

        val criticalEvidence = if (sol.criticalEvidenceIds.isNotEmpty()) sol.criticalEvidenceIds.toSet()
        else caseDef.evidenceList.filter { it.isCritical }.map { it.id }.toSet()
        val supportingCount = state.discoveredEvidenceIds.intersect(criticalEvidence).size

        val isReady = hasSuspect &&
                motiveConfidence == TheoryConfidence.ESTABLISHED &&
                opportunityConfidence == TheoryConfidence.ESTABLISHED &&
                methodConfidence == TheoryConfidence.ESTABLISHED &&
                hasRequiredContradiction

        val hint = when {
            !hasSuspect -> "Designate a primary suspect in your Theory."
            motiveConfidence != TheoryConfidence.ESTABLISHED -> "Uncover key investigative findings to solidify motive."
            opportunityConfidence != TheoryConfidence.ESTABLISHED -> "Review access records and compare them with suspect statements to break alibis."
            methodConfidence != TheoryConfidence.ESTABLISHED -> "Inspect physical scene objects and struggle marks to confirm the murder weapon."
            !hasRequiredContradiction -> "Compare suspect testimony against digital surveillance evidence in the Reasoning tab."
            else -> "All core pillars of the prosecution are established. You are ready to indict."
        }

        return CaseReadiness(
            hasSuspect = hasSuspect,
            motiveConfidence = motiveConfidence,
            opportunityConfidence = opportunityConfidence,
            methodConfidence = methodConfidence,
            hasContradiction = hasRequiredContradiction,
            supportingEvidenceCount = supportingCount,
            isReadyForAccusation = isReady,
            guidanceHint = hint
        )
    }

    fun evaluateAccusation(
        submission: AccusationSubmission,
        state: CaseState,
        caseDef: CaseDefinition
    ): AccusationEvaluation {
        return caseDef.evaluateAccusation(submission, state)
    }
}
