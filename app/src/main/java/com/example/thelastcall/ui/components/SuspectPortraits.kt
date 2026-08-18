package com.example.thelastcall.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class SuspectEmotion {
    NEUTRAL,
    THINKING,
    NERVOUS,
    DEFENSIVE,
    SHOCKED
}

/**
 * High-craft stylized character portrait generator for UNRESOLVED.
 * Renders distinct noir character designs with dynamic emotional expressions.
 */
@Composable
fun SuspectPortrait(
    suspectId: String,
    emotion: SuspectEmotion = SuspectEmotion.NEUTRAL,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    showBorder: Boolean = true
) {
    val borderColor by animateColorAsState(
        targetValue = when (emotion) {
            SuspectEmotion.SHOCKED -> AccentRed
            SuspectEmotion.DEFENSIVE -> AccentAmber
            SuspectEmotion.NERVOUS -> AccentCyan
            else -> SurfaceBorder
        },
        animationSpec = tween(300),
        label = "portrait_border"
    )

    Surface(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (showBorder) Modifier.border(2.dp, borderColor, RoundedCornerShape(16.dp))
                else Modifier
            ),
        color = SurfaceElevated
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Background Studio Lighting
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        when (suspectId) {
                            "S001" -> Color(0xFF2A2B3D) // Maya - cool indigo
                            "S002" -> Color(0xFF332B25) // Victor - warm mahogany
                            "S003" -> Color(0xFF23322B) // Nora - sage green
                            "S004" -> Color(0xFF242A38) // Daniel - deep slate
                            else -> Color(0xFF252830)
                        },
                        BackgroundDark
                    ),
                    center = Offset(w * 0.5f, h * 0.35f),
                    radius = w * 0.8f
                )
            )

            // Draw character based on ID
            when (suspectId) {
                "S001" -> drawMayaPortrait(w, h, emotion)
                "S002" -> drawVictorPortrait(w, h, emotion)
                "S003" -> drawNoraPortrait(w, h, emotion)
                "S004" -> drawDanielPortrait(w, h, emotion)
                else -> drawGenericPortrait(w, h, emotion)
            }
        }
    }
}

private fun DrawScope.drawMayaPortrait(w: Float, h: Float, emotion: SuspectEmotion) {
    // Maya Voss: Sharp dark bob haircut, amber glasses, modern charcoal blazer
    val skinColor = Color(0xFFDEB093)
    val hairColor = Color(0xFF1E1A22)
    val blazerColor = Color(0xFF282A36)
    val shirtColor = Color(0xFFE2E4EB)
    val glassesColor = Color(0xFFD4A359)

    // Shoulders / Blazer
    val shoulderPath = Path().apply {
        moveTo(w * 0.1f, h)
        lineTo(w * 0.3f, h * 0.72f)
        lineTo(w * 0.7f, h * 0.72f)
        lineTo(w * 0.9f, h)
        close()
    }
    drawPath(shoulderPath, blazerColor)

    // Inner Shirt V-neck
    val shirtPath = Path().apply {
        moveTo(w * 0.42f, h * 0.72f)
        lineTo(w * 0.5f, h * 0.88f)
        lineTo(w * 0.58f, h * 0.72f)
        close()
    }
    drawPath(shirtPath, shirtColor)

    // Neck
    drawRect(
        color = skinColor.copy(red = skinColor.red * 0.9f),
        topLeft = Offset(w * 0.44f, h * 0.60f),
        size = Size(w * 0.12f, h * 0.16f)
    )

    // Face Oval
    drawOval(
        color = skinColor,
        topLeft = Offset(w * 0.32f, h * 0.28f),
        size = Size(w * 0.36f, h * 0.40f)
    )

    // Hair Back/Sides (Bob)
    val hairPath = Path().apply {
        moveTo(w * 0.26f, h * 0.55f)
        cubicTo(w * 0.22f, h * 0.20f, w * 0.78f, h * 0.20f, w * 0.74f, h * 0.55f)
        lineTo(w * 0.68f, h * 0.52f)
        cubicTo(w * 0.65f, h * 0.26f, w * 0.35f, h * 0.26f, w * 0.32f, h * 0.52f)
        close()
    }
    drawPath(hairPath, hairColor)

    // Bangs
    val bangsPath = Path().apply {
        moveTo(w * 0.32f, h * 0.34f)
        cubicTo(w * 0.42f, h * 0.30f, w * 0.58f, h * 0.32f, w * 0.68f, h * 0.38f)
        lineTo(w * 0.64f, h * 0.30f)
        cubicTo(w * 0.5f, h * 0.24f, w * 0.4f, h * 0.26f, w * 0.32f, h * 0.34f)
        close()
    }
    drawPath(bangsPath, hairColor)

    // Glasses Frames
    val glassesY = h * 0.42f
    drawRoundRect(
        color = glassesColor,
        topLeft = Offset(w * 0.36f, glassesY),
        size = Size(w * 0.11f, h * 0.08f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
        style = Stroke(width = 2.5f)
    )
    drawRoundRect(
        color = glassesColor,
        topLeft = Offset(w * 0.53f, glassesY),
        size = Size(w * 0.11f, h * 0.08f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
        style = Stroke(width = 2.5f)
    )
    drawLine(glassesColor, Offset(w * 0.47f, glassesY + h * 0.04f), Offset(w * 0.53f, glassesY + h * 0.04f), strokeWidth = 2.5f)

    // Eyes behind glasses
    drawCircle(Color(0xFF221A16), radius = 2.5f, center = Offset(w * 0.415f, glassesY + h * 0.04f))
    drawCircle(Color(0xFF221A16), radius = 2.5f, center = Offset(w * 0.585f, glassesY + h * 0.04f))

    // Nose
    drawLine(skinColor.copy(alpha = 0.6f), Offset(w * 0.5f, h * 0.48f), Offset(w * 0.49f, h * 0.54f), strokeWidth = 2f)

    // Mouth based on emotion
    when (emotion) {
        SuspectEmotion.NERVOUS -> {
            drawLine(Color(0xFF8B4D4D), Offset(w * 0.45f, h * 0.61f), Offset(w * 0.55f, h * 0.60f), strokeWidth = 2.5f)
        }
        SuspectEmotion.SHOCKED -> {
            drawOval(Color(0xFF5A2A2A), topLeft = Offset(w * 0.47f, h * 0.59f), size = Size(w * 0.06f, h * 0.04f))
        }
        SuspectEmotion.THINKING -> {
            drawLine(Color(0xFF8B4D4D), Offset(w * 0.44f, h * 0.61f), Offset(w * 0.54f, h * 0.59f), strokeWidth = 2.5f)
        }
        else -> {
            drawLine(Color(0xFF8B4D4D), Offset(w * 0.45f, h * 0.60f), Offset(w * 0.55f, h * 0.60f), strokeWidth = 2.5f)
        }
    }
}

private fun DrawScope.drawVictorPortrait(w: Float, h: Float, emotion: SuspectEmotion) {
    // Victor Hale: Senior partner, slicked silver hair, tailored burgundy three-piece suit
    val skinColor = Color(0xFFE5BFA3)
    val hairColor = Color(0xFFB8BCC6)
    val suitColor = Color(0xFF3F2025)
    val tieColor = Color(0xFFD4A359)

    // Shoulders / Suit
    val shoulderPath = Path().apply {
        moveTo(w * 0.08f, h)
        lineTo(w * 0.28f, h * 0.70f)
        lineTo(w * 0.72f, h * 0.70f)
        lineTo(w * 0.92f, h)
        close()
    }
    drawPath(shoulderPath, suitColor)

    // Tie
    val tiePath = Path().apply {
        moveTo(w * 0.48f, h * 0.72f)
        lineTo(w * 0.52f, h * 0.72f)
        lineTo(w * 0.54f, h * 0.92f)
        lineTo(w * 0.50f, h * 0.96f)
        lineTo(w * 0.46f, h * 0.92f)
        close()
    }
    drawPath(tiePath, tieColor)

    // Neck
    drawRect(
        color = skinColor.copy(red = skinColor.red * 0.9f),
        topLeft = Offset(w * 0.43f, h * 0.58f),
        size = Size(w * 0.14f, h * 0.16f)
    )

    // Face / Jawline
    val facePath = Path().apply {
        moveTo(w * 0.33f, h * 0.30f)
        lineTo(w * 0.67f, h * 0.30f)
        lineTo(w * 0.65f, h * 0.56f)
        lineTo(w * 0.50f, h * 0.68f)
        lineTo(w * 0.35f, h * 0.56f)
        close()
    }
    drawPath(facePath, skinColor)

    // Slicked Back Silver Hair
    val hairPath = Path().apply {
        moveTo(w * 0.30f, h * 0.34f)
        cubicTo(w * 0.30f, h * 0.16f, w * 0.70f, h * 0.16f, w * 0.70f, h * 0.34f)
        lineTo(w * 0.66f, h * 0.28f)
        cubicTo(w * 0.60f, h * 0.22f, w * 0.40f, h * 0.22f, w * 0.34f, h * 0.28f)
        close()
    }
    drawPath(hairPath, hairColor)

    // Eyebrows
    val browY = if (emotion == SuspectEmotion.DEFENSIVE) h * 0.35f else h * 0.37f
    drawLine(hairColor.copy(alpha = 0.8f), Offset(w * 0.37f, browY), Offset(w * 0.45f, browY + 4f), strokeWidth = 3f)
    drawLine(hairColor.copy(alpha = 0.8f), Offset(w * 0.55f, browY + 4f), Offset(w * 0.63f, browY), strokeWidth = 3f)

    // Eyes
    drawCircle(Color(0xFF2C323B), radius = 2.8f, center = Offset(w * 0.41f, h * 0.42f))
    drawCircle(Color(0xFF2C323B), radius = 2.8f, center = Offset(w * 0.59f, h * 0.42f))

    // Mustache / Trimmed Facial Feature
    drawLine(hairColor.copy(alpha = 0.6f), Offset(w * 0.44f, h * 0.56f), Offset(w * 0.56f, h * 0.56f), strokeWidth = 2.5f)

    // Mouth
    when (emotion) {
        SuspectEmotion.DEFENSIVE, SuspectEmotion.SHOCKED -> {
            drawLine(Color(0xFF6B3A3A), Offset(w * 0.44f, h * 0.61f), Offset(w * 0.56f, h * 0.61f), strokeWidth = 3f)
        }
        else -> {
            drawLine(Color(0xFF6B3A3A), Offset(w * 0.45f, h * 0.60f), Offset(w * 0.55f, h * 0.60f), strokeWidth = 2.5f)
        }
    }
}

private fun DrawScope.drawNoraPortrait(w: Float, h: Float, emotion: SuspectEmotion) {
    // Nora Bennett: Concierge, elegant bun, dark emerald service vest & gold lapel badge
    val skinColor = Color(0xFFC99570)
    val hairColor = Color(0xFF1B1917)
    val uniformColor = Color(0xFF1E352B)
    val goldBadge = Color(0xFFE5B84B)

    // Shoulders / Vest
    val shoulderPath = Path().apply {
        moveTo(w * 0.10f, h)
        lineTo(w * 0.30f, h * 0.72f)
        lineTo(w * 0.70f, h * 0.72f)
        lineTo(w * 0.90f, h)
        close()
    }
    drawPath(shoulderPath, uniformColor)

    // Gold Concierge Lapel Pin
    drawCircle(goldBadge, radius = 5f, center = Offset(w * 0.35f, h * 0.82f))

    // Neck
    drawRect(
        color = skinColor.copy(red = skinColor.red * 0.9f),
        topLeft = Offset(w * 0.44f, h * 0.60f),
        size = Size(w * 0.12f, h * 0.15f)
    )

    // High Bun Hair
    drawCircle(hairColor, radius = w * 0.15f, center = Offset(w * 0.5f, h * 0.18f))

    // Face
    drawOval(
        color = skinColor,
        topLeft = Offset(w * 0.33f, h * 0.28f),
        size = Size(w * 0.34f, h * 0.40f)
    )

    // Front Hairline
    val hairlinePath = Path().apply {
        moveTo(w * 0.32f, h * 0.36f)
        cubicTo(w * 0.40f, h * 0.24f, w * 0.60f, h * 0.24f, w * 0.68f, h * 0.36f)
        lineTo(w * 0.66f, h * 0.28f)
        cubicTo(w * 0.58f, h * 0.20f, w * 0.42f, h * 0.20f, w * 0.34f, h * 0.28f)
        close()
    }
    drawPath(hairlinePath, hairColor)

    // Earrings
    drawCircle(goldBadge, radius = 2.5f, center = Offset(w * 0.32f, h * 0.48f))
    drawCircle(goldBadge, radius = 2.5f, center = Offset(w * 0.68f, h * 0.48f))

    // Eyes
    drawCircle(Color(0xFF281E19), radius = 2.6f, center = Offset(w * 0.42f, h * 0.43f))
    drawCircle(Color(0xFF281E19), radius = 2.6f, center = Offset(w * 0.58f, h * 0.43f))

    // Mouth
    when (emotion) {
        SuspectEmotion.SHOCKED -> {
            drawOval(Color(0xFF7A3535), topLeft = Offset(w * 0.47f, h * 0.57f), size = Size(w * 0.06f, h * 0.05f))
        }
        else -> {
            drawLine(Color(0xFF9E4B4B), Offset(w * 0.45f, h * 0.59f), Offset(w * 0.55f, h * 0.59f), strokeWidth = 2.5f)
        }
    }
}

private fun DrawScope.drawDanielPortrait(w: Float, h: Float, emotion: SuspectEmotion) {
    // Daniel Mercer: Younger brother, CFO, sharp haircut, navy crewneck over white collar
    val skinColor = Color(0xFFE8C8AE)
    val hairColor = Color(0xFF32271F)
    val sweaterColor = Color(0xFF1E2838)
    val collarColor = Color(0xFFF0F2F6)

    // Shoulders
    val shoulderPath = Path().apply {
        moveTo(w * 0.08f, h)
        lineTo(w * 0.28f, h * 0.70f)
        lineTo(w * 0.72f, h * 0.70f)
        lineTo(w * 0.92f, h)
        close()
    }
    drawPath(shoulderPath, sweaterColor)

    // White Shirt Collar showing
    val collarPath = Path().apply {
        moveTo(w * 0.42f, h * 0.70f)
        lineTo(w * 0.50f, h * 0.78f)
        lineTo(w * 0.58f, h * 0.70f)
        close()
    }
    drawPath(collarPath, collarColor)

    // Neck
    drawRect(
        color = skinColor.copy(red = skinColor.red * 0.92f),
        topLeft = Offset(w * 0.43f, h * 0.58f),
        size = Size(w * 0.14f, h * 0.15f)
    )

    // Face / Jawline
    val facePath = Path().apply {
        moveTo(w * 0.33f, h * 0.28f)
        lineTo(w * 0.67f, h * 0.28f)
        lineTo(w * 0.64f, h * 0.54f)
        lineTo(w * 0.50f, h * 0.66f)
        lineTo(w * 0.36f, h * 0.54f)
        close()
    }
    drawPath(facePath, skinColor)

    // Hair (Neat side part)
    val hairPath = Path().apply {
        moveTo(w * 0.31f, h * 0.32f)
        cubicTo(w * 0.32f, h * 0.14f, w * 0.68f, h * 0.14f, w * 0.69f, h * 0.32f)
        lineTo(w * 0.64f, h * 0.26f)
        cubicTo(w * 0.56f, h * 0.20f, w * 0.40f, h * 0.20f, w * 0.33f, h * 0.26f)
        close()
    }
    drawPath(hairPath, hairColor)

    // Eyebrows
    val browY = when (emotion) {
        SuspectEmotion.DEFENSIVE -> h * 0.36f
        SuspectEmotion.NERVOUS, SuspectEmotion.SHOCKED -> h * 0.34f
        else -> h * 0.36f
    }
    drawLine(hairColor, Offset(w * 0.37f, browY), Offset(w * 0.46f, browY + 2f), strokeWidth = 2.5f)
    drawLine(hairColor, Offset(w * 0.54f, browY + 2f), Offset(w * 0.63f, browY), strokeWidth = 2.5f)

    // Eyes
    val eyeY = h * 0.41f
    drawCircle(Color(0xFF282522), radius = 2.8f, center = Offset(w * 0.41f, eyeY))
    drawCircle(Color(0xFF282522), radius = 2.8f, center = Offset(w * 0.59f, eyeY))

    // Nervous Sweat drop if cornered / defensive
    if (emotion == SuspectEmotion.NERVOUS || emotion == SuspectEmotion.DEFENSIVE || emotion == SuspectEmotion.SHOCKED) {
        drawCircle(AccentCyan.copy(alpha = 0.8f), radius = 2f, center = Offset(w * 0.66f, h * 0.38f))
    }

    // Mouth
    when (emotion) {
        SuspectEmotion.SHOCKED -> {
            drawOval(Color(0xFF6B3333), topLeft = Offset(w * 0.46f, h * 0.56f), size = Size(w * 0.08f, h * 0.04f))
        }
        SuspectEmotion.DEFENSIVE -> {
            drawLine(Color(0xFF6B3333), Offset(w * 0.43f, h * 0.58f), Offset(w * 0.57f, h * 0.57f), strokeWidth = 3f)
        }
        SuspectEmotion.NERVOUS -> {
            drawLine(Color(0xFF6B3333), Offset(w * 0.44f, h * 0.58f), Offset(w * 0.56f, h * 0.59f), strokeWidth = 2.5f)
        }
        else -> {
            drawLine(Color(0xFF7A3E3E), Offset(w * 0.44f, h * 0.58f), Offset(w * 0.56f, h * 0.58f), strokeWidth = 2.5f)
        }
    }
}

private fun DrawScope.drawGenericPortrait(w: Float, h: Float, emotion: SuspectEmotion) {
    drawCircle(SurfaceBorder, radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.45f))
}
