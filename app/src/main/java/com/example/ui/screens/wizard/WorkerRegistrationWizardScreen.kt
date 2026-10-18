package com.example.ui.screens.wizard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerRegistrationWizardScreen(
    viewModel: WorkerConnectViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        if (viewModel.wizardStep.value > 1) {
            viewModel.wizardStep.value -= 1
        } else {
            viewModel.navigateBack()
        }
    }

    val lang by viewModel.currentLanguage.collectAsState()
    val step by viewModel.wizardStep.collectAsState()

    val fullName by viewModel.wizardFullName.collectAsState()
    val displayName by viewModel.wizardDisplayName.collectAsState()
    val categoryId by viewModel.wizardCategory.collectAsState()
    val skills by viewModel.wizardSkills.collectAsState()
    val bio by viewModel.wizardBio.collectAsState()
    val city by viewModel.wizardCity.collectAsState()
    val locality by viewModel.wizardLocality.collectAsState()
    val pinCode by viewModel.wizardPin.collectAsState()
    val exp by viewModel.wizardExperience.collectAsState()
    val visitCharge by viewModel.wizardVisitCharge.collectAsState()
    val labourCharge by viewModel.wizardLabourCharge.collectAsState()
    val phone by viewModel.wizardPhone.collectAsState()
    val email by viewModel.wizardEmail.collectAsState()
    val docType by viewModel.wizardDocType.collectAsState()
    val docNumber by viewModel.wizardDocNumber.collectAsState()
    val tradeCert by viewModel.wizardTradeCertificate.collectAsState()
    val consent by viewModel.wizardConsent.collectAsState()

    val cities = listOf("Chennai", "Coimbatore", "Madurai", "Salem", "Tiruchirappalli", "Tirunelveli", "Vellore")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LocalizedStrings.get("reg_wizard_title", lang), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (step > 1) viewModel.wizardStep.value -= 1 else viewModel.navigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Progress Bar (Steps 1 to 4)
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (step) {
                                1 -> "Step 1: ${LocalizedStrings.get("step_personal", lang)}"
                                2 -> "Step 2: ${LocalizedStrings.get("step_skills", lang)}"
                                3 -> "Step 3: ${LocalizedStrings.get("step_pricing", lang)}"
                                else -> "Step 4: ${LocalizedStrings.get("step_kyc", lang)}"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryTeal
                        )
                        Text(text = "$step of 4", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { step / 4f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = PrimaryTeal,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                when (step) {
                    1 -> {
                        // STEP 1: Personal Info
                        item {
                            Text("Full Legal Name *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { viewModel.wizardFullName.value = it },
                                placeholder = { Text("e.g. S. Ramachandran") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("wizard_fullname_input"),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Public Display Name (Trade / Shop)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = displayName,
                                onValueChange = { viewModel.wizardDisplayName.value = it },
                                placeholder = { Text("e.g. Ramu Electricals & Wiring") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Mobile Number for OTP Verification *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { viewModel.wizardPhone.value = it },
                                placeholder = { Text("+91 98400 12345") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("wizard_phone_input"),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Email Address (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { viewModel.wizardEmail.value = it },
                                placeholder = { Text("worker@example.com") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    if (fullName.isBlank()) {
                                        viewModel.showMessage("Please enter your legal name")
                                    } else {
                                        viewModel.wizardStep.value = 2
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("wizard_step1_next"),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(LocalizedStrings.get("btn_next", lang), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    2 -> {
                        // STEP 2: Trade & Skills
                        item {
                            Text("Primary Service Category *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                viewModel.allCategories.chunked(2).forEach { rowCats ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowCats.forEach { cat ->
                                            val isSel = categoryId == cat.id
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isSel) TealContainer else MaterialTheme.colorScheme.surface,
                                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) PrimaryTeal else MaterialTheme.colorScheme.outline),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { viewModel.wizardCategory.value = cat.id }
                                            ) {
                                                Text(
                                                    text = if (lang == AppLanguage.TAMIL) cat.nameTa else cat.nameEn,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSel) TealDark else MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text("Key Skills & Specialties (Comma separated) *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = skills,
                                onValueChange = { viewModel.wizardSkills.value = it },
                                placeholder = { Text("e.g. Inverter Wiring, MCB Tripping, Ceiling Fan, Lighting") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Years of Professional Experience", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = exp,
                                onValueChange = { viewModel.wizardExperience.value = it },
                                placeholder = { Text("e.g. 8") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Professional Bio / Description", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = bio,
                                onValueChange = { viewModel.wizardBio.value = it },
                                placeholder = { Text("Briefly introduce your experience, quality standards, and customer warranty...") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.wizardStep.value = 1 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(LocalizedStrings.get("btn_back", lang))
                                }
                                Button(
                                    onClick = { viewModel.wizardStep.value = 3 },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("wizard_step2_next"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(LocalizedStrings.get("btn_next", lang), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    3 -> {
                        // STEP 3: Location & Pricing
                        item {
                            Text("City in Tamil Nadu *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                cities.forEach { c ->
                                    val isSel = city == c
                                    FilterChip(
                                        selected = isSel,
                                        onClick = { viewModel.wizardCity.value = c },
                                        label = { Text(c, fontSize = 12.sp) }
                                    )
                                }
                            }
                        }

                        item {
                            Text("Locality / Base Area *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = locality,
                                onValueChange = { viewModel.wizardLocality.value = it },
                                placeholder = { Text("e.g. T. Nagar / Gandhipuram / KK Nagar") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("PIN Code *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = pinCode,
                                onValueChange = { viewModel.wizardPin.value = it },
                                placeholder = { Text("e.g. 600017") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Standard Visit / Inspection Charge (INR ₹) *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = visitCharge,
                                onValueChange = { viewModel.wizardVisitCharge.value = it },
                                placeholder = { Text("200") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Text(
                                text = "This covers your travel and diagnostic fee if customer does not proceed with repair.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        item {
                            Text("Minimum Base Labour Charge (INR ₹) *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = labourCharge,
                                onValueChange = { viewModel.wizardLabourCharge.value = it },
                                placeholder = { Text("350") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.wizardStep.value = 2 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(LocalizedStrings.get("btn_back", lang))
                                }
                                Button(
                                    onClick = { viewModel.wizardStep.value = 4 },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .testTag("wizard_step3_next"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(LocalizedStrings.get("btn_next", lang), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    4 -> {
                        // STEP 4: Verification & Consent
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = DeepNavyDark)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lock, null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Identity Security Notice", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Your Government ID and documents are stored privately in encrypted AWS S3 storage. They are never published or visible to customers.",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        item {
                            Text("Government ID Document Type *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = docType,
                                onValueChange = { viewModel.wizardDocType.value = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Document ID Number *", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = docNumber,
                                onValueChange = { viewModel.wizardDocNumber.value = it },
                                placeholder = { Text("e.g. XXXX-XXXX-9842") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Text("Trade Qualification / License (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = tradeCert,
                                onValueChange = { viewModel.wizardTradeCertificate.value = it },
                                placeholder = { Text("e.g. Wireman Permit 'B' / ITI Plumber Certificate") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = consent,
                                    onCheckedChange = { viewModel.wizardConsent.value = it }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "I consent to publish my selected professional trade profile, pricing, and phone number for customer contact on Worker Connect.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.wizardStep.value = 3 },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(LocalizedStrings.get("btn_back", lang))
                                }
                                Button(
                                    onClick = { viewModel.submitRegistrationWizard() },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                    modifier = Modifier
                                        .weight(2f)
                                        .testTag("submit_worker_wizard_btn"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(LocalizedStrings.get("btn_submit_application", lang), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
