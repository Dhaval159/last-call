package com.example.thelastcall.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.thelastcall.ui.components.SuspectEmotion
import com.example.thelastcall.ui.components.SuspectPortrait
import com.example.ui.theme.*

@Composable
fun PersonProfileScreen(
    suspectId: String,
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onInterview: (String) -> Unit,
    onOpenEvidence: (String) -> Unit,
    onOpenTimeline: () -> Unit,
    onOpenStatements: () -> Unit,
    onBack: () -> Unit
) {
    val suspect = caseDef.getSuspect(suspectId) ?: return
    val isCleared = state.clearedSuspectIds.contains(suspectId)
    val isInterviewed = state.interviewedSuspectIds.contains(suspectId)
    val presentedToThisSuspect = state.presentedEvidenceRecords[suspectId] ?: emptySet()

    val suspectQuestions = remember(suspectId, caseDef) {
        caseDef.questions.filter { it.suspectId == suspectId }
    }
    val askedCount = remember(state.askedQuestionIds, suspectQuestions) {
        state.askedQuestionIds.count { qId -> suspectQuestions.any { it.id == qId } }
    }

    val hasContradictionExposed = caseDef.contradictions.filter { it.suspectId == suspectId }
        .any { state.unlockedContradictionIds.contains(it.id) }
    val hasMotiveExposed = (caseDef.culpritSolution.culpritSuspectId == suspectId && state.hasDiscoveredMotive) ||
        caseDef.reactions.filter { it.suspectId == suspectId && it.triggersMotiveId != null }
            .any { presentedToThisSuspect.contains(it.evidenceId) }

    val behaviorState = remember(isCleared, hasContradictionExposed, hasMotiveExposed, askedCount, presentedToThisSuspect.size) {
        suspect.getDynamicBehaviorState(
            isCleared = isCleared,
            hasContradictionExposed = hasContradictionExposed,
            hasMotiveExposed = hasMotiveExposed,
            askedCount = askedCount,
            presentedEvidenceCount = presentedToThisSuspect.size
        )
    }

    val recordedStatements = remember(suspectId, state.recordedStatementIds, caseDef) {
        caseDef.statements.filter { it.suspectId == suspectId && state.recordedStatementIds.contains(it.id) }
    }

    val discoveredRelatedEvidence = remember(suspectId, state.discoveredEvidenceIds, caseDef) {
        caseDef.evidenceList.filter { ev ->
            state.discoveredEvidenceIds.contains(ev.id) && ev.relatedSuspects.contains(suspectId)
        }
    }

    val timelineEventsForSuspect = remember(suspectId, state.unlockedTimelineEventIds, caseDef) {
        caseDef.timelineEvents.filter { te ->
            state.unlockedTimelineEventIds.contains(te.id) && te.relatedSuspectId == suspectId
        }
    }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "DOSSIER: ${suspect.name.uppercase()}",
                subtitle = "${caseDef.id.replace('-', ' ')} • PERSON OF INTEREST",
                onBack = onBack
            )
        },
        containerColor = ArchiveBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Profile Card
            item {
                Surface(
                    color = ArchiveCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ArchiveDivider),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("person_profile_header")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SuspectPortrait(
                                suspectId = suspect.id,
                                emotion = when (behaviorState) {
                                    SuspectBehaviorState.CORNERED -> SuspectEmotion.SHOCKED
                                    SuspectBehaviorState.DEFENSIVE -> SuspectEmotion.DEFENSIVE
                                    SuspectBehaviorState.NERVOUS -> SuspectEmotion.NERVOUS
                                    SuspectBehaviorState.COOPERATIVE -> SuspectEmotion.THINKING
                                    else -> SuspectEmotion.NEUTRAL
                                },
                                size = 80.dp,
                                showBorder = true
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = suspect.name.uppercase(),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        ),
                                        color = TextPrimary
                                    )

                                    val badgeColor = when (behaviorState) {
                                        SuspectBehaviorState.CORNERED -> AccentRed
                                        SuspectBehaviorState.DEFENSIVE -> AccentAmber
                                        SuspectBehaviorState.NERVOUS -> AccentCyan
                                        SuspectBehaviorState.ALIBI_VERIFIED -> StatusConfirmed
                                        SuspectBehaviorState.COOPERATIVE -> Color(0xFF66BB6A)
                                        SuspectBehaviorState.CALM -> CaseSlate
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
                                                color = badgeColor
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${suspect.occupation} • Age ${suspect.age}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = CaseGold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "Relationship: ${suspect.relationship}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onInterview(suspect.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CaseGold,
                                contentColor = ArchiveBackground
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("profile_interrogate_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isInterviewed) "CONTINUE INTERROGATION" else "CONDUCT INTERVIEW",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Behavioral Analysis & Status Note
            item {
                Surface(
                    color = ArchiveCard,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ArchiveDivider),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "BEHAVIORAL ASSESSMENT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = CaseSlate
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = behaviorState.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                        if (isCleared) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "✓ Verified alibi eliminates suspect from the critical murder window.",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = StatusConfirmed
                            )
                        }
                    }
                }
            }

            // Background & Account
            item {
                Surface(
                    color = ArchiveCard,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ArchiveDivider),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "PUBLIC ACCOUNT & ALIBI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = CaseSlate
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = suspect.publicStory,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Initial claimed alibi: ${suspect.initialAlibiSummary}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Discovered Statements Section
            item {
                SectionHeader(
                    title = "RECORDED STATEMENTS",
                    countLabel = "${recordedStatements.size} recorded",
                    onAction = onOpenStatements
                )
            }

            if (recordedStatements.isEmpty()) {
                item {
                    EmptySectionCard(message = "No official statements recorded from this person yet. Interrogate to capture testimony.")
                }
            } else {
                items(recordedStatements) { statement ->
                    val isContradicted = statement.contradictionId != null && state.unlockedContradictionIds.contains(statement.contradictionId)
                    Surface(
                        color = ArchiveCard,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isContradicted) StatusContradiction.copy(alpha = 0.6f) else ArchiveDivider
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = statement.sourceContext.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isContradicted) StatusContradiction else CaseGold
                                )
                                if (statement.timestamp != null) {
                                    Text(
                                        text = statement.timestamp,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = CaseSlate
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${statement.statementText}\"",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = TextPrimary
                            )
                            if (isContradicted) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "⚠ Contradicted by physical evidence.",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = StatusContradiction
                                )
                            }
                        }
                    }
                }
            }

            // Linked Evidence Section
            item {
                SectionHeader(
                    title = "CORROBORATING EVIDENCE",
                    countLabel = "${discoveredRelatedEvidence.size} items discovered",
                    onAction = null
                )
            }

            if (discoveredRelatedEvidence.isEmpty()) {
                item {
                    EmptySectionCard(message = "No material evidence directly linking this person has been added to the Case File yet.")
                }
            } else {
                items(discoveredRelatedEvidence) { evidence ->
                    Surface(
                        color = ArchiveCard,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (evidence.isCritical) StatusContradiction.copy(alpha = 0.6f) else ArchiveDivider
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenEvidence(evidence.id) }
                            .testTag("profile_evidence_${evidence.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = evidence.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = evidence.significanceText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Inspect",
                                tint = CaseGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Timeline Entries Section
            if (timelineEventsForSuspect.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "CHRONOLOGY EVENTS",
                        countLabel = "${timelineEventsForSuspect.size} confirmed",
                        onAction = onOpenTimeline
                    )
                }

                items(timelineEventsForSuspect) { event ->
                    Surface(
                        color = ArchiveCard,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, ArchiveDivider),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = ArchiveCardElevated,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = event.time,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CaseGold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = event.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = event.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    countLabel: String,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            color = CaseSlate
        )
        if (onAction != null) {
            Text(
                text = "VIEW IN CASE FILE",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaseGold
                ),
                modifier = Modifier.clickable(onClick = onAction)
            )
        } else {
            Text(
                text = countLabel.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaseSlateDim
                )
            )
        }
    }
}

@Composable
private fun EmptySectionCard(message: String) {
    Surface(
        color = ArchiveCard,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, ArchiveDivider),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(14.dp)
        )
    }
}
