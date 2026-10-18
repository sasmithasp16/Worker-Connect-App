package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val shortLabel: String) {
    ENGLISH("en", "English", "EN"),
    TAMIL("ta", "தமிழ்", "தமிழ்");

    fun toggle(): AppLanguage = if (this == ENGLISH) TAMIL else ENGLISH
}

object LocalizedStrings {
    fun get(key: String, lang: AppLanguage): String {
        val entry = strings[key] ?: return key
        return if (lang == AppLanguage.TAMIL) entry.second else entry.first
    }

    private val strings = mapOf(
        // App Identity & Header
        "app_name" to Pair("Worker Connect", "ஒர்க்கர் கனெக்ட்"),
        "tagline" to Pair("Find Trusted Local Workers Near You", "உங்கள் அருகில் உள்ள நம்பகமான தொழிலாளர்களைக் கண்டறியுங்கள்"),
        "sub_tagline" to Pair("Plumbers, Electricians, Carpenters & Technicians in Tamil Nadu", "தமிழ்நாட்டின் சிறந்த பிளம்பர்கள், எலக்ட்ரீஷியன்கள், தச்சர்கள் மற்றும் தொழில்நுட்ப வல்லுநர்கள்"),
        
        // Navigation
        "nav_home" to Pair("Home", "முகப்பு"),
        "nav_explore" to Pair("Find Workers", "தொழிலாளர்கள்"),
        "nav_requests" to Pair("My Enquiries", "என் கோரிக்கைகள்"),
        "nav_worker_hub" to Pair("Worker Hub", "பணியாளர் மையம்"),
        "nav_admin" to Pair("Admin Queue", "நிர்வாக மையம்"),
        "nav_aws_guide" to Pair("AWS Cloud Blueprint", "AWS கட்டமைப்பு"),

        // Roles
        "role_customer" to Pair("Customer", "வாடிக்கையாளர்"),
        "role_worker" to Pair("Service Worker", "தொழிலாளி"),
        "role_admin" to Pair("Administrator", "நிர்வாகி"),
        "switch_role" to Pair("Switch Mode", "பயனர் முறை மாற்றம்"),

        // Service Categories
        "cat_all" to Pair("All Services", "அனைத்து சேவைகள்"),
        "cat_plumber" to Pair("Plumber", "குழாய் பழுதுபார்ப்பவர்"),
        "cat_electrician" to Pair("Electrician", "மின் பணியாளர்"),
        "cat_carpenter" to Pair("Carpenter", "தச்சர்"),
        "cat_painter" to Pair("Painter", "வண்ணம் பூசுபவர்"),
        "cat_cleaner" to Pair("Cleaner", "தூய்மை பணியாளர்"),
        "cat_ac_tech" to Pair("AC Technician", "ஏசி தொழில்நுட்ப வல்லுநர்"),
        "cat_appliance" to Pair("Appliance Repair", "வீட்டு உபகரண பழுதுநீக்குபவர்"),
        "cat_mechanic" to Pair("Mechanic", "வாகன மெக்கானிக்"),

        // Search & Filters
        "search_hint" to Pair("Search service, skill, locality (e.g. T. Nagar, Anna Nagar)...", "சேவை, பகுதி அல்லது திறனைத் தேடுங்கள்..."),
        "filter_city" to Pair("City", "நகரம்"),
        "filter_locality" to Pair("Locality", "பகுதி"),
        "filter_category" to Pair("Category", "பிரிவு"),
        "filter_availability" to Pair("Availability", "கிடைக்கும் நேரம்"),
        "filter_verified_only" to Pair("Approved & Verified Only", "சரிபார்க்கப்பட்டவர்கள் மட்டும்"),
        "filter_emergency" to Pair("Emergency / Same-Day", "அவசர சேவை"),
        "filter_min_price" to Pair("Max Inspection Fee", "அதிகபட்ச ஆய்வுக் கட்டணம்"),
        "filter_sort" to Pair("Sort By", "வரிசைப்படுத்து"),
        "sort_rating" to Pair("Highest Rating", "அதிக மதிப்பீடு"),
        "sort_price_low" to Pair("Lowest Visit Charge", "குறைந்த ஆய்வுக் கட்டணம்"),
        "sort_exp" to Pair("Experience (Years)", "அனுபவம்"),
        "clear_filters" to Pair("Clear Filters", "வடிகட்டிகளை நீக்கு"),
        "apply_filters" to Pair("Apply", "பயன்படுத்து"),

        // Pricing Transparency
        "pricing_notice" to Pair("Transparent Pricing in INR (₹)", "வெளிப்படையான கட்டண விவரம் (₹)"),
        "visit_charge" to Pair("Inspection / Visit Charge", "ஆய்வு / வருகைக் கட்டணம்"),
        "min_labour_charge" to Pair("Min Service / Labour Charge", "குறைந்தபட்ச பணி கட்டணம்"),
        "final_quote_note" to Pair("Materials & final charges agreed directly before work commences.", "பொருட்கள் மற்றும் இறுதி கட்டணம் பணி தொடங்கும் முன் வாடிக்கையாளரிடம் உறுதி செய்யப்படும்."),
        "indicative_pricing" to Pair("Task Pricing Estimates", "உத்தேச பணி கட்டணங்கள்"),

        // Availability Status
        "status_available_now" to Pair("Available Now", "இப்போது கிடைக்கிறார்"),
        "status_available_today" to Pair("Available Today", "இன்று கிடைக்கிறார்"),
        "status_busy" to Pair("Busy with Job", "பணியில் உள்ளார்"),
        "status_offline" to Pair("Offline / Shift Ended", "இணைப்பில் இல்லை"),
        "status_unknown" to Pair("Status Unverified", "நிலை உறுதி செய்யப்படவில்லை"),
        "last_updated" to Pair("Status updated", "நிலை புதுப்பிக்கப்பட்டது"),

        // Verification Badges
        "badge_phone" to Pair("Phone Verified", "தொலைபேசி சரிபார்க்கப்பட்டது"),
        "badge_kyc" to Pair("Govt ID Reviewed", "அரசு சான்று சரிபார்க்கப்பட்டது"),
        "badge_trade" to Pair("Trade Skill Certified", "தொழில் திறன் சான்றிதழ் பெற்றது"),
        "verification_explanation" to Pair("Badges are awarded only after strict manual verification of ID and trade licenses by Worker Connect operations team.", "ஒர்க்கர் கனெக்ட் குழுவினால் ஆவணங்கள் நேரில் சரிபார்க்கப்பட்ட பின்னரே இந்த பேட்ஜ் வழங்கப்படுகிறது."),

        // Worker Profile Page
        "btn_book_enquiry" to Pair("Send Direct Enquiry", "நேரடி சேவை கோரிக்கை அனுப்பு"),
        "btn_call_worker" to Pair("Call Contact", "அழைக்கவும்"),
        "btn_save_favorite" to Pair("Save to Favourites", "விருப்பப்பட்டியலில் சேர்"),
        "saved" to Pair("Saved", "சேமிக்கப்பட்டது"),
        "about_worker" to Pair("Professional Bio", "தொழில் விவரம்"),
        "service_radius" to Pair("Service Coverage Radius", "சேவை வழங்கும் தூரம்"),
        "languages_spoken" to Pair("Languages Spoken", "பேசும் மொழிகள்"),
        "experience" to Pair("Experience", "அனுபவம்"),
        "years" to Pair("years", "ஆண்டுகள்"),
        "reviews_heading" to Pair("Verified Customer Reviews", "சரிபார்க்கப்பட்ட வாடிக்கையாளர் விமர்சனங்கள்"),
        "no_reviews_yet" to Pair("No reviews yet. First jobs are underway.", "இன்னும் விமர்சனங்கள் இல்லை."),
        "verified_customer" to Pair("Verified Customer", "சரிபார்க்கப்பட்ட வாடிக்கையாளர்"),
        "report_worker" to Pair("Report Profile or Issue", "புகார் அளிக்கவும்"),

        // Service Request Modal / Booking
        "modal_book_title" to Pair("Book Service Enquiry", "சேவை கோரிக்கை பதிவு"),
        "booking_desc_hint" to Pair("Describe your issue (e.g. leaking tap in kitchen sink, circuit trip)...", "உங்கள் பிரச்சனை பற்றி விவரிக்கவும்..."),
        "booking_locality_hint" to Pair("Your locality / street address", "உங்கள் முகவரி / பகுதி"),
        "booking_pin_hint" to Pair("PIN Code (e.g. 600017)", "அஞ்சல் குறியீடு"),
        "booking_date_slot" to Pair("Preferred Date & Time Slot", "விருப்பமான தேதி மற்றும் நேரம்"),
        "booking_urgency" to Pair("Urgency", "முன்னுரிமை"),
        "urgency_normal" to Pair("Standard (Within 24-48 hrs)", "வழக்கமான (24-48 மணி நேரம்)"),
        "urgency_same_day" to Pair("Same Day Priority", "அதே நாள் விரைவு சேவை"),
        "urgency_emergency" to Pair("Urgent / Emergency", "அவசர சேவை"),
        "btn_submit_request" to Pair("Submit Enquiry", "கோரிக்கையை சமர்ப்பிக்கவும்"),
        "booking_success_msg" to Pair("Enquiry sent directly to worker. You can track status in My Enquiries.", "கோரிக்கை பணியாளருக்கு அனுப்பப்பட்டது! நிலையை எனது கோரிக்கைகளில் கண்காணிக்கலாம்."),

        // Request Statuses
        "req_submitted" to Pair("Enquiry Submitted", "சமர்ப்பிக்கப்பட்டது"),
        "req_sent_worker" to Pair("Sent to Worker", "பணியாளருக்கு அனுப்பப்பட்டது"),
        "req_accepted" to Pair("Accepted & Confirmed", "ஏற்றுக்கொள்ளப்பட்டது"),
        "req_declined" to Pair("Worker Busy / Declined", "நிராகரிக்கப்பட்டது"),
        "req_scheduled" to Pair("Scheduled for Visit", "வருகை திட்டமிடப்பட்டது"),
        "req_in_progress" to Pair("Work In Progress", "பணி நடக்கிறது"),
        "req_completed" to Pair("Completed", "நிறைவடைந்தது"),
        "req_cancelled" to Pair("Cancelled", "ரத்து செய்யப்பட்டது"),

        // Worker Registration Wizard
        "reg_wizard_title" to Pair("Join as a Professional Worker", "தொழிலாளியாக இணையுங்கள்"),
        "step_personal" to Pair("Personal Info", "தனிப்பட்ட விவரங்கள்"),
        "step_skills" to Pair("Trade & Skills", "தொழில் மற்றும் திறன்கள்"),
        "step_pricing" to Pair("Pricing & Location", "கட்டணம் மற்றும் பகுதி"),
        "step_kyc" to Pair("Verification & Consent", "சரிபார்ப்பு மற்றும் ஒப்புதல்"),
        "btn_next" to Pair("Continue Next Step", "அடுத்த படிக்குச் செல்"),
        "btn_back" to Pair("Back", "பின்செல்"),
        "btn_submit_application" to Pair("Submit Application for Review", "விண்ணப்பத்தை சமர்ப்பி"),
        "wizard_success_msg" to Pair("Application submitted! Admin verification takes 24 hours. Check Worker Hub for status.", "விண்ணப்பம் சமர்ப்பிக்கப்பட்டது! 24 மணி நேரத்திற்குள் சரிபார்க்கப்படும்."),

        // Worker Dashboard
        "worker_hub_title" to Pair("Worker Control Hub", "பணியாளர் கட்டுப்பாட்டு மையம்"),
        "worker_current_status" to Pair("Current Work Status", "தற்போதைய வேலை நிலை"),
        "worker_update_status" to Pair("Change Live Status", "நேரலை நிலையை மாற்று"),
        "worker_inbox" to Pair("Customer Enquiries", "வாடிக்கையாளர் கோரிக்கைகள்"),
        "btn_accept" to Pair("Accept Job", "ஏற்றுக்கொள்"),
        "btn_decline" to Pair("Decline", "நிராகரி"),
        "btn_mark_progress" to Pair("Start Work", "பணியைத் தொடங்கு"),
        "btn_mark_complete" to Pair("Mark Completed", "முடித்துவிட்டதாகக் குறி"),
        "worker_stats" to Pair("Performance & Views", "செயல்திறன் மற்றும் பார்வைகள்"),

        // Customer Dashboard
        "customer_dash_title" to Pair("Customer Activity & Bookings", "வாடிக்கையாளர் முன்பதிவுகள்"),
        "my_saved_workers" to Pair("My Favourite Workers", "என் விருப்பப்பட்டியல்"),
        "rate_and_review" to Pair("Write a Review", "விமர்சனம் எழுதுங்கள்"),
        "rating_prompt" to Pair("How was your service experience?", "உங்கள் சேவை அனுபவம் எப்படி இருந்தது?"),

        // Admin Dashboard
        "admin_title" to Pair("Admin Verification & Safety Queue", "நிர்வாக சரிபார்ப்பு வரிசை"),
        "admin_pending_workers" to Pair("Pending Worker Applications", "நிலுவையில் உள்ள விண்ணப்பங்கள்"),
        "admin_action_approve" to Pair("Approve & Award Badge", "ஒப்புதல் அளித்து பேட்ஜ் வழங்கு"),
        "admin_action_request_info" to Pair("Request More Info", "கூடுதல் விவரம் கோரு"),
        "admin_action_reject" to Pair("Reject Application", "விண்ணப்பத்தை நிராகரி"),
        "admin_action_suspend" to Pair("Suspend Account", "கணக்கை இடைநிறுத்து"),
        "admin_audit_log" to Pair("Administrative Audit Trail", "நிர்வாக தணிக்கை பதிவு"),

        // Trust & How it works
        "how_it_works_title" to Pair("How Worker Connect Works", "ஒர்க்கர் கனெக்ட் எப்படி செயல்படுகிறது?"),
        "how_step1" to Pair("1. Search & Compare", "1. தேடி ஒப்பீடு செய்யுங்கள்"),
        "how_step1_desc" to Pair("Browse nearby verified pros with real ratings and upfront inspection fees.", "உண்மை மதிப்பீடுகள் மற்றும் தெளிவான கட்டணங்களுடன் கூடிய தொழிலாளர்களைக் கண்டறியுங்கள்."),
        "how_step2" to Pair("2. Send Direct Enquiry", "2. நேரடி கோரிக்கை அனுப்புங்கள்"),
        "how_step2_desc" to Pair("Share problem details and preferred time. Worker receives notification instantly.", "உங்கள் பிரச்சனை மற்றும் நேரத்தை பகிருங்கள். தொழிலாளி உடனடியாக தகவல் பெறுவார்."),
        "how_step3" to Pair("3. Inspect & Agree Price", "3. ஆய்வு செய்து பணி தொடங்குங்கள்"),
        "how_step3_desc" to Pair("Worker arrives, diagnoses the issue, agrees final cost with you, and completes work.", "தொழிலாளி ஆய்வு செய்து, நியாயமான விலையை பேசி பணியை முடிப்பார்."),
        "how_step4" to Pair("4. Review & Build Trust", "4. விமர்சனம் செய்து நம்பிக்கையை வளருங்கள்"),
        "how_step4_desc" to Pair("Rate your experience to help local workers build their independent trade livelihood.", "மதிப்பீடு செய்து உள்ளூர் தொழிலாளர்களின் வாழ்வாதாரத்திற்கு உதவுங்கள்.")
    )
}
