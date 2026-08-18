package com.example.thelastcall.data

import androidx.compose.ui.graphics.vector.ImageVector

data class IntroFact(
    val title: String,
    val description: String,
    val iconType: String = "info",
    val highlightColorHex: Long = 0xFFE05252
)

data class CaseMotiveOption(
    val key: String,
    val label: String
)

data class CaseWeaponOption(
    val key: String,
    val label: String
)

data class ChronologicalStep(
    val time: String,
    val description: String
)

data class CulpritSolution(
    val culpritSuspectId: String,
    val correctMotiveKey: String,
    val correctWeaponKey: String,
    val requiredContradictionIds: List<String> = emptyList(),
    val requiredMotiveEvidenceIds: List<String> = emptyList(),
    val requiredTimeAnchorEvidenceIds: List<String> = emptyList(),
    val criticalEvidenceIds: List<String> = emptyList(),
    val clearedSuspectIdsForPerfect: List<String> = emptyList(),
    val minEvidenceCountForPerfect: Int = 16,
    val prematureFeedbackTitle: String = "Suspicion is Not Proof",
    val prematureFeedbackMessage: String = "You suspect the right person, but have not assembled the decisive contradiction, time anchor, and motive required for prosecution.",
    val wrongSuspectFeedbackTemplate: String = "The established evidence does not support %s as the perpetrator. The investigation remains open.",
    val solvedTitle: String = "Case Solved",
    val perfectTitle: String = "Perfect Investigation",
    val solvedFeedbackMessage: String = "The suspect's false alibi collapsed under the weight of the evidence and contradictions.",
    val culpritSummaryHeader: String = "CULPRIT CONVICTED",
    val culpritSummaryDetails: String = "",
    val decisiveContradictionSummary: String = "",
    val chronologicalReconstructionSteps: List<ChronologicalStep> = emptyList()
)

interface CaseDefinition {
    val id: String
    val title: String
    val subtitle: String
    val incidentDate: String
    val incidentTime: String
    val location: String
    val victimName: String
    val victimAge: Int
    val victimOccupation: String
    val briefingSummary: String
    val primaryObjectiveText: String
    val initialDialogueText: String
    val introFacts: List<IntroFact>
    val motives: List<CaseMotiveOption>
    val weapons: List<CaseWeaponOption>
    val initialUnlockedTimelineIds: Set<String>

    val evidenceList: List<EvidenceItem>
    val suspects: List<Suspect>
    val questions: List<InterviewQuestion>
    val statements: List<StatementItem>
    val reactions: List<EvidenceReaction>
    val timelineEvents: List<TimelineEvent>
    val objectives: List<Objective>
    val contradictions: List<Contradiction>
    val contradictionChallenges: List<ContradictionChallenge> get() = emptyList()
    val communicationThreads: List<CommunicationThread> get() = emptyList()
    val callLogs: List<CallLogEntry> get() = emptyList()
    val deductions: List<Deduction>
    val customDeductionMessages: Map<Pair<String, String>, String>
    val crimeSceneHotspots: List<CrimeSceneHotspot>
    val culpritSolution: CulpritSolution
    val isPlaceholder: Boolean get() = false
    val isAvailable: Boolean get() = true

    fun getEvidence(id: String): EvidenceItem? =
        evidenceList.find { it.id == id }

    fun getSuspect(id: String): Suspect? =
        suspects.find { it.id == id }

    fun getQuestion(id: String): InterviewQuestion? =
        questions.find { it.id == id }

    fun getStatement(id: String): StatementItem? =
        statements.find { it.id == id }

    fun getTimelineEvent(id: String): TimelineEvent? =
        timelineEvents.find { it.id == id }

    fun getContradiction(id: String): Contradiction? =
        contradictions.find { it.id == id }

    fun getContradictionChallenge(suspectId: String, evidenceId: String): ContradictionChallenge? =
        contradictionChallenges.find { it.suspectId == suspectId && it.evidenceId == evidenceId }

    fun getContradictionChallenge(id: String): ContradictionChallenge? =
        contradictionChallenges.find { it.id == id }

    fun getDeduction(id: String): Deduction? =
        deductions.find { it.id == id }

    fun getHotspot(id: String): CrimeSceneHotspot? =
        crimeSceneHotspots.find { it.id == id }

    fun getReaction(suspectId: String, evidenceId: String): EvidenceReaction? =
        reactions.find { it.suspectId == suspectId && it.evidenceId == evidenceId }

    /** The active detective lead: the first objective not yet followed up. */
    fun getCurrentLead(state: CaseState): Objective? =
        objectives.firstOrNull { !state.completedObjectiveIds.contains(it.id) }

    fun checkContradictionPair(source1Id: String, source2Id: String): Contradiction? {
        val s1 = source1Id.trim()
        val s2 = source2Id.trim()
        return contradictions.find { c ->
            val matches1 = (c.statementIds.contains(s1) && c.evidenceIds.contains(s2)) ||
                    (c.statementIds.contains(s2) && c.evidenceIds.contains(s1))
            val matches2 = (c.evidenceIds.contains(s1) && c.evidenceIds.contains(s2))
            matches1 || matches2
        }
    }

    fun checkDeductionPair(
        source1Id: String,
        source2Id: String,
        relationship: ReasoningRelationship
    ): Pair<Deduction?, String?> {
        val s1 = source1Id.trim()
        val s2 = source2Id.trim()

        val matchingDeduction = deductions.find { d ->
            (d.requiredEvidence.contains(s1) && d.requiredEvidence.contains(s2)) ||
                    (d.supportingEvidenceIds.contains(s1) && d.supportingEvidenceIds.contains(s2))
        }

        if (matchingDeduction != null) {
            return Pair(matchingDeduction, null)
        }

        val customMsg = customDeductionMessages[Pair(s1, s2)] ?: customDeductionMessages[Pair(s2, s1)]
        return Pair(null, customMsg)
    }

    fun evaluateAccusation(
        submission: AccusationSubmission,
        currentState: CaseState
    ): AccusationEvaluation {
        val isCorrectCulprit = submission.suspectId == culpritSolution.culpritSuspectId

        if (!isCorrectCulprit) {
            val suspectName = getSuspect(submission.suspectId)?.name ?: "This suspect"
            val message = String.format(culpritSolution.wrongSuspectFeedbackTemplate, suspectName)
            return AccusationEvaluation(
                isCorrectCulprit = false,
                isPremature = false,
                isPerfect = false,
                title = "Accusation Not Supported",
                feedbackMessage = message,
                resultStatus = currentState.caseStatus
            )
        }

        // Check required contradiction, motive, and time anchor
        val hasRequiredContradictions = culpritSolution.requiredContradictionIds.isEmpty() ||
                culpritSolution.requiredContradictionIds.any {
                    currentState.unlockedContradictionIds.contains(it) ||
                            (submission.selectedEvidenceIds.contains("E018") || submission.selectedEvidenceIds.contains(it))
                }

        val hasRequiredMotive = culpritSolution.requiredMotiveEvidenceIds.isEmpty() ||
                culpritSolution.requiredMotiveEvidenceIds.any {
                    currentState.discoveredEvidenceIds.contains(it) || submission.selectedEvidenceIds.contains(it)
                }

        val hasRequiredTimeAnchor = culpritSolution.requiredTimeAnchorEvidenceIds.isEmpty() ||
                culpritSolution.requiredTimeAnchorEvidenceIds.any {
                    currentState.discoveredEvidenceIds.contains(it) || submission.selectedEvidenceIds.contains(it)
                }

        val isPremature = !hasRequiredContradictions || !hasRequiredMotive || !hasRequiredTimeAnchor

        if (isPremature) {
            return AccusationEvaluation(
                isCorrectCulprit = true,
                isPremature = true,
                isPerfect = false,
                title = culpritSolution.prematureFeedbackTitle,
                feedbackMessage = culpritSolution.prematureFeedbackMessage,
                resultStatus = currentState.caseStatus
            )
        }

        // Check if perfect investigation
        val allCriticalFound = culpritSolution.criticalEvidenceIds.isEmpty() ||
                currentState.discoveredEvidenceIds.containsAll(culpritSolution.criticalEvidenceIds)

        val allSuspectsCleared = culpritSolution.clearedSuspectIdsForPerfect.isEmpty() ||
                currentState.clearedSuspectIds.containsAll(culpritSolution.clearedSuspectIdsForPerfect)

        val hasMinEvidence = currentState.discoveredEvidenceIds.size >= culpritSolution.minEvidenceCountForPerfect

        val isPerfect = allCriticalFound && allSuspectsCleared && hasMinEvidence
        val finalStatus = if (isPerfect) CaseStatus.SOLVED_PERFECT else CaseStatus.SOLVED

        return AccusationEvaluation(
            isCorrectCulprit = true,
            isPremature = false,
            isPerfect = isPerfect,
            title = if (isPerfect) culpritSolution.perfectTitle else culpritSolution.solvedTitle,
            feedbackMessage = culpritSolution.solvedFeedbackMessage,
            resultStatus = finalStatus
        )
    }
}
