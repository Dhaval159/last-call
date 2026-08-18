package com.example.thelastcall.ui.components

import androidx.compose.animation.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.ui.theme.*

@Composable
fun StatementsListView(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onCompareStatement: (String) -> Unit
) {
    var selectedSuspectFilter by remember { mutableStateOf<String?>(null) }

    val allStatements = caseDef.statements
    val filteredStatements = if (selectedSuspectFilter != null) {
        allStatements.filter { it.suspectId == selectedSuspectFilter }
    } else {
        allStatements
    }

    val recordedCount = allStatements.count { state.recordedStatementIds.contains(it.id) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary & Stats Bar
        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RECORDED TESTIMONY",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$recordedCount of ${allStatements.size} Official Statements",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                Surface(
                    color = if (state.unlockedContradictionIds.isNotEmpty()) AccentRedDark.copy(alpha = 0.5f) else SurfaceCard,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (state.unlockedContradictionIds.isNotEmpty()) "CONTRADICTION FOUND" else "TESTIMONY UNVERIFIED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (state.unlockedContradictionIds.isNotEmpty()) AccentRed else AccentAmber
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Suspect Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedSuspectFilter == null,
                    onClick = { selectedSuspectFilter = null },
                    label = { Text("All Suspects") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = SurfaceCard,
                        labelColor = TextSecondary
                    )
                )
            }
            items(caseDef.suspects) { suspect ->
                val isSelected = selectedSuspectFilter == suspect.id
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedSuspectFilter = if (isSelected) null else suspect.id },
                    label = { Text(suspect.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = SurfaceCard,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        // Statements List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredStatements) { statement ->
                val isRecorded = state.recordedStatementIds.contains(statement.id)
                val suspect = caseDef.getSuspect(statement.suspectId)
                val isContradicted = statement.contradictionId != null && state.unlockedContradictionIds.contains(statement.contradictionId)

                StatementCard(
                    statement = statement,
                    suspect = suspect,
                    isRecorded = isRecorded,
                    isContradicted = isContradicted,
                    onCompareClick = { onCompareStatement(statement.id) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ReasoningBoardView(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    initialPreselectedSourceId: String? = null,
    onTestContradiction: (String, String) -> Contradiction?,
    onTestDeduction: (String, String, ReasoningRelationship) -> Pair<Boolean, String>,
    onSelectEvidenceForDetail: (String) -> Unit
) {
    var selectedItem1Id by remember { mutableStateOf(initialPreselectedSourceId ?: caseDef.statements.firstOrNull()?.id ?: "ST006") }
    var selectedItem2Id by remember { mutableStateOf(caseDef.evidenceList.firstOrNull()?.id ?: "E018") }
    var selectedRelationship by remember { mutableStateOf(ReasoningRelationship.CONTRADICTS) }

    var lastReasoningResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    var activeTab by remember { mutableStateOf(0) } // 0: Workbench, 1: Case Board, 2: Established Deductions

    // Build selectable options from discovered/recorded items
    val discoveredEvidenceList = remember(state.discoveredEvidenceIds, caseDef) {
        caseDef.evidenceList.filter { state.discoveredEvidenceIds.contains(it.id) }
    }
    val recordedStatementsList = remember(state.recordedStatementIds, caseDef) {
        caseDef.statements.filter { state.recordedStatementIds.contains(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sub-tabs: Workbench | Visual Case Board | Established Logic
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = SurfaceElevated,
            contentColor = AccentCyan,
            divider = {}
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text("REASONING BENCH", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = { Text("CASE BOARD", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)) }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = { Text("DEDUCTIONS (${state.unlockedDeductionIds.size})", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)) }
            )
        }

        when (activeTab) {
            0 -> {
                // Interactive Reasoning Workbench
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Surface(
                            color = SurfaceCard,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "DEDUCTION & CONTRADICTION WORKBENCH",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                    color = AccentCyan
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Pair testimony, evidence, or physical findings to test logical connections.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Item 1 Selector Box
                                Text(
                                    text = "SOURCE ITEM 1 (STATEMENT / EVIDENCE)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                ItemSelectorDropdown(
                                    selectedId = selectedItem1Id,
                                    caseDef = caseDef,
                                    evidenceList = discoveredEvidenceList,
                                    statementList = recordedStatementsList,
                                    onItemSelected = { selectedItem1Id = it }
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Relationship Selector
                                Text(
                                    text = "LOGICAL RELATIONSHIP",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(ReasoningRelationship.values()) { rel ->
                                        val isSelected = selectedRelationship == rel
                                        Surface(
                                            color = if (isSelected) {
                                                if (rel == ReasoningRelationship.CONTRADICTS) AccentRed else AccentCyan
                                            } else SurfaceElevated,
                                            shape = RoundedCornerShape(6.dp),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (isSelected) Color.Transparent else SurfaceBorder
                                            ),
                                            modifier = Modifier.clickable { selectedRelationship = rel }
                                        ) {
                                            Text(
                                                text = rel.displayName.uppercase(),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.Black else TextPrimary
                                                ),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Item 2 Selector Box
                                Text(
                                    text = "SOURCE ITEM 2 (EVIDENCE / CLUE)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                ItemSelectorDropdown(
                                    selectedId = selectedItem2Id,
                                    caseDef = caseDef,
                                    evidenceList = discoveredEvidenceList,
                                    statementList = recordedStatementsList,
                                    onItemSelected = { selectedItem2Id = it }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Action Button
                                Button(
                                    onClick = {
                                        if (selectedRelationship == ReasoningRelationship.CONTRADICTS) {
                                            val contradiction = onTestContradiction(selectedItem1Id, selectedItem2Id)
                                            if (contradiction != null) {
                                                lastReasoningResult = Pair(true, "CRITICAL CONTRADICTION FOUND:\n${contradiction.conflictSummary}")
                                            } else {
                                                val (isDed, msg) = onTestDeduction(selectedItem1Id, selectedItem2Id, selectedRelationship)
                                                lastReasoningResult = Pair(isDed, msg)
                                            }
                                        } else {
                                            val (isDed, msg) = onTestDeduction(selectedItem1Id, selectedItem2Id, selectedRelationship)
                                            lastReasoningResult = Pair(isDed, msg)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentAmber,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("action_test_reasoning")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "TEST HYPOTHESIS & REASONING",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black)
                                    )
                                }
                            }
                        }
                    }

                    // Result Banner
                    lastReasoningResult?.let { (isSuccess, message) ->
                        item {
                            Surface(
                                color = if (isSuccess) StatusConfirmed.copy(alpha = 0.15f) else SurfaceCard,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSuccess) StatusConfirmed else SurfaceBorder
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (isSuccess) StatusConfirmed else AccentAmber,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (isSuccess) "LOGICAL CONNECTION ESTABLISHED" else "INCONCLUSIVE CONNECTION",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSuccess) StatusConfirmed else AccentAmber
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = message,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Active Contradictions Found
                    if (state.unlockedContradictionIds.isNotEmpty()) {
                        item {
                            Text(
                                text = "ACTIVE CONTRADICTIONS",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentRed
                            )
                        }
                        items(state.unlockedContradictionIds.toList()) { cId ->
                            caseDef.getContradiction(cId)?.let { contradiction ->
                                val suspect = caseDef.getSuspect(contradiction.suspectId)
                                ContradictionCard(
                                    contradiction = contradiction,
                                    suspectName = suspect?.name ?: "Suspect"
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            1 -> {
                // Interactive Case Board / Connection Visualizer
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Surface(
                            color = SurfaceElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "VISUAL CASE PINBOARD",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AccentCyan
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Key figures and linked material evidence connecting the timeline of the murder.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    // Suspect Nodes dynamically from caseDef
                    items(caseDef.suspects) { suspect ->
                        val linkedEvidenceIds = caseDef.evidenceList.filter { it.relatedSuspects.contains(suspect.id) }.map { it.id }
                        val contradiction = caseDef.contradictions.find { it.suspectId == suspect.id }
                        SuspectBoardNode(
                            suspectId = suspect.id,
                            caseDef = caseDef,
                            state = state,
                            linkedEvidence = linkedEvidenceIds,
                            contradictionId = contradiction?.id,
                            onSelectEvidence = onSelectEvidenceForDetail
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }

            2 -> {
                // Established Deductions List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(caseDef.deductions) { deduction ->
                        val isUnlocked = state.unlockedDeductionIds.contains(deduction.id)
                        DeductionCard(
                            deduction = deduction,
                            isUnlocked = isUnlocked
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemSelectorDropdown(
    selectedId: String,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    evidenceList: List<EvidenceItem>,
    statementList: List<StatementItem>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val displayText = when {
        selectedId.startsWith("E") -> {
            val ev = caseDef.getEvidence(selectedId)
            "[EVIDENCE] ${ev?.name ?: selectedId}"
        }
        selectedId.startsWith("ST") -> {
            val st = caseDef.getStatement(selectedId)
            val suspect = st?.let { caseDef.getSuspect(it.suspectId) }
            "[STATEMENT] ${suspect?.name ?: "Suspect"}: ${st?.summary ?: selectedId}"
        }
        else -> selectedId
    }

    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = AccentCyan
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(SurfaceCard)
                .widthIn(min = 280.dp)
        ) {
            Text(
                text = "DISCOVERED EVIDENCE",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                color = AccentCyan,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
            evidenceList.forEach { ev ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = ev.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )
                    },
                    onClick = {
                        onItemSelected(ev.id)
                        expanded = false
                    }
                )
            }

            if (statementList.isNotEmpty()) {
                Divider(color = SurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "RECORDED STATEMENTS",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                    color = AccentAmber,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
                statementList.forEach { st ->
                    val suspect = caseDef.getSuspect(st.suspectId)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${suspect?.name ?: "Suspect"}: ${st.summary}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                        },
                        onClick = {
                            onItemSelected(st.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SuspectBoardNode(
    suspectId: String,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    state: CaseState,
    linkedEvidence: List<String>,
    contradictionId: String?,
    onSelectEvidence: (String) -> Unit
) {
    val suspect = caseDef.getSuspect(suspectId) ?: return
    val isCleared = state.clearedSuspectIds.contains(suspectId)
    val hasContradiction = contradictionId != null && state.unlockedContradictionIds.contains(contradictionId)

    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                hasContradiction -> AccentRed
                isCleared -> StatusConfirmed.copy(alpha = 0.5f)
                else -> SurfaceBorder
            }
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(suspect.avatarColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = suspect.initials,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = suspect.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = suspect.relationship,
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentAmber
                        )
                    }
                }

                Surface(
                    color = when {
                        hasContradiction -> AccentRedDark.copy(alpha = 0.5f)
                        isCleared -> StatusConfirmed.copy(alpha = 0.2f)
                        else -> SurfaceElevated
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when {
                            hasContradiction -> "ALIBI BROKEN"
                            isCleared -> "CLEARED"
                            else -> "UNDER INVESTIGATION"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                hasContradiction -> AccentRed
                                isCleared -> StatusConfirmed
                                else -> TextSecondary
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "LINKED EVIDENCE & RECORDS:",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(linkedEvidence) { evId ->
                    val ev = caseDef.getEvidence(evId)
                    val isDiscovered = state.discoveredEvidenceIds.contains(evId)

                    Surface(
                        color = if (isDiscovered) SurfaceElevated else SurfaceDark,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDiscovered) {
                                if (ev?.isCritical == true) AccentRed.copy(alpha = 0.6f) else AccentCyan.copy(alpha = 0.4f)
                            } else SurfaceBorder.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.clickable(enabled = isDiscovered) { onSelectEvidence(evId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isDiscovered) Icons.Default.Description else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isDiscovered) AccentCyan else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDiscovered) (ev?.name ?: "Clue") else "Undiscovered Clue",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDiscovered) TextPrimary else TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TheoryBuilderView(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onSelectSuspect: (String?) -> Unit,
    onSelectMotive: (String?) -> Unit,
    onSelectWeapon: (String?) -> Unit,
    onToggleSupportingEvidence: (String) -> Unit,
    onProceedToAccusation: () -> Unit
) {
    val theory = state.playerTheory
    val readiness = state.getCaseReadiness(caseDef)

    val motives = caseDef.motives.map { it.key to it.label }
    val weapons = caseDef.weapons.map { it.key to it.label }

    val criticalEvidenceOptions = remember(caseDef) {
        val criticalIds = caseDef.culpritSolution.criticalEvidenceIds.ifEmpty {
            caseDef.evidenceList.filter { it.isCritical }.map { it.id }
        }
        criticalIds.mapNotNull { evId ->
            caseDef.getEvidence(evId)?.let { ev -> evId to ev.name }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Readiness Gauge Banner
        item {
            CaseReadinessCard(
                readiness = readiness,
                onAccuseClick = onProceedToAccusation
            )
        }

        // Section 1: Designated Prime Suspect
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "1. DESIGNATE PRIME SUSPECT",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    caseDef.suspects.forEach { suspect ->
                        val isSelected = theory.suspectId == suspect.id
                        val isCleared = state.clearedSuspectIds.contains(suspect.id)

                        Surface(
                            color = if (isSelected) AccentCyan.copy(alpha = 0.15f) else SurfaceElevated,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AccentCyan else SurfaceBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onSelectSuspect(suspect.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { onSelectSuspect(suspect.id) },
                                        colors = RadioButtonDefaults.colors(selectedColor = AccentCyan)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = suspect.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = suspect.relationship,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                if (isCleared) {
                                    Surface(
                                        color = StatusConfirmed.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "ALIBI VERIFIED",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                            color = StatusConfirmed,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Established Motive
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "2. CRIMINAL MOTIVE",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AccentCyan
                        )
                        ConfidenceBadge(readiness.motiveConfidence)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    motives.forEach { (key, label) ->
                        val isSelected = theory.motiveKey == key
                        Surface(
                            color = if (isSelected) AccentCyan.copy(alpha = 0.15f) else SurfaceElevated,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AccentCyan else SurfaceBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onSelectMotive(key) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onSelectMotive(key) },
                                    colors = RadioButtonDefaults.colors(selectedColor = AccentCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Weapon / Method
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. MURDER WEAPON & METHOD",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AccentCyan
                        )
                        ConfidenceBadge(readiness.methodConfidence)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    weapons.forEach { (key, label) ->
                        val isSelected = theory.weaponKey == key
                        Surface(
                            color = if (isSelected) AccentCyan.copy(alpha = 0.15f) else SurfaceElevated,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) AccentCyan else SurfaceBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable { onSelectWeapon(key) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onSelectWeapon(key) },
                                    colors = RadioButtonDefaults.colors(selectedColor = AccentCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Critical Supporting Evidence Checklist
        item {
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "4. KEY EVIDENCE PILLARS",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    criticalEvidenceOptions.forEach { (evId, label) ->
                        val isDiscovered = state.discoveredEvidenceIds.contains(evId)
                        val isChecked = theory.supportingEvidenceIds.contains(evId)

                        Surface(
                            color = if (isChecked) AccentAmber.copy(alpha = 0.15f) else SurfaceElevated,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isChecked) AccentAmber else SurfaceBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable(enabled = isDiscovered) { onToggleSupportingEvidence(evId) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { if (isDiscovered) onToggleSupportingEvidence(evId) },
                                        enabled = isDiscovered,
                                        colors = CheckboxDefaults.colors(checkedColor = AccentAmber)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isDiscovered) label else "Undiscovered Physical Lead",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isDiscovered) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ConfidenceBadge(confidence: TheoryConfidence) {
    val (bgColor, textColor) = when (confidence) {
        TheoryConfidence.ESTABLISHED -> Pair(StatusConfirmed.copy(alpha = 0.2f), StatusConfirmed)
        TheoryConfidence.SUPPORTED -> Pair(AccentCyan.copy(alpha = 0.2f), AccentCyan)
        TheoryConfidence.SUGGESTED -> Pair(AccentAmber.copy(alpha = 0.2f), AccentAmber)
        TheoryConfidence.UNKNOWN -> Pair(SurfaceElevated, TextMuted)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = confidence.label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = textColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
