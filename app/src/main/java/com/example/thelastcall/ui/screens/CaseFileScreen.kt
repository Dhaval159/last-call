package com.example.thelastcall.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.thelastcall.ui.components.*
import com.example.ui.theme.*

@Composable
fun CaseFileScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onTabSelected: (CaseFileTab) -> Unit,
    onSelectSuspect: (String) -> Unit,
    onOpenPersonProfile: (String) -> Unit = {},
    onSelectEvidence: (String) -> Unit,
    onNavigateToAccusation: () -> Unit,
    onCompareStatement: (String) -> Unit,
    onTestContradiction: (String, String) -> Contradiction?,
    onTestDeduction: (String, String, ReasoningRelationship) -> Pair<Boolean, String>,
    onSelectSuspectInTheory: (String?) -> Unit,
    onSelectMotiveInTheory: (String?) -> Unit,
    onSelectWeaponInTheory: (String?) -> Unit,
    onToggleTheoryEvidence: (String) -> Unit,
    onAddNote: (String) -> Unit = {},
    onDeleteNote: (String) -> Unit = {},
    onFollowLead: (Objective) -> Unit = {},
    onAddConnection: (String, String, ReasoningRelationship) -> com.example.thelastcall.engine.BoardConnectionResult = { id1, id2, rel ->
        com.example.thelastcall.engine.ReasoningEngine.validateBoardConnection(id1, id2, rel, caseDef, state)
    },
    onRemoveConnection: (String) -> Unit = {},
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<EvidenceCategory?>(null) }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "CASE FILE",
                subtitle = "${caseDef.id.replace('-', ' ')} • ${caseDef.title.uppercase()}",
                onBack = onBack
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Segmented Tab Bar for all 7 Notebook Tabs
            ScrollableTabRow(
                selectedTabIndex = state.caseFileTab.ordinal,
                containerColor = SurfaceDark,
                contentColor = TextPrimary,
                edgePadding = 16.dp,
                divider = { HorizontalDivider(color = SurfaceBorder) }
            ) {
                CaseFileTab.entries.forEach { tab ->
                    val isSelected = state.caseFileTab == tab
                    val badge = when (tab) {
                        CaseFileTab.BOARD -> if (state.unlockedContradictionIds.isNotEmpty()) "!" else null
                        CaseFileTab.EVIDENCE -> "${state.discoveredEvidenceIds.size}/${caseDef.evidenceList.size}"
                        CaseFileTab.STATEMENTS -> "${state.recordedStatementIds.size}/${caseDef.statements.size}"
                        CaseFileTab.SUSPECTS -> "${state.interviewedSuspectIds.size}/${caseDef.suspects.size}"
                        CaseFileTab.TIMELINE -> "${state.unlockedTimelineEventIds.size}/${caseDef.timelineEvents.size}"
                        CaseFileTab.DEDUCTIONS -> if (state.unlockedContradictionIds.isNotEmpty()) "!" else "${state.unlockedDeductionIds.size}"
                        CaseFileTab.THEORY -> if (state.getCaseReadiness(caseDef).isReadyForAccusation) "READY" else null
                        CaseFileTab.NOTES -> if (state.playerNotes.isNotEmpty()) "${state.playerNotes.size}" else null
                        CaseFileTab.OBJECTIVES -> "${state.completedObjectiveIds.size}/${caseDef.objectives.size}"
                    }

                    Tab(
                        selected = isSelected,
                        onClick = { onTabSelected(tab) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = tab.label.uppercase(),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    ),
                                    color = if (isSelected) AccentAmber else TextSecondary
                                )
                                if (badge != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = when (badge) {
                                            "!" -> AccentRed.copy(alpha = 0.4f)
                                            "READY" -> StatusConfirmed.copy(alpha = 0.3f)
                                            else -> SurfaceElevated
                                        },
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = badge,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = when (badge) {
                                                "!" -> AccentRed
                                                "READY" -> StatusConfirmed
                                                else -> TextMuted
                                            },
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.testTag("case_file_tab_${tab.name.lowercase()}")
                    )
                }
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                when (state.caseFileTab) {
                    CaseFileTab.BOARD -> DetectiveBoardCanvas(
                        state = state,
                        caseDef = caseDef,
                        onSelectEvidenceForDetail = onSelectEvidence,
                        onSelectSuspectForInterview = onSelectSuspect,
                        onOpenPersonProfile = onOpenPersonProfile,
                        onOpenCaseFileTab = onTabSelected,
                        onAddConnection = onAddConnection,
                        onRemoveConnection = onRemoveConnection,
                        onTestContradiction = onTestContradiction,
                        onTestDeduction = onTestDeduction,
                        modifier = Modifier.fillMaxSize()
                    )
                    CaseFileTab.EVIDENCE -> EvidenceTabContent(
                        state = state,
                        caseDef = caseDef,
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        selectedCategory = selectedCategory,
                        onCategorySelect = { selectedCategory = if (selectedCategory == it) null else it },
                        onEvidenceClick = onSelectEvidence
                    )
                    CaseFileTab.STATEMENTS -> StatementsListView(
                        state = state,
                        caseDef = caseDef,
                        onCompareStatement = onCompareStatement
                    )
                    CaseFileTab.SUSPECTS -> SuspectsTabContent(
                        state = state,
                        caseDef = caseDef,
                        onSelectSuspect = onSelectSuspect,
                        onOpenPersonProfile = onOpenPersonProfile
                    )
                    CaseFileTab.TIMELINE -> TimelineTabContent(state = state, caseDef = caseDef)
                    CaseFileTab.DEDUCTIONS -> ReasoningBoardView(
                        state = state,
                        caseDef = caseDef,
                        initialPreselectedSourceId = state.selectedStatementId,
                        onTestContradiction = onTestContradiction,
                        onTestDeduction = onTestDeduction,
                        onSelectEvidenceForDetail = onSelectEvidence
                    )
                    CaseFileTab.THEORY -> TheoryBuilderView(
                        state = state,
                        caseDef = caseDef,
                        onSelectSuspect = onSelectSuspectInTheory,
                        onSelectMotive = onSelectMotiveInTheory,
                        onSelectWeapon = onSelectWeaponInTheory,
                        onToggleSupportingEvidence = onToggleTheoryEvidence,
                        onProceedToAccusation = onNavigateToAccusation
                    )
                    CaseFileTab.NOTES -> NotesTabContent(
                        state = state,
                        onAddNote = onAddNote,
                        onDeleteNote = onDeleteNote
                    )
                    CaseFileTab.OBJECTIVES -> ObjectivesTabContent(
                        state = state,
                        caseDef = caseDef,
                        onFollowLead = onFollowLead
                    )
                }
            }
        }
    }
}

@Composable
private fun EvidenceTabContent(
    state: CaseState,
    caseDef: CaseDefinition,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: EvidenceCategory?,
    onCategorySelect: (EvidenceCategory) -> Unit,
    onEvidenceClick: (String) -> Unit
) {
    val filteredEvidence = remember(state.discoveredEvidenceIds, searchQuery, selectedCategory, caseDef) {
        caseDef.evidenceList.filter { item ->
            val matchesCategory = selectedCategory == null || item.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true) || item.id.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search & Category Chips
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search clues, logs, reports...", color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AccentCyan) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = SurfaceBorder,
                focusedContainerColor = SurfaceCard,
                unfocusedContainerColor = SurfaceCard,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("evidence_search_input")
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredEvidence) { evidence ->
                val isDiscovered = state.discoveredEvidenceIds.contains(evidence.id)
                val isInspected = state.inspectedEvidenceIds.contains(evidence.id)

                EvidenceCard(
                    evidence = evidence,
                    isDiscovered = isDiscovered,
                    isInspected = isInspected,
                    onClick = { onEvidenceClick(evidence.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SuspectsTabContent(
    state: CaseState,
    caseDef: CaseDefinition,
    onSelectSuspect: (String) -> Unit,
    onOpenPersonProfile: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(caseDef.suspects) { suspect ->
            val isInterviewed = state.interviewedSuspectIds.contains(suspect.id)
            val isCleared = state.clearedSuspectIds.contains(suspect.id)
            val presentedToThisSuspect = state.presentedEvidenceRecords[suspect.id] ?: emptySet()
            val suspectQuestions = caseDef.questions.filter { it.suspectId == suspect.id }
            val askedCount = state.askedQuestionIds.count { qId -> suspectQuestions.any { it.id == qId } }

            val hasContradictionExposed = caseDef.contradictions.filter { it.suspectId == suspect.id }
                .any { state.unlockedContradictionIds.contains(it.id) }
            val hasMotiveExposed = (caseDef.culpritSolution.culpritSuspectId == suspect.id && state.hasDiscoveredMotive) ||
                caseDef.reactions.filter { it.suspectId == suspect.id && it.triggersMotiveId != null }
                    .any { presentedToThisSuspect.contains(it.evidenceId) }

            val behaviorState = suspect.getDynamicBehaviorState(
                isCleared = isCleared,
                hasContradictionExposed = hasContradictionExposed,
                hasMotiveExposed = hasMotiveExposed,
                askedCount = askedCount,
                presentedEvidenceCount = presentedToThisSuspect.size
            )

            SuspectCard(
                suspect = suspect,
                isInterviewed = isInterviewed,
                isCleared = isCleared,
                behaviorState = behaviorState,
                askedCount = askedCount,
                totalQuestions = suspectQuestions.size,
                onInterviewClick = { onSelectSuspect(suspect.id) },
                onProfileClick = { onOpenPersonProfile(suspect.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TimelineTabContent(state: CaseState, caseDef: CaseDefinition) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "INCIDENT CHRONOLOGY",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = AccentAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Reconstruct the chronological sequence of events surrounding ${caseDef.victimName}'s death.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        items(caseDef.timelineEvents) { event ->
            val isUnlocked = state.unlockedTimelineEventIds.contains(event.id)
            val supportingEvidence = event.sourceEvidenceId?.let { caseDef.getEvidence(it) }

            TimelineCard(
                event = event,
                isUnlocked = isUnlocked,
                supportingEvidenceName = supportingEvidence?.name
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ObjectivesTabContent(
    state: CaseState,
    caseDef: CaseDefinition,
    onFollowLead: (Objective) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "INVESTIGATION CHECKLIST",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = AccentAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Milestones track your progress from crime scene exploration to formal indictment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        items(caseDef.objectives) { objective ->
            val isComplete = state.completedObjectiveIds.contains(objective.id)
            Surface(
                color = if (isComplete) SurfaceCard else SurfaceDark.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isComplete) StatusConfirmed.copy(alpha = 0.4f) else SurfaceBorder
                ),
                modifier = Modifier.fillMaxWidth().testTag("objective_item_${objective.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isComplete) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isComplete) StatusConfirmed else TextMuted,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "MILESTONE",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                                    color = if (isComplete) StatusConfirmed else AccentAmber
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = objective.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isComplete) TextPrimary else TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = objective.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isComplete) TextSecondary else TextMuted
                            )
                        }
                    }

                    if (!isComplete && objective.leadActionLabel != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { onFollowLead(objective) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = objective.leadActionLabel.uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NotesTabContent(
    state: CaseState,
    onAddNote: (String) -> Unit,
    onDeleteNote: (String) -> Unit
) {
    var newNoteText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Add Note Input Box
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "INVESTIGATOR'S LOGBOOK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = CaseGold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newNoteText,
                    onValueChange = { newNoteText = it },
                    placeholder = { Text("Jot down hypotheses, contradictions, or lead ideas...", color = TextMuted) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 70.dp)
                        .testTag("new_note_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CaseGold,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (newNoteText.isNotBlank()) {
                            onAddNote(newNoteText.trim())
                            newNoteText = ""
                        }
                    },
                    enabled = newNoteText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CaseGold,
                        contentColor = ArchiveBackground
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .testTag("add_note_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RECORD NOTE", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // Notes List
        if (state.playerNotes.isEmpty()) {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = CaseSlate, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No personal detective notes added yet.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Use this notebook to track custom deduction paths and suspect alibis.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.playerNotes) { note ->
                    Surface(
                        color = SurfaceCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("player_note_${note.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.timestamp,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = CaseGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = note.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                            IconButton(
                                onClick = { onDeleteNote(note.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Note",
                                    tint = CaseSlate,
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

