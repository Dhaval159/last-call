package com.example.thelastcall.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.engine.BoardConnectionResult
import com.example.thelastcall.engine.ReasoningEngine
import com.example.ui.theme.*
import kotlin.math.roundToInt

enum class BoardNodeType(val label: String, val pinColor: Color) {
    SUSPECT("Suspect", AccentCyan),
    EVIDENCE("Evidence", AccentAmber),
    STATEMENT("Statement", Color(0xFF81D4FA)),
    TIMELINE("Timeline", Color(0xFFCE93D8)),
    CONTRADICTION("Contradiction", AccentRed),
    DEDUCTION("Deduction", Color(0xFFFFD54F))
}

data class BoardNodeData(
    val id: String,
    val type: BoardNodeType,
    val title: String,
    val subtitle: String,
    val description: String,
    val x: Float, // in virtual board coordinates (0..2000)
    val y: Float, // in virtual board coordinates (0..1600)
    val colorHex: Long? = null,
    val isCritical: Boolean = false,
    val isContradicted: Boolean = false,
    val isCleared: Boolean = false,
    val connectionCount: Int = 0,
    val rawId: String
)

data class BoardThreadLink(
    val id: String,
    val sourceId: String,
    val targetId: String,
    val color: Color,
    val isContradiction: Boolean = false,
    val isDeduction: Boolean = false,
    val isCustom: Boolean = true,
    val label: String
)

enum class BoardFilterType(val label: String) {
    ALL("All Discovered"),
    SUSPECTS("Suspects"),
    EVIDENCE("Evidence"),
    STATEMENTS("Statements"),
    TIMELINE("Timeline"),
    CONTRADICTIONS("Contradictions"),
    DEDUCTIONS("Deductions")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetectiveBoardCanvas(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onSelectEvidenceForDetail: (String) -> Unit = {},
    onSelectSuspectForInterview: (String) -> Unit = {},
    onOpenPersonProfile: (String) -> Unit = {},
    onOpenCaseFileTab: (CaseFileTab) -> Unit = {},
    onAddConnection: (String, String, ReasoningRelationship) -> BoardConnectionResult = { id1, id2, rel ->
        ReasoningEngine.validateBoardConnection(id1, id2, rel, caseDef, state)
    },
    onRemoveConnection: (String) -> Unit = {},
    onTestContradiction: (String, String) -> Contradiction? = { _, _ -> null },
    onTestDeduction: (String, String, ReasoningRelationship) -> Pair<Boolean, String> = { _, _, _ -> Pair(false, "") },
    modifier: Modifier = Modifier
) {
    // Zoom and Pan state
    var scale by remember { mutableFloatStateOf(0.85f) }
    var offsetX by remember { mutableFloatStateOf(-180f) }
    var offsetY by remember { mutableFloatStateOf(-120f) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(BoardFilterType.ALL) }
    var selectedNodeA by remember { mutableStateOf<BoardNodeData?>(null) }
    var selectedNodeB by remember { mutableStateOf<BoardNodeData?>(null) }
    var inspectingNode by remember { mutableStateOf<BoardNodeData?>(null) }

    var selectedRelationship by remember { mutableStateOf(ReasoningRelationship.SUPPORTS) }
    var reasoningResultBanner by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    // Count player connections for a given node rawId
    fun getNodeConnectionCount(rawId: String): Int {
        return state.customConnections.count { it.sourceId == rawId || it.targetId == rawId }
    }

    // Build data-driven nodes strictly from player's unlocked knowledge (NO UNDISCOVERED SPOILERS)
    val allNodes = remember(
        state.discoveredEvidenceIds,
        state.recordedStatementIds,
        state.interviewedSuspectIds,
        state.unlockedDeductionIds,
        state.unlockedContradictionIds,
        state.unlockedTimelineEventIds,
        state.customConnections,
        state.clearedSuspectIds
    ) {
        val nodes = mutableListOf<BoardNodeData>()

        // 1. Suspect Nodes (Positioned horizontally across top-middle)
        caseDef.suspects.forEachIndexed { index, suspect ->
            val isCleared = state.clearedSuspectIds.contains(suspect.id)
            val contradiction = caseDef.contradictions.find { it.suspectId == suspect.id && state.unlockedContradictionIds.contains(it.id) }
            val hasContradiction = contradiction != null

            val posX = 260f + index * 420f
            val posY = 380f

            nodes.add(
                BoardNodeData(
                    id = "SUSPECT_${suspect.id}",
                    type = BoardNodeType.SUSPECT,
                    title = suspect.name,
                    subtitle = suspect.relationship.uppercase(),
                    description = suspect.initialAlibiSummary,
                    x = posX,
                    y = posY,
                    colorHex = suspect.avatarColorHex,
                    isContradicted = hasContradiction,
                    isCleared = isCleared,
                    connectionCount = getNodeConnectionCount(suspect.id),
                    rawId = suspect.id
                )
            )
        }

        // 2. Discovered Timeline Nodes (Anchored top center above suspects)
        val visibleTimelineIds = state.unlockedTimelineEventIds
        caseDef.timelineEvents.filter { visibleTimelineIds.contains(it.id) }.forEachIndexed { index, event ->
            val posX = 200f + (index % 4) * 440f
            val posY = 140f + (index / 4) * 110f

            nodes.add(
                BoardNodeData(
                    id = "TIMELINE_${event.id}",
                    type = BoardNodeType.TIMELINE,
                    title = "${event.time} — ${event.title}",
                    subtitle = "VERIFIED TIMELINE ANCHOR",
                    description = event.description,
                    x = posX,
                    y = posY,
                    connectionCount = getNodeConnectionCount(event.id),
                    rawId = event.id
                )
            )
        }

        // 3. Discovered Evidence Nodes ONLY
        val discoveredEvidence = caseDef.evidenceList.filter { state.discoveredEvidenceIds.contains(it.id) }
        discoveredEvidence.forEachIndexed { index, ev ->
            val (posX, posY) = when (ev.category) {
                EvidenceCategory.DIGITAL -> Pair(150f + (index % 2) * 270f, 680f + (index / 2) * 190f)
                EvidenceCategory.PHYSICAL -> Pair(720f + (index % 3) * 270f, 700f + (index / 3) * 190f)
                EvidenceCategory.DOCUMENTARY -> Pair(1480f + (index % 2) * 270f, 680f + (index / 2) * 190f)
                else -> Pair(420f + (index % 4) * 270f, 1080f + (index / 4) * 190f)
            }

            nodes.add(
                BoardNodeData(
                    id = "EVIDENCE_${ev.id}",
                    type = BoardNodeType.EVIDENCE,
                    title = ev.name,
                    subtitle = "${ev.category.displayName.uppercase()} • ${ev.location}",
                    description = ev.playerDescription,
                    x = posX,
                    y = posY,
                    isCritical = ev.isCritical,
                    connectionCount = getNodeConnectionCount(ev.id),
                    rawId = ev.id
                )
            )
        }

        // 4. Recorded Statements ONLY (Clustered under suspects)
        val recordedStatements = caseDef.statements.filter { state.recordedStatementIds.contains(it.id) }
        recordedStatements.forEachIndexed { _, st ->
            val suspectIndex = caseDef.suspects.indexOfFirst { it.id == st.suspectId }.coerceAtLeast(0)
            val suspectBaseX = 260f + suspectIndex * 420f
            val suspectStCount = recordedStatements.filter { it.suspectId == st.suspectId }.indexOf(st)

            val posX = suspectBaseX - 60f + (suspectStCount % 2) * 170f
            val posY = 530f + (suspectStCount / 2) * 120f

            val isContradicted = st.contradictionId != null && state.unlockedContradictionIds.contains(st.contradictionId)

            nodes.add(
                BoardNodeData(
                    id = "STATEMENT_${st.id}",
                    type = BoardNodeType.STATEMENT,
                    title = st.sourceContext,
                    subtitle = st.timestamp ?: "Testimony",
                    description = "\"${st.summary}\"",
                    x = posX,
                    y = posY,
                    isContradicted = isContradicted,
                    connectionCount = getNodeConnectionCount(st.id),
                    rawId = st.id
                )
            )
        }

        // 5. Unlocked Contradictions ONLY
        state.unlockedContradictionIds.forEachIndexed { index, cId ->
            caseDef.getContradiction(cId)?.let { contradiction ->
                nodes.add(
                    BoardNodeData(
                        id = "CONTRADICTION_${contradiction.id}",
                        type = BoardNodeType.CONTRADICTION,
                        title = "CONTRADICTION: ${contradiction.title}",
                        subtitle = "CRITICAL ALIBI BREACH",
                        description = contradiction.conflictSummary,
                        x = 750f + index * 360f,
                        y = 1350f,
                        isCritical = true,
                        isContradicted = true,
                        connectionCount = getNodeConnectionCount(contradiction.id),
                        rawId = contradiction.id
                    )
                )
            }
        }

        // 6. Unlocked Deductions ONLY
        state.unlockedDeductionIds.forEachIndexed { index, dId ->
            caseDef.getDeduction(dId)?.let { deduction ->
                val posX = 200f + (index % 4) * 440f
                val posY = 1500f + (index / 4) * 160f

                nodes.add(
                    BoardNodeData(
                        id = "DEDUCTION_${deduction.id}",
                        type = BoardNodeType.DEDUCTION,
                        title = "DEDUCTION: ${deduction.title}",
                        subtitle = "ESTABLISHED DEDUCTIVE FACT",
                        description = deduction.reasoning,
                        x = posX,
                        y = posY,
                        connectionCount = getNodeConnectionCount(deduction.id),
                        rawId = deduction.id
                    )
                )
            }
        }

        nodes
    }

    // Filter nodes based on active filter and live search query
    val filteredNodes = remember(allNodes, selectedFilter, searchQuery) {
        val query = searchQuery.trim().lowercase()
        val categoryFiltered = when (selectedFilter) {
            BoardFilterType.ALL -> allNodes
            BoardFilterType.SUSPECTS -> allNodes.filter { it.type == BoardNodeType.SUSPECT }
            BoardFilterType.EVIDENCE -> allNodes.filter { it.type == BoardNodeType.EVIDENCE }
            BoardFilterType.STATEMENTS -> allNodes.filter { it.type == BoardNodeType.STATEMENT }
            BoardFilterType.TIMELINE -> allNodes.filter { it.type == BoardNodeType.TIMELINE }
            BoardFilterType.CONTRADICTIONS -> allNodes.filter { it.type == BoardNodeType.CONTRADICTION }
            BoardFilterType.DEDUCTIONS -> allNodes.filter { it.type == BoardNodeType.DEDUCTION }
        }
        if (query.isEmpty()) {
            categoryFiltered
        } else {
            categoryFiltered.filter {
                it.title.lowercase().contains(query) ||
                        it.subtitle.lowercase().contains(query) ||
                        it.description.lowercase().contains(query)
            }
        }
    }

    // Build thread connections: Player-Created Connections + Unlocked Contradiction/Deduction Pillars
    val links = remember(allNodes, state.customConnections, state.unlockedContradictionIds, state.unlockedDeductionIds) {
        val linkList = mutableListOf<BoardThreadLink>()
        val nodeMap = allNodes.associateBy { it.rawId }

        // 1. Player-Created Custom Connections (Primary)
        state.customConnections.forEach { conn ->
            val srcNode = nodeMap[conn.sourceId]
            val tgtNode = nodeMap[conn.targetId]
            if (srcNode != null && tgtNode != null) {
                val threadColor = when (conn.relationship) {
                    ReasoningRelationship.CONTRADICTS -> AccentRed
                    ReasoningRelationship.DISPROVES -> AccentRed
                    ReasoningRelationship.ESTABLISHES -> Color(0xFFFFD54F)
                    ReasoningRelationship.SUPPORTS -> Color(0xFF81D4FA)
                    ReasoningRelationship.CONNECTS -> AccentCyan
                }
                linkList.add(
                    BoardThreadLink(
                        id = conn.id,
                        sourceId = srcNode.id,
                        targetId = tgtNode.id,
                        color = threadColor,
                        isContradiction = conn.relationship == ReasoningRelationship.CONTRADICTS,
                        isDeduction = conn.relationship == ReasoningRelationship.ESTABLISHES,
                        isCustom = true,
                        label = conn.description
                    )
                )
            }
        }

        // 2. Unlocked Contradiction crimson yarn links (Only for unlocked items)
        state.unlockedContradictionIds.forEach { cId ->
            caseDef.getContradiction(cId)?.let { contradiction ->
                contradiction.statementIds.filter { state.recordedStatementIds.contains(it) }.forEach { stId ->
                    val src = "STATEMENT_$stId"
                    val tgt = "CONTRADICTION_${contradiction.id}"
                    if (linkList.none { it.sourceId == src && it.targetId == tgt }) {
                        linkList.add(
                            BoardThreadLink(
                                id = "con_st_${contradiction.id}_$stId",
                                sourceId = src,
                                targetId = tgt,
                                color = AccentRed,
                                isContradiction = true,
                                isCustom = false,
                                label = "CONTRADICTED CLAIM"
                            )
                        )
                    }
                }
                contradiction.evidenceIds.filter { state.discoveredEvidenceIds.contains(it) }.forEach { evId ->
                    val src = "EVIDENCE_$evId"
                    val tgt = "CONTRADICTION_${contradiction.id}"
                    if (linkList.none { it.sourceId == src && it.targetId == tgt }) {
                        linkList.add(
                            BoardThreadLink(
                                id = "con_ev_${contradiction.id}_$evId",
                                sourceId = src,
                                targetId = tgt,
                                color = AccentRed,
                                isContradiction = true,
                                isCustom = false,
                                label = "DECISIVE PROOF"
                            )
                        )
                    }
                }
            }
        }

        // 3. Unlocked Deduction golden yarn links
        state.unlockedDeductionIds.forEach { dId ->
            caseDef.getDeduction(dId)?.let { deduction ->
                deduction.supportingEvidenceIds.filter { state.discoveredEvidenceIds.contains(it) }.forEach { evId ->
                    val src = "EVIDENCE_$evId"
                    val tgt = "DEDUCTION_${deduction.id}"
                    if (linkList.none { it.sourceId == src && it.targetId == tgt }) {
                        linkList.add(
                            BoardThreadLink(
                                id = "ded_ev_${deduction.id}_$evId",
                                sourceId = src,
                                targetId = tgt,
                                color = Color(0xFFFFD54F),
                                isDeduction = true,
                                isCustom = false,
                                label = "DEDUCTIVE PILLAR"
                            )
                        )
                    }
                }
            }
        }

        linkList
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF141210)) // Rich detective corkboard dark hue
    ) {
        // Main Interactive Canvas with Transform Gestures (Pan & Zoom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.45f, 2.2f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
        ) {
            // Corkboard Grid Pattern & Yarn Lines Background
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                // Draw Board Grid Points / Texture
                val gridSpacing = 80f * scale
                val startX = (offsetX % gridSpacing)
                val startY = (offsetY % gridSpacing)

                var x = startX
                while (x < size.width) {
                    var y = startY
                    while (y < size.height) {
                        drawCircle(
                            color = Color(0x18FFFFFF),
                            radius = 1.5f * scale,
                            center = Offset(x, y)
                        )
                        y += gridSpacing
                    }
                    x += gridSpacing
                }

                // Node Position Map for Link Rendering
                val nodeCoordMap = allNodes.associate { node ->
                    node.id to Offset(
                        x = (node.x * scale) + offsetX + (110f * scale),
                        y = (node.y * scale) + offsetY + (45f * scale)
                    )
                }

                // Draw Connecting Threads (Yarn lines)
                links.forEach { link ->
                    val startPos = nodeCoordMap[link.sourceId]
                    val endPos = nodeCoordMap[link.targetId]

                    if (startPos != null && endPos != null) {
                        val isHighlighted = (selectedNodeA?.id == link.sourceId || selectedNodeA?.id == link.targetId) ||
                                (selectedNodeB?.id == link.sourceId || selectedNodeB?.id == link.targetId)

                        val threadColor = if (isHighlighted) {
                            if (link.isContradiction) AccentRed else Color.White
                        } else link.color

                        val strokeWidth = if (link.isContradiction || link.isDeduction) 3.5f * scale else 2.2f * scale

                        // Draw curved string with natural tension
                        val midX = (startPos.x + endPos.x) / 2f
                        val midY = (startPos.y + endPos.y) / 2f + (22f * scale)

                        val path = Path().apply {
                            moveTo(startPos.x, startPos.y)
                            quadraticTo(midX, midY, endPos.x, endPos.y)
                        }

                        drawPath(
                            path = path,
                            color = threadColor,
                            style = Stroke(
                                width = strokeWidth,
                                pathEffect = if (!link.isContradiction && !link.isDeduction && !isHighlighted && !link.isCustom) {
                                    PathEffect.dashPathEffect(floatArrayOf(12f, 6f), 0f)
                                } else null
                            )
                        )

                        // Draw pushpins at anchor points
                        drawCircle(
                            color = if (link.isContradiction) AccentRed else AccentAmber,
                            radius = 4.5f * scale,
                            center = startPos
                        )
                        drawCircle(
                            color = if (link.isContradiction) AccentRed else AccentAmber,
                            radius = 4.5f * scale,
                            center = endPos
                        )
                    }
                }
            }

            // Render Nodes as Positioned Compose Cards on the Virtual Plane
            filteredNodes.forEach { node ->
                val screenX = (node.x * scale) + offsetX
                val screenY = (node.y * scale) + offsetY

                val isSelectedA = selectedNodeA?.id == node.id
                val isSelectedB = selectedNodeB?.id == node.id

                Box(
                    modifier = Modifier
                        .offset { IntOffset(screenX.roundToInt(), screenY.roundToInt()) }
                        .width((220.dp * scale).coerceAtLeast(160.dp))
                        .pointerInput(node.id) {
                            detectTapGestures(
                                onTap = {
                                    if (selectedNodeA == null) {
                                        selectedNodeA = node
                                        reasoningResultBanner = null
                                    } else if (selectedNodeA?.id == node.id) {
                                        // Tap selected node again to open inspector
                                        inspectingNode = node
                                    } else if (selectedNodeB == null) {
                                        selectedNodeB = node
                                    } else if (selectedNodeB?.id == node.id) {
                                        inspectingNode = node
                                    } else {
                                        selectedNodeB = node
                                    }
                                },
                                onLongPress = {
                                    inspectingNode = node
                                }
                            )
                        }
                        .testTag("board_node_${node.id}")
                ) {
                    DetectiveBoardNodeCard(
                        node = node,
                        scale = scale,
                        isSelectedA = isSelectedA,
                        isSelectedB = isSelectedB,
                        onInspectClick = { inspectingNode = node }
                    )
                }
            }
        }

        // Top Control Header: Search Bar, Category Filters & Stats HUD
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 10.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                color = SurfaceElevated.copy(alpha = 0.96f),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "INVESTIGATOR WORKSPACE",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                                color = TextPrimary
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = SurfaceCard,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "LINKS: ${state.customConnections.size}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentCyan
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            if (state.unlockedContradictionIds.isNotEmpty()) {
                                Surface(
                                    color = AccentRedDark.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "CONTRADICTIONS: ${state.unlockedContradictionIds.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentRed
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            if (state.unlockedDeductionIds.isNotEmpty()) {
                                Surface(
                                    color = SurfaceCard,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "DEDUCTIONS: ${state.unlockedDeductionIds.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentAmber
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Live Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Search discovered clues, suspects, and notes...",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = TextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentAmber,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("board_search_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter Chips Row (Only categories containing discovered data)
                    val availableFilters = remember(allNodes) {
                        BoardFilterType.entries.filter { filter ->
                            when (filter) {
                                BoardFilterType.ALL -> true
                                BoardFilterType.SUSPECTS -> allNodes.any { it.type == BoardNodeType.SUSPECT }
                                BoardFilterType.EVIDENCE -> allNodes.any { it.type == BoardNodeType.EVIDENCE }
                                BoardFilterType.STATEMENTS -> allNodes.any { it.type == BoardNodeType.STATEMENT }
                                BoardFilterType.TIMELINE -> allNodes.any { it.type == BoardNodeType.TIMELINE }
                                BoardFilterType.CONTRADICTIONS -> allNodes.any { it.type == BoardNodeType.CONTRADICTION }
                                BoardFilterType.DEDUCTIONS -> allNodes.any { it.type == BoardNodeType.DEDUCTION }
                            }
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(availableFilters) { filter ->
                            val isSelected = selectedFilter == filter
                            val count = when (filter) {
                                BoardFilterType.ALL -> allNodes.size
                                BoardFilterType.SUSPECTS -> allNodes.count { it.type == BoardNodeType.SUSPECT }
                                BoardFilterType.EVIDENCE -> allNodes.count { it.type == BoardNodeType.EVIDENCE }
                                BoardFilterType.STATEMENTS -> allNodes.count { it.type == BoardNodeType.STATEMENT }
                                BoardFilterType.TIMELINE -> allNodes.count { it.type == BoardNodeType.TIMELINE }
                                BoardFilterType.CONTRADICTIONS -> allNodes.count { it.type == BoardNodeType.CONTRADICTION }
                                BoardFilterType.DEDUCTIONS -> allNodes.count { it.type == BoardNodeType.DEDUCTION }
                            }

                            Surface(
                                color = if (isSelected) AccentAmber else SurfaceCard,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) AccentAmber else SurfaceBorder
                                ),
                                modifier = Modifier
                                    .clickable { selectedFilter = filter }
                                    .testTag("board_filter_${filter.name.lowercase()}")
                            ) {
                                Text(
                                    text = "${filter.label.uppercase()} ($count)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = if (isSelected) Color.Black else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Controls (Zoom Controls & Re-Center)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { scale = (scale * 1.25f).coerceAtMost(2.2f) },
                containerColor = SurfaceElevated,
                contentColor = AccentCyan,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }

            FloatingActionButton(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.45f) },
                containerColor = SurfaceElevated,
                contentColor = AccentCyan,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }

            FloatingActionButton(
                onClick = {
                    scale = 0.85f
                    offsetX = -180f
                    offsetY = -120f
                },
                containerColor = SurfaceElevated,
                contentColor = AccentAmber,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.CenterFocusStrong, contentDescription = "Center Board")
            }
        }

        // Bottom Reasoning & Node Connection Workbench
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Result Feedback Notification Banner
            reasoningResultBanner?.let { (isSuccess, message) ->
                Surface(
                    color = if (isSuccess) StatusConfirmed.copy(alpha = 0.95f) else SurfaceCard.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSuccess) StatusConfirmed else AccentAmber
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isSuccess) Color.Black else AccentAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = if (isSuccess) Color.Black else TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { reasoningResultBanner = null },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = if (isSuccess) Color.Black else TextMuted)
                        }
                    }
                }
            }

            // Connection Action Bar (When nodes are selected)
            if (selectedNodeA != null) {
                Surface(
                    color = SurfaceElevated.copy(alpha = 0.98f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.8f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedNodeB == null) "TAP SECOND PIN TO FORM THREAD" else "FORM INVESTIGATIVE LINK",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentAmber
                            )

                            TextButton(
                                onClick = {
                                    selectedNodeA = null
                                    selectedNodeB = null
                                    reasoningResultBanner = null
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("CLEAR SELECTION", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Connection Node Chips Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Node A Card
                            Surface(
                                color = SurfaceCard,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = null,
                                        tint = AccentCyan,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = selectedNodeA?.title ?: "",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.SyncAlt,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(18.dp)
                            )

                            // Node B Card
                            Surface(
                                color = if (selectedNodeB != null) SurfaceCard else SurfaceDark,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (selectedNodeB != null) AccentAmber else SurfaceBorder
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = null,
                                        tint = if (selectedNodeB != null) AccentAmber else TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = selectedNodeB?.title ?: "Select Target Pin...",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (selectedNodeB != null) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (selectedNodeB != null) TextPrimary else TextMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        // Relationship Selector & Connect Button (Enabled when Node B is selected)
                        if (selectedNodeB != null) {
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(ReasoningRelationship.entries) { rel ->
                                    val isSelected = selectedRelationship == rel
                                    Surface(
                                        color = if (isSelected) {
                                            if (rel == ReasoningRelationship.CONTRADICTS) AccentRed else AccentCyan
                                        } else SurfaceCard,
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.clickable { selectedRelationship = rel }
                                    ) {
                                        Text(
                                            text = rel.displayName.uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.Black else TextPrimary,
                                                fontSize = 10.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    val nodeA = selectedNodeA ?: return@Button
                                    val nodeB = selectedNodeB ?: return@Button

                                    val result = onAddConnection(nodeA.rawId, nodeB.rawId, selectedRelationship)
                                    reasoningResultBanner = Pair(result.isValid, result.feedbackMessage)
                                    if (result.isValid) {
                                        selectedNodeA = null
                                        selectedNodeB = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentAmber,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("action_test_board_connection")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Hub,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "LINK TO CASE BOARD",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Node Inspector Modal Bottom Sheet
        inspectingNode?.let { node ->
            ModalBottomSheet(
                onDismissRequest = { inspectingNode = null },
                containerColor = SurfaceDark,
                contentColor = TextPrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = node.type.pinColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = node.type.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = node.type.pinColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (node.isCritical) {
                            Surface(
                                color = AccentRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "CRITICAL EVIDENCE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AccentRed
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = node.title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    Text(
                        text = node.subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = AccentAmber
                    )

                    HorizontalDivider(color = SurfaceBorder)

                    Text(
                        text = node.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    // Display active player connections for this node
                    val connectedLinks = state.customConnections.filter { it.sourceId == node.rawId || it.targetId == node.rawId }
                    if (connectedLinks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ACTIVE BOARD LINKS (${connectedLinks.size})",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AccentCyan
                        )
                        connectedLinks.forEach { conn ->
                            val otherId = if (conn.sourceId == node.rawId) conn.targetId else conn.sourceId
                            val otherNode = allNodes.find { it.rawId == otherId }
                            val otherTitle = otherNode?.title ?: otherId

                            Surface(
                                color = SurfaceCard,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Link, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${conn.relationship.displayName.uppercase()}: $otherTitle",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                            color = TextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    IconButton(
                                        onClick = { onRemoveConnection(conn.id) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove Link", tint = TextMuted, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Contextual Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        when (node.type) {
                            BoardNodeType.EVIDENCE -> {
                                Button(
                                    onClick = {
                                        val evId = node.rawId
                                        inspectingNode = null
                                        onSelectEvidenceForDetail(evId)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("EXAMINE CLUE", fontWeight = FontWeight.Bold)
                                }
                            }
                            BoardNodeType.SUSPECT -> {
                                Button(
                                    onClick = {
                                        val suspectId = node.rawId
                                        inspectingNode = null
                                        onSelectSuspectForInterview(suspectId)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber, contentColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.QuestionAnswer, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("INTERVIEW", fontWeight = FontWeight.Bold)
                                }
                            }
                            BoardNodeType.STATEMENT -> {
                                Button(
                                    onClick = {
                                        inspectingNode = null
                                        onOpenCaseFileTab(CaseFileTab.STATEMENTS)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA), contentColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("VIEW STATEMENTS", fontWeight = FontWeight.Bold)
                                }
                            }
                            BoardNodeType.TIMELINE -> {
                                Button(
                                    onClick = {
                                        inspectingNode = null
                                        onOpenCaseFileTab(CaseFileTab.TIMELINE)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCE93D8), contentColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("VIEW TIMELINE", fontWeight = FontWeight.Bold)
                                }
                            }
                            BoardNodeType.CONTRADICTION, BoardNodeType.DEDUCTION -> {
                                Button(
                                    onClick = {
                                        inspectingNode = null
                                        onOpenCaseFileTab(CaseFileTab.DEDUCTIONS)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F), contentColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("REASONING HUB", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                selectedNodeA = node
                                selectedNodeB = null
                                inspectingNode = null
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PushPin, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PIN FOR LINK", color = AccentAmber, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun DetectiveBoardNodeCard(
    node: BoardNodeData,
    scale: Float,
    isSelectedA: Boolean,
    isSelectedB: Boolean,
    onInspectClick: () -> Unit
) {
    val borderColor = when {
        isSelectedA -> AccentCyan
        isSelectedB -> AccentAmber
        node.isContradicted -> AccentRed
        node.isCleared -> StatusConfirmed.copy(alpha = 0.6f)
        node.isCritical -> AccentRed.copy(alpha = 0.6f)
        else -> SurfaceBorder
    }

    val cardBg = when {
        isSelectedA -> SurfaceCard.copy(alpha = 0.95f)
        isSelectedB -> SurfaceCard.copy(alpha = 0.95f)
        node.type == BoardNodeType.CONTRADICTION -> Color(0xFF2A1212)
        node.type == BoardNodeType.DEDUCTION -> Color(0xFF2B2412)
        else -> SurfaceElevated.copy(alpha = 0.92f)
    }

    Surface(
        color = cardBg,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelectedA || isSelectedB) 2.dp else 1.dp,
            borderColor
        ),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Pushpin & Type Badge Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelectedA -> AccentCyan
                                    isSelectedB -> AccentAmber
                                    node.isContradicted -> AccentRed
                                    else -> node.type.pinColor
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = node.type.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = node.type.pinColor
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (node.connectionCount > 0) {
                        Surface(
                            color = AccentCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "${node.connectionCount} LINKS",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp, fontWeight = FontWeight.Bold),
                                color = AccentCyan,
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                            )
                        }
                    }

                    if (node.isContradicted) {
                        Surface(
                            color = AccentRed,
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "CONTRADICTED",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp, fontWeight = FontWeight.Black),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                            )
                        }
                    } else if (node.isCleared) {
                        Surface(
                            color = StatusConfirmed.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "CLEARED",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp, fontWeight = FontWeight.Black),
                                color = StatusConfirmed,
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Node Title
            Text(
                text = node.title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Node Subtitle / Meta
            Text(
                text = node.subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = AccentAmber,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Node Description Preview
            Text(
                text = node.description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
