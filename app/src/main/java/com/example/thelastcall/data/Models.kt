package com.example.thelastcall.data

enum class EvidenceCategory(val displayName: String) {
    PHYSICAL("Physical"),
    DIGITAL("Digital"),
    DOCUMENTARY("Documentary"),
    TESTIMONIAL("Testimonial"),
    ENVIRONMENTAL("Environmental")
}

enum class EvidenceImportance(val level: Int, val label: String) {
    CONTEXT(1, "Context"),
    RELEVANT(2, "Relevant"),
    IMPORTANT(3, "Important"),
    CRITICAL(4, "Critical")
}

data class EvidenceItem(
    val id: String,
    val name: String,
    val category: EvidenceCategory,
    val location: String,
    val discoveryCondition: String,
    val playerDescription: String,
    val detailedInvestigation: String,
    val significanceText: String,
    val relatedSuspects: List<String> = emptyList(),
    val relatedTimelineEvents: List<String> = emptyList(),
    val relatedEvidence: List<String> = emptyList(),
    val importance: EvidenceImportance = EvidenceImportance.RELEVANT,
    val isCritical: Boolean = false,
    val discoverableInitially: Boolean = true,
    val iconType: String = "document",
    val unlocksEvidenceOnInspect: List<String> = emptyList()
)

enum class SuspectBehaviorState(val label: String, val description: String) {
    CALM("Calm", "Composed and willing to answer general inquiries."),
    COOPERATIVE("Cooperative", "Actively offering details to establish facts."),
    NERVOUS("Nervous", "Agitated by sensitive evidence or conflicting timelines."),
    DEFENSIVE("Defensive", "Guarded, deflecting questions, or guarding secrets."),
    CORNERED("Cornered", "Caught in a direct contradiction with physical proof."),
    ALIBI_VERIFIED("Alibi Verified", "Formally eliminated from the critical murder window.")
}

data class Suspect(
    val id: String,
    val name: String,
    val age: Int,
    val occupation: String,
    val relationship: String,
    val publicStory: String,
    val hiddenTruth: String,
    val personalityDescription: String,
    val initialAlibiSummary: String,
    val avatarColorHex: Long = 0xFF37474F,
    val initials: String
) {
    fun getDynamicBehaviorState(
        isCleared: Boolean,
        hasContradictionExposed: Boolean,
        hasMotiveExposed: Boolean,
        askedCount: Int,
        presentedEvidenceCount: Int
    ): SuspectBehaviorState {
        return when {
            isCleared -> SuspectBehaviorState.ALIBI_VERIFIED
            hasContradictionExposed -> SuspectBehaviorState.CORNERED
            hasMotiveExposed -> SuspectBehaviorState.DEFENSIVE
            presentedEvidenceCount > 0 -> SuspectBehaviorState.NERVOUS
            askedCount >= 3 -> SuspectBehaviorState.DEFENSIVE
            askedCount >= 2 -> SuspectBehaviorState.COOPERATIVE
            else -> SuspectBehaviorState.CALM
        }
    }

    fun getDynamicGreeting(
        state: CaseState,
        caseDef: CaseDefinition
    ): String {
        val isCleared = state.clearedSuspectIds.contains(id)
        val presented = state.presentedEvidenceRecords[id] ?: emptySet()
        val askedCount = state.askedQuestionIds.filter { qId ->
            caseDef.questions.any { it.id == qId && it.suspectId == id }
        }.size
        val hasContradictionExposed = caseDef.contradictions.filter { it.suspectId == id }
            .any { state.unlockedContradictionIds.contains(it.id) }
        val hasMotiveExposed = (caseDef.culpritSolution.culpritSuspectId == id && state.hasDiscoveredMotive) ||
            caseDef.reactions.filter { it.suspectId == id && it.triggersMotiveId != null }
                .any { presented.contains(it.evidenceId) }

        val behavior = getDynamicBehaviorState(isCleared, hasContradictionExposed, hasMotiveExposed, askedCount, presented.size)
        return when (behavior) {
            SuspectBehaviorState.CORNERED -> "\"I... I don't know what you're talking about! There must be an explanation!\""
            SuspectBehaviorState.DEFENSIVE -> "\"I've already told you what I know, Detective. Be careful what you insinuate.\""
            SuspectBehaviorState.NERVOUS -> "\"I'm telling you everything I can... I just want to understand what happened.\""
            SuspectBehaviorState.ALIBI_VERIFIED -> "\"My alibi is verified, Detective. Let me know if you need anything else.\""
            SuspectBehaviorState.COOPERATIVE -> "\"I want to help get to the bottom of this, Detective. Ask what you need.\""
            SuspectBehaviorState.CALM -> "\"I am ready to answer your questions, Detective.\""
        }
    }
}

data class InterviewQuestion(
    val id: String,
    val suspectId: String,
    val questionText: String,
    val answerText: String,
    val requiredEvidenceId: String? = null,
    val requiredStatementId: String? = null,
    val recordedStatementId: String? = null,
    val statementSummary: String? = null,
    val unlocksEvidenceIds: List<String> = emptyList(),
    val unlocksQuestionIds: List<String> = emptyList()
)

data class EvidenceReaction(
    val suspectId: String,
    val evidenceId: String,
    val detectivePrompt: String,
    val suspectResponse: String,
    val isContradiction: Boolean = false,
    val triggersContradictionId: String? = null,
    val triggersMotiveId: String? = null,
    val unlocksQuestionIds: List<String> = emptyList(),
    val unlocksEvidenceIds: List<String> = emptyList(),
    val clearsSuspectCriticalPeriod: Boolean = false
)

enum class TimelineConfidence(val label: String) {
    UNKNOWN("Unknown"),
    STATEMENT("Statement"),
    SUPPORTED("Supported"),
    CONFIRMED("Confirmed")
}

data class TimelineEvent(
    val id: String,
    val time: String,
    val title: String,
    val description: String,
    val sourceEvidenceId: String? = null,
    val relatedSuspectId: String? = null,
    val requiredEvidenceForUnlock: List<String> = emptyList()
)

sealed interface ObjectiveCondition {
    fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean

    data class DiscoverEvidence(val evidenceIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.discoveredEvidenceIds.containsAll(evidenceIds)
            else evidenceIds.any { state.discoveredEvidenceIds.contains(it) }
        }
    }

    data class InterviewSuspects(val suspectIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.interviewedSuspectIds.containsAll(suspectIds)
            else suspectIds.any { state.interviewedSuspectIds.contains(it) }
        }
    }

    data class AskQuestions(val questionIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.askedQuestionIds.containsAll(questionIds)
            else questionIds.any { state.askedQuestionIds.contains(it) }
        }
    }

    data class UnlockContradictions(val contradictionIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.unlockedContradictionIds.containsAll(contradictionIds)
            else contradictionIds.any { state.unlockedContradictionIds.contains(it) }
        }
    }

    data class FormDeductions(val deductionIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.unlockedDeductionIds.containsAll(deductionIds)
            else deductionIds.any { state.unlockedDeductionIds.contains(it) }
        }
    }

    data class DiscoveredMotiveAndOpportunity(val requireContradiction: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            val hasMotive = state.hasDiscoveredMotive || caseDef.culpritSolution.requiredMotiveEvidenceIds.any { state.discoveredEvidenceIds.contains(it) }
            val hasOpportunity = caseDef.culpritSolution.requiredTimeAnchorEvidenceIds.all { state.discoveredEvidenceIds.contains(it) } &&
                    (!requireContradiction || caseDef.culpritSolution.requiredContradictionIds.all { state.unlockedContradictionIds.contains(it) })
            return hasMotive && hasOpportunity
        }
    }

    data class InspectEvidence(val evidenceIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.inspectedEvidenceIds.containsAll(evidenceIds)
            else evidenceIds.any { state.inspectedEvidenceIds.contains(it) }
        }
    }

    data class InspectHotspots(val hotspotIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.inspectedHotspotIds.containsAll(hotspotIds)
            else hotspotIds.any { state.inspectedHotspotIds.contains(it) }
        }
    }

    data class RecordedStatements(val statementIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.recordedStatementIds.containsAll(statementIds)
            else statementIds.any { state.recordedStatementIds.contains(it) }
        }
    }

    data class ClearedSuspects(val suspectIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.clearedSuspectIds.containsAll(suspectIds)
            else suspectIds.any { state.clearedSuspectIds.contains(it) }
        }
    }

    data class UnlockedTimelineEvents(val eventIds: List<String>, val matchAll: Boolean = true) : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return if (matchAll) state.unlockedTimelineEventIds.containsAll(eventIds)
            else eventIds.any { state.unlockedTimelineEventIds.contains(it) }
        }
    }

    object CaseSolved : ObjectiveCondition {
        override fun isMet(state: CaseState, caseDef: CaseDefinition): Boolean {
            return state.caseStatus == CaseStatus.SOLVED ||
                    state.caseStatus == CaseStatus.SOLVED_PERFECT ||
                    state.caseStatus == CaseStatus.SOLVED_INCOMPLETE
        }
    }
}

data class Objective(
    val id: String,
    val title: String,
    val description: String,
    val condition: ObjectiveCondition? = null,
    val focusTab: CaseFileTab? = null,
    val leadActionLabel: String? = null,
    val leadTarget: Screen? = null
)

data class StatementItem(
    val id: String,
    val suspectId: String,
    val statementText: String,
    val summary: String,
    val sourceContext: String,
    val timestamp: String? = null,
    val relatedEvidenceIds: List<String> = emptyList(),
    val contradictionId: String? = null
)

enum class ReasoningRelationship(val displayName: String, val verb: String) {
    SUPPORTS("Supports", "supports"),
    CONTRADICTS("Contradicts", "contradicts"),
    CONNECTS("Connects To", "connects with"),
    ESTABLISHES("Establishes", "establishes"),
    DISPROVES("Disproves", "disproves")
}

enum class TheoryConfidence(val label: String) {
    UNKNOWN("Unknown"),
    SUGGESTED("Suggested"),
    SUPPORTED("Supported"),
    ESTABLISHED("Established")
}

data class PlayerTheory(
    val suspectId: String? = null,
    val motiveKey: String? = null,
    val weaponKey: String? = null,
    val opportunityConfirmed: Boolean = false,
    val supportingEvidenceIds: Set<String> = emptySet(),
    val establishedDeductionIds: Set<String> = emptySet()
)

data class EvidenceConnection(
    val id: String,
    val sourceId: String,
    val targetId: String,
    val relationship: ReasoningRelationship,
    val description: String
)

data class CaseReadiness(
    val hasSuspect: Boolean,
    val motiveConfidence: TheoryConfidence,
    val opportunityConfidence: TheoryConfidence,
    val methodConfidence: TheoryConfidence,
    val hasContradiction: Boolean,
    val supportingEvidenceCount: Int,
    val isReadyForAccusation: Boolean,
    val guidanceHint: String
)

data class Contradiction(
    val id: String,
    val title: String,
    val suspectId: String,
    val statementIds: List<String>,
    val evidenceIds: List<String>,
    val conflictSummary: String,
    val fullExplanation: String
)

data class Deduction(
    val id: String,
    val title: String,
    val reasoning: String,
    val supportingEvidenceIds: List<String>,
    val requiredEvidence: List<String> = emptyList(),
    val requiredContradictions: List<String> = emptyList()
)

data class CrimeSceneHotspot(
    val id: String,
    val name: String,
    val locationLabel: String,
    val description: String,
    val xPercent: Float,
    val yPercent: Float,
    val primaryEvidenceId: String,
    val secondaryEvidenceId: String? = null,
    val requiredEvidenceForSecondary: String? = null
)

/**
 * A multiple-choice option the player may choose when resolving a
 * [ContradictionChallenge]. The correct answer is defined in case data,
 * never in the engine or UI, so it is never revealed before the player answers.
 */
data class ChallengeOption(
    val key: String,
    val text: String,
    val isCorrect: Boolean = false,
    val feedback: String = ""
)

/**
 * Player-driven contradiction challenge. When a player presents [evidenceId]
 * to [suspectId] and the case data defines a legitimate contradiction, the
 * player must explain what the evidence tells them by selecting one of the
 * [options]. A correct choice establishes [contradictionId]; an incorrect
 * choice allows a retry without revealing the answer.
 */
data class ContradictionChallenge(
    val id: String,
    val suspectId: String,
    val evidenceId: String,
    val contradictionId: String,
    val prompt: String,
    val options: List<ChallengeOption>,
    val successFeedback: String,
    val failurePrompt: String
)

/** A freeform note the player writes into the case file. */
data class PlayerNote(
    val id: String,
    val text: String,
    val timestamp: String
)

enum class ActivityKind(val label: String) {
    EVIDENCE("Evidence Secured"),
    INTERVIEW("Interview Conducted"),
    CLEARED("Alibi Verified"),
    CONTRADICTION("Contradiction Established"),
    DEDUCTION("Deduction Formed"),
    TIMELINE("Timeline Reconstructed"),
    LEAD("Lead Followed"),
    MESSAGE("New Communication"),
    NOTE("Note Added")
}

/** A timestamped entry in the investigator's activity feed. */
data class ActivityLogEntry(
    val id: String,
    val kind: ActivityKind,
    val detail: String,
    val timestamp: String
)

/** How a piece of digital/communicative evidence should be presented. */
enum class EvidencePresentation(val label: String) {
    CALL_RECORD("Call Record"),
    DOCUMENT("Document"),
    ACCESS_LOG("Access Log"),
    TRANSPORT("Transport Record"),
    PHYSICAL("Physical Artifact"),
    STATEMENT("Statement"),
    MESSAGE("Message"),
    PHOTO("Photo"),
    CONTACT("Contact"),
    NOTE("Note"),
    OTHER("Archive Item");

    companion object {
        fun fromIconType(iconType: String, category: EvidenceCategory): EvidencePresentation {
            return when (iconType.lowercase()) {
                "phone", "call", "digital" -> CALL_RECORD
                "document", "folder", "receipt", "vault" -> DOCUMENT
                "access", "keycard", "surveillance" -> ACCESS_LOG
                "transit", "ticket" -> TRANSPORT
                "weapon", "paperweight", "fingerprint", "glass", "window", "door" -> PHYSICAL
                "statement", "verdict" -> STATEMENT
                "message", "sms", "chat" -> MESSAGE
                "photo", "image" -> PHOTO
                "contact" -> CONTACT
                "note" -> NOTE
                else -> when (category) {
                    EvidenceCategory.DIGITAL -> CALL_RECORD
                    EvidenceCategory.DOCUMENTARY -> DOCUMENT
                    EvidenceCategory.PHYSICAL, EvidenceCategory.ENVIRONMENTAL -> PHYSICAL
                    EvidenceCategory.TESTIMONIAL -> STATEMENT
                }
            }
        }
    }
}

fun EvidenceItem.presentation(): EvidencePresentation =
    EvidencePresentation.fromIconType(iconType, category)

/** A message inside a recovered communication thread (SMS, voicemail transcript, etc.). */
data class CommunicationMessage(
    val id: String,
    val sender: String,
    val isFromVictim: Boolean,
    val timestamp: String,
    val text: String,
    val attachmentEvidenceId: String? = null
)

/** A recovered conversation thread presented in the messaging UI. */
data class CommunicationThread(
    val id: String,
    val title: String,
    val contactInitials: String,
    val contactColorHex: Long,
    val channelLabel: String,
    val messages: List<CommunicationMessage>,
    val suspectId: String? = null
)

enum class CallDirection(val label: String) {
    OUTGOING("OUTGOING"),
    INCOMING("INCOMING"),
    MISSED("MISSED")
}

/** An entry in the recovered call log, derived from phone evidence. */
data class CallLogEntry(
    val id: String,
    val contactName: String,
    val direction: CallDirection,
    val timestamp: String,
    val durationLabel: String?,
    val linkedEvidenceId: String,
    val isCritical: Boolean = false
)

enum class LeadStatus(val label: String) {
    LOCKED("Locked"),
    AVAILABLE("Available"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    OPTIONAL("Optional")
}

sealed interface LeadActionTarget {
    object Hub : LeadActionTarget
    data class CrimeScene(val hotspotId: String? = null) : LeadActionTarget
    data class Evidence(val evidenceId: String) : LeadActionTarget
    data class Suspect(val suspectId: String, val questionId: String? = null) : LeadActionTarget
    data class Communication(val threadId: String? = null) : LeadActionTarget
    data class Timeline(val eventId: String? = null) : LeadActionTarget
    data class Reasoning(val tab: CaseFileTab = CaseFileTab.DEDUCTIONS) : LeadActionTarget
    data class CaseFile(val tab: CaseFileTab = CaseFileTab.EVIDENCE) : LeadActionTarget
    object DetectiveBoard : LeadActionTarget
    object CaseReview : LeadActionTarget
}

data class LeadObjective(
    val id: String,
    val title: String,
    val description: String,
    val actionLabel: String? = null,
    val target: LeadActionTarget = LeadActionTarget.Hub,
    val condition: ObjectiveCondition? = null,
    val isOptional: Boolean = false,
    val hint: String? = null
)

data class InvestigationLead(
    val id: String,
    val title: String,
    val subtitle: String,
    val shortDescription: String,
    val briefing: String,
    val objectives: List<LeadObjective>,
    val unlockLeadIds: List<String> = emptyList(),
    val unlockEvidenceIds: List<String> = emptyList(),
    val completionSummary: String = "",
    val nextLeadId: String? = null,
    val isMajorBreakthrough: Boolean = false,
    val breakthroughTitle: String? = null,
    val breakthroughDescription: String? = null,
    val associatedEvidenceIds: List<String> = emptyList(),
    val associatedSuspectIds: List<String> = emptyList(),
    val associatedLocation: String? = null,
    val centralQuestion: String? = null,
    val isOptional: Boolean = false,
    val orderIndex: Int = 0
) {
    val totalRequiredObjectives: Int
        get() = objectives.count { !it.isOptional }

    fun isCompleted(state: CaseState, caseDef: CaseDefinition): Boolean {
        val required = objectives.filter { !it.isOptional }
        if (required.isEmpty()) return false
        return required.all { obj ->
            state.completedLeadObjectiveIds.contains(obj.id) ||
                (obj.condition != null && obj.condition.isMet(state, caseDef))
        }
    }

    fun isUnlocked(state: CaseState, caseDef: CaseDefinition): Boolean {
        if (unlockLeadIds.isNotEmpty() && !unlockLeadIds.all { state.completedLeadIds.contains(it) }) {
            return false
        }
        if (unlockEvidenceIds.isNotEmpty() && !unlockEvidenceIds.all { state.discoveredEvidenceIds.contains(it) }) {
            return false
        }
        return true
    }

    fun getStatus(state: CaseState, caseDef: CaseDefinition): LeadStatus {
        if (state.completedLeadIds.contains(id) || isCompleted(state, caseDef)) {
            return LeadStatus.COMPLETED
        }
        if (state.activeLeadId == id) {
            return LeadStatus.ACTIVE
        }
        if (isUnlocked(state, caseDef)) {
            return if (isOptional) LeadStatus.OPTIONAL else LeadStatus.AVAILABLE
        }
        return LeadStatus.LOCKED
    }

    fun getNextUncompletedObjective(state: CaseState, caseDef: CaseDefinition): LeadObjective? {
        return objectives.firstOrNull { obj ->
            !state.completedLeadObjectiveIds.contains(obj.id) &&
                (obj.condition == null || !obj.condition.isMet(state, caseDef))
        }
    }
}

data class LeadNavigationContext(
    val sourceLeadId: String,
    val sourceObjectiveId: String? = null,
    val leadTitle: String,
    val returnScreen: Screen = Screen.INVESTIGATION_LEAD
)

enum class InvestigationMomentType(val displayName: String) {
    NEW_DEVELOPMENT("New Development"),
    EVIDENCE_CONNECTION("Evidence Connection"),
    STATEMENT_UPDATE("Statement Update"),
    BREAKTHROUGH("Breakthrough"),
    COMMUNICATION_RECOVERED("Communication Recovered")
}

data class InvestigationMoment(
    val id: String,
    val title: String,
    val subtitle: String,
    val narrativeText: String,
    val type: InvestigationMomentType = InvestigationMomentType.NEW_DEVELOPMENT,
    val triggerCondition: ObjectiveCondition? = null,
    val associatedEvidenceId: String? = null,
    val associatedSuspectId: String? = null,
    val associatedLeadId: String? = null,
    val actionLabel: String = "CONTINUE",
    val actionTarget: LeadActionTarget = LeadActionTarget.Hub,
    val isMajorBreakthrough: Boolean = false,
    val priority: Int = 0
)

