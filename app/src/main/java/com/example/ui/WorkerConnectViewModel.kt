package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.WorkerConnectRepository
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    data class WorkerProfileDetail(val workerId: String) : Screen()
    object CustomerDashboard : Screen()
    object WorkerDashboard : Screen()
    object WorkerRegistrationWizard : Screen()
    object AdminDashboard : Screen()
    object AwsCloudGuide : Screen()
}

data class BookingDialogState(
    val isOpen: Boolean = false,
    val worker: WorkerProfile? = null,
    val description: String = "",
    val locality: String = "",
    val pinCode: String = "",
    val preferredDate: String = "Today",
    val preferredTimeSlot: String = "Morning (9 AM - 12 PM)",
    val urgency: RequestUrgency = RequestUrgency.NORMAL
)

data class ReviewDialogState(
    val isOpen: Boolean = false,
    val requestId: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val rating: Int = 5,
    val comment: String = ""
)

class WorkerConnectViewModel(
    val repository: WorkerConnectRepository = WorkerConnectRepository()
) : ViewModel() {

    val currentLanguage: StateFlow<AppLanguage> = repository.currentLanguage
    val currentRole: StateFlow<UserRole> = repository.currentRole
    val savedWorkerIds: StateFlow<Set<String>> = repository.savedWorkerIds
    val activeWorkerId: StateFlow<String> = repository.activeWorkerId

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<Screen>()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _bookingState = MutableStateFlow(BookingDialogState())
    val bookingState: StateFlow<BookingDialogState> = _bookingState.asStateFlow()

    private val _reviewState = MutableStateFlow(ReviewDialogState())
    val reviewState: StateFlow<ReviewDialogState> = _reviewState.asStateFlow()

    private val _userFeedbackMessage = MutableStateFlow<String?>(null)
    val userFeedbackMessage: StateFlow<String?> = _userFeedbackMessage.asStateFlow()

    // Wizard form state
    var wizardStep = MutableStateFlow(1)
    var wizardFullName = MutableStateFlow("")
    var wizardDisplayName = MutableStateFlow("")
    var wizardCategory = MutableStateFlow("cat_plumber")
    var wizardSkills = MutableStateFlow("")
    var wizardBio = MutableStateFlow("")
    var wizardCity = MutableStateFlow("Chennai")
    var wizardLocality = MutableStateFlow("")
    var wizardPin = MutableStateFlow("")
    var wizardExperience = MutableStateFlow("5")
    var wizardVisitCharge = MutableStateFlow("200")
    var wizardLabourCharge = MutableStateFlow("350")
    var wizardPhone = MutableStateFlow("+91 ")
    var wizardEmail = MutableStateFlow("")
    var wizardDocType = MutableStateFlow("Aadhaar / Voter ID")
    var wizardDocNumber = MutableStateFlow("")
    var wizardTradeCertificate = MutableStateFlow("")
    var wizardConsent = MutableStateFlow(true)

    // Filtered worker list
    val filteredWorkers: StateFlow<List<WorkerProfile>> = combine(
        repository.workers,
        _filterState
    ) { workers, filter ->
        workers.filter { worker ->
            val matchesCategory = filter.categoryId == null || worker.serviceCategoryId == filter.categoryId
            val matchesCity = filter.city == "All Tamil Nadu" || worker.city.equals(filter.city, ignoreCase = true)
            val matchesQuery = filter.query.isBlank() ||
                    worker.displayName.contains(filter.query, ignoreCase = true) ||
                    worker.fullName.contains(filter.query, ignoreCase = true) ||
                    worker.locality.contains(filter.query, ignoreCase = true) ||
                    worker.skills.any { it.contains(filter.query, ignoreCase = true) }
            val matchesVerified = !filter.verifiedOnly || worker.verificationStatus == VerificationStatus.APPROVED
            val matchesEmergency = !filter.emergencyOnly || worker.emergencyService
            val matchesAvailability = filter.availability == null || worker.availabilityStatus == filter.availability
            val matchesPrice = filter.maxVisitCharge == null || worker.visitCharge <= filter.maxVisitCharge

            matchesCategory && matchesCity && matchesQuery && matchesVerified && matchesEmergency && matchesAvailability && matchesPrice
        }.sortedWith(
            when (filter.sortBy) {
                SortOption.RATING_DESC -> compareByDescending { it.rating }
                SortOption.PRICE_ASC -> compareBy { it.visitCharge }
                SortOption.EXPERIENCE_DESC -> compareByDescending { it.experienceYears }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories = repository.categories
    val allRequests = repository.requests
    val allReviews = repository.reviews
    val allVerificationCases = repository.verificationCases

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            return true
        }
        if (_currentScreen.value !is Screen.Home) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    fun toggleLanguage() {
        val next = currentLanguage.value.toggle()
        repository.setLanguage(next)
        showMessage(if (next == AppLanguage.TAMIL) "மொழி தமிழில் மாற்றப்பட்டது" else "Language changed to English")
    }

    fun setRole(role: UserRole) {
        repository.setRole(role)
        when (role) {
            UserRole.CUSTOMER -> navigateTo(Screen.Home)
            UserRole.WORKER -> navigateTo(Screen.WorkerDashboard)
            UserRole.ADMINISTRATOR -> navigateTo(Screen.AdminDashboard)
        }
    }

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun selectCategoryFilter(catId: String?) {
        _filterState.value = _filterState.value.copy(categoryId = catId)
    }

    fun selectCityFilter(city: String) {
        _filterState.value = _filterState.value.copy(city = city)
    }

    fun setSortOption(sort: SortOption) {
        _filterState.value = _filterState.value.copy(sortBy = sort)
    }

    fun toggleVerifiedOnly() {
        _filterState.value = _filterState.value.copy(verifiedOnly = !_filterState.value.verifiedOnly)
    }

    fun toggleEmergencyOnly() {
        _filterState.value = _filterState.value.copy(emergencyOnly = !_filterState.value.emergencyOnly)
    }

    fun setAvailabilityFilter(availability: AvailabilityStatus?) {
        _filterState.value = _filterState.value.copy(availability = availability)
    }

    fun clearAllFilters() {
        _filterState.value = FilterState()
    }

    fun toggleFavorite(workerId: String) {
        repository.toggleFavorite(workerId)
    }

    fun openBookingDialog(worker: WorkerProfile) {
        _bookingState.value = BookingDialogState(
            isOpen = true,
            worker = worker,
            description = "",
            locality = worker.locality,
            pinCode = worker.pinCode
        )
    }

    fun closeBookingDialog() {
        _bookingState.value = _bookingState.value.copy(isOpen = false)
    }

    fun updateBookingDescription(text: String) {
        _bookingState.value = _bookingState.value.copy(description = text)
    }

    fun updateBookingLocality(text: String) {
        _bookingState.value = _bookingState.value.copy(locality = text)
    }

    fun updateBookingPin(text: String) {
        _bookingState.value = _bookingState.value.copy(pinCode = text)
    }

    fun updateBookingDate(date: String) {
        _bookingState.value = _bookingState.value.copy(preferredDate = date)
    }

    fun updateBookingTime(slot: String) {
        _bookingState.value = _bookingState.value.copy(preferredTimeSlot = slot)
    }

    fun updateBookingUrgency(urgency: RequestUrgency) {
        _bookingState.value = _bookingState.value.copy(urgency = urgency)
    }

    fun submitBooking() {
        val state = _bookingState.value
        val worker = state.worker ?: return
        if (state.description.isBlank()) {
            showMessage("Please describe your problem or requirements")
            return
        }

        repository.createServiceRequest(
            workerId = worker.id,
            description = state.description,
            locality = state.locality.ifBlank { worker.locality },
            city = worker.city,
            pinCode = state.pinCode.ifBlank { worker.pinCode },
            preferredDate = state.preferredDate,
            preferredTimeSlot = state.preferredTimeSlot,
            urgency = state.urgency
        )

        closeBookingDialog()
        showMessage(LocalizedStrings.get("booking_success_msg", currentLanguage.value))
        navigateTo(Screen.CustomerDashboard)
    }

    fun openReviewDialog(request: ServiceRequest) {
        _reviewState.value = ReviewDialogState(
            isOpen = true,
            requestId = request.id,
            workerId = request.workerId,
            workerName = request.workerName,
            rating = 5,
            comment = ""
        )
    }

    fun closeReviewDialog() {
        _reviewState.value = _reviewState.value.copy(isOpen = false)
    }

    fun updateReviewRating(rating: Int) {
        _reviewState.value = _reviewState.value.copy(rating = rating)
    }

    fun updateReviewComment(comment: String) {
        _reviewState.value = _reviewState.value.copy(comment = comment)
    }

    fun submitReview() {
        val state = _reviewState.value
        if (state.comment.isBlank()) {
            showMessage("Please share a brief comment about the service experience")
            return
        }
        repository.submitReview(
            requestId = state.requestId,
            workerId = state.workerId,
            rating = state.rating,
            comment = state.comment
        )
        closeReviewDialog()
        showMessage("Thank you! Your verified review has been published.")
    }

    fun updateWorkerStatus(workerId: String, status: AvailabilityStatus) {
        repository.updateWorkerAvailability(workerId, status)
        showMessage("Live availability updated to ${status.name.replace("_", " ")}")
    }

    fun updateRequestStatusByWorker(requestId: String, status: RequestStatus, note: String = "") {
        repository.updateRequestStatus(requestId, status, note)
        val msg = when (status) {
            RequestStatus.ACCEPTED -> "Job enquiry accepted! Customer notified."
            RequestStatus.DECLINED -> "Job enquiry declined."
            RequestStatus.IN_PROGRESS -> "Work marked In Progress."
            RequestStatus.COMPLETED -> "Job marked Completed! Customer invited to review."
            else -> "Status updated to ${status.name}"
        }
        showMessage(msg)
    }

    fun adminReview(caseId: String, status: VerificationStatus, note: String) {
        repository.adminReviewWorker(caseId, status, note)
        showMessage("Verification case updated: ${status.name}")
    }

    fun submitRegistrationWizard() {
        if (wizardFullName.value.isBlank() || wizardPhone.value.length < 10) {
            showMessage("Please provide your full legal name and valid phone number")
            return
        }
        val skillsList = wizardSkills.value.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val finalSkills = if (skillsList.isEmpty()) listOf("General Repair & Service") else skillsList
        val visit = wizardVisitCharge.value.toIntOrNull() ?: 200
        val labour = wizardLabourCharge.value.toIntOrNull() ?: 350
        val exp = wizardExperience.value.toIntOrNull() ?: 5

        repository.registerNewWorker(
            fullName = wizardFullName.value,
            displayName = wizardDisplayName.value.ifBlank { wizardFullName.value },
            categoryId = wizardCategory.value,
            skills = finalSkills,
            bio = wizardBio.value.ifBlank { "Experienced professional providing reliable local services." },
            city = wizardCity.value,
            locality = wizardLocality.value.ifBlank { "Central" },
            pinCode = wizardPin.value.ifBlank { "600001" },
            experienceYears = exp,
            visitCharge = visit,
            minLabourCharge = labour,
            phone = wizardPhone.value,
            email = wizardEmail.value.ifBlank { "worker@workerconnect.in" },
            documentType = wizardDocType.value,
            documentNumber = wizardDocNumber.value.ifBlank { "98421002" },
            tradeCertificate = wizardTradeCertificate.value
        )

        wizardStep.value = 1
        showMessage(LocalizedStrings.get("wizard_success_msg", currentLanguage.value))
        setRole(UserRole.WORKER)
    }

    fun showMessage(msg: String) {
        _userFeedbackMessage.value = msg
    }

    fun clearFeedbackMessage() {
        _userFeedbackMessage.value = null
    }
}
