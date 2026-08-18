package com.example.thelastcall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.thelastcall.ui.components.ConfirmDialog
import com.example.ui.theme.*

@Composable
fun SettingsDialog(
    settings: GameSettings,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onUpdateSettings: (sound: Boolean, haptics: Boolean, speed: TextSpeed, hints: Boolean) -> Unit,
    onResetCase: () -> Unit,
    onDismiss: () -> Unit
) {
    var sound by remember { mutableStateOf(settings.soundEnabled) }
    var haptics by remember { mutableStateOf(settings.hapticsEnabled) }
    var textSpeed by remember { mutableStateOf(settings.textSpeed) }
    var hints by remember { mutableStateOf(settings.hintsEnabled) }
    var showResetConfirm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = AccentAmber,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "SETTINGS & PREFERENCES",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Audio Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Sound Effects",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Atmospheric audio cues & chimes",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = sound,
                        onCheckedChange = {
                            sound = it
                            onUpdateSettings(sound, haptics, textSpeed, hints)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AccentAmber,
                            checkedTrackColor = AccentAmber.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag("setting_sound_switch")
                    )
                }

                HorizontalDivider(color = SurfaceBorder)

                // Haptics Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Haptic Feedback",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Tactile pulses for clues & contradictions",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = haptics,
                        onCheckedChange = {
                            haptics = it
                            onUpdateSettings(sound, haptics, textSpeed, hints)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AccentAmber,
                            checkedTrackColor = AccentAmber.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag("setting_haptics_switch")
                    )
                }

                HorizontalDivider(color = SurfaceBorder)

                // Text Speed Selector
                Column {
                    Text(
                        text = "Dialogue Text Speed",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TextSpeed.entries.forEach { speed ->
                            val isSelected = textSpeed == speed
                            Surface(
                                color = if (isSelected) AccentAmber else SurfaceCard,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) AccentAmber else SurfaceBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        textSpeed = speed
                                        onUpdateSettings(sound, haptics, textSpeed, hints)
                                    }
                            ) {
                                Text(
                                    text = speed.label,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) BackgroundDark else TextSecondary,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = SurfaceBorder)

                // Reset Case
                Button(
                    onClick = { showResetConfirm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRedDark.copy(alpha = 0.3f),
                        contentColor = AccentRed
                    ),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("reset_case_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = AccentRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RESET ${caseDef.title.uppercase()} PROGRESS", fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceElevated,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("DONE", fontWeight = FontWeight.Bold)
            }
        }
    )

    if (showResetConfirm) {
        ConfirmDialog(
            title = "Reset ${caseDef.title}?",
            message = "All discovered evidence, recorded statements, deductions, and case progress for ${caseDef.title} will be reset. Settings will be preserved.",
            confirmText = "RESET PROGRESS",
            isDestructive = true,
            onConfirm = {
                showResetConfirm = false
                onResetCase()
                onDismiss()
            },
            onDismiss = { showResetConfirm = false }
        )
    }
}
