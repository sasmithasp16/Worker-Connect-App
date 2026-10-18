package com.example

import com.example.data.WorkerConnectRepository
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: WorkerConnectRepository

    @Before
    fun setup() {
        repository = WorkerConnectRepository()
    }

    @Test
    fun testInitialSeedDataAndCategories() {
        val categories = repository.categories
        assertTrue("Categories should not be empty", categories.isNotEmpty())
        assertEquals("Plumber", categories.first().nameEn)

        val workers = repository.workers.value
        assertTrue("Initial workers should be present", workers.size >= 5)
        assertTrue(workers.any { it.city == "Chennai" })
        assertTrue(workers.any { it.city == "Coimbatore" })
    }

    @Test
    fun testLanguageToggleAndLocalization() {
        assertEquals("Worker Connect", LocalizedStrings.get("app_name", AppLanguage.ENGLISH))
        assertEquals("ஒர்க்கர் கனெக்ட்", LocalizedStrings.get("app_name", AppLanguage.TAMIL))

        repository.setLanguage(AppLanguage.TAMIL)
        assertEquals(AppLanguage.TAMIL, repository.currentLanguage.value)
    }

    @Test
    fun testServiceRequestLifecycle() {
        val newReq = repository.createServiceRequest(
            workerId = "w_001",
            description = "Kitchen tap leak repair",
            locality = "T. Nagar",
            city = "Chennai",
            pinCode = "600017",
            preferredDate = "Today",
            preferredTimeSlot = "Morning",
            urgency = RequestUrgency.SAME_DAY
        )

        assertNotNull(newReq)
        assertEquals(RequestStatus.SUBMITTED, newReq.status)

        // Worker accepts job
        repository.updateRequestStatus(newReq.id, RequestStatus.ACCEPTED, "Accepted by Selvakumar")
        val acceptedReq = repository.requests.value.find { it.id == newReq.id }
        assertEquals(RequestStatus.ACCEPTED, acceptedReq?.status)

        // Worker marks in progress and completed
        repository.updateRequestStatus(newReq.id, RequestStatus.IN_PROGRESS, "Started work")
        repository.updateRequestStatus(newReq.id, RequestStatus.COMPLETED, "Completed repair")

        val completedReq = repository.requests.value.find { it.id == newReq.id }
        assertEquals(RequestStatus.COMPLETED, completedReq?.status)
    }

    @Test
    fun testVerifiedReviewAndDynamicRatingCalculation() {
        val workerId = "w_001"
        val initialWorker = repository.workers.value.find { it.id == workerId }!!
        val initialCount = initialWorker.reviewCount

        repository.submitReview(
            requestId = "req_101",
            workerId = workerId,
            rating = 5,
            comment = "Outstanding and clean work"
        )

        val updatedWorker = repository.workers.value.find { it.id == workerId }!!
        assertEquals(initialCount + 1, updatedWorker.reviewCount)
    }

    @Test
    fun testWorkerRegistrationAndAdminApprovalWorkflow() {
        val newWorkerId = repository.registerNewWorker(
            fullName = "A. Murugesan",
            displayName = "Murugesan Plumbing",
            categoryId = "cat_plumber",
            skills = listOf("Pipe Welding", "Tank Cleaning"),
            bio = "Master plumber in Coimbatore",
            city = "Coimbatore",
            locality = "Gandhipuram",
            pinCode = "641012",
            experienceYears = 10,
            visitCharge = 200,
            minLabourCharge = 400,
            phone = "+91 94430 11223",
            email = "murugesan@test.com",
            documentType = "Aadhaar Card",
            documentNumber = "XXXX-XXXX-4421",
            tradeCertificate = "ITI Plumber Trade Certificate"
        )

        val registeredWorker = repository.workers.value.find { it.id == newWorkerId }
        assertNotNull(registeredWorker)
        assertEquals(VerificationStatus.SUBMITTED, registeredWorker?.verificationStatus)

        // Admin reviews and approves
        val caseItem = repository.verificationCases.value.find { it.workerId == newWorkerId }
        assertNotNull(caseItem)

        repository.adminReviewWorker(caseItem!!.id, VerificationStatus.APPROVED, "Verified with ITI registry")

        val approvedWorker = repository.workers.value.find { it.id == newWorkerId }
        assertEquals(VerificationStatus.APPROVED, approvedWorker?.verificationStatus)
        assertTrue(approvedWorker?.badges?.contains(VerificationBadge.TRADE_CERTIFIED) == true)
    }
}
