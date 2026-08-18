package com.example.thelastcall.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.engine.*
import com.example.thelastcall.ui.components.*
import com.example.ui.theme.*

@Composable
fun SuspectInterviewScreen(
    suspectId: String,
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onAskQuestion: (InterviewQuestion) -> Unit,
    onPresentEvidence: (String, String) -> EvidencePresentationOutcome,
    onAttemptChallenge: (String) -> ChallengeAttemptResult?,
    onDismissChallenge: () -> Unit,
    onOpenCaseFile: () -> Unit,
    onBack: () -> Unit
) {
    val suspect = caseDef.getSuspect(suspectId) ?: return
    val isCleared = state.clearedSuspectIds.contains(suspectId)
    val presentedToThisSuspect = state.presentedEvidenceRecords[suspectId] ?: emptySet()
    val askedForThisSuspect = state.askedQuestionIds.filter { qId ->
        caseDef.questions.any { it.id == qId && it.suspectId == suspectId }
    }.size

    val hasContradictionExposed = caseDef.contradictions.filter { it.suspectId == suspectId }
        .any { state.unlockedContradictionIds.contains(it.id) }
    val hasMotiveExposed = (caseDef.culpritSolution.culpritSuspectId == suspectId && state.hasDiscoveredMotive) ||
        caseDef.reactions.filter { it.suspectId == suspectId && it.triggersMotiveId != null }
            .any { presentedToThisSuspect.contains(it.evidenceId) }

    val behaviorState = remember(isCleared, hasContradictionExposed, hasMotiveExposed, askedForThisSuspect, presentedToThisSuspect.size) {
        suspect.getDynamicBehaviorState(
            isCleared = isCleared,
            hasContradictionExposed = hasContradictionExposed,
            hasMotiveExposed = hasMotiveExposed,
            askedCount = askedForThisSuspect,
            presentedEvidenceCount = presentedToThisSuspect.size
        )
    }

    val defaultEmotion = remember(behaviorState) {
        when (behaviorState) {
            SuspectBehaviorState.CORNERED -> SuspectEmotion.SHOCKED
            SuspectBehaviorState.DEFENSIVE -> SuspectEmotion.DEFENSIVE
            SuspectBehaviorState.NERVOUS -> SuspectEmotion.NERVOUS
            SuspectBehaviorState.COOPERATIVE -> SuspectEmotion.THINKING
            SuspectBehaviorState.ALIBI_VERIFIED -> SuspectEmotion.NEUTRAL
            SuspectBehaviorState.CALM -> SuspectEmotion.NEUTRAL
        }
    }

    var currentSpeaker by remember { mutableStateOf(suspect.name) }
    var currentDialogueText by remember {
        mutableStateOf(
            when (behaviorState) {
                SuspectBehaviorState.CORNERED -> "\"I... I don't know what you're talking about! There must be an explanation!\""
                SuspectBehaviorState.DEFENSIVE -> "\"I've already told you what I know, Detective. Be careful what you insinuate.\""
                SuspectBehaviorState.NERVOUS -> "\"I'm telling you everything I can... I just want to understand what happened.\""
                SuspectBehaviorState.ALIBI_VERIFIED -> "\"My alibi is verified, Detective. Let me know if you need anything else.\""
                else -> "\"I am ready to answer your questions, Detective.\""
            }
        )
    }
    var currentEmotion by remember { mutableStateOf(defaultEmotion) }
    var showPresentEvidenceModal by remember { mutableStateOf(false) }
    var showHistoryModal by remember { mutableStateOf(false) }
    var selectedEvidenceToPresent by remember { mutableStateOf<EvidenceItem?>(null) }
    var lastChallengeFeedback by remember { mutableStateOf<ChallengeAttemptResult?>(null) }
    val dialogueHistory = remember { mutableStateListOf<DialogueHistoryEntry>() }

    val activeChallenge = remember(state.pendingChallengeId, caseDef) {
        state.pendingChallengeId?.let { caseDef.getContradictionChallenge(it) }
    }

    val suspectQuestions = remember(suspectId, state.discoveredEvidenceIds, state.recordedStatementIds, caseDef) {
        caseDef.questions.filter { it.suspectId == suspectId }
    }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "INTERROGATION: ${suspect.name.uppercase()}",
                subtitle = suspect.relationship,
                onBack = onBack,
                onOpenCaseFile = onOpenCaseFile
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            // Character Portrait & Persona Row
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuspectPortrait(
                        suspectId = suspect.id,
                        emotion = currentEmotion,
                        size = 84.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = suspect.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Dynamic Behavior State Badge
                            val badgeColor = when (behaviorState) {
                                SuspectBehaviorState.CORNERED -> AccentRed
                                SuspectBehaviorState.DEFENSIVE -> AccentAmber
                                SuspectBehaviorState.NERVOUS -> AccentCyan
                                SuspectBehaviorState.ALIBI_VERIFIED -> StatusConfirmed
                                SuspectBehaviorState.COOPERATIVE -> Color(0xFF66BB6A)
                                SuspectBehaviorState.CALM -> TextMuted
                            }

                            Surface(
                                color = badgeColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = behaviorState.label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = badgeColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "${suspect.occupation} • Age ${suspect.age}",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentAmber
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = behaviorState.description,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Active Typewriter Dialogue Box
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    1.dp,
                    if (currentEmotion == SuspectEmotion.SHOCKED) AccentRed
                    else if (currentEmotion == SuspectEmotion.DEFENSIVE) AccentAmber
                    else SurfaceBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp, max = 140.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentSpeaker.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (currentSpeaker == "Detective") AccentCyan else AccentAmber
                            )
                            if (currentSpeaker != "Detective") {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${currentEmotion.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextMuted
                                )
                            }
                        }

                        IconButton(
                            onClick = { showHistoryModal = true },
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("dialogue_history_icon_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.HistoryEdu,
                                contentDescription = "Transcript History",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TypewriterText(
                        fullText = currentDialogueText,
                        textSpeed = state.settings.textSpeed,
                        modifier = Modifier.testTag("typewriter_dialogue_text")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Confront / Present Evidence Action Button
            Button(
                onClick = { showPresentEvidenceModal = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasContradictionExposed) AccentRedDark else AccentRed,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("present_evidence_action_button")
            ) {
                Icon(
                    imageVector = Icons.Default.FactCheck,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PRESENT EVIDENCE TO ${suspect.name.uppercase()}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INTERROGATION TOPICS",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.Bold),
                    color = TextMuted
                )
                Text(
                    text = "${suspectQuestions.count { state.askedQuestionIds.contains(it.id) }}/${suspectQuestions.size} ASKED",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Questions List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(suspectQuestions) { question ->
                    val isAsked = state.askedQuestionIds.contains(question.id)
                    val isLocked = (question.requiredEvidenceId != null && !state.discoveredEvidenceIds.contains(question.requiredEvidenceId)) ||
                            (question.requiredStatementId != null && !state.recordedStatementIds.contains(question.requiredStatementId))

                    val reqEvName = question.requiredEvidenceId?.let { caseDef.getEvidence(it)?.name }

                    InterviewQuestionCard(
                        question = question,
                        isAsked = isAsked,
                        isLocked = isLocked,
                        requiredEvidenceName = reqEvName,
                        onClick = {
                            if (!isLocked) {
                                currentSpeaker = suspect.name
                                currentDialogueText = "\"${question.answerText}\""
                                currentEmotion = when (behaviorState) {
                                    SuspectBehaviorState.CORNERED -> SuspectEmotion.SHOCKED
                                    SuspectBehaviorState.DEFENSIVE -> SuspectEmotion.DEFENSIVE
                                    SuspectBehaviorState.NERVOUS -> SuspectEmotion.NERVOUS
                                    SuspectBehaviorState.COOPERATIVE -> SuspectEmotion.THINKING
                                    SuspectBehaviorState.ALIBI_VERIFIED -> SuspectEmotion.NEUTRAL
                                    SuspectBehaviorState.CALM -> if (question.requiredEvidenceId != null) SuspectEmotion.DEFENSIVE else SuspectEmotion.THINKING
                                }

                                dialogueHistory.add(
                                    DialogueHistoryEntry(
                                        speaker = "Detective",
                                        text = question.questionText,
                                        isDetective = true
                                    )
                                )
                                dialogueHistory.add(
                                    DialogueHistoryEntry(
                                        speaker = suspect.name,
                                        text = question.answerText,
                                        isDetective = false
                                    )
                                )

                                onAskQuestion(question)
                            }
                        }
                    )
                }
            }
        }
    }

    // Dialogue History Bottom Sheet Modal
    if (showHistoryModal) {
        AlertDialog(
            onDismissRequest = { showHistoryModal = false },
            containerColor = SurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.HistoryEdu, contentDescription = null, tint = AccentAmber)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Interview Transcript: ${suspect.name}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            },
            text = {
                Box(modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)) {
                    if (dialogueHistory.isEmpty()) {
                        Text("No statements recorded in this session yet.", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(dialogueHistory) { entry ->
                                Surface(
                                    color = if (entry.isDetective) Color(0xFF16202A) else SurfaceCard,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = entry.speaker.uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (entry.isDetective) AccentCyan else AccentAmber
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = entry.text,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHistoryModal = false }) {
                    Text("CLOSE", color = AccentAmber, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Present Evidence Modal
    if (showPresentEvidenceModal) {
        val discoveredEvidenceList = remember(state.discoveredEvidenceIds, caseDef) {
            caseDef.evidenceList.filter { state.discoveredEvidenceIds.contains(it.id) }
        }

        AlertDialog(
            onDismissRequest = {
                showPresentEvidenceModal = false
                selectedEvidenceToPresent = null
            },
            containerColor = SurfaceElevated,
            title = {
                Text(
                    text = "Present Clue to ${suspect.name}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Choose an item from your Case File to confront them with:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (discoveredEvidenceList.isEmpty()) {
                        Text(
                            text = "No evidence collected yet. Search the crime scene first.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(discoveredEvidenceList) { evidence ->
                                val isSelected = selectedEvidenceToPresent?.id == evidence.id
                                val isAlreadyPresented = presentedToThisSuspect.contains(evidence.id)

                                Surface(
                                    color = if (isSelected) AccentAmber.copy(alpha = 0.2f) else SurfaceCard,
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) AccentAmber else SurfaceBorder
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedEvidenceToPresent = evidence }
                                        .testTag("present_evidence_item_${evidence.id}")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "[${evidence.category.displayName.uppercase()}]",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = AccentCyan
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = evidence.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = if (isSelected) AccentAmber else TextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isAlreadyPresented) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = SurfaceElevated,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "PRESENTED",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = TextMuted,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val ev = selectedEvidenceToPresent
                        if (ev != null) {
                            showPresentEvidenceModal = false
                            selectedEvidenceToPresent = null
                            val outcome = onPresentEvidence(suspectId, ev.id)
                            if (outcome.challenge != null) {
                                lastChallengeFeedback = null
                            } else {
                                val reaction = outcome.reaction
                                if (reaction != null) {
                                    currentSpeaker = suspect.name
                                    currentDialogueText = "\"${reaction.suspectResponse}\""
                                    currentEmotion = if (reaction.isContradiction) SuspectEmotion.SHOCKED else SuspectEmotion.DEFENSIVE

                                    dialogueHistory.add(
                                        DialogueHistoryEntry(
                                            speaker = "Detective",
                                            text = reaction.detectivePrompt,
                                            isDetective = true
                                        )
                                    )
                                    dialogueHistory.add(
                                        DialogueHistoryEntry(
                                            speaker = suspect.name,
                                            text = reaction.suspectResponse,
                                            isDetective = false
                                        )
                                    )
                                } else {
                                    currentSpeaker = suspect.name
                                    currentDialogueText = "\"I don't see what that has to do with me, Detective.\""
                                    currentEmotion = SuspectEmotion.NEUTRAL

                                    dialogueHistory.add(
                                        DialogueHistoryEntry(
                                            speaker = "Detective",
                                            text = "Confronted suspect with ${ev.name}.",
                                            isDetective = true
                                        )
                                    )
                                    dialogueHistory.add(
                                        DialogueHistoryEntry(
                                            speaker = suspect.name,
                                            text = "I don't see what that has to do with me, Detective.",
                                            isDetective = false
                                        )
                                    )
                                }
                            }
                        }
                    },
                    enabled = selectedEvidenceToPresent != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("confirm_present_evidence_button")
                ) {
                    Text("CONFRONT SUSPECT", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPresentEvidenceModal = false
                        selectedEvidenceToPresent = null
                    }
                ) {
                    Text("CANCEL", color = TextSecondary)
                }
            }
        )
    }

    // Contradiction Challenge Dialog
    val challenge = activeChallenge
    if (challenge != null) {
        ContradictionChallengeDialog(
            challenge = challenge,
            lastAttempt = lastChallengeFeedback,
            contradictionTitle = caseDef.getContradiction(challenge.contradictionId)?.title,
            onSelectOption = { optionKey ->
                val attempt = onAttemptChallenge(optionKey)
                lastChallengeFeedback = attempt
                if (attempt?.accepted == true) {
                    val reaction = attempt.reaction
                    if (reaction != null) {
                        currentSpeaker = suspect.name
                        currentDialogueText = "\"${reaction.suspectResponse}\""
                        currentEmotion = SuspectEmotion.SHOCKED
                        dialogueHistory.add(
                            DialogueHistoryEntry(
                                speaker = "Detective",
                                text = reaction.detectivePrompt,
                                isDetective = true
                            )
                        )
                        dialogueHistory.add(
                            DialogueHistoryEntry(
                                speaker = suspect.name,
                                text = reaction.suspectResponse,
                                isDetective = false
                            )
                        )
                    }
                }
            },
            onDismiss = onDismissChallenge
        )
    }
}

@Composable
private fun InterviewQuestionCard(
    question: InterviewQuestion,
    isAsked: Boolean,
    isLocked: Boolean,
    requiredEvidenceName: String?,
    onClick: () -> Unit
) {
    Surface(
        color = if (isLocked) SurfaceDark.copy(alpha = 0.4f) else SurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (isLocked) SurfaceBorder.copy(alpha = 0.3f) else if (!isAsked) AccentAmber.copy(alpha = 0.5f) else SurfaceBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked, onClick = onClick)
            .testTag("question_row_${question.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when {
                    isLocked -> Icons.Default.Lock
                    isAsked -> Icons.Default.Check
                    else -> Icons.Default.HelpOutline
                },
                contentDescription = null,
                tint = when {
                    isLocked -> TextMuted
                    isAsked -> StatusConfirmed
                    else -> AccentAmber
                },
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isLocked) "Locked Topic" else question.questionText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (!isAsked && !isLocked) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    color = if (isLocked) TextMuted else TextPrimary
                )

                if (isLocked && requiredEvidenceName != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Requires evidence: $requiredEvidenceName",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentRed
                    )
                } else if (isAsked && question.statementSummary != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Statement recorded in Case File",
                        style = MaterialTheme.typography.labelSmall,
                        color = StatusConfirmed
                    )
                }
            }
        }
    }
}
