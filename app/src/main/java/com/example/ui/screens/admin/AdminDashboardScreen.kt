package com.example.ui.screens.admin

import androidx.compose.foundation.background
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
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.VerificationCase
import com.example.model.VerificationStatus
import com.example.ui.WorkerConnectViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: WorkerConnectViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val cases by viewModel.allVerificationCases.collectAsState()
    val workers by viewModel.filteredWorkers.collectAsState()
    val requests by viewModel.allRequests.collectAsState()

    var selectedStatusFilter by remember { mutableStateOf<VerificationStatus?>(null) }
    var noteInput by remember { mutableStateOf("") }
    var selectedCaseForReview by remember { mutableStateOf<VerificationCase?>(null) }

    val pendingCases = cases.filter { selectedStatusFilter == null || it.status == selectedStatusFilter }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Admin Header
        item {
            Surface(
                color = DeepNavyDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color(0xFF60A5FA),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = LocalizedStrings.get("admin_title", lang),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Restricted Operations & KYC Verification Queue",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Overview Metrics Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MetricItem(title = "Verified Pros", count = "${workers.count { it.verificationStatus == VerificationStatus.APPROVED }}")
                        MetricItem(title = "Pending Queue", count = "${cases.count { it.status == VerificationStatus.SUBMITTED || it.status == VerificationStatus.UNDER_REVIEW }}")
                        MetricItem(title = "Total Enquiries", count = "${requests.size}")
                        MetricItem(title = "Completed", count = "${requests.count { it.status == com.example.model.RequestStatus.COMPLETED }}")
                    }
                }
            }
        }

        // Filter Pills
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStatusFilter == null,
                    onClick = { selectedStatusFilter = null },
                    label = { Text("All Cases (${cases.size})", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedStatusFilter == VerificationStatus.SUBMITTED,
                    onClick = { selectedStatusFilter = VerificationStatus.SUBMITTED },
                    label = { Text("Submitted", fontSize = 11.sp) }
                )
                FilterChip(
                    selected = selectedStatusFilter == VerificationStatus.APPROVED,
                    onClick = { selectedStatusFilter = VerificationStatus.APPROVED },
                    label = { Text("Approved", fontSize = 11.sp) }
                )
            }
        }

        // Verification Cases List
        items(pendingCases) { itemCase ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("admin_case_${itemCase.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = itemCase.workerName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Category: ${itemCase.categoryName} • Case ID: #${itemCase.id}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (itemCase.status) {
                                VerificationStatus.APPROVED -> AccentGreenLight
                                VerificationStatus.SUBMITTED -> AccentAmberLight
                                VerificationStatus.UNDER_REVIEW -> Color(0xFFDBEAFE)
                                VerificationStatus.REJECTED, VerificationStatus.SUSPENDED -> AccentRedLight
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ) {
                            Text(
                                text = itemCase.status.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = when (itemCase.status) {
                                    VerificationStatus.APPROVED -> Color(0xFF065F46)
                                    VerificationStatus.SUBMITTED -> Color(0xFF92400E)
                                    VerificationStatus.UNDER_REVIEW -> Color(0xFF1E40AF)
                                    VerificationStatus.REJECTED, VerificationStatus.SUSPENDED -> AccentRed
                                    else -> Color.Black
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Document: ${itemCase.documentType} (${itemCase.documentNumberMasked})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Trade Certificate: ${itemCase.tradeCertificateName}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Submission Date: ${itemCase.submittedAt}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (itemCase.reviewerNotes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Notes: ${itemCase.reviewerNotes}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    // Audit Trail Snippet
                    if (itemCase.auditTrail.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Audit Log: " + itemCase.auditTrail.last(),
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Admin Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (itemCase.status != VerificationStatus.APPROVED) {
                            Button(
                                onClick = {
                                    viewModel.adminReview(
                                        caseId = itemCase.id,
                                        status = VerificationStatus.APPROVED,
                                        note = "Documents verified against Govt registry by Admin."
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .testTag("admin_approve_btn_${itemCase.id}")
                            ) {
                                Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve & Award Badge", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.adminReview(
                                        caseId = itemCase.id,
                                        status = VerificationStatus.REJECTED,
                                        note = "Incomplete trade documentation provided."
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .testTag("admin_reject_btn_${itemCase.id}")
                            ) {
                                Text("Reject", color = AccentRed, fontSize = 11.sp)
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    viewModel.adminReview(
                                        caseId = itemCase.id,
                                        status = VerificationStatus.SUSPENDED,
                                        note = "Account temporarily suspended for policy review."
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Suspend Verified Account", color = AccentRed, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricItem(title: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        Text(text = title, color = Color(0xFF94A3B8), fontSize = 10.sp)
    }
}
