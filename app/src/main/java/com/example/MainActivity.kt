package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.BookingDialogState
import com.example.ui.ReviewDialogState
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.components.BookingBottomSheet
import com.example.ui.components.ReviewDialog
import com.example.ui.components.TopHeader
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.aws_guide.AwsCloudGuideScreen
import com.example.ui.screens.customer.CustomerDashboardScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.wizard.WorkerRegistrationWizardScreen
import com.example.ui.screens.worker.WorkerProfileScreen
import com.example.ui.screens.worker_dashboard.WorkerDashboardScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: WorkerConnectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                WorkerConnectApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WorkerConnectApp(viewModel: WorkerConnectViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val bookingState by viewModel.bookingState.collectAsState()
    val reviewState by viewModel.reviewState.collectAsState()
    val userFeedbackMessage by viewModel.userFeedbackMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userFeedbackMessage) {
        userFeedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedbackMessage()
        }
    }

    val showBottomNav = currentScreen !is Screen.WorkerProfileDetail &&
            currentScreen !is Screen.WorkerRegistrationWizard

    Scaffold(
        modifier = Modifier.fillMaxSize().testTag("worker_connect_root_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentScreen !is Screen.WorkerProfileDetail && currentScreen !is Screen.WorkerRegistrationWizard) {
                TopHeader(
                    viewModel = viewModel,
                    onOpenAwsGuide = { viewModel.navigateTo(Screen.AwsCloudGuide) }
                )
            }
        },
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    language = currentLanguage,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(viewModel = viewModel)
                }
                is Screen.WorkerProfileDetail -> {
                    WorkerProfileScreen(workerId = screen.workerId, viewModel = viewModel)
                }
                is Screen.CustomerDashboard -> {
                    CustomerDashboardScreen(viewModel = viewModel)
                }
                is Screen.WorkerDashboard -> {
                    WorkerDashboardScreen(viewModel = viewModel)
                }
                is Screen.WorkerRegistrationWizard -> {
                    WorkerRegistrationWizardScreen(viewModel = viewModel)
                }
                is Screen.AdminDashboard -> {
                    AdminDashboardScreen(viewModel = viewModel)
                }
                is Screen.AwsCloudGuide -> {
                    AwsCloudGuideScreen(viewModel = viewModel)
                }
            }
        }

        // Booking Modal Bottom Sheet
        if (bookingState.isOpen) {
            BookingBottomSheet(
                state = bookingState,
                language = currentLanguage,
                onDismiss = { viewModel.closeBookingDialog() },
                onDescChange = { viewModel.updateBookingDescription(it) },
                onLocalityChange = { viewModel.updateBookingLocality(it) },
                onPinChange = { viewModel.updateBookingPin(it) },
                onDateChange = { viewModel.updateBookingDate(it) },
                onSlotChange = { viewModel.updateBookingTime(it) },
                onUrgencyChange = { viewModel.updateBookingUrgency(it) },
                onSubmit = { viewModel.submitBooking() }
            )
        }

        // Review Submission Dialog
        if (reviewState.isOpen) {
            ReviewDialog(
                state = reviewState,
                language = currentLanguage,
                onDismiss = { viewModel.closeReviewDialog() },
                onRatingChange = { viewModel.updateReviewRating(it) },
                onCommentChange = { viewModel.updateReviewComment(it) },
                onSubmit = { viewModel.submitReview() }
            )
        }
    }
}
