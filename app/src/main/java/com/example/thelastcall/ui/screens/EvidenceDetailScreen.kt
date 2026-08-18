package com.example.thelastcall.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.thelastcall.ui.components.EvidenceVisualCard
import com.example.ui.theme.*

enum class EvidenceExamTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    ARTIFACT("ARTIFACT", Icons.Default.Search),
    FORENSICS("FORENSICS", Icons.Default.Biotech),
    ANALYSIS("ANALYSIS", Icons.Default.Psychology),
    LINKS("LINKS", Icons.Default.Hub)
}

@Composable
fun EvidenceDetailScreen(
    evidenceId: String,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    state: CaseState? = null,
    onSelectRelatedEvidence: (String) -> Unit = {},
    onSelectSuspect: (String) -> Unit = {},
    onBack: () -> Unit
) {
    val evidence = caseDef.getEvidence(evidenceId) ?: return
    var selectedTab by remember { mutableStateOf(EvidenceExamTab.ARTIFACT) }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "EVIDENCE EXAMINATION",
                subtitle = "${evidence.name} • ${evidence.category.displayName.uppercase()}",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Info Banner
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (evidence.isCritical) AccentRed else SurfaceBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = SurfaceCard,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = evidence.category.displayName.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = if (evidence.isCritical) AccentRedDark.copy(alpha = 0.4f) else SurfaceCard,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "IMPORTANCE: ${evidence.importance.label.uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (evidence.isCritical) AccentRed else AccentAmber
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = evidence.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = AccentRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Found at: ${evidence.location}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Examination Layer Tab Selector
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EvidenceExamTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        Surface(
                            color = if (isSelected) AccentAmber.copy(alpha = 0.2f) else Color.Transparent,
                            shape = RoundedCornerShape(6.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, AccentAmber) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = tab }
                                .testTag("exam_tab_${tab.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) AccentAmber else TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tab.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = if (isSelected) AccentAmber else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                EvidenceExamTab.ARTIFACT -> {
                    // Visual Artifact Rendering
                    EvidenceVisualCard(evidence = evidence)

                    // Initial Physical Observation Card
                    Surface(
                        color = SurfaceCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "INITIAL OBSERVATION",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AccentAmber
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = evidence.playerDescription,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // Nested sub-clues section (if any)
                    if (evidence.unlocksEvidenceOnInspect.isNotEmpty()) {
                        Surface(
                            color = Color(0xFF131A26),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.TravelExplore,
                                        contentDescription = null,
                                        tint = AccentCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "NESTED DATA & DERIVED CLUES",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = AccentCyan
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                evidence.unlocksEvidenceOnInspect.forEach { subId ->
                                    val subEv = caseDef.getEvidence(subId)
                                    if (subEv != null) {
                                        Surface(
                                            color = SurfaceElevated,
                                            shape = RoundedCornerShape(6.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable { onSelectRelatedEvidence(subId) }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = subEv.name,
                                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                        color = TextPrimary
                                                    )
                                                    Text(
                                                        text = subEv.significanceText,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = TextSecondary,
                                                        maxLines = 1
                                                    )
                                                }
                                                Icon(
                                                    imageVector = Icons.Default.ArrowForward,
                                                    contentDescription = null,
                                                    tint = AccentAmber,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                EvidenceExamTab.FORENSICS -> {
                    // Forensic & Detailed Investigation Card
                    Surface(
                        color = SurfaceCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Biotech,
                                    contentDescription = null,
                                    tint = AccentCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "FORENSIC ANALYSIS & LAB FINDINGS",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AccentCyan
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = evidence.detailedInvestigation,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // Discovery Condition Card
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "DISCOVERY LOG",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = evidence.discoveryCondition,
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                                color = TextSecondary
                            )
                        }
                    }
                }

                EvidenceExamTab.ANALYSIS -> {
                    // Case Significance Card
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = AccentAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "INVESTIGATIVE SIGNIFICANCE",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AccentAmber
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = evidence.significanceText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                EvidenceExamTab.LINKS -> {
                    // Related Connections
                    if (evidence.relatedSuspects.isNotEmpty() || evidence.relatedTimelineEvents.isNotEmpty() || evidence.relatedEvidence.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (evidence.relatedSuspects.isNotEmpty()) {
                                Surface(
                                    color = SurfaceCard,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "CONNECTED SUSPECTS",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = AccentCyan
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        evidence.relatedSuspects.forEach { sId ->
                                            val suspect = caseDef.getSuspect(sId)
                                            if (suspect != null) {
                                                Surface(
                                                    color = SurfaceElevated,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp)
                                                        .clickable { onSelectSuspect(sId) }
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(10.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = "${suspect.name} (${suspect.relationship})",
                                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                            color = TextPrimary
                                                        )
                                                        Text("INTERROGATE", color = AccentCyan, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (evidence.relatedEvidence.isNotEmpty()) {
                                Surface(
                                    color = SurfaceCard,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "CORROBORATING EVIDENCE",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = AccentAmber
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        evidence.relatedEvidence.forEach { relId ->
                                            val relEv = caseDef.getEvidence(relId)
                                            if (relEv != null) {
                                                Surface(
                                                    color = SurfaceElevated,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp)
                                                        .clickable { onSelectRelatedEvidence(relId) }
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(10.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = relEv.name,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = TextPrimary
                                                        )
                                                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Surface(
                            color = SurfaceCard,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No cross-references recorded yet for this piece of evidence.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceElevated,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("evidence_detail_back_button")
            ) {
                Text("RETURN TO CASE FILE", fontWeight = FontWeight.Bold)
            }
        }
    }
}
