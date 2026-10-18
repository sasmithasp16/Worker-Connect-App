package com.example.ui.screens.worker_dashboard

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.*
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.components.AvailabilityPill
import com.example.ui.components.RequestStatusPill
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDashboardScreen(
    viewModel: WorkerConnectViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val activeWorkerId by viewModel.activeWorkerId.collectAsState()
    val allWorkers by viewModel.filteredWorkers.collectAsState()
    val allRequests by viewModel.allRequests.collectAsState()

    val currentWorker = allWorkers.find { it.id == activeWorkerId } ?: allWorkers.firstOrNull() ?: return
    val incomingRequests = allRequests.filter { it.workerId == currentWorker.id }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Worker Header Card
        item {
            Surface(
                color = DeepNavy,
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
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(PrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentWorker.displayName.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentWorker.displayName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = TealLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Legal: ${currentWorker.fullName} • ${currentWorker.city}",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Performance Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatPill(title = "Rating", value = "★ ${currentWorker.rating}")
                        StatPill(title = "Jobs Done", value = "${currentWorker.reviewCount}")
                        StatPill(title = "Inspection", value = "₹${currentWorker.visitCharge}")
                        StatPill(title = "Base Labour", value = "₹${currentWorker.minLabourCharge}")
                    }
                }
            }
        }

        // Live Availability Status Switcher (CRITICAL REQUIREMENT)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = LocalizedStrings.get("worker_current_status", lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${LocalizedStrings.get("last_updated", lang)}: ${currentWorker.lastStatusUpdateTime}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        AvailabilityPill(status = currentWorker.availabilityStatus, language = lang)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = LocalizedStrings.get("worker_update_status", lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Buttons to change status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusButton(
                            label = "Available Now",
                            isSelected = currentWorker.availabilityStatus == AvailabilityStatus.AVAILABLE_NOW,
                            color = AccentGreen,
                            onClick = { viewModel.updateWorkerStatus(currentWorker.id, AvailabilityStatus.AVAILABLE_NOW) },
                            modifier = Modifier.weight(1f)
                        )
                        StatusButton(
                            label = "Today",
                            isSelected = currentWorker.availabilityStatus == AvailabilityStatus.AVAILABLE_TODAY,
                            color = AccentBlue,
                            onClick = { viewModel.updateWorkerStatus(currentWorker.id, AvailabilityStatus.AVAILABLE_TODAY) },
                            modifier = Modifier.weight(0.8f)
                        )
                        StatusButton(
                            label = "Busy",
                            isSelected = currentWorker.availabilityStatus == AvailabilityStatus.BUSY,
                            color = AccentAmber,
                            onClick = { viewModel.updateWorkerStatus(currentWorker.id, AvailabilityStatus.BUSY) },
                            modifier = Modifier.weight(0.8f)
                        )
                        StatusButton(
                            label = "Offline",
                            isSelected = currentWorker.availabilityStatus == AvailabilityStatus.OFFLINE,
                            color = Color.Gray,
                            onClick = { viewModel.updateWorkerStatus(currentWorker.id, AvailabilityStatus.OFFLINE) },
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
            }
        }

        // Customer Enquiries Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${LocalizedStrings.get("worker_inbox", lang)} (${incomingRequests.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (incomingRequests.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.NotificationsNone, null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "No Enquiries At This Moment", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ensure your live status is set to 'Available Now' to receive customer leads.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(incomingRequests) { req ->
                WorkerIncomingRequestCard(
                    req = req,
                    language = lang,
                    onAccept = {
                        viewModel.updateRequestStatusByWorker(req.id, RequestStatus.ACCEPTED, "Worker confirmed availability")
                    },
                    onDecline = {
                        viewModel.updateRequestStatusByWorker(req.id, RequestStatus.DECLINED, "Worker schedule full")
                    },
                    onStart = {
                        viewModel.updateRequestStatusByWorker(req.id, RequestStatus.IN_PROGRESS, "Worker arrived and started work")
                    },
                    onComplete = {
                        viewModel.updateRequestStatusByWorker(req.id, RequestStatus.COMPLETED, "Work completed successfully")
                    }
                )
            }
        }

        // Verification & Compliance Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Worker Verification Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, null, tint = PrimaryTeal, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Verification: ${currentWorker.verificationStatus.name}",
                            color = PrimaryTeal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Phone Verified: Yes • Government KYC: Approved • Trade Certificate: Verified by Admin",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun WorkerIncomingRequestCard(
    req: ServiceRequest,
    language: AppLanguage,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("worker_inbox_card_${req.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Customer: ${req.customerName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                RequestStatusPill(status = req.status, language = language)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = req.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Location: ${req.locality}, ${req.city}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Phone: ${if (req.status != RequestStatus.SUBMITTED && req.status != RequestStatus.SENT_TO_WORKER) req.customerPhone else "Revealed upon Accept"}",
                    fontSize = 11.sp,
                    color = PrimaryTeal,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons depending on status
            when (req.status) {
                RequestStatus.SUBMITTED, RequestStatus.SENT_TO_WORKER -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDecline,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("decline_job_btn_${req.id}"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(LocalizedStrings.get("btn_decline", language), color = AccentRed, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("accept_job_btn_${req.id}"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(LocalizedStrings.get("btn_accept", language), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
                RequestStatus.ACCEPTED, RequestStatus.SCHEDULED -> {
                    Button(
                        onClick = onStart,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_job_btn_${req.id}"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LocalizedStrings.get("btn_mark_progress", language), fontWeight = FontWeight.Bold)
                    }
                }
                RequestStatus.IN_PROGRESS -> {
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("complete_job_btn_${req.id}"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LocalizedStrings.get("btn_mark_complete", language), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                RequestStatus.COMPLETED -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DoneAll, null, tint = AccentGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Job completed and invoice recorded.", color = AccentGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun StatusButton(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) color else Color.Transparent),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatPill(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        Text(text = title, color = Color(0xFF94A3B8), fontSize = 10.sp)
    }
}
