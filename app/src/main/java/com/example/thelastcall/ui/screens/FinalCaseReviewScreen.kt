package com.example.thelastcall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.ui.theme.*

@Composable
fun FinalCaseReviewScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onProceedToAccusation: () -> Unit,
    onReturnToInvestigation: () -> Unit,
    onOpenTheory: () -> Unit,
    onBack: () -> Unit
) {
    val readiness = state.getCaseReadiness(caseDef)
    var showIncompleteWarning by remember { mutableStateOf(false) }

    val suspect = state.playerTheory.suspectId?.let { caseDef.getSuspect(it) }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "CASE REVIEW",
                subtitle = "Pre-Indictment Assessment",
                onBack = onBack
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Readiness Header Banner
            Surface(
                color = if (readiness.isReadyForAccusation) StatusConfirmed.copy(alpha = 0.15f) else AccentAmber.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (readiness.isReadyForAccusation) StatusConfirmed else AccentAmber
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (readiness.isReadyForAccusation) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (readiness.isReadyForAccusation) StatusConfirmed else AccentAmber,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (readiness.isReadyForAccusation) "PROSECUTION READY" else "CASE INCOMPLETE",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (readiness.isReadyForAccusation) StatusConfirmed else AccentAmber
                        )
                        Text(
                            text = readiness.guidanceHint,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Theory Overview Card
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CURRENT CASE THEORY",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = AccentAmber
                        )

                        TextButton(
                            onClick = onOpenTheory,
                            modifier = Modifier.testTag("review_edit_theory_button")
                        ) {
                            Text("EDIT THEORY", color = AccentAmber, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ReviewPillarItem(
                        label = "PRIMARY SUSPECT",
                        value = suspect?.name ?: "None selected in Theory",
                        isEstablished = suspect != null
                    )

                    ReviewPillarItem(
                        label = "MOTIVE",
                        value = when (state.playerTheory.motiveKey) {
                            "MOTIVE_FINANCIAL" -> "Financial Fraud & Embezzlement Exposure"
                            "MOTIVE_FAMILY" -> "Family / Sibling Dispute"
                            "MOTIVE_CORPORATE" -> "Corporate Retaliation"
                            "MOTIVE_PANIC" -> "Panic over duplicate records"
                            else -> "Motive not assigned in Theory"
                        },
                        isEstablished = readiness.motiveConfidence == TheoryConfidence.ESTABLISHED
                    )

                    ReviewPillarItem(
                        label = "OPPORTUNITY & ALIBI",
                        value = if (readiness.opportunityConfidence == TheoryConfidence.ESTABLISHED) {
                            "Alibi broken by timeline contradiction and presence evidence"
                        } else {
                            "Alibi not yet broken by contradiction"
                        },
                        isEstablished = readiness.opportunityConfidence == TheoryConfidence.ESTABLISHED
                    )

                    ReviewPillarItem(
                        label = "MURDER WEAPON",
                        value = when (state.playerTheory.weaponKey) {
                            "WEAPON_PAPERWEIGHT" -> "Cast-iron heavy desk paperweight"
                            "WEAPON_GLASS" -> "Shattered drinking tumbler"
                            "WEAPON_UNARMED" -> "Unarmed physical altercation"
                            else -> "Weapon not assigned in Theory"
                        },
                        isEstablished = readiness.methodConfidence == TheoryConfidence.ESTABLISHED
                    )
                }
            }

            // Discovered Evidence Summary
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CRITICAL EVIDENCE CHAIN",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AccentCyan
                        )
                        Text(
                            text = "${state.discoveredEvidenceIds.size}/${caseDef.evidenceList.size} Found",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val criticalItems = caseDef.culpritSolution.criticalEvidenceIds.mapNotNull { id ->
                        caseDef.getEvidence(id)?.let { id to it.name }
                    }

                    criticalItems.forEach { (id, name) ->
                        val found = state.discoveredEvidenceIds.contains(id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (found) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (found) StatusConfirmed else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "[$id] $name",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (found) TextPrimary else TextMuted
                            )
                        }
                    }
                }
            }

            // Contradictions & Deductions Card
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ESTABLISHED LOGICAL REASONING",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentRed
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Contradictions Uncovered: ${state.unlockedContradictionIds.size}/${caseDef.contradictions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.unlockedContradictionIds.isNotEmpty()) TextPrimary else TextMuted
                    )

                    Text(
                        text = "• Deductions Established: ${state.unlockedDeductionIds.size}/${caseDef.deductions.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.unlockedDeductionIds.isNotEmpty()) TextPrimary else TextMuted
                    )

                    val nonCulprits = caseDef.suspects.count { it.id != caseDef.culpritSolution.culpritSuspectId }
                    Text(
                        text = "• Suspects Cleared by Alibi: ${state.clearedSuspectIds.size}/$nonCulprits",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.clearedSuspectIds.isNotEmpty()) StatusConfirmed else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Button(
                onClick = {
                    if (!readiness.isReadyForAccusation) {
                        showIncompleteWarning = true
                    } else {
                        onProceedToAccusation()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentRed,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("review_proceed_to_accusation_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PROCEED TO FINAL ACCUSATION",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }

            OutlinedButton(
                onClick = onReturnToInvestigation,
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("review_return_investigation_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("RETURN TO INVESTIGATION", color = TextPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showIncompleteWarning) {
        AlertDialog(
            onDismissRequest = { showIncompleteWarning = false },
            containerColor = SurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Case May Be Incomplete", color = AccentAmber, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "You have not yet established all core prosecution pillars (Motive, Opportunity Contradiction, and Fatal Weapon). Submitting premature charges may allow the defense to dismiss the case.\n\nDo you wish to return to the investigation or proceed to accusation anyway?",
                    color = TextPrimary,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showIncompleteWarning = false
                        onProceedToAccusation()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("warning_accuse_anyway_button")
                ) {
                    Text("PROCEED ANYWAY", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showIncompleteWarning = false },
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("warning_return_investigation_button")
                ) {
                    Text("KEEP INVESTIGATING", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun ReviewPillarItem(label: String, value: String, isEstablished: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextMuted
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isEstablished) TextPrimary else TextSecondary
            )
        }
        Icon(
            imageVector = if (isEstablished) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
            contentDescription = null,
            tint = if (isEstablished) StatusConfirmed else AccentAmber,
            modifier = Modifier.size(18.dp)
        )
    }
}
