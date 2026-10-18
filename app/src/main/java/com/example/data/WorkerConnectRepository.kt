package com.example.data

import com.example.localization.AppLanguage
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class WorkerConnectRepository {

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Default logged in demo IDs for instant testing
    val currentCustomerId = "cust_001"
    val currentCustomerName = "Karthik Subramanian"
    val currentCustomerPhone = "+91 98401 23456"

    private val _activeWorkerId = MutableStateFlow("w_001")
    val activeWorkerId: StateFlow<String> = _activeWorkerId.asStateFlow()

    private val _savedWorkerIds = MutableStateFlow<Set<String>>(setOf("w_001", "w_003"))
    val savedWorkerIds: StateFlow<Set<String>> = _savedWorkerIds.asStateFlow()

    val categories = listOf(
        ServiceCategoryItem(
            id = "cat_plumber",
            key = "cat_plumber",
            nameEn = "Plumber",
            nameTa = "குழாய் பழுதுபார்ப்பவர்",
            descriptionEn = "Pipe leaks, tap fixing, bathroom fittings, water tank cleaning",
            descriptionTa = "குழாய் கசிவு, புதிய குழாய் பொருத்துதல், தொட்டி பராமரிப்பு",
            iconKey = "plumbing",
            popularTasks = listOf("Tap Leak Repair", "Overhead Tank Valve", "Concealed Pipe Seepage", "Washbasin Installation")
        ),
        ServiceCategoryItem(
            id = "cat_electrician",
            key = "cat_electrician",
            nameEn = "Electrician",
            nameTa = "மின் பணியாளர்",
            descriptionEn = "Wiring, MCB tripping, fan repair, inverter setup, light fittings",
            descriptionTa = "வயரிங், எம்சிபி சரிசெய்தல், மின்விசிறி மற்றும் இன்வெர்ட்டர் பராமரிப்பு",
            iconKey = "electrical_services",
            popularTasks = listOf("Short Circuit Fixing", "Ceiling Fan Installation", "Inverter Connection", "Switchboard Upgrade")
        ),
        ServiceCategoryItem(
            id = "cat_carpenter",
            key = "cat_carpenter",
            nameEn = "Carpenter",
            nameTa = "தச்சர்",
            descriptionEn = "Door locks, modular kitchen repair, wardrobe hinges, custom wood work",
            descriptionTa = "கதவு பூட்டு, மட்டு சமையலறை பழுது, அலமாரி சரிசெய்தல்",
            iconKey = "carpenter",
            popularTasks = listOf("Main Door Lock Fitting", "Modular Shelf Alignment", "Hinges Replacement", "Window Frame Fix")
        ),
        ServiceCategoryItem(
            id = "cat_painter",
            key = "cat_painter",
            nameEn = "Painter",
            nameTa = "வண்ணம் பூசுபவர்",
            descriptionEn = "Interior wall painting, waterproofing, damp wall putty, exterior coat",
            descriptionTa = "உள்சுவர் வண்ணம் தீட்டுதல், நீர்க்கசிவு தடுப்பு, பட்டி பூச்சு",
            iconKey = "format_paint",
            popularTasks = listOf("Single Room Fresh Coat", "Waterproof Primer", "Texture Wall Touchup", "Ceiling Damp Treatment")
        ),
        ServiceCategoryItem(
            id = "cat_cleaner",
            key = "cat_cleaner",
            nameEn = "Cleaner",
            nameTa = "தூய்மை பணியாளர்",
            descriptionEn = "Deep home cleaning, kitchen degreasing, bathroom scrubbing",
            descriptionTa = "வீட்டு ஆழ்துப்புரவு, சமையலறை மற்றும் குளியலறை தூய்மை",
            iconKey = "cleaning_services",
            popularTasks = listOf("Deep Bathroom Scrub", "Kitchen Chimney & Tile Degreasing", "Full 2BHK Deep Clean", "Sofa Shampooing")
        ),
        ServiceCategoryItem(
            id = "cat_ac_tech",
            key = "cat_ac_tech",
            nameEn = "AC Technician",
            nameTa = "ஏசி தொழில்நுட்ப வல்லுநர்",
            descriptionEn = "Split AC water leakage, gas refill, foam jet servicing, installation",
            descriptionTa = "ஏசி வாயு நிரப்புதல், நீர் கசிவு சரிசெய்தல், முழு சேவை",
            iconKey = "ac_unit",
            popularTasks = listOf("Foam Jet Wet Service", "Gas Topup R32/R410A", "Indoor Unit Water Dripping", "Uninstallation & Shifting")
        ),
        ServiceCategoryItem(
            id = "cat_appliance",
            key = "cat_appliance",
            nameEn = "Appliance Repair",
            nameTa = "வீட்டு உபகரண பழுதுநீக்குபவர்",
            descriptionEn = "Washing machine motor, refrigerator cooling, microwave oven",
            descriptionTa = "சலவை இயந்திரம், குளிர்சாதன பெட்டி, மைக்ரோவேவ் பழுதுநீக்கம்",
            iconKey = "kitchen",
            popularTasks = listOf("Washing Machine Drain Block", "Fridge Cooling Coil Inspection", "Microwave Heating Fix", "RO Filter Replacement")
        ),
        ServiceCategoryItem(
            id = "cat_mechanic",
            key = "cat_mechanic",
            nameEn = "Mechanic",
            nameTa = "வாகன மெக்கானிக்",
            descriptionEn = "Two-wheeler breakdown, puncture, engine oil change, battery jumpstart",
            descriptionTa = "இருசக்கர வாகன பழுது, டயர் பஞ்சர், பேட்டரி சார்ஜிங்",
            iconKey = "two_wheeler",
            popularTasks = listOf("On-site Puncture Repair", "Doorstep Bike General Service", "Battery Jumpstart", "Brake Shoe Replacement")
        )
    )

    private val _workers = MutableStateFlow(seedWorkers())
    val workers: StateFlow<List<WorkerProfile>> = _workers.asStateFlow()

    private val _requests = MutableStateFlow(seedRequests())
    val requests: StateFlow<List<ServiceRequest>> = _requests.asStateFlow()

    private val _reviews = MutableStateFlow(seedReviews())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _verificationCases = MutableStateFlow(seedVerificationCases())
    val verificationCases: StateFlow<List<VerificationCase>> = _verificationCases.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setActiveWorker(workerId: String) {
        _activeWorkerId.value = workerId
    }

    fun toggleFavorite(workerId: String) {
        val current = _savedWorkerIds.value.toMutableSet()
        if (current.contains(workerId)) {
            current.remove(workerId)
        } else {
            current.add(workerId)
        }
        _savedWorkerIds.value = current
    }

    fun updateWorkerAvailability(workerId: String, status: AvailabilityStatus) {
        val timeNow = SimpleDateFormat("h:mm a, dd MMM", Locale.ENGLISH).format(Date())
        _workers.value = _workers.value.map { worker ->
            if (worker.id == workerId) {
                worker.copy(
                    availabilityStatus = status,
                    lastStatusUpdateTime = timeNow
                )
            } else worker
        }
    }

    fun createServiceRequest(
        workerId: String,
        description: String,
        locality: String,
        city: String,
        pinCode: String,
        preferredDate: String,
        preferredTimeSlot: String,
        urgency: RequestUrgency
    ): ServiceRequest {
        val worker = _workers.value.find { it.id == workerId }
        val workerName = worker?.displayName ?: "Service Worker"
        val categoryId = worker?.serviceCategoryId ?: "cat_plumber"
        val timeNow = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.ENGLISH).format(Date())

        val newRequest = ServiceRequest(
            id = "req_${System.currentTimeMillis() % 100000}",
            customerId = currentCustomerId,
            customerName = currentCustomerName,
            customerPhone = currentCustomerPhone,
            workerId = workerId,
            workerName = workerName,
            serviceCategoryId = categoryId,
            description = description,
            locality = locality,
            city = city,
            pinCode = pinCode,
            preferredDate = preferredDate,
            preferredTimeSlot = preferredTimeSlot,
            urgency = urgency,
            status = RequestStatus.SUBMITTED,
            formattedDate = timeNow,
            statusHistory = listOf(
                StatusHistoryItem(RequestStatus.SUBMITTED, timeNow, "Customer created enquiry"),
                StatusHistoryItem(RequestStatus.SENT_TO_WORKER, timeNow, "Enquiry routed to worker profile")
            )
        )

        _requests.value = listOf(newRequest) + _requests.value
        return newRequest
    }

    fun updateRequestStatus(requestId: String, newStatus: RequestStatus, note: String = "") {
        val timeNow = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.ENGLISH).format(Date())
        _requests.value = _requests.value.map { req ->
            if (req.id == requestId) {
                val updatedHistory = req.statusHistory + StatusHistoryItem(newStatus, timeNow, note)
                req.copy(status = newStatus, statusHistory = updatedHistory)
            } else req
        }
    }

    fun submitReview(requestId: String, workerId: String, rating: Int, comment: String) {
        val timeNow = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date())
        val newReview = Review(
            id = "rev_${System.currentTimeMillis() % 100000}",
            requestId = requestId,
            workerId = workerId,
            customerId = currentCustomerId,
            customerName = currentCustomerName,
            rating = rating,
            comment = comment,
            date = timeNow
        )

        _reviews.value = listOf(newReview) + _reviews.value

        // Recalculate average rating for worker
        val allWorkerReviews = _reviews.value.filter { it.workerId == workerId }
        val avg = if (allWorkerReviews.isNotEmpty()) {
            allWorkerReviews.map { it.rating }.average().toFloat()
        } else rating.toFloat()

        _workers.value = _workers.value.map { worker ->
            if (worker.id == workerId) {
                worker.copy(
                    rating = String.format(Locale.ENGLISH, "%.1f", avg).toFloat(),
                    reviewCount = worker.reviewCount + 1
                )
            } else worker
        }

        // Mark request as reviewed
        _requests.value = _requests.value.map { req ->
            if (req.id == requestId) req.copy(isReviewed = true) else req
        }
    }

    fun registerNewWorker(
        fullName: String,
        displayName: String,
        categoryId: String,
        skills: List<String>,
        bio: String,
        city: String,
        locality: String,
        pinCode: String,
        experienceYears: Int,
        visitCharge: Int,
        minLabourCharge: Int,
        phone: String,
        email: String,
        documentType: String,
        documentNumber: String,
        tradeCertificate: String
    ): String {
        val newId = "w_${System.currentTimeMillis() % 100000}"
        val timeNow = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date())

        val newWorker = WorkerProfile(
            id = newId,
            fullName = fullName,
            displayName = displayName.ifBlank { fullName },
            serviceCategoryId = categoryId,
            skills = skills,
            bio = bio,
            city = city,
            locality = locality,
            pinCode = pinCode,
            coverageRadiusKm = 10,
            experienceYears = experienceYears,
            rating = 5.0f,
            reviewCount = 0,
            visitCharge = visitCharge,
            minLabourCharge = minLabourCharge,
            availabilityStatus = AvailabilityStatus.AVAILABLE_TODAY,
            lastStatusUpdateTime = "Just registered",
            emergencyService = true,
            languagesSpoken = listOf("Tamil", "English"),
            verificationStatus = VerificationStatus.SUBMITTED,
            badges = setOf(VerificationBadge.PHONE_VERIFIED),
            phoneNumber = phone,
            email = email,
            portfolioJobs = listOf("Sample job assessment completed"),
            createdAt = System.currentTimeMillis()
        )

        val newCase = VerificationCase(
            id = "case_${newId}",
            workerId = newId,
            workerName = fullName,
            categoryName = categories.find { it.id == categoryId }?.nameEn ?: "Services",
            documentType = documentType,
            documentNumberMasked = maskDoc(documentNumber),
            tradeCertificateName = tradeCertificate.ifBlank { "National Skill Registry / ITI Trade Certificate" },
            submittedAt = timeNow,
            status = VerificationStatus.SUBMITTED,
            reviewerNotes = "Application received through online onboarding portal. Pending document verification.",
            auditTrail = listOf("Worker submitted application on $timeNow")
        )

        _workers.value = listOf(newWorker) + _workers.value
        _verificationCases.value = listOf(newCase) + _verificationCases.value
        _activeWorkerId.value = newId
        return newId
    }

    fun adminReviewWorker(caseId: String, newStatus: VerificationStatus, note: String) {
        val timeNow = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.ENGLISH).format(Date())
        var targetWorkerId: String? = null

        _verificationCases.value = _verificationCases.value.map { caseItem ->
            if (caseItem.id == caseId) {
                targetWorkerId = caseItem.workerId
                val actionDesc = when (newStatus) {
                    VerificationStatus.APPROVED -> "Approved & granted verified badges"
                    VerificationStatus.REJECTED -> "Rejected application ($note)"
                    VerificationStatus.UNDER_REVIEW -> "Marked under review"
                    VerificationStatus.SUSPENDED -> "Suspended account ($note)"
                    else -> "Updated status to $newStatus"
                }
                caseItem.copy(
                    status = newStatus,
                    reviewerNotes = note,
                    auditTrail = caseItem.auditTrail + "$actionDesc by Admin at $timeNow"
                )
            } else caseItem
        }

        targetWorkerId?.let { workerId ->
            _workers.value = _workers.value.map { worker ->
                if (worker.id == workerId) {
                    val updatedBadges = if (newStatus == VerificationStatus.APPROVED) {
                        worker.badges + setOf(VerificationBadge.GOVT_ID_REVIEWED, VerificationBadge.TRADE_CERTIFIED)
                    } else worker.badges

                    worker.copy(
                        verificationStatus = newStatus,
                        badges = updatedBadges
                    )
                } else worker
            }
        }
    }

    private fun maskDoc(docNum: String): String {
        val cleaned = docNum.trim()
        if (cleaned.length < 5) return "XXXX-XXXX-9842"
        val last4 = cleaned.takeLast(4)
        return "XXXX-XXXX-$last4"
    }

    // Realistic Tamil Nadu seed data with verified profiles and transparent charges
    private fun seedWorkers(): List<WorkerProfile> {
        return listOf(
            WorkerProfile(
                id = "w_001",
                fullName = "R. Selvakumar",
                displayName = "Selvakumar Plumbing Works",
                serviceCategoryId = "cat_plumber",
                skills = listOf("Concealed Pipe Leaks", "CPVC & PVC Line", "Overhead Tank Float Valve", "Bathroom Renovation"),
                bio = "12+ years of licensed plumbing experience in Chennai. Specialized in residential apartments, rapid concealed pipe seepage detection, and solar water heater connections.",
                city = "Chennai",
                locality = "T. Nagar",
                pinCode = "600017",
                coverageRadiusKm = 12,
                experienceYears = 12,
                rating = 4.9f,
                reviewCount = 54,
                visitCharge = 200,
                minLabourCharge = 350,
                taskEstimates = mapOf(
                    "Tap Spindle Replacement" to 150,
                    "Washbasin Drain Siphon Fix" to 250,
                    "Concealed Wall Pipe Seepage Diagnosis" to 400,
                    "Overhead Tank Automatic Float Valve" to 550
                ),
                availabilityStatus = AvailabilityStatus.AVAILABLE_NOW,
                lastStatusUpdateTime = "5 mins ago",
                emergencyService = true,
                languagesSpoken = listOf("Tamil", "English"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED, VerificationBadge.TRADE_CERTIFIED),
                phoneNumber = "+91 98412 88471",
                email = "selva.plumbing.tn@workerconnect.in",
                portfolioJobs = listOf("100+ apartment plumbing completed in T. Nagar & Kodambakkam")
            ),
            WorkerProfile(
                id = "w_002",
                fullName = "M. Anbazhagan",
                displayName = "Anbu Electricals & Inverters",
                serviceCategoryId = "cat_electrician",
                skills = listOf("Main Distribution Board", "MCB Tripping Fix", "Inverter Dual Battery Setup", "LED Architectural Wiring"),
                bio = "Govt Licensed 'B' Grade Wireman. 10 years serving homeowners and commercial shops in Anna Nagar and Kilpauk. Dedicated to zero-hazard electrical safety.",
                city = "Chennai",
                locality = "Anna Nagar",
                pinCode = "600040",
                coverageRadiusKm = 15,
                experienceYears = 10,
                rating = 4.8f,
                reviewCount = 42,
                visitCharge = 200,
                minLabourCharge = 300,
                taskEstimates = mapOf(
                    "Switchboard Modular Replacement" to 200,
                    "Ceiling Fan Assembly & Hooking" to 250,
                    "Inverter Battery Wiring & Earth Check" to 600,
                    "Complete House Short-Circuit Diagnosis" to 500
                ),
                availabilityStatus = AvailabilityStatus.AVAILABLE_TODAY,
                lastStatusUpdateTime = "30 mins ago",
                emergencyService = true,
                languagesSpoken = listOf("Tamil", "English"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED, VerificationBadge.TRADE_CERTIFIED),
                phoneNumber = "+91 97908 44123",
                email = "anbu.electricals@workerconnect.in"
            ),
            WorkerProfile(
                id = "w_003",
                fullName = "P. Manikandan",
                displayName = "Mani AC Pro Care",
                serviceCategoryId = "cat_ac_tech",
                skills = listOf("Foam Jet Jetwash", "Copper Pipe Brazing", "Inverter PCB Diagnostic", "R32/R410A Gas Topup"),
                bio = "Certified HVAC technician with expertise in Daikin, Voltas, Carrier, and LG split systems. Offers jet spray cleaning with zero mess guarantee.",
                city = "Coimbatore",
                locality = "RS Puram",
                pinCode = "641002",
                coverageRadiusKm = 18,
                experienceYears = 8,
                rating = 4.9f,
                reviewCount = 38,
                visitCharge = 250,
                minLabourCharge = 450,
                taskEstimates = mapOf(
                    "Foam Jet Chemical Master Service" to 599,
                    "Indoor Blower Leakage Correction" to 400,
                    "Inverter Compressor Capacitor Replacement" to 750,
                    "AC Relocation & Re-installation" to 1400
                ),
                availabilityStatus = AvailabilityStatus.AVAILABLE_NOW,
                lastStatusUpdateTime = "12 mins ago",
                emergencyService = true,
                languagesSpoken = listOf("Tamil", "English"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED, VerificationBadge.TRADE_CERTIFIED),
                phoneNumber = "+91 94432 99821",
                email = "mani.ac.cbe@workerconnect.in"
            ),
            WorkerProfile(
                id = "w_004",
                fullName = "S. Murugan",
                displayName = "Murugan Woodworks & Modular",
                serviceCategoryId = "cat_carpenter",
                skills = listOf("Godrej Lock Installation", "Modular Drawer Channels", "Teak Door Repair", "Wardrobe Sliding Roller"),
                bio = "15 years master carpenter from Madurai. Expert in all wooden repairs, hydraulic hinges, sofa frame restoration, and custom shelf alterations.",
                city = "Madurai",
                locality = "KK Nagar",
                pinCode = "625020",
                coverageRadiusKm = 10,
                experienceYears = 15,
                rating = 4.7f,
                reviewCount = 29,
                visitCharge = 200,
                minLabourCharge = 400,
                taskEstimates = mapOf(
                    "Ultra-Lock Installation on Teak Door" to 450,
                    "Hydraulic Cabinet Hinge Pair Replacement" to 300,
                    "Sliding Wardrobe Track Realignment" to 500,
                    "Dining Chair Joint Glue & Reinforce" to 250
                ),
                availabilityStatus = AvailabilityStatus.BUSY,
                lastStatusUpdateTime = "1 hour ago",
                emergencyService = false,
                languagesSpoken = listOf("Tamil"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED),
                phoneNumber = "+91 94862 11094",
                email = "murugan.carpentry.mdu@workerconnect.in"
            ),
            WorkerProfile(
                id = "w_005",
                fullName = "D. Saravanan",
                displayName = "Rainbow Home Painting Solutions",
                serviceCategoryId = "cat_painter",
                skills = listOf("Asian Paints Royal Emulsion", "Waterproof Wall Putty", "Texture Coating", "Exterior Weathercoat"),
                bio = "Professional wall painting contractor in Salem. Provides honest material estimates, spotless masking tape protection for furniture, and smooth roller finishes.",
                city = "Salem",
                locality = "Hasthampatti",
                pinCode = "636007",
                coverageRadiusKm = 20,
                experienceYears = 9,
                rating = 4.8f,
                reviewCount = 31,
                visitCharge = 150,
                minLabourCharge = 500,
                taskEstimates = mapOf(
                    "Single Bedroom Touchup Coat" to 1200,
                    "Balcony Ceiling Damp Sealing & Primer" to 650,
                    "Wall Putty Sanding & Crack Filling" to 450
                ),
                availabilityStatus = AvailabilityStatus.AVAILABLE_TODAY,
                lastStatusUpdateTime = "45 mins ago",
                emergencyService = false,
                languagesSpoken = listOf("Tamil", "English"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED, VerificationBadge.TRADE_CERTIFIED),
                phoneNumber = "+91 98941 77334",
                email = "saravanan.painter.slm@workerconnect.in"
            ),
            WorkerProfile(
                id = "w_006",
                fullName = "K. Vijayakumar",
                displayName = "Vijayan Doorstep Auto Mechanic",
                serviceCategoryId = "cat_mechanic",
                skills = listOf("Doorstep Bike Service", "Tubeless Puncture Fix", "Engine Oil Flushing", "Battery Health Diagnostic"),
                bio = "Quick doorstep breakdown assistance for all 100cc to 350cc bikes and scooters in Trichy. Carries mobile tools and standard consumable parts.",
                city = "Tiruchirappalli",
                locality = "Thillai Nagar",
                pinCode = "620018",
                coverageRadiusKm = 15,
                experienceYears = 7,
                rating = 4.9f,
                reviewCount = 47,
                visitCharge = 150,
                minLabourCharge = 250,
                taskEstimates = mapOf(
                    "Doorstep Tubeless Puncture Fix" to 150,
                    "General Periodic Bike Inspection" to 350,
                    "Brake Cable & Shoe Adjustment" to 200,
                    "Battery Charging & Terminal Cleaning" to 250
                ),
                availabilityStatus = AvailabilityStatus.AVAILABLE_NOW,
                lastStatusUpdateTime = "3 mins ago",
                emergencyService = true,
                languagesSpoken = listOf("Tamil", "English"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED, VerificationBadge.TRADE_CERTIFIED),
                phoneNumber = "+91 93612 00192",
                email = "vijayan.mechanic.trichy@workerconnect.in"
            ),
            WorkerProfile(
                id = "w_007",
                fullName = "Mrs. Lakshmi Priya",
                displayName = "Priya Sparkle Home Deep Cleaning",
                serviceCategoryId = "cat_cleaner",
                skills = listOf("Kitchen Tile Degreasing", "Bathroom Acid-free Descaling", "Full House Sanitation", "Window Track Scrubbing"),
                bio = "Specialized women-led residential deep cleaning team in Chennai Velachery & OMR. We use professional scrubbing machines and eco-friendly descaling liquids.",
                city = "Chennai",
                locality = "Velachery",
                pinCode = "600042",
                coverageRadiusKm = 14,
                experienceYears = 6,
                rating = 4.9f,
                reviewCount = 63,
                visitCharge = 200,
                minLabourCharge = 600,
                taskEstimates = mapOf(
                    "Master Bathroom Acid-Free Descaling" to 650,
                    "Modular Kitchen Chimney & Rack Degreasing" to 950,
                    "Full 2BHK Floor Scrubbing & Dusting" to 1800
                ),
                availabilityStatus = AvailabilityStatus.AVAILABLE_NOW,
                lastStatusUpdateTime = "Just now",
                emergencyService = false,
                languagesSpoken = listOf("Tamil", "English"),
                verificationStatus = VerificationStatus.APPROVED,
                badges = setOf(VerificationBadge.PHONE_VERIFIED, VerificationBadge.GOVT_ID_REVIEWED),
                phoneNumber = "+91 98409 66781",
                email = "priya.sparkleclean@workerconnect.in"
            )
        )
    }

    private fun seedRequests(): List<ServiceRequest> {
        return listOf(
            ServiceRequest(
                id = "req_101",
                customerId = currentCustomerId,
                customerName = currentCustomerName,
                customerPhone = currentCustomerPhone,
                workerId = "w_001",
                workerName = "Selvakumar Plumbing Works",
                serviceCategoryId = "cat_plumber",
                description = "Kitchen sink drain pipe is leaking continuously into the under-counter cabinet. Need urgent replacement of the PVC siphon.",
                locality = "T. Nagar (Near Panagal Park)",
                city = "Chennai",
                pinCode = "600017",
                preferredDate = "Today",
                preferredTimeSlot = "2:00 PM - 4:00 PM",
                urgency = RequestUrgency.SAME_DAY,
                status = RequestStatus.ACCEPTED,
                formattedDate = "Today, 10:15 AM",
                statusHistory = listOf(
                    StatusHistoryItem(RequestStatus.SUBMITTED, "Today, 10:15 AM", "Enquiry posted by customer"),
                    StatusHistoryItem(RequestStatus.SENT_TO_WORKER, "Today, 10:16 AM", "Direct SMS/Push dispatched to Selvakumar"),
                    StatusHistoryItem(RequestStatus.ACCEPTED, "Today, 10:22 AM", "Worker accepted job and confirmed arrival by 2:30 PM")
                ),
                isReviewed = false
            ),
            ServiceRequest(
                id = "req_102",
                customerId = currentCustomerId,
                customerName = currentCustomerName,
                customerPhone = currentCustomerPhone,
                workerId = "w_002",
                workerName = "Anbu Electricals & Inverters",
                serviceCategoryId = "cat_electrician",
                description = "Hall AC power socket sparking and tripped the main MCB. Need socket upgrade to 25A heavy duty.",
                locality = "Anna Nagar East",
                city = "Chennai",
                pinCode = "600102",
                preferredDate = "Yesterday",
                preferredTimeSlot = "Morning",
                urgency = RequestUrgency.NORMAL,
                status = RequestStatus.COMPLETED,
                formattedDate = "Yesterday, 3:30 PM",
                statusHistory = listOf(
                    StatusHistoryItem(RequestStatus.SUBMITTED, "Yesterday, 9:00 AM", "Enquiry created"),
                    StatusHistoryItem(RequestStatus.ACCEPTED, "Yesterday, 9:20 AM", "Accepted"),
                    StatusHistoryItem(RequestStatus.IN_PROGRESS, "Yesterday, 11:00 AM", "Anbazhagan reached site"),
                    StatusHistoryItem(RequestStatus.COMPLETED, "Yesterday, 12:15 PM", "Installed heavy duty modular box. Tested safely.")
                ),
                isReviewed = true
            )
        )
    }

    private fun seedReviews(): List<Review> {
        return listOf(
            Review(
                id = "rev_01",
                requestId = "req_102",
                workerId = "w_001",
                customerId = "cust_002",
                customerName = "Dinesh Kumar (Anna Nagar)",
                rating = 5,
                comment = "Selvakumar came within 40 minutes of confirmation. Explained the leak issue clearly before touching the pipe. Very honest pricing — charged only ₹350 for labor as listed.",
                date = "14 Oct 2026"
            ),
            Review(
                id = "rev_02",
                requestId = "req_103",
                workerId = "w_001",
                customerId = "cust_003",
                customerName = "Meenakshi Sundaram (T. Nagar)",
                rating = 5,
                comment = "Excellent plumbing craftsmanship. Cleaned up all water debris after fixing the float valve. Highly recommended local professional.",
                date = "08 Oct 2026"
            ),
            Review(
                id = "rev_03",
                requestId = "req_104",
                workerId = "w_002",
                customerId = "cust_004",
                customerName = "Gnanavel R. (Kilpauk)",
                rating = 5,
                comment = "Anbu is very knowledgeable with MCB line balancing. Saved us from a costly rewiring scam by someone else. True gentleman.",
                date = "11 Oct 2026"
            ),
            Review(
                id = "rev_04",
                requestId = "req_105",
                workerId = "w_003",
                customerId = "cust_005",
                customerName = "Senthil Nathan (RS Puram, Coimbatore)",
                rating = 5,
                comment = "Best AC jet service in Coimbatore. Air flow is now super chill and odorless. Thoroughly tested refrigerant pressures.",
                date = "15 Oct 2026"
            )
        )
    }

    private fun seedVerificationCases(): List<VerificationCase> {
        return listOf(
            VerificationCase(
                id = "case_w_008",
                workerId = "w_008",
                workerName = "V. Natarajan",
                categoryName = "Electrician",
                documentType = "Aadhaar / Voter ID + Govt Wireman License",
                documentNumberMasked = "XXXX-XXXX-6712",
                tradeCertificateName = "Tamil Nadu Electrical Licensing Board 'B' Permit #TN/ELB/44910",
                submittedAt = "17 Oct 2026",
                status = VerificationStatus.SUBMITTED,
                reviewerNotes = "Uploaded high-resolution Wireman license copy. Address matches Salem residential jurisdiction.",
                auditTrail = listOf(
                    "Submitted on 17 Oct 2026 via Mobile Onboarding",
                    "Automated phone OTP verification succeeded"
                )
            ),
            VerificationCase(
                id = "case_w_009",
                workerId = "w_009",
                workerName = "K. Balamurugan",
                categoryName = "Plumber",
                documentType = "Govt Aadhaar ID",
                documentNumberMasked = "XXXX-XXXX-8901",
                tradeCertificateName = "ITI Plumber Trade Certificate (Govt ITI Guindy)",
                submittedAt = "16 Oct 2026",
                status = VerificationStatus.UNDER_REVIEW,
                reviewerNotes = "Operations officer checked trade certificate with Guindy database. Phone interview scheduled.",
                auditTrail = listOf(
                    "Submitted on 16 Oct 2026",
                    "Moved to Under Review by Admin on 17 Oct 2026"
                )
            )
        )
    }
}
