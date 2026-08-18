package com.example.thelastcall.data

import com.example.thelastcall.engine.AccusationEngine

enum class CaseStatus {
    NOT_STARTED,
    IN_PROGRESS,
    READY_FOR_ACCUSATION,
    SOLVED_PERFECT,
    SOLVED,
    SOLVED_INCOMPLETE
}

enum class Screen {
    MAIN_MENU,
    CASE_INTRO,
    BRIEFING,
    CASE_HUB,
    CRIME_SCENE,
    CASE_FILE,
    DETECTIVE_BOARD,
    SUSPECT_INTERVIEW,
    EVIDENCE_DETAIL,
    PERSON_PROFILE,
    COMMUNICATIONS,
    FINAL_CASE_REVIEW,
    FINAL_ACCUSATION,
    CASE_RESULT,
    SETTINGS
}

enum class CaseFileTab(val label: String) {
    BOARD("Board"),
    EVIDENCE("Evidence"),
    STATEMENTS("Statements"),
    SUSPECTS("People"),
    TIMELINE("Timeline"),
    DEDUCTIONS("Reasoning"),
    THEORY("Theory"),
    NOTES("Notes"),
    OBJECTIVES("Leads")
}

enum class TextSpeed(val delayMs: Long, val label: String) {
    SLOW(40L, "Slow"),
    NORMAL(20L, "Normal"),
    FAST(5L, "Fast"),
    INSTANT(0L, "Instant")
}

data class GameSettings(
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val textSpeed: TextSpeed = TextSpeed.NORMAL,
    val hintsEnabled: Boolean = true
)

data class GameNotification(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val type: NotificationType = NotificationType.EVIDENCE,
    val actionLabel: String? = null,
    val actionTarget: Screen? = null,
    val actionTab: CaseFileTab? = null
)

enum class NotificationType {
    EVIDENCE,
    CONTRADICTION,
    DEDUCTION,
    OBJECTIVE,
    LEAD,
    MESSAGE,
    INFO
}

data class AccusationSubmission(
    val suspectId: String,
    val motiveKey: String,
    val weaponKey: String,
    val selectedEvidenceIds: List<String>
)

data class AccusationEvaluation(
    val isCorrectCulprit: Boolean,
    val isPremature: Boolean,
    val isPerfect: Boolean,
    val title: String,
    val feedbackMessage: String,
    val resultStatus: CaseStatus
)

data class CaseState(
    val caseId: String = "CASE-001",
    val caseStatus: CaseStatus = CaseStatus.NOT_STARTED,
    val currentScreen: Screen = Screen.MAIN_MENU,
    val caseFileTab: CaseFileTab = CaseFileTab.EVIDENCE,
    val selectedSuspectId: String? = null,
    val selectedEvidenceId: String? = null,
    val selectedStatementId: String? = null,
    val discoveredEvidenceIds: Set<String> = emptySet(),
    val inspectedEvidenceIds: Set<String> = emptySet(),
    val interviewedSuspectIds: Set<String> = emptySet(),
    val askedQuestionIds: Set<String> = emptySet(),
    val recordedStatementIds: Set<String> = emptySet(),
    val clearedSuspectIds: Set<String> = emptySet(),
    val presentedEvidenceRecords: Map<String, Set<String>> = emptyMap(),
    val unlockedTimelineEventIds: Set<String> = emptySet(),
    val unlockedDeductionIds: Set<String> = emptySet(),
    val unlockedContradictionIds: Set<String> = emptySet(),
    val completedObjectiveIds: Set<String> = emptySet(),
    val inspectedHotspotIds: Set<String> = emptySet(),
    val customConnections: List<EvidenceConnection> = emptyList(),
    val playerTheory: PlayerTheory = PlayerTheory(),
    val hasDiscoveredMotive: Boolean = false,
    val hasDiscoveredOpportunity: Boolean = false,
    val lastAccusationSubmission: AccusationSubmission? = null,
    val lastAccusationEvaluation: AccusationEvaluation? = null,
    val settings: GameSettings = GameSettings(),
    val activeNotification: GameNotification? = null,
    val hasSeenCrimeSceneTutorial: Boolean = false,
    val playerNotes: List<PlayerNote> = emptyList(),
    val pendingChallengeId: String? = null,
    val activityLog: List<ActivityLogEntry> = emptyList(),
    val investigationMinutes: Int = 0
) {
    fun getCaseReadiness(caseDef: CaseDefinition? = null): CaseReadiness {
        val targetDef = caseDef ?: CaseRegistry.getCase(caseId) ?: CaseRegistry.getDefaultCase()
        return AccusationEngine.evaluateReadiness(this, targetDef)
    }
}

