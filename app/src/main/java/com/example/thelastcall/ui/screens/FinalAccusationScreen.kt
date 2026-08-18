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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.ui.theme.*

@Composable
fun FinalAccusationScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onSubmitAccusation: (AccusationSubmission) -> Unit,
    onDismissFeedback: () -> Unit,
    onBack: () -> Unit
) {
    var selectedSuspectId by remember { mutableStateOf(state.playerTheory.suspectId ?: state.selectedSuspectId ?: caseDef.suspects.firstOrNull()?.id ?: "") }
    var selectedMotiveKey by remember { mutableStateOf(state.playerTheory.motiveKey ?: caseDef.motives.firstOrNull()?.key ?: "") }
    var selectedWeaponKey by remember { mutableStateOf(state.playerTheory.weaponKey ?: caseDef.weapons.firstOrNull()?.key ?: "") }
    val selectedEvidenceIds = remember { mutableStateListOf<String>() }
    var showSubmissionConfirmation by remember { mutableStateOf(false) }

    // Pre-populate with theory's supporting evidence or critical items discovered
    LaunchedEffect(Unit) {
        if (state.playerTheory.supportingEvidenceIds.isNotEmpty()) {
            state.playerTheory.supportingEvidenceIds.forEach {
                if (!selectedEvidenceIds.contains(it)) selectedEvidenceIds.add(it)
            }
        } else {
            caseDef.culpritSolution.criticalEvidenceIds.forEach {
                if (state.discoveredEvidenceIds.contains(it) && !selectedEvidenceIds.contains(it)) {
                    selectedEvidenceIds.add(it)
                }
            }
        }
    }

    val motives = caseDef.motives.map { it.key to it.label }
    val weapons = caseDef.weapons.map { it.key to it.label }

    val discoveredEvidenceList = remember(state.discoveredEvidenceIds, caseDef) {
        caseDef.evidenceList.filter { state.discoveredEvidenceIds.contains(it.id) }
    }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "FINAL ACCUSATION",
                subtitle = "Formal Indictment Filing",
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
            // Header Warning
            Surface(
                color = AccentRedDark.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = AccentRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "You are about to file formal homicide charges with the prosecution office. Ensure your accusation is backed by physical evidence and broken alibis.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }

            // Step 1: Suspect Selection
            Text(
                text = "1. WHO IS RESPONSIBLE FOR ${caseDef.victimName.uppercase()}'S DEATH?",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = AccentAmber
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                caseDef.suspects.forEach { suspect ->
                    val isSelected = selectedSuspectId == suspect.id
                    Surface(
                        color = if (isSelected) AccentRedDark.copy(alpha = 0.3f) else SurfaceCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) AccentRed else SurfaceBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSuspectId = suspect.id }
                            .testTag("accuse_suspect_${suspect.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedSuspectId = suspect.id },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AccentRed,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = suspect.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) TextPrimary else TextSecondary
                                )
                                Text(
                                    text = "${suspect.relationship} • ${suspect.occupation}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Step 2: Motive Selection
            Text(
                text = "2. WHAT WAS THE PRIMARY MOTIVE?",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = AccentAmber
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                motives.forEach { (key, label) ->
                    val isSelected = selectedMotiveKey == key
                    Surface(
                        color = if (isSelected) AccentAmber.copy(alpha = 0.15f) else SurfaceCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) AccentAmber else SurfaceBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMotiveKey = key }
                            .testTag("accuse_motive_$key")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedMotiveKey = key },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AccentAmber,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }

            // Step 3: Weapon Selection
            Text(
                text = "3. WHAT WAS THE FATAL WEAPON / MEANS?",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = AccentAmber
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                weapons.forEach { (key, label) ->
                    val isSelected = selectedWeaponKey == key
                    Surface(
                        color = if (isSelected) AccentCyan.copy(alpha = 0.15f) else SurfaceCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) AccentCyan else SurfaceBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedWeaponKey = key }
                            .testTag("accuse_weapon_$key")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedWeaponKey = key },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = AccentCyan,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }

            // Step 4: Supporting Evidence
            Text(
                text = "4. SELECT SUPPORTING EVIDENCE (${selectedEvidenceIds.size} SELECTED)",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = AccentAmber
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                discoveredEvidenceList.forEach { evidence ->
                    val isChecked = selectedEvidenceIds.contains(evidence.id)
                    Surface(
                        color = if (isChecked) SurfaceElevated else SurfaceCard,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isChecked) AccentAmber else SurfaceBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) selectedEvidenceIds.remove(evidence.id)
                                else selectedEvidenceIds.add(evidence.id)
                            }
                            .testTag("accuse_evidence_${evidence.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = {
                                    if (isChecked) selectedEvidenceIds.remove(evidence.id)
                                    else selectedEvidenceIds.add(evidence.id)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentAmber,
                                    uncheckedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = evidence.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = if (isChecked) TextPrimary else TextSecondary
                                    )
                                    if (evidence.isCritical) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = AccentRedDark.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "CRITICAL",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                                color = AccentRed,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = evidence.significanceText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Submit Button
            Button(
                onClick = { showSubmissionConfirmation = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentRed,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_accusation_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CONFIRM AND SUBMIT CHARGES",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }

    // Submission Confirmation Dialog
    if (showSubmissionConfirmation) {
        val suspectName = caseDef.getSuspect(selectedSuspectId)?.name ?: "the accused"
        AlertDialog(
            onDismissRequest = { showSubmissionConfirmation = false },
            containerColor = SurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = AccentRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Final Indictment Confirmation",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = AccentRed
                    )
                }
            },
            text = {
                Text(
                    text = "You are formally submitting murder charges against $suspectName with ${selectedEvidenceIds.size} supporting evidence pieces.\n\nOnce submitted, the prosecution will evaluate your findings. Are you ready to proceed?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmissionConfirmation = false
                        val submission = AccusationSubmission(
                            suspectId = selectedSuspectId,
                            motiveKey = selectedMotiveKey,
                            weaponKey = selectedWeaponKey,
                            selectedEvidenceIds = selectedEvidenceIds.toList()
                        )
                        onSubmitAccusation(submission)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("confirm_submit_accusation_dialog_button")
                ) {
                    Text("SUBMIT CHARGES", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSubmissionConfirmation = false },
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("cancel_submit_accusation_dialog_button")
                ) {
                    Text("REVIEW CASE", color = TextSecondary)
                }
            }
        )
    }

    // Feedback Dialog for Non-Fatal (Wrong or Premature) Accusations
    state.lastAccusationEvaluation?.let { evaluation ->
        if (!evaluation.isCorrectCulprit || evaluation.isPremature) {
            AlertDialog(
                onDismissRequest = onDismissFeedback,
                containerColor = SurfaceElevated,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AccentRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = evaluation.title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AccentRed
                        )
                    }
                },
                text = {
                    Text(
                        text = evaluation.feedbackMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = onDismissFeedback,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentAmber,
                            contentColor = BackgroundDark
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("dismiss_accusation_feedback_button")
                    ) {
                        Text("RETURN TO INVESTIGATION", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
