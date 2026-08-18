package com.example.thelastcall.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.ContradictionChallenge
import com.example.thelastcall.engine.ChallengeAttemptResult
import com.example.ui.theme.*

/**
 * Player-driven contradiction challenge. The suspect has been confronted with
 * evidence; the player must explain the conflict by selecting the option that
 * correctly articulates what the evidence establishes. The correct answer lives
 * in case data and is never surfaced here before the player responds.
 */
@Composable
fun ContradictionChallengeDialog(
    challenge: ContradictionChallenge,
    lastAttempt: ChallengeAttemptResult?,
    contradictionTitle: String?,
    onSelectOption: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val accepted = lastAttempt?.accepted == true
    val hasAttempted = lastAttempt != null

    AlertDialog(
        onDismissRequest = {
            if (!accepted) onDismiss()
        },
        containerColor = SurfaceElevated,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (accepted) Icons.Default.CheckCircle else Icons.Default.PriorityHigh,
                    contentDescription = null,
                    tint = if (accepted) StatusConfirmed else AccentRed,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (accepted) "CONTRADICTION ESTABLISHED" else "CONTRADICTION CHALLENGE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = if (accepted) StatusConfirmed else TextPrimary
                    )
                    Text(
                        text = "CRITICAL TESTIMONY INCONSISTENCY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp
                        ),
                        color = if (accepted) StatusConfirmed else AccentAmber
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (contradictionTitle != null) {
                    Surface(
                        color = if (accepted) StatusConfirmed.copy(alpha = 0.12f) else AccentRed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, if (accepted) StatusConfirmed.copy(alpha = 0.4f) else AccentRed.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = contradictionTitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = if (accepted) StatusConfirmed else AccentRedGlow,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = "WHAT EXACTLY DOES THE EVIDENCE REFUTE?",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = CaseSlate
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = challenge.prompt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                challenge.options.forEach { option ->
                    val isSelected = lastAttempt?.selectedOption?.key == option.key
                    val wasCorrect = accepted && option.isCorrect
                    val wasWrongChoice = hasAttempted && !accepted && isSelected

                    Surface(
                        color = when {
                            wasCorrect -> StatusConfirmed.copy(alpha = 0.18f)
                            wasWrongChoice -> AccentRed.copy(alpha = 0.15f)
                            else -> SurfaceCard
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            when {
                                wasCorrect -> StatusConfirmed
                                wasWrongChoice -> AccentRed
                                else -> SurfaceBorder
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("challenge_option_${option.key}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !accepted) { onSelectOption(option.key) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                color = when {
                                    wasCorrect -> StatusConfirmed
                                    wasWrongChoice -> AccentRed
                                    else -> SurfaceElevated
                                },
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.size(width = 28.dp, height = 28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = option.key,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = if (wasCorrect || wasWrongChoice) BackgroundDark else TextPrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (wasCorrect) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = TextPrimary,
                                modifier = Modifier.weight(1f),
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                if (lastAttempt != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        color = if (accepted) Color(0xFF131F16) else Color(0xFF221616),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            if (accepted) StatusConfirmed.copy(alpha = 0.6f) else AccentRed.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (accepted) Icons.Default.CheckCircle else Icons.Default.PriorityHigh,
                                    contentDescription = null,
                                    tint = if (accepted) StatusConfirmed else AccentAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (accepted) "LOGICAL DEDUCTION ESTABLISHED" else "INCONSISTENT REASONING",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = if (accepted) StatusConfirmed else AccentAmber
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lastAttempt.feedback,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                            if (!accepted) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Reconsider the physical traces and select the direct conflict.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (accepted) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StatusConfirmed,
                        contentColor = BackgroundDark
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("challenge_continue_button")
                ) {
                    Text("CONTINUE INTERROGATION", fontWeight = FontWeight.Bold)
                }
            } else if (!hasAttempted) {
                TextButton(onClick = onDismiss) {
                    Text("REVIEW CASE FILE", color = TextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}
