package com.example.model

enum class UserRole {
    CUSTOMER,
    WORKER,
    ADMINISTRATOR
}

enum class AvailabilityStatus {
    AVAILABLE_NOW,
    AVAILABLE_TODAY,
    BUSY,
    OFFLINE
}

enum class VerificationStatus {
    DRAFT,
    SUBMITTED,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    SUSPENDED
}

enum class VerificationBadge {
    PHONE_VERIFIED,
    GOVT_ID_REVIEWED,
    TRADE_CERTIFIED
}

enum class RequestUrgency {
    NORMAL,
    SAME_DAY,
    EMERGENCY
}

enum class RequestStatus {
    SUBMITTED,
    SENT_TO_WORKER,
    ACCEPTED,
    DECLINED,
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

data class StatusHistoryItem(
    val status: RequestStatus,
    val timestamp: String,
    val note: String = ""
)

data class WorkerProfile(
    val id: String,
    val fullName: String,
    val displayName: String,
    val serviceCategoryId: String,
    val skills: List<String>,
    val bio: String,
    val city: String,
    val locality: String,
    val pinCode: String,
    val coverageRadiusKm: Int,
    val experienceYears: Int,
    val rating: Float,
    val reviewCount: Int,
    val visitCharge: Int, // Inspection/visit fee in INR
    val minLabourCharge: Int, // Base labor in INR
    val taskEstimates: Map<String, Int> = emptyMap(),
    val workingDays: String = "Mon - Sat",
    val workingHours: String = "8:00 AM - 7:00 PM",
    val availabilityStatus: AvailabilityStatus = AvailabilityStatus.AVAILABLE_NOW,
    val lastStatusUpdateTime: String = "Just now",
    val emergencyService: Boolean = true,
    val languagesSpoken: List<String> = listOf("Tamil", "English"),
    val verificationStatus: VerificationStatus = VerificationStatus.APPROVED,
    val badges: Set<VerificationBadge> = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED),
    val phoneNumber: String,
    val email: String,
    val avatarUrl: String? = null,
    val portfolioJobs: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class ServiceCategoryItem(
    val id: String,
    val key: String,
    val nameEn: String,
    val nameTa: String,
    val descriptionEn: String,
    val descriptionTa: String,
    val iconKey: String,
    val popularTasks: List<String>
)

data class ServiceRequest(
    val id: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val workerId: String,
    val workerName: String,
    val serviceCategoryId: String,
    val description: String,
    val locality: String,
    val city: String,
    val pinCode: String,
    val preferredDate: String,
    val preferredTimeSlot: String,
    val urgency: RequestUrgency,
    val status: RequestStatus,
    val createdAt: Long = System.currentTimeMillis(),
    val formattedDate: String,
    val statusHistory: List<StatusHistoryItem> = emptyList(),
    val isReviewed: Boolean = false
)

data class Review(
    val id: String,
    val requestId: String,
    val workerId: String,
    val customerId: String,
    val customerName: String,
    val rating: Int, // 1 to 5
    val comment: String,
    val date: String
)

data class VerificationCase(
    val id: String,
    val workerId: String,
    val workerName: String,
    val categoryName: String,
    val documentType: String,
    val documentNumberMasked: String,
    val tradeCertificateName: String,
    val submittedAt: String,
    val status: VerificationStatus,
    val reviewerNotes: String = "",
    val auditTrail: List<String> = emptyList()
)

data class FilterState(
    val query: String = "",
    val categoryId: String? = null,
    val city: String = "All Tamil Nadu",
    val locality: String = "",
    val maxVisitCharge: Int? = null,
    val verifiedOnly: Boolean = false,
    val emergencyOnly: Boolean = false,
    val availability: AvailabilityStatus? = null,
    val sortBy: SortOption = SortOption.RATING_DESC
)

enum class SortOption {
    RATING_DESC,
    PRICE_ASC,
    EXPERIENCE_DESC
}
