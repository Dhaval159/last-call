package com.example.thelastcall.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.ui.theme.*

@Composable
fun EvidenceCard(
    evidence: EvidenceItem,
    isDiscovered: Boolean,
    isInspected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isDiscovered) SurfaceCard else SurfaceDark.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (evidence.isCritical && isDiscovered) AccentRed.copy(alpha = 0.6f) else SurfaceBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isDiscovered, onClick = onClick)
            .testTag("evidence_card_${evidence.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (isDiscovered) {
                            if (evidence.isCritical) AccentRedDark.copy(alpha = 0.4f) else SurfaceElevated
                        } else SurfaceBorder.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getEvidenceIcon(evidence.iconType, isDiscovered),
                    contentDescription = evidence.name,
                    tint = if (isDiscovered) {
                        if (evidence.isCritical) AccentRed else AccentAmber
                    } else TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isDiscovered) evidence.name else "Undiscovered Clue",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isDiscovered) TextPrimary else TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = evidence.category.displayName.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = AccentCyan,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (evidence.isCritical && isDiscovered) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = AccentRedDark.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "CRITICAL",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                color = AccentRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isDiscovered) evidence.location else "Location unknown",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                if (isDiscovered) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = evidence.playerDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun SuspectCard(
    suspect: Suspect,
    isInterviewed: Boolean,
    isCleared: Boolean,
    behaviorState: SuspectBehaviorState = SuspectBehaviorState.CALM,
    askedCount: Int = 0,
    totalQuestions: Int = 0,
    onInterviewClick: () -> Unit,
    onProfileClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = onProfileClick != null) { onProfileClick?.invoke() }
            .testTag("suspect_card_${suspect.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SuspectPortrait(
                    suspectId = suspect.id,
                    size = 56.dp,
                    showBorder = false
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = suspect.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )

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
                            border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = behaviorState.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                color = badgeColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${suspect.relationship} • ${suspect.occupation}, Age ${suspect.age}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentAmber
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = suspect.publicStory,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (totalQuestions > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Questions Explored",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = "$askedCount / $totalQuestions answered",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (askedCount == totalQuestions) StatusConfirmed else AccentAmber
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onInterviewClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceElevated,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("interview_button_${suspect.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Forum,
                    contentDescription = "Interrogate",
                    tint = AccentCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isInterviewed) "CONTINUE INTERROGATION" else "BEGIN INTERROGATION",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (onProfileClick != null) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onProfileClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentAmber),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderLight),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("profile_button_${suspect.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "VIEW PROFILE & DOSSIER",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineCard(
    event: TimelineEvent,
    isUnlocked: Boolean,
    supportingEvidenceName: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isUnlocked) SurfaceCard else SurfaceDark.copy(alpha = 0.6f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnlocked) SurfaceBorder else SurfaceBorder.copy(alpha = 0.4f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("timeline_card_${event.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(72.dp)
            ) {
                Surface(
                    color = if (isUnlocked) SurfaceElevated else SurfaceBorder.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = event.time,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (isUnlocked) AccentAmber else TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isUnlocked) "CONFIRMED" else "UNKNOWN",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                    color = if (isUnlocked) StatusConfirmed else TextMuted
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isUnlocked) event.title else "Unconfirmed Sequence",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isUnlocked) TextPrimary else TextMuted
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isUnlocked) event.description else "Discover supporting evidence to reconstruct this timeline entry.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUnlocked) TextSecondary else TextMuted
                )

                if (isUnlocked && supportingEvidenceName != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SOURCE: ",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AccentCyan
                        )
                        Text(
                            text = supportingEvidenceName,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatementCard(
    statement: StatementItem,
    suspect: Suspect?,
    isRecorded: Boolean,
    isContradicted: Boolean,
    onCompareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isRecorded) SurfaceCard else SurfaceDark.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                !isRecorded -> SurfaceBorder.copy(alpha = 0.3f)
                isContradicted -> AccentRed.copy(alpha = 0.8f)
                else -> SurfaceBorder
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("statement_card_${statement.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (suspect != null && isRecorded) Color(suspect.avatarColorHex) else SurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isRecorded) (suspect?.initials ?: "?") else "?",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRecorded) (suspect?.name ?: "Unknown Suspect") else "Unrecorded Statement",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (isRecorded) TextPrimary else TextMuted
                    )
                }

                Surface(
                    color = when {
                        !isRecorded -> SurfaceElevated
                        isContradicted -> AccentRedDark.copy(alpha = 0.5f)
                        else -> SurfaceElevated
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when {
                            !isRecorded -> "NOT RECORDED"
                            isContradicted -> "CONTRADICTED"
                            else -> "RECORDED"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                !isRecorded -> TextMuted
                                isContradicted -> AccentRed
                                else -> AccentCyan
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isRecorded) {
                Text(
                    text = "\"${statement.statementText}\"",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Source: ${statement.sourceContext}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentAmber
                    )

                    statement.timestamp?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onCompareClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AccentCyan
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("compare_statement_${statement.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "COMPARE IN REASONING BOARD",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            } else {
                Text(
                    text = "Interrogate suspects during interviews to capture this official statement.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
fun ContradictionCard(
    contradiction: Contradiction,
    suspectName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, AccentRed),
        modifier = modifier
            .fillMaxWidth()
            .testTag("contradiction_card_${contradiction.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = AccentRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CONTRADICTION FOUND",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                        color = AccentRed
                    )
                }

                Surface(
                    color = AccentRedDark.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = suspectName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = AccentRed,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = contradiction.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = contradiction.fullExplanation,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun CaseReadinessCard(
    readiness: CaseReadiness,
    onAccuseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (readiness.isReadyForAccusation) AccentRed else SurfaceBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("case_readiness_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "PROSECUTION READINESS",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, letterSpacing = 1.sp),
                    color = if (readiness.isReadyForAccusation) AccentRed else AccentAmber
                )

                Surface(
                    color = if (readiness.isReadyForAccusation) StatusConfirmed.copy(alpha = 0.2f) else SurfaceCard,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (readiness.isReadyForAccusation) "INDICTMENT READY" else "INCOMPLETE DOSSIER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (readiness.isReadyForAccusation) StatusConfirmed else AccentAmber
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pillar status indicators
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ReadinessPillarRow("Primary Suspect", readiness.hasSuspect)
                ReadinessPillarRow("Criminal Motive", readiness.motiveConfidence == TheoryConfidence.ESTABLISHED, readiness.motiveConfidence.label)
                ReadinessPillarRow("Opportunity / Timeline Anchor", readiness.opportunityConfidence == TheoryConfidence.ESTABLISHED, readiness.opportunityConfidence.label)
                ReadinessPillarRow("Murder Method & Weapon", readiness.methodConfidence == TheoryConfidence.ESTABLISHED, readiness.methodConfidence.label)
                ReadinessPillarRow("Decisive Alibi Contradiction", readiness.hasContradiction)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = readiness.guidanceHint,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAccuseClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (readiness.isReadyForAccusation) AccentRed else SurfaceCard,
                    contentColor = if (readiness.isReadyForAccusation) Color.White else TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("action_proceed_to_accusation")
            ) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (readiness.isReadyForAccusation) "FILE FORMAL HOMICIDE CHARGES" else "REVIEW ACCUSATION STANDING",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun ReadinessPillarRow(
    label: String,
    isComplete: Boolean,
    statusOverride: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isComplete) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isComplete) StatusConfirmed else TextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isComplete) TextPrimary else TextSecondary
            )
        }

        Text(
            text = statusOverride ?: if (isComplete) "ESTABLISHED" else "REQUIRED",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = if (isComplete) StatusConfirmed else TextMuted
        )
    }
}

@Composable
fun DeductionCard(
    deduction: Deduction,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isUnlocked) SurfaceCard else SurfaceDark.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnlocked) StatusConfirmed.copy(alpha = 0.5f) else SurfaceBorder.copy(alpha = 0.4f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("deduction_card_${deduction.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isUnlocked) Icons.Default.Lightbulb else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isUnlocked) AccentAmber else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "REASONING LOG",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isUnlocked) AccentAmber else TextMuted
                        )
                    }

                Surface(
                    color = if (isUnlocked) StatusConfirmed.copy(alpha = 0.2f) else SurfaceElevated,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isUnlocked) "DEDUCTION ESTABLISHED" else "LOCKED",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = if (isUnlocked) StatusConfirmed else TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isUnlocked) deduction.title else "Undiscovered Connection",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isUnlocked) TextPrimary else TextMuted
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isUnlocked) deduction.reasoning else "Connect relevant clues and contradictions to formulate this deduction.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUnlocked) TextSecondary else TextMuted
            )
        }
    }
}

private fun getEvidenceIcon(iconType: String, isDiscovered: Boolean): ImageVector {
    if (!isDiscovered) return Icons.Default.Search
    return when (iconType) {
        "phone", "call" -> Icons.Default.Phone
        "document", "folder", "receipt", "vault" -> Icons.Default.Description
        "fingerprint" -> Icons.Default.Fingerprint
        "glass" -> Icons.Default.LocalBar
        "weapon" -> Icons.Default.Build
        "door" -> Icons.Default.MeetingRoom
        "window" -> Icons.Default.Window
        "laptop", "digital" -> Icons.Default.Laptop
        "transit" -> Icons.Default.DirectionsCar
        "access_card" -> Icons.Default.CreditCard
        "statement", "verdict" -> Icons.Default.RecordVoiceOver
        else -> Icons.Default.Article
    }
}
