package com.example.ui.screens.customer

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
import com.example.model.RequestStatus
import com.example.model.ServiceRequest
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.components.RequestStatusPill
import com.example.ui.components.WorkerCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDashboardScreen(
    viewModel: WorkerConnectViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val allRequests by viewModel.allRequests.collectAsState()
    val savedWorkerIds by viewModel.savedWorkerIds.collectAsState()
    val allWorkers by viewModel.filteredWorkers.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Active, 1: Past, 2: Saved

    val myRequests = allRequests.filter { it.customerId == viewModel.repository.currentCustomerId }
    val activeRequests = myRequests.filter {
        it.status != RequestStatus.COMPLETED && it.status != RequestStatus.CANCELLED && it.status != RequestStatus.DECLINED
    }
    val pastRequests = myRequests.filter {
        it.status == RequestStatus.COMPLETED || it.status == RequestStatus.CANCELLED || it.status == RequestStatus.DECLINED
    }
    val savedWorkers = allWorkers.filter { savedWorkerIds.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Customer Profile Header
        Surface(
            color = DeepNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(PrimaryTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "K",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = viewModel.repository.currentCustomerName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "${viewModel.repository.currentCustomerPhone} • Chennai",
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${activeRequests.size}",
                            color = AccentAmber,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(text = "Active Enquiries", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${pastRequests.size}",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(text = "Past Bookings", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${savedWorkers.size}",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(text = "Saved Pros", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    }
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = PrimaryTeal
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Active (${activeRequests.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("History (${pastRequests.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text(LocalizedStrings.get("my_saved_workers", lang), fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            )
        }

        // Tab Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    if (activeRequests.isEmpty()) {
                        item {
                            EmptyStateNotice(
                                title = "No Active Service Enquiries",
                                subtitle = "Explore verified local workers in Tamil Nadu and send a direct enquiry.",
                                actionLabel = "Browse Services",
                                onAction = { viewModel.navigateTo(Screen.Home) }
                            )
                        }
                    } else {
                        items(activeRequests) { req ->
                            ServiceRequestCustomerCard(
                                req = req,
                                language = lang,
                                onCancel = {
                                    viewModel.updateRequestStatusByWorker(req.id, RequestStatus.CANCELLED, "Cancelled by customer")
                                }
                            )
                        }
                    }
                }
                1 -> {
                    if (pastRequests.isEmpty()) {
                        item {
                            EmptyStateNotice(
                                title = "No Past Bookings Yet",
                                subtitle = "Completed services and historical requests will appear here.",
                                actionLabel = "Find Workers",
                                onAction = { viewModel.navigateTo(Screen.Home) }
                            )
                        }
                    } else {
                        items(pastRequests) { req ->
                            ServiceRequestCustomerCard(
                                req = req,
                                language = lang,
                                onReview = { viewModel.openReviewDialog(req) }
                            )
                        }
                    }
                }
                2 -> {
                    if (savedWorkers.isEmpty()) {
                        item {
                            EmptyStateNotice(
                                title = "No Saved Favourites",
                                subtitle = "Tap the heart icon on any worker profile to keep quick access for emergencies.",
                                actionLabel = "Explore Workers",
                                onAction = { viewModel.navigateTo(Screen.Home) }
                            )
                        }
                    } else {
                        items(savedWorkers) { worker ->
                            WorkerCard(
                                worker = worker,
                                isSaved = true,
                                language = lang,
                                onCardClick = { viewModel.navigateTo(Screen.WorkerProfileDetail(worker.id)) },
                                onBookClick = { viewModel.openBookingDialog(worker) },
                                onToggleFavorite = { viewModel.toggleFavorite(worker.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceRequestCustomerCard(
    req: ServiceRequest,
    language: AppLanguage,
    onCancel: (() -> Unit)? = null,
    onReview: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Request ID, Date, Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Booking #${req.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = req.formattedDate,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                RequestStatusPill(status = req.status, language = language)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))

            // Worker Assigned
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Engineering, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = req.workerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
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
                    text = "Slot: ${req.preferredDate} (${req.preferredTimeSlot})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Urgency: ${req.urgency.name}",
                    fontSize = 11.sp,
                    color = if (req.urgency == com.example.model.RequestUrgency.EMERGENCY) AccentRed else PrimaryTeal,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Status Timeline summary
            if (req.statusHistory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Latest Activity: ${req.statusHistory.last().note.ifBlank { req.status.name }}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Action Buttons
            if (req.status == RequestStatus.COMPLETED && !req.isReviewed && onReview != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onReview,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rate_booking_btn_${req.id}")
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LocalizedStrings.get("rate_and_review", language),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            } else if (req.isReviewed) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Review submitted. Thank you for rating!",
                        fontSize = 11.sp,
                        color = AccentGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (onCancel != null && (req.status == RequestStatus.SUBMITTED || req.status == RequestStatus.SENT_TO_WORKER)) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel Request", color = AccentRed, fontSize = 12.sp)
                }
            }
        }
    }
}


@Composable
fun EmptyStateNotice(
    title: String,
    subtitle: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Inbox, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
            ) {
                Text(actionLabel)
            }
        }
    }
}
