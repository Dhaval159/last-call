package com.example.thelastcall.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.ui.theme.*

@Composable
fun LeadInvestigationScreen(
    state: CaseState,
    caseDef: CaseDefinition,
    onBackToHub: () -> Unit,
    onSelectLead: (String) -> Unit,
    onFollowObjective: (LeadObjective, String) -> Unit,
    onCompleteLead: (String) -> Unit
) {
    val leads = caseDef.leads
    val activeLead = caseDef.getCurrentInvestigationLead(state) ?: leads.firstOrNull()

    // Local selected lead for browsing if player clicks other lead tabs
    var selectedLeadId by remember(state.activeLeadId, activeLead?.id) {
        mutableStateOf(state.activeLeadId ?: activeLead?.id ?: leads.firstOrNull()?.id.orEmpty())
    }

    val currentLead = caseDef.getLead(selectedLeadId) ?: activeLead

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
    ) {
        // Top Navigation Bar
        LeadTopBar(
            caseDef = caseDef,
            onBackToHub = onBackToHub
        )

        // Lead Carousel / Selector Tabs
        if (leads.size > 1) {
            LeadSelectorRow(
                leads = leads,
                selectedLeadId = selectedLeadId,
                state = state,
                caseDef = caseDef,
                onSelect = { selectedLeadId = it }
            )
        }

        if (currentLead == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO INVESTIGATION LEADS REGISTERED FOR THIS CASE.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted
                )
            }
        } else {
            val isLeadCompleted = currentLead.isCompleted(state, caseDef) || state.completedLeadIds.contains(currentLead.id)
            val isUnlocked = currentLead.isUnlocked(state, caseDef)
            val completedObjCount = currentLead.objectives.count { obj ->
                state.completedLeadObjectiveIds.contains(obj.id) || obj.condition?.isMet(state, caseDef) == true
            }
            val totalObjCount = currentLead.objectives.size

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header & Status Card
                item {
                    LeadHeaderCard(
                        lead = currentLead,
                        isUnlocked = isUnlocked,
                        isCompleted = isLeadCompleted,
                        completedCount = completedObjCount,
                        totalCount = totalObjCount
                    )
                }

                // Narrative Briefing Section
                item {
                    LeadBriefingCard(lead = currentLead)
                }

                // Major Breakthrough Banner (if achieved)
                if (isLeadCompleted && currentLead.isMajorBreakthrough) {
                    item {
                        BreakthroughBanner(lead = currentLead)
                    }
                }

                // Lead Completion Banner (if completed but not necessarily breakthrough)
                if (isLeadCompleted && !currentLead.isMajorBreakthrough) {
                    item {
                        LeadCompletedBanner(lead = currentLead)
                    }
                }

                // Objectives Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(3.dp, 16.dp)
                                    .background(AccentAmber, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "INVESTIGATION OBJECTIVES",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                ),
                                color = TextPrimary
                            )
                        }

                        Text(
                            text = "$completedObjCount / $totalObjCount MET",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (completedObjCount == totalObjCount) StatusConfirmed else AccentAmber
                        )
                    }
                }

                // Objectives List
                items(currentLead.objectives) { objective ->
                    val isObjCompleted = state.completedLeadObjectiveIds.contains(objective.id) ||
                            objective.condition?.isMet(state, caseDef) == true

                    LeadObjectiveCard(
                        objective = objective,
                        isCompleted = isObjCompleted,
                        onAction = {
                            onFollowObjective(objective, currentLead.id)
                        }
                    )
                }

                // Next Lead / Progression Button
                if (isLeadCompleted && currentLead.nextLeadId != null) {
                    val nextLead = caseDef.getLead(currentLead.nextLeadId)
                    if (nextLead != null) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    selectedLeadId = nextLead.id
                                    onSelectLead(nextLead.id)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("next_lead_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentAmber,
                                    contentColor = BackgroundDark
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "PROCEED TO NEXT LEAD: ${nextLead.title}",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LeadTopBar(
    caseDef: CaseDefinition,
    onBackToHub: () -> Unit
) {
    Surface(
        color = SurfaceDark,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackToHub,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("lead_back_to_hub_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to Case Hub",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "LEAD INVESTIGATION",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = "${caseDef.id}: ${caseDef.title}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentAmber
                    )
                }
            }

            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Text(
                    text = "ACTIVE DOSSIER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = AccentCyan,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun LeadSelectorRow(
    leads: List<InvestigationLead>,
    selectedLeadId: String,
    state: CaseState,
    caseDef: CaseDefinition,
    onSelect: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark.copy(alpha = 0.6f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(leads) { lead ->
            val isSelected = lead.id == selectedLeadId
            val isCompleted = lead.isCompleted(state, caseDef) || state.completedLeadIds.contains(lead.id)
            val isUnlocked = lead.isUnlocked(state, caseDef)

            val chipBg = when {
                isSelected -> AccentAmber.copy(alpha = 0.2f)
                isCompleted -> StatusConfirmed.copy(alpha = 0.12f)
                isUnlocked -> SurfaceElevated
                else -> SurfaceDark.copy(alpha = 0.4f)
            }

            val chipBorder = when {
                isSelected -> AccentAmber
                isCompleted -> StatusConfirmed.copy(alpha = 0.6f)
                isUnlocked -> SurfaceBorder
                else -> Color.Transparent
            }

            val chipTextColor = when {
                isSelected -> AccentAmber
                isCompleted -> StatusConfirmed
                isUnlocked -> TextPrimary
                else -> TextMuted
            }

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(enabled = isUnlocked || isCompleted) { onSelect(lead.id) }
                    .testTag("lead_tab_${lead.id}"),
                color = chipBg,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, chipBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = StatusConfirmed,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    } else if (!isUnlocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = "LEAD 0${lead.orderIndex}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        color = chipTextColor
                    )
                }
            }
        }
    }
}

@Composable
private fun LeadHeaderCard(
    lead: InvestigationLead,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    completedCount: Int,
    totalCount: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceCard,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (isCompleted) StatusConfirmed.copy(alpha = 0.5f) else SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (isCompleted) StatusConfirmed.copy(alpha = 0.2f) else AccentAmber.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "LEAD 0${lead.orderIndex}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = if (isCompleted) StatusConfirmed else AccentAmber,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (lead.associatedLocation != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lead.associatedLocation,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    color = when {
                        isCompleted -> StatusConfirmed.copy(alpha = 0.15f)
                        isUnlocked -> AccentCyan.copy(alpha = 0.15f)
                        else -> SurfaceElevated
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when {
                            isCompleted -> "COMPLETED"
                            isUnlocked -> "IN PROGRESS"
                            else -> "LOCKED"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        ),
                        color = when {
                            isCompleted -> StatusConfirmed
                            isUnlocked -> AccentCyan
                            else -> TextMuted
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = lead.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = TextPrimary
            )

            if (lead.subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = lead.subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = AccentAmberLight
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isCompleted) StatusConfirmed else AccentAmber,
                    trackColor = SurfaceElevated
                )
            }
        }
    }
}

@Composable
private fun LeadBriefingCard(lead: InvestigationLead) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceElevated,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "INVESTIGATION BRIEFING",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = AccentCyan
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = lead.briefing,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun BreakthroughBanner(lead: InvestigationLead) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = StatusConfirmed.copy(alpha = 0.12f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, StatusConfirmed.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AccentAmber,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INVESTIGATION BREAKTHROUGH",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = AccentAmber
                )
            }

            if (lead.breakthroughTitle != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = lead.breakthroughTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            if (lead.breakthroughDescription != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lead.breakthroughDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            if (lead.completionSummary.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lead.completionSummary,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 18.sp),
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun LeadCompletedBanner(lead: InvestigationLead) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = StatusConfirmed.copy(alpha = 0.10f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, StatusConfirmed.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = StatusConfirmed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LEAD CONCLUDED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = StatusConfirmed
                )
            }

            if (lead.completionSummary.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = lead.completionSummary,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun LeadObjectiveCard(
    objective: LeadObjective,
    isCompleted: Boolean,
    onAction: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (isCompleted) SurfaceCard.copy(alpha = 0.7f) else SurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (isCompleted) StatusConfirmed.copy(alpha = 0.3f) else SurfaceBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) StatusConfirmed.copy(alpha = 0.2f)
                                else AccentAmber.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Search,
                            contentDescription = null,
                            tint = if (isCompleted) StatusConfirmed else AccentAmber,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = objective.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = if (isCompleted) TextPrimary else TextPrimary
                        )

                        if (objective.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = objective.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCompleted) {
                    Surface(
                        color = StatusConfirmed.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = StatusConfirmed,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "MET",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                ),
                                color = StatusConfirmed
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) SurfaceElevated else AccentAmber,
                        contentColor = if (isCompleted) TextPrimary else BackgroundDark
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("objective_action_${objective.id}")
                ) {
                    Text(
                        text = (objective.actionLabel ?: "INVESTIGATE").uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
