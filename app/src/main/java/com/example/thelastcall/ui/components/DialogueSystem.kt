package com.example.thelastcall.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.thelastcall.data.TextSpeed
import com.example.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Typewriter text effect that renders dialogue character-by-character based on TextSpeed.
 * Supports tap-to-complete.
 */
@Composable
fun TypewriterText(
    fullText: String,
    textSpeed: TextSpeed,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    var displayedLength by remember(fullText) { mutableStateOf(0) }
    val isComplete = displayedLength >= fullText.length

    val charDelayMs = when (textSpeed) {
        TextSpeed.SLOW -> 45L
        TextSpeed.NORMAL -> 22L
        TextSpeed.FAST -> 8L
        TextSpeed.INSTANT -> 0L
    }

    LaunchedEffect(fullText, textSpeed) {
        if (textSpeed == TextSpeed.INSTANT || charDelayMs == 0L) {
            displayedLength = fullText.length
            onComplete()
        } else {
            displayedLength = 0
            while (displayedLength < fullText.length) {
                delay(charDelayMs)
                displayedLength++
            }
            onComplete()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!isComplete) {
                    displayedLength = fullText.length
                    onComplete()
                }
            }
    ) {
        Text(
            text = fullText.take(displayedLength),
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            lineHeight = 22.sp
        )
    }
}

/**
 * Dialogue history record item for transcript review during interrogations.
 */
data class DialogueHistoryEntry(
    val speaker: String,
    val text: String,
    val isDetective: Boolean = false,
    val timestamp: String? = null
)

@Composable
fun DialogueHistorySheet(
    history: List<DialogueHistoryEntry>,
    onDismiss: () -> Unit
) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HistoryEdu,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "INTERVIEW TRANSCRIPT",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_transcript_button")) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No statements recorded yet.", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(history) { entry ->
                        Surface(
                            color = if (entry.isDetective) Color(0xFF161E28) else SurfaceCard,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (entry.isDetective) AccentCyan.copy(alpha = 0.3f) else SurfaceBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = entry.speaker.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (entry.isDetective) AccentCyan else AccentAmber
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = entry.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
