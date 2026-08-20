package com.example.thelastcall.engine

import com.example.thelastcall.data.*

data class HotspotInspectionResult(
    val primary: EvidenceItem?,
    val secondary: EvidenceItem?,
    val newlyDiscoveredEvidence: List<EvidenceItem>,
    val updatedDiscoveredEvidenceIds: Set<String>,
    val updatedInspectedHotspotIds: Set<String>
)

data class QuestionAskResult(
    val updatedAskedQuestionIds: Set<String>,
    val updatedRecordedStatementIds: Set<String>,
    val updatedDiscoveredEvidenceIds: Set<String>,
    val newlyDiscoveredEvidenceIds: Set<String>
)

data class EvidencePresentationResult(
    val reaction: EvidenceReaction?,
    val updatedPresentedEvidenceRecords: Map<String, Set<String>>,
    val updatedClearedSuspectIds: Set<String>,
    val updatedDiscoveredEvidenceIds: Set<String>,
    val newlyDiscoveredEvidenceIds: Set<String>,
    val hasDiscoveredMotive: Boolean,
    val pendingChallenge: ContradictionChallenge?
)

/** Result surfaced to the UI after a player presents evidence. */
data class EvidencePresentationOutcome(
    val reaction: EvidenceReaction?,
    val challenge: ContradictionChallenge?
)

/** Result of a player's attempt to resolve a [ContradictionChallenge]. */
data class ChallengeAttemptResult(
    val accepted: Boolean,
    val challenge: ContradictionChallenge,
    val selectedOption: ChallengeOption,
    val feedback: String,
    val reaction: EvidenceReaction? = null
)

object InvestigationEngine {

    fun inspectHotspot(
        hotspot: CrimeSceneHotspot,
        currentState: CaseState,
        caseDef: CaseDefinition
    ): HotspotInspectionResult {
        val newlyDiscovered = mutableListOf<EvidenceItem>()
        var primary: EvidenceItem? = null
        var secondary: EvidenceItem? = null

        // Primary evidence check
        if (!currentState.discoveredEvidenceIds.contains(hotspot.primaryEvidenceId)) {
            caseDef.getEvidence(hotspot.primaryEvidenceId)?.let {
                newlyDiscovered.add(it)
                primary = it
            }
        }

        // Secondary evidence check
        if (hotspot.secondaryEvidenceId != null && !currentState.discoveredEvidenceIds.contains(hotspot.secondaryEvidenceId)) {
            val reqMet = hotspot.requiredEvidenceForSecondary == null ||
                    currentState.discoveredEvidenceIds.contains(hotspot.requiredEvidenceForSecondary)
            if (reqMet) {
                caseDef.getEvidence(hotspot.secondaryEvidenceId)?.let {
                    newlyDiscovered.add(it)
                    secondary = it
                }
            }
        }

        val allDiscoveredIds = currentState.discoveredEvidenceIds + newlyDiscovered.map { it.id }
        val allInspectedHotspots = currentState.inspectedHotspotIds + hotspot.id

        return HotspotInspectionResult(
            primary = primary,
            secondary = secondary,
            newlyDiscoveredEvidence = newlyDiscovered,
            updatedDiscoveredEvidenceIds = allDiscoveredIds,
            updatedInspectedHotspotIds = allInspectedHotspots
        )
    }

    fun inspectEvidence(
        evidenceId: String,
        currentState: CaseState,
        caseDef: CaseDefinition
    ): Set<String> {
        val ev = caseDef.getEvidence(evidenceId) ?: return emptySet()
        return ev.unlocksEvidenceOnInspect.filter { !currentState.discoveredEvidenceIds.contains(it) }.toSet()
    }

    fun askQuestion(
        question: InterviewQuestion,
        currentState: CaseState,
        caseDef: CaseDefinition
    ): QuestionAskResult {
        val updatedStatements = if (question.recordedStatementId != null) {
            currentState.recordedStatementIds + question.recordedStatementId
        } else {
            currentState.recordedStatementIds
        }

        val newEvidenceFromQuestion = question.unlocksEvidenceIds.filter {
            !currentState.discoveredEvidenceIds.contains(it)
        }.toSet()

        return QuestionAskResult(
            updatedAskedQuestionIds = currentState.askedQuestionIds + question.id,
            updatedRecordedStatementIds = updatedStatements,
            updatedDiscoveredEvidenceIds = currentState.discoveredEvidenceIds + newEvidenceFromQuestion,
            newlyDiscoveredEvidenceIds = newEvidenceFromQuestion
        )
    }

    fun presentEvidence(
        suspectId: String,
        evidenceId: String,
        currentState: CaseState,
        caseDef: CaseDefinition
    ): EvidencePresentationResult {
        val reaction = caseDef.getReaction(suspectId, evidenceId)
        val currentRecords = currentState.presentedEvidenceRecords.toMutableMap()
        val currentPresentedForSuspect = (currentRecords[suspectId] ?: emptySet()) + evidenceId
        currentRecords[suspectId] = currentPresentedForSuspect

        // If the case data defines a contradiction challenge for this presentation,
        // the reaction (and its unlocks) is withheld until the player explains
        // the conflict correctly. The contradiction is NEVER auto-unlocked here.
        val challenge = caseDef.getContradictionChallenge(suspectId, evidenceId)
        val challengePending = challenge != null && reaction?.triggersContradictionId != null

        val updatedCleared = if (!challengePending && reaction?.clearsSuspectCriticalPeriod == true) {
            currentState.clearedSuspectIds + suspectId
        } else {
            currentState.clearedSuspectIds
        }

        val newBonusEvidence = if (!challengePending) {
            reaction?.unlocksEvidenceIds?.filter {
                !currentState.discoveredEvidenceIds.contains(it)
            }?.toSet() ?: emptySet()
        } else {
            emptySet()
        }

        val motiveDiscovered = currentState.hasDiscoveredMotive || (reaction?.triggersMotiveId != null)

        return EvidencePresentationResult(
            reaction = if (challengePending) null else reaction,
            updatedPresentedEvidenceRecords = currentRecords,
            updatedClearedSuspectIds = updatedCleared,
            updatedDiscoveredEvidenceIds = currentState.discoveredEvidenceIds + newBonusEvidence,
            newlyDiscoveredEvidenceIds = newBonusEvidence,
            hasDiscoveredMotive = motiveDiscovered,
            pendingChallenge = challenge.takeIf { challengePending }
        )
    }

    /**
     * Evaluates a player's answer to a [ContradictionChallenge]. A correct answer
     * is never determined here or in the UI — it lives in the case data, so the
     * answer cannot leak before the player responds.
     */
    fun evaluateContradictionChallenge(
        challenge: ContradictionChallenge,
        optionKey: String,
        currentState: CaseState,
        caseDef: CaseDefinition
    ): ChallengeAttemptResult {
        val option = challenge.options.firstOrNull { it.key == optionKey }
            ?: challenge.options.first()
        val accepted = option.isCorrect
        return ChallengeAttemptResult(
            accepted = accepted,
            challenge = challenge,
            selectedOption = option,
            feedback = if (accepted) challenge.successFeedback else option.feedback.ifEmpty { challenge.failurePrompt },
            reaction = if (accepted) caseDef.getReaction(challenge.suspectId, challenge.evidenceId) else null
        )
    }

    fun evaluateTimelineUnlocks(state: CaseState, caseDef: CaseDefinition): Set<String> {
        val unlocked = state.unlockedTimelineEventIds.toMutableSet()
        caseDef.timelineEvents.forEach { event ->
            if (event.requiredEvidenceForUnlock.isEmpty() ||
                event.requiredEvidenceForUnlock.all { state.discoveredEvidenceIds.contains(it) }
            ) {
                unlocked.add(event.id)
            }
        }
        return unlocked
    }

    fun evaluateObjectives(state: CaseState, caseDef: CaseDefinition): Set<String> {
        val completed = state.completedObjectiveIds.toMutableSet()
        caseDef.objectives.forEach { objective ->
            if (objective.condition != null && objective.condition.isMet(state, caseDef)) {
                completed.add(objective.id)
            }
        }
        return completed
    }

    fun evaluateLeadObjectives(state: CaseState, caseDef: CaseDefinition): Set<String> {
        val completed = state.completedLeadObjectiveIds.toMutableSet()
        caseDef.leads.forEach { lead ->
            lead.objectives.forEach { obj ->
                if (obj.condition != null && obj.condition.isMet(state, caseDef)) {
                    completed.add(obj.id)
                }
            }
        }
        return completed
    }

    fun evaluateCompletedLeads(state: CaseState, caseDef: CaseDefinition): Set<String> {
        val completed = state.completedLeadIds.toMutableSet()
        caseDef.leads.forEach { lead ->
            if (lead.isCompleted(state, caseDef)) {
                completed.add(lead.id)
            }
        }
        return completed
    }

    fun evaluateInvestigationMoments(state: CaseState, caseDef: CaseDefinition): List<InvestigationMoment> {
        return caseDef.investigationMoments.filter { moment ->
            !state.seenMomentIds.contains(moment.id) &&
                (moment.triggerCondition == null || moment.triggerCondition.isMet(state, caseDef))
        }.sortedByDescending { it.priority }
    }
}
