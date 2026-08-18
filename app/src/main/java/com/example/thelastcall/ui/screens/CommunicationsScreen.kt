package com.example.thelastcall.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thelastcall.data.*
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.ui.theme.*

enum class CommSection(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    MESSAGES("MESSAGES", Icons.Default.ChatBubble),
    CALL_LOG("CALL LOG", Icons.Default.Phone),
    CONTACTS("DIRECTORY", Icons.Default.ContactPhone)
}

@Composable
fun CommunicationsScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onOpenEvidence: (String) -> Unit,
    onOpenContact: (String) -> Unit,
    onOpenCaseFile: () -> Unit,
    onBack: () -> Unit
) {
    var activeSection by remember { mutableStateOf(CommSection.MESSAGES) }
    var selectedThreadId by remember { mutableStateOf<String?>(null) }

    val threads = caseDef.communicationThreads
    val activeThread = remember(selectedThreadId, threads) {
        threads.firstOrNull { it.id == selectedThreadId } ?: threads.firstOrNull()
    }

    // Call logs derived from phone evidence
    val callLogs = remember(state.discoveredEvidenceIds, caseDef) {
        caseDef.callLogs.filter { log ->
            state.discoveredEvidenceIds.contains(log.linkedEvidenceId)
        }
    }

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "COMMUNICATIONS ARCHIVE",
                subtitle = "VICTIM DIGITAL RECOVERY • ${caseDef.id.replace('-', ' ')}",
                onBack = onBack
            )
        },
        containerColor = ArchiveBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Segmented Section Tabs
            Surface(
                color = ArchiveCard,
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, ArchiveDivider)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CommSection.values().forEach { section ->
                        val isSelected = activeSection == section
                        Surface(
                            color = if (isSelected) CaseGold.copy(alpha = 0.18f) else ArchiveCardElevated,
                            shape = RoundedCornerShape(6.dp),
                            border = if (isSelected) BorderStroke(1.dp, CaseGold) else BorderStroke(1.dp, ArchiveDivider),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeSection = section }
                                .testTag("comm_tab_${section.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = section.icon,
                                    contentDescription = null,
                                    tint = if (isSelected) CaseGold else CaseSlate,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = section.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = if (isSelected) CaseGold else TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Section Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeSection) {
                    CommSection.MESSAGES -> {
                        if (threads.isEmpty()) {
                            EmptyCommunicationsView(
                                message = "No new communications.",
                                caption = "Recover digital evidence from the crime scene to decrypt message threads."
                            )
                        } else {
                            MessagesView(
                                threads = threads,
                                activeThread = activeThread,
                                onSelectThread = { selectedThreadId = it.id },
                                onOpenAttachment = onOpenEvidence,
                                caseDef = caseDef
                            )
                        }
                    }

                    CommSection.CALL_LOG -> {
                        if (callLogs.isEmpty()) {
                            EmptyCommunicationsView(
                                message = "Call log encrypted or undiscovered.",
                                caption = "Inspect ${caseDef.victimName}'s phone at the crime scene to decrypt timestamp records."
                            )
                        } else {
                            CallLogView(
                                callLogs = callLogs,
                                onOpenEvidence = onOpenEvidence,
                                caseDef = caseDef
                            )
                        }
                    }

                    CommSection.CONTACTS -> {
                        ContactsDirectoryView(
                            suspects = caseDef.suspects,
                            onSelectContact = onOpenContact
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessagesView(
    threads: List<CommunicationThread>,
    activeThread: CommunicationThread?,
    onSelectThread: (CommunicationThread) -> Unit,
    onOpenAttachment: (String) -> Unit,
    caseDef: CaseDefinition
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Thread Selector Pills
        if (threads.size > 1) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 110.dp)
                    .background(ArchiveSurface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(threads) { thread ->
                    val isSelected = activeThread?.id == thread.id
                    Surface(
                        color = if (isSelected) ArchiveCardElevated else ArchiveCard,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (isSelected) CaseGold else ArchiveDivider),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectThread(thread) }
                            .testTag("thread_item_${thread.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(thread.contactColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = thread.contactInitials,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = thread.title,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) CaseGold else TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = thread.channelLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = CaseSlate
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider(color = ArchiveDivider)
        }

        // Active Thread Chat Bubbles
        if (activeThread != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = ArchiveCard,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ArchiveDivider)
                        ) {
                            Text(
                                text = "DECRYPTED CHANNEL: ${activeThread.channelLabel.uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = CaseSlate,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                items(activeThread.messages) { message ->
                    MessageBubble(
                        message = message,
                        onOpenAttachment = onOpenAttachment,
                        caseDef = caseDef
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: CommunicationMessage,
    onOpenAttachment: (String) -> Unit,
    caseDef: CaseDefinition
) {
    val isVictim = message.isFromVictim

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isVictim) Alignment.End else Alignment.Start
    ) {
        Text(
            text = "${message.sender} • ${message.timestamp}",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = CaseSlate,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )

        Surface(
            color = if (isVictim) Color(0xFF1E2838) else ArchiveCard,
            shape = RoundedCornerShape(
                topStart = 10.dp,
                topEnd = 10.dp,
                bottomStart = if (isVictim) 10.dp else 2.dp,
                bottomEnd = if (isVictim) 2.dp else 10.dp
            ),
            border = BorderStroke(
                1.dp,
                if (isVictim) AccentBlue.copy(alpha = 0.4f) else ArchiveDivider
            ),
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )

                if (message.attachmentEvidenceId != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = ArchiveCardElevated,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, CaseGold.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenAttachment(message.attachmentEvidenceId) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Attachment,
                                contentDescription = "Attachment",
                                tint = CaseGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val attachEvidence = caseDef.getEvidence(message.attachmentEvidenceId)
                            Text(
                                text = "ATTACHMENT: ${attachEvidence?.name?.uppercase() ?: "RECOVERED ARTIFACT"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = CaseGold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "INSPECT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallLogView(
    callLogs: List<CallLogEntry>,
    onOpenEvidence: (String) -> Unit,
    caseDef: CaseDefinition
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Surface(
                color = ArchiveCard,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, ArchiveDivider),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneIphone,
                        contentDescription = null,
                        tint = CaseGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DEVICE: ${caseDef.victimName.uppercase()} PERSONAL PHONE • CARRIER ARCHIVE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = CaseGold
                    )
                }
            }
        }

        items(callLogs) { entry ->
            Surface(
                color = ArchiveCard,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    1.dp,
                    if (entry.isCritical) StatusContradiction.copy(alpha = 0.6f) else ArchiveDivider
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenEvidence(entry.linkedEvidenceId) }
                    .testTag("call_log_entry_${entry.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                when (entry.direction) {
                                    CallDirection.OUTGOING -> AccentCyan.copy(alpha = 0.15f)
                                    CallDirection.INCOMING -> StatusConfirmed.copy(alpha = 0.15f)
                                    CallDirection.MISSED -> StatusContradiction.copy(alpha = 0.15f)
                                },
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (entry.direction) {
                                CallDirection.OUTGOING -> Icons.Default.CallMade
                                CallDirection.INCOMING -> Icons.Default.CallReceived
                                CallDirection.MISSED -> Icons.Default.CallMissed
                            },
                            contentDescription = null,
                            tint = when (entry.direction) {
                                CallDirection.OUTGOING -> AccentCyan
                                CallDirection.INCOMING -> StatusConfirmed
                                CallDirection.MISSED -> StatusContradiction
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = entry.contactName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = entry.timestamp,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (entry.isCritical) StatusContradiction else CaseGold
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${entry.direction.label}${if (entry.durationLabel != null) " • ${entry.durationLabel}" else ""}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CaseSlate
                            )
                            if (entry.isCritical) {
                                Text(
                                    text = "CRITICAL TIME ANCHOR",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = StatusContradiction
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactsDirectoryView(
    suspects: List<Suspect>,
    onSelectContact: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "ADDRESS BOOK & ASSOCIATES",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                ),
                color = CaseSlate
            )
        }

        items(suspects) { suspect ->
            Surface(
                color = ArchiveCard,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, ArchiveDivider),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectContact(suspect.id) }
                    .testTag("contact_item_${suspect.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = suspect.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${suspect.relationship} • ${suspect.occupation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "View Dossier",
                        tint = CaseGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCommunicationsView(
    message: String,
    caption: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = ArchiveCard,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, ArchiveDivider),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.MarkEmailUnread,
                    contentDescription = null,
                    tint = CaseSlate,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = caption,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
