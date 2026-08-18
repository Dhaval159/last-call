package com.example.thelastcall.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.EvidenceCategory
import com.example.thelastcall.data.EvidenceItem
import com.example.ui.theme.*

/**
 * Renders dedicated visual representations of evidence objects.
 * Gives each clue tangible texture (e.g. glowing digital screen UI, ledger sheet, forensic lab tag).
 */
@Composable
fun EvidenceVisualCard(
    evidence: EvidenceItem,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        when (evidence.iconType.lowercase()) {
            "phone", "call", "digital" -> DigitalRecordVisual(evidence)
            "document", "folder", "financial" -> DocumentaryFileVisual(evidence)
            "access", "keycard", "surveillance", "transit" -> AccessSecurityVisual(evidence)
            "weapon", "paperweight", "fingerprint" -> PhysicalWeaponVisual(evidence)
            "glass", "tumbler", "wine" -> ChemicalForensicsVisual(evidence)
            "autopsy", "medical", "report" -> MedicalPostMortemVisual(evidence)
            else -> when (evidence.category) {
                EvidenceCategory.DIGITAL -> DigitalRecordVisual(evidence)
                EvidenceCategory.DOCUMENTARY -> DocumentaryFileVisual(evidence)
                EvidenceCategory.PHYSICAL -> PhysicalWeaponVisual(evidence)
                EvidenceCategory.TESTIMONIAL -> AccessSecurityVisual(evidence)
                EvidenceCategory.ENVIRONMENTAL -> PhysicalWeaponVisual(evidence)
            }
        }
    }
}

@Composable
private fun DigitalRecordVisual(evidence: EvidenceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F1218))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DIGITAL DEVICE / NETWORK TELEMETRY",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = AccentCyan
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.SignalCellularAlt, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.Wifi, contentDescription = null, tint = StatusConfirmed, modifier = Modifier.size(14.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            color = Color(0xFF1B2230),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(AccentCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.PhoneIphone, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = evidence.name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Location: ${evidence.location}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentAmber
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = evidence.detailedInvestigation,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun DocumentaryFileVisual(evidence: EvidenceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E211F))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DOCUMENTARY ARCHIVE // OFFICIAL RECORD",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = AccentAmber
            )

            Surface(
                color = if (evidence.isCritical) AccentRed.copy(alpha = 0.2f) else SurfaceCard,
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (evidence.isCritical) AccentRed else SurfaceBorder)
            ) {
                Text(
                    text = if (evidence.isCritical) "CRITICAL" else "CONFIDENTIAL",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                    color = if (evidence.isCritical) AccentRed else AccentAmber,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = Color(0xFF141715),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = evidence.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                    color = AccentAmber
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = evidence.detailedInvestigation,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun AccessSecurityVisual(evidence: EvidenceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF151820))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SURVEILLANCE & ACCESS TELEMETRY",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = AccentCyan
            )
            Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F1116), RoundedCornerShape(6.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = evidence.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = evidence.detailedInvestigation,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun PhysicalWeaponVisual(evidence: EvidenceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF20181A))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PHYSICAL WEAPON / TRACE FORENSIC SCAN",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AccentRed
            )
            Surface(
                color = AccentRed.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = evidence.importance.label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                    color = AccentRed,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF2D1E22), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Biotech, contentDescription = null, tint = AccentRed, modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = evidence.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = evidence.detailedInvestigation,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ChemicalForensicsVisual(evidence: EvidenceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1B1A22))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LAB ANALYSIS: TOXICOLOGY & CHEMICAL TRACES",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AccentCyan
            )
            Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = evidence.name,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = evidence.detailedInvestigation,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun MedicalPostMortemVisual(evidence: EvidenceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF221616))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CORONER'S POST-MORTEM REPORT",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AccentRed
            )
            Icon(imageVector = Icons.Default.MedicalServices, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = evidence.detailedInvestigation,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = TextPrimary,
            lineHeight = 20.sp
        )
    }
}
