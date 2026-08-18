package com.example.thelastcall.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.CrimeSceneHotspot
import com.example.ui.theme.*

/**
 * Interactive 2D Vector Crime Scene Environment for Apt 7B (Elias Voss Penthouse).
 * Renders an architectural noir study with interactive forensic hotspot nodes.
 */
@Composable
fun InteractiveCrimeSceneView(
    hotspots: List<CrimeSceneHotspot>,
    discoveredEvidenceIds: Set<String>,
    selectedHotspot: CrimeSceneHotspot?,
    onSelectHotspot: (CrimeSceneHotspot) -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulse animation for undiscovered clues
    val infiniteTransition = rememberInfiniteTransition(label = "hotspot_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val scanlineOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline_offset"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
            .background(Color(0xFF0A0C10))
    ) {
        val width = maxWidth
        val height = maxHeight

        // Base Architectural Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Atmospheric Ambient Noir Gradient
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF141822), Color(0xFF080A0E)),
                    center = Offset(w * 0.45f, h * 0.42f),
                    radius = w * 0.95f
                )
            )

            // 2. City Skyline Penthouse Window (Top Right)
            val windowLeft = w * 0.70f
            val windowTop = h * 0.06f
            val windowWidth = w * 0.24f
            val windowHeight = h * 0.32f

            // Outside night sky & city buildings
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F1A2A), Color(0xFF060D18)),
                    startY = windowTop,
                    endY = windowTop + windowHeight
                ),
                topLeft = Offset(windowLeft, windowTop),
                size = Size(windowWidth, windowHeight)
            )
            // Distant skyline silhouettes
            drawRect(Color(0xFF0B121E), topLeft = Offset(windowLeft + windowWidth * 0.1f, windowTop + windowHeight * 0.4f), size = Size(windowWidth * 0.25f, windowHeight * 0.6f))
            drawRect(Color(0xFF090E16), topLeft = Offset(windowLeft + windowWidth * 0.45f, windowTop + windowHeight * 0.25f), size = Size(windowWidth * 0.35f, windowHeight * 0.75f))
            // Distant yellow building windows
            drawCircle(Color(0xFFF59E0B).copy(alpha = 0.4f), radius = 1.5f, center = Offset(windowLeft + windowWidth * 0.2f, windowTop + windowHeight * 0.5f))
            drawCircle(Color(0xFF06B6D4).copy(alpha = 0.4f), radius = 1.5f, center = Offset(windowLeft + windowWidth * 0.6f, windowTop + windowHeight * 0.4f))

            // Window Glass Frame
            drawRect(
                color = Color(0xFF263244),
                topLeft = Offset(windowLeft, windowTop),
                size = Size(windowWidth, windowHeight),
                style = Stroke(width = 3f)
            )
            // Window Crossbars
            drawLine(Color(0xFF263244), Offset(windowLeft + windowWidth * 0.5f, windowTop), Offset(windowLeft + windowWidth * 0.5f, windowTop + windowHeight), strokeWidth = 2f)
            drawLine(Color(0xFF263244), Offset(windowLeft, windowTop + windowHeight * 0.5f), Offset(windowLeft + windowWidth, windowTop + windowHeight * 0.5f), strokeWidth = 2f)

            // Rain streak diagonals on window
            drawLine(Color(0x3360A5FA), Offset(windowLeft + windowWidth * 0.2f, windowTop + 10f), Offset(windowLeft + windowWidth * 0.35f, windowTop + 40f), strokeWidth = 1f)
            drawLine(Color(0x3360A5FA), Offset(windowLeft + windowWidth * 0.6f, windowTop + 20f), Offset(windowLeft + windowWidth * 0.75f, windowTop + 55f), strokeWidth = 1f)

            // 3. Dark Hardwood Floor Perspective (Lower 45%)
            val floorTopY = h * 0.52f
            val floorPath = Path().apply {
                moveTo(0f, floorTopY)
                lineTo(w, floorTopY)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                floorPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF16151B), Color(0xFF0C0B0E)),
                    startY = floorTopY,
                    endY = h
                )
            )

            // Hardwood floor plank lines
            for (i in 0..6) {
                val startX = w * (i * 0.16f)
                val endX = w * (i * 0.20f - 0.08f)
                drawLine(Color(0xFF221F28), Offset(startX, floorTopY), Offset(endX, h), strokeWidth = 1.2f)
            }

            // 4. Large Persian Rug (Living Room / Struggle Area)
            val rugLeft = w * 0.22f
            val rugTop = h * 0.56f
            val rugWidth = w * 0.68f
            val rugHeight = h * 0.38f

            drawRoundRect(
                color = Color(0xFF241518),
                topLeft = Offset(rugLeft, rugTop),
                size = Size(rugWidth, rugHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )
            // Rug decorative border
            drawRoundRect(
                color = Color(0xFF4A1F26),
                topLeft = Offset(rugLeft + 5f, rugTop + 5f),
                size = Size(rugWidth - 10f, rugHeight - 10f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f),
                style = Stroke(width = 2.5f)
            )
            // Inner ornate diamond motif
            drawRoundRect(
                color = Color(0xFF38171D),
                topLeft = Offset(rugLeft + rugWidth * 0.25f, rugTop + rugHeight * 0.2f),
                size = Size(rugWidth * 0.5f, rugHeight * 0.6f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                style = Stroke(width = 1.5f)
            )

            // 5. Entrance Foyer Door (Left Side, x ~ 0.15, y ~ 0.55)
            val doorLeft = w * 0.05f
            val doorTop = h * 0.35f
            val doorWidth = w * 0.14f
            val doorHeight = h * 0.42f

            drawRect(
                color = Color(0xFF1A1D24),
                topLeft = Offset(doorLeft, doorTop),
                size = Size(doorWidth, doorHeight)
            )
            drawRect(
                color = Color(0xFF2B3342),
                topLeft = Offset(doorLeft, doorTop),
                size = Size(doorWidth, doorHeight),
                style = Stroke(width = 2.5f)
            )
            // Door Handle
            drawCircle(Color(0xFFC5A868), radius = 3.5f, center = Offset(doorLeft + doorWidth * 0.8f, doorTop + doorHeight * 0.5f))
            // Smart Lock Cyan Status LED
            drawCircle(AccentCyan, radius = 2.5f, center = Offset(doorLeft + doorWidth * 0.8f, doorTop + doorHeight * 0.42f))

            // 6. Left Wall File Cabinet & Organizer (x ~ 0.30, y ~ 0.35)
            val cabinetLeft = w * 0.24f
            val cabinetTop = h * 0.24f
            val cabinetWidth = w * 0.14f
            val cabinetHeight = h * 0.22f

            drawRect(
                color = Color(0xFF1E232D),
                topLeft = Offset(cabinetLeft, cabinetTop),
                size = Size(cabinetWidth, cabinetHeight)
            )
            drawRect(
                color = Color(0xFF333B4D),
                topLeft = Offset(cabinetLeft, cabinetTop),
                size = Size(cabinetWidth, cabinetHeight),
                style = Stroke(width = 2f)
            )
            // Drawer seams
            drawLine(Color(0xFF333B4D), Offset(cabinetLeft, cabinetTop + cabinetHeight * 0.33f), Offset(cabinetLeft + cabinetWidth, cabinetTop + cabinetHeight * 0.33f), strokeWidth = 1.5f)
            drawLine(Color(0xFF333B4D), Offset(cabinetLeft, cabinetTop + cabinetHeight * 0.66f), Offset(cabinetLeft + cabinetWidth, cabinetTop + cabinetHeight * 0.66f), strokeWidth = 1.5f)
            // Dossier folder tabs sticking out
            drawRect(AccentAmber.copy(alpha = 0.8f), topLeft = Offset(cabinetLeft + 6f, cabinetTop - 4f), size = Size(12f, 4f))
            drawRect(AccentCyan.copy(alpha = 0.8f), topLeft = Offset(cabinetLeft + 22f, cabinetTop - 4f), size = Size(12f, 4f))

            // 7. Executive Mahogany Study Desk (Center Left, x ~ 0.45 - 0.65, y ~ 0.35 - 0.50)
            val deskLeft = w * 0.38f
            val deskTop = h * 0.32f
            val deskWidth = w * 0.32f
            val deskHeight = h * 0.20f

            // Desk Lamp Light Cone (Warm radial illumination onto desk)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x3DF59E0B), Color(0x00F59E0B)),
                    center = Offset(deskLeft + deskWidth * 0.75f, deskTop + deskHeight * 0.3f),
                    radius = w * 0.28f
                ),
                topLeft = Offset(deskLeft - 20f, deskTop - 20f),
                size = Size(deskWidth + 40f, deskHeight + 40f)
            )

            // Mahogany Desk Top Surface
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF3B241C), Color(0xFF4C3026))
                ),
                topLeft = Offset(deskLeft, deskTop),
                size = Size(deskWidth, deskHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = Color(0xFF6B4537),
                topLeft = Offset(deskLeft, deskTop),
                size = Size(deskWidth, deskHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f),
                style = Stroke(width = 2f)
            )

            // Desk Leather Blotter
            drawRoundRect(
                color = Color(0xFF1E1B1B),
                topLeft = Offset(deskLeft + deskWidth * 0.15f, deskTop + deskHeight * 0.2f),
                size = Size(deskWidth * 0.5f, deskHeight * 0.6f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
            // Scattered Research Documents on Desk
            drawRect(Color(0xFFD4D4D8), topLeft = Offset(deskLeft + deskWidth * 0.2f, deskTop + deskHeight * 0.25f), size = Size(deskWidth * 0.22f, deskHeight * 0.4f))
            drawRect(Color(0xFFE4E4E7), topLeft = Offset(deskLeft + deskWidth * 0.28f, deskTop + deskHeight * 0.3f), size = Size(deskWidth * 0.22f, deskHeight * 0.4f))
            // Smartphone lying on right side of desk (x ~ 0.62)
            drawRoundRect(
                color = Color(0xFF111827),
                topLeft = Offset(deskLeft + deskWidth * 0.72f, deskTop + deskHeight * 0.25f),
                size = Size(deskWidth * 0.18f, deskHeight * 0.45f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f, 2f)
            )
            // Glowing phone screen
            drawRoundRect(
                color = AccentCyan.copy(alpha = 0.7f),
                topLeft = Offset(deskLeft + deskWidth * 0.74f, deskTop + deskHeight * 0.28f),
                size = Size(deskWidth * 0.14f, deskHeight * 0.38f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(1f, 1f)
            )

            // 8. Shattered Glass Area (Right Rug, x ~ 0.70, y ~ 0.65)
            val glassCenterX = w * 0.70f
            val glassCenterY = h * 0.65f
            // Liquid spill puddle
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x66B45309), Color(0x11B45309)),
                    center = Offset(glassCenterX, glassCenterY),
                    radius = 24f
                ),
                topLeft = Offset(glassCenterX - 24f, glassCenterY - 14f),
                size = Size(48f, 28f)
            )
            // Glass shards glints
            drawCircle(Color(0xFFE0F2FE), radius = 2.5f, center = Offset(glassCenterX - 8f, glassCenterY - 4f))
            drawCircle(Color(0xFFBAE6FD), radius = 2f, center = Offset(glassCenterX + 10f, glassCenterY + 3f))
            drawCircle(Color(0xFFE0F2FE), radius = 1.5f, center = Offset(glassCenterX + 2f, glassCenterY - 6f))

            // 9. Cast-Iron Paperweight Weapon Area (Center Floor, x ~ 0.52, y ~ 0.60)
            val weaponX = w * 0.52f
            val weaponY = h * 0.60f
            // Paperweight metallic body
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF374151), Color(0xFF111827)),
                    center = Offset(weaponX - 2f, weaponY - 2f),
                    radius = 12f
                ),
                radius = 10f,
                center = Offset(weaponX, weaponY)
            )
            drawCircle(Color(0xFF4B5563), radius = 10f, center = Offset(weaponX, weaponY), style = Stroke(width = 1.5f))

            // 10. Forensic Grid & Scanning Scanline Effect
            val scanlineY = h * scanlineOffset
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, AccentCyan.copy(alpha = 0.25f), Color.Transparent)
                ),
                start = Offset(0f, scanlineY),
                end = Offset(w, scanlineY),
                strokeWidth = 1.5f
            )

            // Crime Scene Header Stamp on Canvas
            drawRect(Color(0x40000000), topLeft = Offset(12f, 12f), size = Size(160f, 24f))
        }

        // Scene HUD Watermark Top-Left
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .background(Color(0x800D1117), RoundedCornerShape(4.dp))
                .border(1.dp, SurfaceBorder.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Grid4x4,
                contentDescription = null,
                tint = AccentCyan,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "FORENSIC SECTOR GRID",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = AccentCyan
            )
        }

        // Hotspot Overlay Interactive Nodes
        hotspots.forEachIndexed { index, hotspot ->
            val hasPrimary = discoveredEvidenceIds.contains(hotspot.primaryEvidenceId)
            val hasSecondary = hotspot.secondaryEvidenceId == null || discoveredEvidenceIds.contains(hotspot.secondaryEvidenceId)
            val isFullyExamined = hasPrimary && hasSecondary
            val isSelected = selectedHotspot?.id == hotspot.id

            // Position according to hotspot model percentages with safety bounds
            val xPos = width * hotspot.xPercent.coerceIn(0.08f, 0.92f)
            val yPos = height * hotspot.yPercent.coerceIn(0.12f, 0.88f)

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = xPos - 24.dp,
                        y = yPos - 24.dp
                    )
            ) {
                HotspotMarkerNode(
                    index = index + 1,
                    hotspot = hotspot,
                    isFullyExamined = isFullyExamined,
                    hasDiscoveredPrimary = hasPrimary,
                    isSelected = isSelected,
                    pulseAlpha = pulseAlpha,
                    pulseScale = pulseScale,
                    onClick = { onSelectHotspot(hotspot) }
                )
            }
        }
    }
}

@Composable
private fun HotspotMarkerNode(
    index: Int,
    hotspot: CrimeSceneHotspot,
    isFullyExamined: Boolean,
    hasDiscoveredPrimary: Boolean,
    isSelected: Boolean,
    pulseAlpha: Float,
    pulseScale: Float,
    onClick: () -> Unit
) {
    val nodeColor = when {
        isSelected -> AccentAmber
        isFullyExamined -> StatusConfirmed
        hasDiscoveredPrimary -> AccentCyan
        else -> AccentRed
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onClick)
                .testTag("hotspot_node_${hotspot.id}"),
            contentAlignment = Alignment.Center
        ) {
            // Radar pulse ring for unexamined hotspots
            if (!isFullyExamined) {
                Box(
                    modifier = Modifier
                        .size((32 * pulseScale).dp)
                        .background(nodeColor.copy(alpha = pulseAlpha * 0.35f), CircleShape)
                )
            }

            // Selection crosshair ring
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .border(1.5.dp, AccentAmber, CircleShape)
                )
            }

            // Inner Marker Badge
            Surface(
                color = if (isSelected) AccentAmber else SurfaceElevated,
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    if (isSelected) Color.White else nodeColor
                ),
                modifier = Modifier.size(32.dp),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isFullyExamined) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = hotspot.name,
                            tint = StatusConfirmed,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "$index",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            ),
                            color = if (isSelected) BackgroundDark else nodeColor
                        )
                    }
                }
            }
        }

        // Floating label on selection
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Surface(
                color = BackgroundDark.copy(alpha = 0.92f),
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.8f)),
                modifier = Modifier
                    .padding(top = 2.dp)
                    .wrapContentWidth()
            ) {
                Text(
                    text = hotspot.name.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = AccentAmber,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
