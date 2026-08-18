package com.example.thelastcall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.thelastcall.ui.components.NotificationToast
import com.example.ui.theme.*

@Composable
fun CaseHubScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onEnterCrimeScene: () -> Unit,
    onOpenCaseFile: (CaseFileTab) -> Unit,
    onOpenDetectiveBoard: () -> Unit,
    onOpenCaseReview: () -> Unit,
    onOpenCommunications: () -> Unit,
    onFollowLead: (Objective) -> Unit,
    onNotificationAction: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismissNotification: () -> Unit,
    onBack: () -> Unit
) {
    val readiness = state.getCaseReadiness(caseDef)
    val currentLead = caseDef.getCurrentLead(state)
    val caseTitle = "${caseDef.id} • ${caseDef.title.uppercase()}"

    Scaffold(
        topBar = {
            CaseTopBar(
                title = caseTitle,
                subtitle = caseDef.location,
                onBack = onBack
            )
        },
        containerColor = ArchiveBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { HubSectionLabel("CASE FILE") }

                item {
                    CaseIdentityHeader(
                        caseId = caseDef.id,
                        caseTitle = caseDef.title,
                        status = state.caseStatus,
                        onOpenObjectives = { onOpenCaseFile(CaseFileTab.OBJECTIVES) }
                    )
                }

                item { HubSectionLabel("CURRENT LEAD") }

                item {
                    CurrentLeadCard(
                        objective = currentLead,
                        completedCount = state.completedObjectiveIds.size,
                        totalCount = caseDef.objectives.size,
                        onFollow = { lead -> lead.let(onFollowLead) }
                    )
                }

                item { HubSectionLabel("RECENT DEVELOPMENTS") }

                item {
                    ActivityFeed(
                        state = state,
                        caseDef = caseDef
                    )
                }

                item { HubSectionLabel("INVESTIGATION TOOLS") }

                item {
                    InvestigationGrid(
                        state = state,
                        caseDef = caseDef,
                        onEnterCrimeScene = onEnterCrimeScene,
                        onOpenCaseFile = onOpenCaseFile,
                        onOpenDetectiveBoard = onOpenDetectiveBoard,
                        onOpenCommunications = onOpenCommunications
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }

                item {
                    Surface(
                        color = ArchiveCardElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (readiness.isReadyForAccusation) CaseGold else ArchiveDivider
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hub_review_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PROSECUTION STANDING",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = CaseGold
                                )
                                Surface(
                                    color = if (readiness.isReadyForAccusation) CaseGold.copy(alpha = 0.15f) else ArchiveCard,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (readiness.isReadyForAccusation) "INDICTMENT READY" else "DOSSIER INCOMPLETE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (readiness.isReadyForAccusation) CaseGold else CaseSlate
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = readiness.guidanceHint,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = onOpenCaseReview,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (readiness.isReadyForAccusation) CaseGold else ArchiveCard,
                                    contentColor = if (readiness.isReadyForAccusation) ArchiveBackground else TextPrimary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("hub_open_review_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (readiness.isReadyForAccusation) "OPEN FINAL CASE REVIEW" else "REVIEW CASE STANDING",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }

                item {
                    OutlinedButton(
                        onClick = onOpenSettings,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ArchiveDivider),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("hub_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SETTINGS & PREFERENCES", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            state.activeNotification?.let { notif ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                ) {
                    NotificationToast(
                        notification = notif,
                        onDismiss = onDismissNotification,
                        onAction = if (notif.actionLabel != null) onNotificationAction else null
                    )
                }
            }
        }
    }
}

@Composable
private fun HubSectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        ),
        color = CaseSlate,
        modifier = Modifier.padding(start = 2.dp)
    )
}

@Composable
private fun CaseIdentityHeader(
    caseId: String,
    caseTitle: String,
    status: CaseStatus,
    onOpenObjectives: () -> Unit
) {
    Surface(
        color = ArchiveCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ArchiveDivider),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hub_identity_header")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = caseId,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = CaseGold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = caseTitle.uppercase(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    letterSpacing = 1.5.sp
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = statusAccent(status).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusAccent(status).copy(alpha = 0.5f))
                ) {
                    Text(
                        text = statusLabel(status),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = statusAccent(status)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                TextButton(
                    onClick = onOpenObjectives,
                    modifier = Modifier.testTag("hub_open_objectives_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Checklist,
                        contentDescription = null,
                        tint = CaseSlate,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("OBJECTIVES", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = CaseSlate)
                }
            }
        }
    }
}

@Composable
private fun CurrentLeadCard(
    objective: Objective?,
    completedCount: Int,
    totalCount: Int,
    onFollow: (Objective) -> Unit
) {
    Surface(
        color = ArchiveCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (objective != null) CaseGold.copy(alpha = 0.6f) else ArchiveDivider),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hub_lead_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        tint = CaseGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CURRENT LEAD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = CaseGold
                    )
                }
                Text(
                    text = "$completedCount / $totalCount RESOLVED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CaseSlate
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (objective != null) {
                Text(
                    text = objective.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = objective.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )

                if (objective.leadActionLabel != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onFollow(objective) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CaseGold,
                            contentColor = ArchiveBackground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("hub_follow_lead_button")
                    ) {
                        Text(
                            text = objective.leadActionLabel.uppercase(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            } else {
                Text(
                    text = "All leads resolved — file formal charges.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = StatusConfirmed,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ActivityFeed(
    state: CaseState,
    caseDef: CaseDefinition
) {
    Surface(
        color = ArchiveCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ArchiveDivider),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hub_activity_card")
    ) {
        if (state.activityLog.isEmpty()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NO ACTIVITY YET",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = CaseSlate
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Enter the crime scene to begin securing evidence and interviewing witnesses.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        } else {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                state.activityLog.take(12).forEachIndexed { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(activityAccent(entry.kind).copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = activityIcon(entry.kind),
                                contentDescription = null,
                                tint = activityAccent(entry.kind),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entry.kind.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = activityAccent(entry.kind)
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = entry.detail,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = entry.timestamp,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = CaseSlate
                        )
                    }
                    if (index < state.activityLog.take(12).size - 1) {
                        HorizontalDivider(
                            color = ArchiveDivider,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun activityIcon(kind: ActivityKind): ImageVector = when (kind) {
    ActivityKind.EVIDENCE -> Icons.Default.FolderOpen
    ActivityKind.INTERVIEW -> Icons.Default.RecordVoiceOver
    ActivityKind.CLEARED -> Icons.Default.CheckCircle
    ActivityKind.CONTRADICTION -> Icons.Default.Warning
    ActivityKind.DEDUCTION -> Icons.Default.Lightbulb
    ActivityKind.TIMELINE -> Icons.Default.Schedule
    ActivityKind.LEAD -> Icons.Default.Navigation
    ActivityKind.MESSAGE -> Icons.Default.Email
    ActivityKind.NOTE -> Icons.Default.MenuBook
}

private fun activityAccent(kind: ActivityKind): Color = when (kind) {
    ActivityKind.EVIDENCE -> CaseGold
    ActivityKind.INTERVIEW -> CaseSlate
    ActivityKind.CLEARED -> StatusConfirmed
    ActivityKind.CONTRADICTION -> StatusContradiction
    ActivityKind.DEDUCTION -> CaseGold
    ActivityKind.TIMELINE -> CaseSlate
    ActivityKind.LEAD -> AccentAmberLight
    ActivityKind.MESSAGE -> AccentCyan
    ActivityKind.NOTE -> CaseSlate
}

@Composable
private fun InvestigationGrid(
    state: CaseState,
    caseDef: CaseDefinition,
    onEnterCrimeScene: () -> Unit,
    onOpenCaseFile: (CaseFileTab) -> Unit,
    onOpenDetectiveBoard: () -> Unit,
    onOpenCommunications: () -> Unit
) {
    val digitalEvidenceCount = caseDef.evidenceList.count {
        it.presentation() == EvidencePresentation.CALL_RECORD || it.presentation() == EvidencePresentation.MESSAGE
    }
    val entries = listOf(
        HubEntry(
            label = "CRIME SCENE",
            caption = "Search & secure traces",
            icon = Icons.Default.Search,
            badge = "${state.discoveredEvidenceIds.size}",
            onClick = onEnterCrimeScene,
            tag = "hub_nav_crime_scene"
        ),
        HubEntry(
            label = "EVIDENCE",
            caption = "Case File artifacts",
            icon = Icons.Default.FolderOpen,
            badge = "${state.discoveredEvidenceIds.size}/${caseDef.evidenceList.size}",
            onClick = { onOpenCaseFile(CaseFileTab.EVIDENCE) },
            tag = "hub_nav_evidence"
        ),
        HubEntry(
            label = "PEOPLE",
            caption = "Suspects & interviews",
            icon = Icons.Default.People,
            badge = "${state.interviewedSuspectIds.size}/${caseDef.suspects.size}",
            onClick = { onOpenCaseFile(CaseFileTab.SUSPECTS) },
            tag = "hub_nav_people"
        ),
        HubEntry(
            label = "TIMELINE",
            caption = "Incident chronology",
            icon = Icons.Default.Schedule,
            badge = "${state.unlockedTimelineEventIds.size}/${caseDef.timelineEvents.size}",
            onClick = { onOpenCaseFile(CaseFileTab.TIMELINE) },
            tag = "hub_nav_timeline"
        ),
        HubEntry(
            label = "BOARD",
            caption = "Visual case synthesis",
            icon = Icons.Default.Hub,
            badge = if (state.unlockedContradictionIds.isNotEmpty()) "!" else null,
            onClick = onOpenDetectiveBoard,
            tag = "hub_nav_board"
        ),
        HubEntry(
            label = "MESSAGES",
            caption = "Calls & communications",
            icon = Icons.Default.Email,
            badge = if (digitalEvidenceCount > 0) "$digitalEvidenceCount" else null,
            onClick = onOpenCommunications,
            tag = "hub_nav_messages"
        ),
        HubEntry(
            label = "REASONING",
            caption = "Contradictions & deductions",
            icon = Icons.Default.Lightbulb,
            badge = if (state.unlockedContradictionIds.isNotEmpty()) "${state.unlockedDeductionIds.size}" else null,
            onClick = { onOpenCaseFile(CaseFileTab.DEDUCTIONS) },
            tag = "hub_nav_reasoning"
        ),
        HubEntry(
            label = "THEORY",
            caption = "Build your case theory",
            icon = Icons.Default.Assignment,
            badge = null,
            onClick = { onOpenCaseFile(CaseFileTab.THEORY) },
            tag = "hub_nav_theory"
        ),
        HubEntry(
            label = "NOTES",
            caption = "Statements & logs",
            icon = Icons.Default.MenuBook,
            badge = "${state.recordedStatementIds.size}/${caseDef.statements.size}",
            onClick = { onOpenCaseFile(CaseFileTab.STATEMENTS) },
            tag = "hub_nav_notes"
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        entries.chunked(2).forEach { rowEntries ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowEntries.forEach { entry ->
                    HubEntryCard(
                        entry = entry,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowEntries.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun HubEntryCard(
    entry: HubEntry,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ArchiveCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ArchiveDivider),
        modifier = modifier
            .height(96.dp)
            .clickable(onClick = entry.onClick)
            .testTag(entry.tag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(CaseGold.copy(alpha = 0.12f), RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = entry.icon,
                        contentDescription = null,
                        tint = CaseGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (entry.badge != null) {
                    Surface(
                        color = ArchiveCardElevated,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = entry.badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (entry.badge == "!") StatusContradiction else CaseSlate
                            ),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Column {
                Text(
                    text = entry.label,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = entry.caption,
                    style = MaterialTheme.typography.labelSmall,
                    color = CaseSlate,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun statusLabel(status: CaseStatus): String = when (status) {
    CaseStatus.NOT_STARTED -> "NOT YET OPENED"
    CaseStatus.IN_PROGRESS -> "IN PROGRESS"
    CaseStatus.READY_FOR_ACCUSATION -> "READY FOR ACCUSATION"
    CaseStatus.SOLVED_PERFECT -> "SOLVED — PERFECT"
    CaseStatus.SOLVED -> "SOLVED"
    CaseStatus.SOLVED_INCOMPLETE -> "SOLVED — INCOMPLETE"
}

@Composable
private fun statusAccent(status: CaseStatus): Color = when (status) {
    CaseStatus.NOT_STARTED -> CaseSlate
    CaseStatus.IN_PROGRESS -> StatusInProgress
    CaseStatus.READY_FOR_ACCUSATION -> CaseGold
    CaseStatus.SOLVED_PERFECT -> StatusConfirmed
    CaseStatus.SOLVED -> StatusConfirmed
    CaseStatus.SOLVED_INCOMPLETE -> StatusWarning
}

private data class HubEntry(
    val label: String,
    val caption: String,
    val icon: ImageVector,
    val badge: String?,
    val onClick: () -> Unit,
    val tag: String
)
