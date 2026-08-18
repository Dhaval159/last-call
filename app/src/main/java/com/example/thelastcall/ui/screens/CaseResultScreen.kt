package com.example.thelastcall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.ConfirmDialog
import com.example.ui.theme.*

@Composable
fun CaseResultScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onReviewCaseFile: () -> Unit,
    onPlayAgain: () -> Unit,
    onReturnToMainMenu: () -> Unit
) {
    val isPerfect = state.caseStatus == CaseStatus.SOLVED_PERFECT
    var showReplayConfirmation by remember { mutableStateOf(false) }

    val title = if (isPerfect) "PERFECT INVESTIGATION" else "CASE SOLVED"
    val bannerColor = if (isPerfect) StatusConfirmed else AccentAmber

    val totalEvidence = caseDef.evidenceList.size
    val missingEvidenceCount = (totalEvidence - state.discoveredEvidenceIds.size).coerceAtLeast(0)
    val culprit = caseDef.getSuspect(caseDef.culpritSolution.culpritSuspectId)

    Scaffold(
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Victory Banner
            Surface(
                color = bannerColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, bannerColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (isPerfect) Icons.Default.Verified else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = bannerColor,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                        color = bannerColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${caseDef.id.uppercase()}: ${caseDef.title.uppercase()} • CLOSED & CONVICTED",
                        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                        color = TextPrimary
                    )
                }
            }

            // Culprit Overview
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CULPRIT CONVICTED",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AccentRed
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = culprit?.name?.uppercase() ?: caseDef.culpritSolution.culpritSummaryHeader.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = caseDef.culpritSolution.culpritSummaryDetails.ifEmpty {
                            "Motive: Confirmed and prosecuted.\nFatal Means: Weapon identified and matched to physical trauma."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Decisive Contradiction
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = AccentRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "THE DECISIVE CONTRADICTION",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AccentRed
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = caseDef.culpritSolution.decisiveContradictionSummary.ifEmpty {
                            caseDef.culpritSolution.solvedFeedbackMessage
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }

            // Complete Chronological Reconstruction
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TRUE SEQUENCE OF EVENTS",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = AccentAmber
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (caseDef.culpritSolution.chronologicalReconstructionSteps.isNotEmpty()) {
                        caseDef.culpritSolution.chronologicalReconstructionSteps.forEach { step ->
                            ReconstructionStep(time = step.time, text = step.description)
                        }
                    } else {
                        caseDef.timelineEvents.forEach { event ->
                            ReconstructionStep(time = event.time, text = "${event.title} — ${event.description}")
                        }
                    }
                }
            }

            // Investigation Scorecard
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "INVESTIGATION METRICS",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricItem("EVIDENCE", "${state.discoveredEvidenceIds.size}/${caseDef.evidenceList.size}")
                        MetricItem("SUSPECTS", "${state.interviewedSuspectIds.size}/${caseDef.suspects.size}")
                        MetricItem("TIMELINE", "${state.unlockedTimelineEventIds.size}/${caseDef.timelineEvents.size}")
                        MetricItem("REASONING", "${state.unlockedDeductionIds.size}/${caseDef.deductions.size}")
                    }

                    if (missingEvidenceCount > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Note: $missingEvidenceCount optional evidence pieces remained undiscovered during this playthrough. Replaying can unlock a 100% complete archive.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Actions
            Button(
                onClick = onReviewCaseFile,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceElevated,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("result_review_case_file_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = AccentAmber,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("REVIEW COMPLETE CASE FILE", fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { showReplayConfirmation = true },
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("result_play_again_button")
                ) {
                    Text("REPLAY CASE", color = TextPrimary, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onReturnToMainMenu,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("result_main_menu_button")
                ) {
                    Text("MAIN MENU", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showReplayConfirmation) {
        ConfirmDialog(
            title = "Replay ${caseDef.title}?",
            message = "Replaying will reset all evidence, suspect interviews, and deduction progress for ${caseDef.title} while preserving your audio and text settings. Proceed?",
            confirmText = "REPLAY CASE",
            isDestructive = false,
            onConfirm = {
                showReplayConfirmation = false
                onPlayAgain()
            },
            onDismiss = { showReplayConfirmation = false }
        )
    }
}

@Composable
private fun ReconstructionStep(time: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AccentAmber,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}
