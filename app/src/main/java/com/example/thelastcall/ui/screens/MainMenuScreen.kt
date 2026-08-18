package com.example.thelastcall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.ConfirmDialog
import com.example.ui.theme.*

@Composable
fun MainMenuScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    registeredCases: List<CaseDefinition> = CaseRegistry.getAllCases(),
    getCaseState: (String) -> CaseState = { state },
    onSelectCase: (String) -> Unit = {},
    onNewCase: () -> Unit,
    onContinue: () -> Unit,
    onOpenSettings: () -> Unit,
    onResetCase: (String) -> Unit = {}
) {
    var caseToRestart by remember { mutableStateOf<CaseDefinition?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArchiveBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "UNRESOLVED",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 34.sp,
                        letterSpacing = 8.sp
                    ),
                    color = PaperTint,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(1.dp)
                            .background(CaseGold.copy(alpha = 0.7f))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "AN ANTHOLOGY OF UNRESOLVED CASES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = CaseGold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(1.dp)
                            .background(CaseGold.copy(alpha = 0.7f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section: CASE FILES
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CASE FILES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.5.sp
                    ),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "OPEN INVESTIGATIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = CaseSlate
                    )
                )
            }

            registeredCases.forEachIndexed { index, registeredCase ->
                val caseState = getCaseState(registeredCase.id)
                val isUnlocked = registeredCase.isAvailable && !registeredCase.isPlaceholder
                val hasActiveSave = isUnlocked && caseState.caseStatus != CaseStatus.NOT_STARTED
                val caseTag = "archive_${registeredCase.id.lowercase().replace('-', '_')}"

                CaseArchiveCard(
                    caseNumber = registeredCase.id.replace('-', ' '),
                    caseTitle = registeredCase.title,
                    statusText = if (isUnlocked) caseStatusText(caseState.caseStatus) else "COMING SOON",
                    statusColor = if (isUnlocked) caseStatusColor(caseState.caseStatus) else StatusLocked,
                    metaLines = if (isUnlocked) {
                        listOf(
                            "VICTIM: ${registeredCase.victimName.uppercase()}, AGE ${registeredCase.victimAge}",
                            "SCENE: ${registeredCase.location.uppercase()}",
                            "TOD: ${registeredCase.incidentTime.uppercase()}"
                        )
                    } else {
                        listOf(
                            "SUBJECT: WITHHELD",
                            "LOCATION: WITHHELD",
                            "STATUS: NOT YET ASSIGNED"
                        )
                    },
                    progressLabel = if (isUnlocked) "EVIDENCE SECURED" else null,
                    progressValue = if (isUnlocked) "${caseState.discoveredEvidenceIds.size}/${registeredCase.evidenceList.size}" else null,
                    progressFraction = if (!isUnlocked || registeredCase.evidenceList.isEmpty()) 0f else caseState.discoveredEvidenceIds.size.toFloat() / registeredCase.evidenceList.size,
                    isUnlocked = isUnlocked,
                    primaryActionLabel = when {
                        !isUnlocked -> "LOCKED"
                        hasActiveSave -> "CONTINUE INVESTIGATION"
                        else -> "OPEN CASE"
                    },
                    onPrimaryAction = {
                        if (isUnlocked) {
                            onSelectCase(registeredCase.id)
                            if (hasActiveSave) onContinue() else onNewCase()
                        }
                    },
                    secondaryActionLabel = if (hasActiveSave) "RESTART CASE" else null,
                    onSecondaryAction = if (hasActiveSave) {
                        { caseToRestart = registeredCase }
                    } else null,
                    testTag = caseTag
                )

                if (index < registeredCases.size - 1) {
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Settings & Audio
            OutlinedButton(
                onClick = onOpenSettings,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                border = androidx.compose.foundation.BorderStroke(1.dp, ArchiveDivider),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("main_menu_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SETTINGS & AUDIO",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    val restartTarget = caseToRestart
    if (restartTarget != null) {
        ConfirmDialog(
            title = "Restart ${restartTarget.title}?",
            message = "Restarting will reset all progress on ${restartTarget.title}. Discovered clues, testimonies, and theories will be wiped.",
            confirmText = "RESTART CASE",
            isDestructive = true,
            onConfirm = {
                val targetId = restartTarget.id
                caseToRestart = null
                onResetCase(targetId)
                onSelectCase(targetId)
                onNewCase()
            },
            onDismiss = { caseToRestart = null }
        )
    }
}

@Composable
private fun CaseArchiveCard(
    caseNumber: String,
    caseTitle: String,
    statusText: String,
    statusColor: Color,
    metaLines: List<String>,
    progressLabel: String?,
    progressValue: String?,
    progressFraction: Float,
    isUnlocked: Boolean,
    primaryActionLabel: String,
    onPrimaryAction: () -> Unit,
    secondaryActionLabel: String?,
    onSecondaryAction: (() -> Unit)?,
    testTag: String
) {
    Surface(
        color = if (isUnlocked) ArchiveCard else ArchiveSurface,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnlocked) ArchiveDivider else ArchiveDivider.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = caseNumber,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    ),
                    color = if (isUnlocked) CaseGold else StatusLocked
                )

                if (isUnlocked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(statusColor, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) CaseGold else StatusLocked,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = caseTitle.uppercase(),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 1.5.sp
                    ),
                    color = if (isUnlocked) PaperTint else StatusLocked
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                metaLines.forEach { line ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = line,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUnlocked) CaseSlate else StatusLocked
                        )
                    }
                }
            }

            if (isUnlocked && progressLabel != null && progressValue != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = progressLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = TextSecondary
                    )
                    Text(
                        text = progressValue,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CaseGold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progressFraction },
                    color = CaseGold,
                    trackColor = ArchiveCardElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPrimaryAction,
                enabled = isUnlocked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isUnlocked) CaseGold else ArchiveCardElevated,
                    contentColor = if (isUnlocked) ArchiveBackground else StatusLocked,
                    disabledContainerColor = ArchiveCardElevated,
                    disabledContentColor = StatusLocked
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("${testTag}_primary_action")
            ) {
                Icon(
                    imageVector = if (isUnlocked) {
                        if (primaryActionLabel.contains("CONTINUE")) Icons.Default.PlayArrow else Icons.Default.FolderOpen
                    } else {
                        Icons.Default.Lock
                    },
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = primaryActionLabel,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (secondaryActionLabel != null && onSecondaryAction != null) {
                Spacer(modifier = Modifier.height(6.dp))
                TextButton(
                    onClick = onSecondaryAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("${testTag}_secondary_action")
                ) {
                    Text(
                        text = secondaryActionLabel,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun caseStatusText(status: CaseStatus): String = when (status) {
    CaseStatus.NOT_STARTED -> "NOT YET OPENED"
    CaseStatus.IN_PROGRESS -> "IN PROGRESS"
    CaseStatus.READY_FOR_ACCUSATION -> "READY FOR ACCUSATION"
    CaseStatus.SOLVED_PERFECT -> "SOLVED — PERFECT"
    CaseStatus.SOLVED -> "SOLVED"
    CaseStatus.SOLVED_INCOMPLETE -> "SOLVED — INCOMPLETE"
}

@Composable
private fun caseStatusColor(status: CaseStatus): Color = when (status) {
    CaseStatus.NOT_STARTED -> CaseSlate
    CaseStatus.IN_PROGRESS -> StatusInProgress
    CaseStatus.READY_FOR_ACCUSATION -> CaseGold
    CaseStatus.SOLVED_PERFECT -> StatusConfirmed
    CaseStatus.SOLVED -> StatusConfirmed
    CaseStatus.SOLVED_INCOMPLETE -> StatusWarning
}
