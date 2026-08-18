package com.example.thelastcall.data

/**
 * Minimal placeholder for Case 002.
 * Contains no mystery, suspects, clues, solution, or invented story content.
 * Status: LOCKED / COMING SOON.
 */
object Case002Placeholder : CaseDefinition {
    const val CASE_ID = "CASE-002"
    const val CASE_TITLE = "COMING SOON"

    override val id: String = CASE_ID
    override val title: String = CASE_TITLE
    override val subtitle: String = "Classified"
    override val incidentDate: String = ""
    override val incidentTime: String = ""
    override val location: String = ""
    override val victimName: String = ""
    override val victimAge: Int = 0
    override val victimOccupation: String = ""
    override val briefingSummary: String = "Case 002 is currently locked and awaiting assignment."
    override val primaryObjectiveText: String = ""
    override val initialDialogueText: String = ""
    override val introFacts: List<IntroFact> = emptyList()
    override val motives: List<CaseMotiveOption> = emptyList()
    override val weapons: List<CaseWeaponOption> = emptyList()
    override val initialUnlockedTimelineIds: Set<String> = emptySet()

    override val evidenceList: List<EvidenceItem> = emptyList()
    override val suspects: List<Suspect> = emptyList()
    override val questions: List<InterviewQuestion> = emptyList()
    override val statements: List<StatementItem> = emptyList()
    override val reactions: List<EvidenceReaction> = emptyList()
    override val timelineEvents: List<TimelineEvent> = emptyList()
    override val objectives: List<Objective> = emptyList()
    override val contradictions: List<Contradiction> = emptyList()
    override val contradictionChallenges: List<ContradictionChallenge> = emptyList()
    override val communicationThreads: List<CommunicationThread> = emptyList()
    override val callLogs: List<CallLogEntry> = emptyList()
    override val deductions: List<Deduction> = emptyList()
    override val customDeductionMessages: Map<Pair<String, String>, String> = emptyMap()
    override val crimeSceneHotspots: List<CrimeSceneHotspot> = emptyList()
    override val culpritSolution: CulpritSolution = CulpritSolution(
        culpritSuspectId = "",
        correctMotiveKey = "",
        correctWeaponKey = ""
    )

    override val isPlaceholder: Boolean = true
    override val isAvailable: Boolean = false
}
