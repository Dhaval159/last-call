package com.example.thelastcall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thelastcall.data.*
import com.example.thelastcall.engine.BoardConnectionResult
import com.example.thelastcall.ui.components.CaseTopBar
import com.example.thelastcall.ui.components.DetectiveBoardCanvas
import com.example.thelastcall.ui.components.NotificationToast
import com.example.ui.theme.*

@Composable
fun DetectiveBoardScreen(
    state: CaseState,
    caseDef: CaseDefinition = CaseRegistry.getDefaultCase(),
    onOpenCaseFile: (CaseFileTab) -> Unit,
    onSelectEvidence: (String) -> Unit,
    onSelectSuspect: (String) -> Unit,
    onOpenPersonProfile: (String) -> Unit = {},
    onAddConnection: (String, String, ReasoningRelationship) -> BoardConnectionResult,
    onRemoveConnection: (String) -> Unit,
    onTestContradiction: (String, String) -> Contradiction?,
    onTestDeduction: (String, String, ReasoningRelationship) -> Pair<Boolean, String>,
    onDismissNotification: () -> Unit,
    onBack: () -> Unit
) {
    val activeObjective = caseDef.objectives.find { !state.completedObjectiveIds.contains(it.id) }?.title
        ?: "Connect evidence and expose contradictions on the pinboard"

    Scaffold(
        topBar = {
            CaseTopBar(
                title = "DETECTIVE PINBOARD",
                subtitle = "Visual Case Synthesis & Reasoning Hub",
                onBack = onBack,
                onOpenCaseFile = { onOpenCaseFile(CaseFileTab.EVIDENCE) },
                activeObjective = activeObjective,
                onObjectiveClick = { onOpenCaseFile(CaseFileTab.OBJECTIVES) }
            )
        },
        containerColor = BackgroundDark
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DetectiveBoardCanvas(
                state = state,
                caseDef = caseDef,
                onSelectEvidenceForDetail = onSelectEvidence,
                onSelectSuspectForInterview = onSelectSuspect,
                onOpenPersonProfile = onOpenPersonProfile,
                onOpenCaseFileTab = onOpenCaseFile,
                onAddConnection = onAddConnection,
                onRemoveConnection = onRemoveConnection,
                onTestContradiction = onTestContradiction,
                onTestDeduction = onTestDeduction,
                modifier = Modifier.fillMaxSize()
            )

            // Active Notification Overlay Toast
            state.activeNotification?.let { notif ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                ) {
                    NotificationToast(
                        notification = notif,
                        onDismiss = onDismissNotification
                    )
                }
            }
        }
    }
}
