package com.example.thelastcall.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrimeSceneScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onInspectHotspot: (CrimeSceneHotspot) -> Pair<EvidenceItem?, EvidenceItem?>,
    onOpenCaseFile: (CaseFileTab) -> Unit,
    onViewEvidenceDetail: (String) -> Unit,
    onDismissNotification: () -> Unit,
    onDismissTutorial: () -> Unit = {},
    onBack: () -> Unit
) {
    var selectedHotspot by remember { mutableStateOf<CrimeSceneHotspot?>(null) }
    var inspectingHotspot by remember { mutableStateOf<CrimeSceneHotspot?>(null) }
    var inspectionResult by remember { mutableStateOf<Pair<EvidenceItem?, EvidenceItem?>?>(null) }
    var isScanning by remember { mutableStateOf(false) }

    val activeObjective = caseDef.objectives.find { !state.completedObjectiveIds.contains(it.id) }?.title
        ?: "Examine remaining evidence or confront suspects"

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "CRIME SCENE",
                subtitle = "${caseDef.location} • ${caseDef.incidentTime}",
                onBack = onBack,
                onOpenCaseFile = { onOpenCaseFile(CaseFileTab.EVIDENCE) },
                activeObjective = activeObjective,
                onObjectiveClick = { onOpenCaseFile(CaseFileTab.OBJECTIVES) }
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                // First-time Investigation Tip Banner
                if (!state.hasSeenCrimeSceneTutorial && state.settings.hintsEnabled) {
                    Surface(
                        color = AccentAmber.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "INVESTIGATION TIP: Tap numbered markers or the carousel to inspect scene objects. Discovered clues are saved in your Case File.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            TextButton(
                                onClick = onDismissTutorial,
                                modifier = Modifier.testTag("dismiss_scene_tutorial_button")
                            ) {
                                Text("GOT IT", color = AccentAmber, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }

                // Minimal Investigation HUD
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationSearching,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedHotspot?.name ?: caseDef.location.uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        Surface(
                            color = SurfaceCard,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "EVIDENCE: ${state.discoveredEvidenceIds.size}/${caseDef.evidenceList.size}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AccentAmber
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Architectural Crime Scene Environment (Dominant Viewport)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    InteractiveCrimeSceneView(
                        hotspots = caseDef.crimeSceneHotspots,
                        discoveredEvidenceIds = state.discoveredEvidenceIds,
                        selectedHotspot = selectedHotspot,
                        onSelectHotspot = { hotspot ->
                            selectedHotspot = hotspot
                            inspectingHotspot = hotspot
                            inspectionResult = null
                            isScanning = false
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal Hotspot Quick Selector Carousel
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(caseDef.crimeSceneHotspots) { hotspot ->
                        val hasPrimary = state.discoveredEvidenceIds.contains(hotspot.primaryEvidenceId)
                        val hasSecondary = hotspot.secondaryEvidenceId == null || state.discoveredEvidenceIds.contains(hotspot.secondaryEvidenceId)
                        val isFullyExamined = hasPrimary && hasSecondary
                        val isSelected = selectedHotspot?.id == hotspot.id

                        Surface(
                            color = if (isSelected) AccentAmber.copy(alpha = 0.2f) else SurfaceCard,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AccentAmber else if (isFullyExamined) StatusConfirmed.copy(alpha = 0.4f) else SurfaceBorder
                            ),
                            modifier = Modifier
                                .clickable {
                                    selectedHotspot = hotspot
                                    inspectingHotspot = hotspot
                                    inspectionResult = null
                                    isScanning = false
                                }
                                .testTag("hotspot_chip_${hotspot.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isFullyExamined) Icons.Default.CheckCircle else Icons.Default.Search,
                                    contentDescription = null,
                                    tint = if (isFullyExamined) StatusConfirmed else AccentAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = hotspot.name,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isSelected) AccentAmber else TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Case Quick Navigation Bar
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        QuickNavButton(
                            label = "PINBOARD",
                            icon = Icons.Default.Hub,
                            badge = if (state.unlockedContradictionIds.isNotEmpty()) "!" else null,
                            badgeColor = if (state.unlockedContradictionIds.isNotEmpty()) AccentRed else AccentAmber,
                            onClick = { onOpenCaseFile(CaseFileTab.BOARD) },
                            modifier = Modifier.weight(1f).testTag("quick_nav_board")
                        )
                        QuickNavButton(
                            label = "SUSPECTS",
                            icon = Icons.Default.People,
                            badge = "${state.interviewedSuspectIds.size}/${caseDef.suspects.size}",
                            onClick = { onOpenCaseFile(CaseFileTab.SUSPECTS) },
                            modifier = Modifier.weight(1f).testTag("quick_nav_suspects")
                        )
                        QuickNavButton(
                            label = "TIMELINE",
                            icon = Icons.Default.Schedule,
                            badge = "${state.unlockedTimelineEventIds.size}/${caseDef.timelineEvents.size}",
                            onClick = { onOpenCaseFile(CaseFileTab.TIMELINE) },
                            modifier = Modifier.weight(1f).testTag("quick_nav_timeline")
                        )
                        QuickNavButton(
                            label = "REASONING",
                            icon = Icons.Default.Lightbulb,
                            badge = "${state.unlockedDeductionIds.size}",
                            badgeColor = AccentCyan,
                            onClick = { onOpenCaseFile(CaseFileTab.DEDUCTIONS) },
                            modifier = Modifier.weight(1f).testTag("quick_nav_deductions")
                        )
                    }
                }
            }

            // Notification Overlay
            state.activeNotification?.let { notif ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                ) {
                    NotificationToast(
                        notification = notif,
                        onDismiss = onDismissNotification
                    )
                }
            }
        }
    }

    // Cinematic Forensic Inspection Panel (Replaces generic AlertDialog)
    inspectingHotspot?.let { hotspot ->
        val hasPrimary = state.discoveredEvidenceIds.contains(hotspot.primaryEvidenceId)
        val hasSecondary = hotspot.secondaryEvidenceId == null || state.discoveredEvidenceIds.contains(hotspot.secondaryEvidenceId)
        val isFullyExamined = hasPrimary && hasSecondary

        val primaryEvidence = caseDef.getEvidence(hotspot.primaryEvidenceId)
        val secondaryEvidence = hotspot.secondaryEvidenceId?.let { caseDef.getEvidence(it) }

        ModalBottomSheet(
            onDismissRequest = {
                inspectingHotspot = null
                inspectionResult = null
                isScanning = false
            },
            containerColor = SurfaceElevated,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(SurfaceBorder, RoundedCornerShape(2.dp))
                )
            },
            modifier = Modifier.testTag("hotspot_inspection_panel")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Bar: Hotspot Title & Sector Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = AccentCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "SECTOR: ${hotspot.locationLabel.uppercase()}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp
                                    ),
                                    color = AccentCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = if (isFullyExamined) StatusConfirmed.copy(alpha = 0.15f) else AccentAmber.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (isFullyExamined) "PROCESSED" else "ACTIVE SEARCH",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = if (isFullyExamined) StatusConfirmed else AccentAmber,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = hotspot.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    IconButton(
                        onClick = {
                            inspectingHotspot = null
                            inspectionResult = null
                            isScanning = false
                        },
                        modifier = Modifier.testTag("hotspot_close_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Sector Detailed Observation Card
                Surface(
                    color = SurfaceCard,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "INITIAL SCENE OBSERVATION",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentAmber
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = hotspot.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }

                // Forensic Scanning Progress Animation
                if (isScanning) {
                    Surface(
                        color = Color(0xFF101824),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                color = AccentCyan,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Analyzing physical traces & searching sector...",
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = AccentCyan
                            )
                        }
                    }
                }

                // Inspection Action Trigger or Discovery Results
                if (inspectionResult == null && !isScanning) {
                    Button(
                        onClick = {
                            val result = onInspectHotspot(hotspot)
                            inspectionResult = result
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentRed,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("hotspot_examine_button")
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isFullyExamined) "RE-EXAMINE SECTOR TRACES" else "SEARCH & INSPECT THIS AREA",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                } else if (inspectionResult != null) {
                    val (prim, sec) = inspectionResult!!

                    // Results Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (prim != null || sec != null) {
                            Text(
                                text = "EVIDENTIARY TRACES SECURED",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = StatusConfirmed
                            )

                            if (prim != null) {
                                DetailedClueDiscoveredCard(
                                    evidence = prim,
                                    onExamine = {
                                        inspectingHotspot = null
                                        inspectionResult = null
                                        onViewEvidenceDetail(prim.id)
                                    }
                                )
                            }

                            if (sec != null) {
                                DetailedClueDiscoveredCard(
                                    evidence = sec,
                                    onExamine = {
                                        inspectingHotspot = null
                                        inspectionResult = null
                                        onViewEvidenceDetail(sec.id)
                                    }
                                )
                            }
                        } else {
                            // Already discovered previously
                            Surface(
                                color = SurfaceCard,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "ALL CLUES ALREADY DOCUMENTED",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "You have already secured all physical and digital evidence from this location into your Case File.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )

                                    if (primaryEvidence != null && state.discoveredEvidenceIds.contains(primaryEvidence.id)) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        OutlinedButton(
                                            onClick = {
                                                inspectingHotspot = null
                                                inspectionResult = null
                                                onViewEvidenceDetail(primaryEvidence.id)
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("VIEW ${primaryEvidence.name.uppercase()}", color = AccentAmber)
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Actions Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val targetEvidenceId = prim?.id ?: sec?.id ?: primaryEvidence?.id
                            if (targetEvidenceId != null) {
                                Button(
                                    onClick = {
                                        inspectingHotspot = null
                                        inspectionResult = null
                                        onViewEvidenceDetail(targetEvidenceId)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentAmber,
                                        contentColor = BackgroundDark
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("hotspot_view_evidence_button")
                                ) {
                                    Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("EXAMINE CLUE", fontWeight = FontWeight.Bold)
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    inspectingHotspot = null
                                    inspectionResult = null
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text("DONE", color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickNavButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badge: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeColor: Color = AccentAmber
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentAmber,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            if (badge != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    color = badgeColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailedClueDiscoveredCard(
    evidence: EvidenceItem,
    onExamine: () -> Unit
) {
    Surface(
        color = Color(0xFF131A15),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StatusConfirmed.copy(alpha = 0.8f)),
        modifier = Modifier.fillMaxWidth().testTag("evidence_secured_card_${evidence.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusConfirmed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EVIDENCE SECURED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = StatusConfirmed
                    )
                }

                Surface(
                    color = if (evidence.isCritical) AccentRed.copy(alpha = 0.25f) else SurfaceCard,
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (evidence.isCritical) AccentRed else SurfaceBorder)
                ) {
                    Text(
                        text = evidence.importance.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = if (evidence.isCritical) AccentRed else AccentAmber
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = evidence.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AccentAmber,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Location: ${evidence.location}",
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentAmber
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Initial observation:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = CaseSlate
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = evidence.playerDescription,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onExamine,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StatusConfirmed,
                    contentColor = BackgroundDark
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("inspect_secured_evidence_button")
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("INSPECT EVIDENCE", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
