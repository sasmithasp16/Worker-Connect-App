package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.model.AvailabilityStatus
import com.example.model.SortOption
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.components.WorkerCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WorkerConnectViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val workers by viewModel.filteredWorkers.collectAsState()
    val savedWorkerIds by viewModel.savedWorkerIds.collectAsState()

    val cities = listOf("All Tamil Nadu", "Chennai", "Coimbatore", "Madurai", "Salem", "Tiruchirappalli")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Banner: Deep Navy & Teal Gradient
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(DeepNavy, DeepNavyLight)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryTeal.copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryTeal)
                        ) {
                            Text(
                                text = "Tamil Nadu's Verified Professional Network",
                                color = TealLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = LocalizedStrings.get("tagline", lang),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = LocalizedStrings.get("sub_tagline", lang),
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Search Input Box
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        shadowElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = DeepNavy,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TextField(
                                value = filterState.query,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                placeholder = {
                                    Text(
                                        text = LocalizedStrings.get("search_hint", lang),
                                        fontSize = 13.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = TextPrimaryLight,
                                    unfocusedTextColor = TextPrimaryLight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("search_workers_input"),
                                singleLine = true
                            )
                            if (filterState.query.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.updateSearchQuery("") },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // City Selector Horizontal Pills
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = LocalizedStrings.get("filter_city", lang),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(cities) { city ->
                        val isSelected = filterState.city.equals(city, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) DeepNavy else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) DeepNavy else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .clickable { viewModel.selectCityFilter(city) }
                                .testTag("city_filter_$city")
                        ) {
                            Text(
                                text = city,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Service Categories Grid/Row
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LocalizedStrings.get("cat_all", lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (filterState.categoryId != null) {
                        TextButton(
                            onClick = { viewModel.selectCategoryFilter(null) }
                        ) {
                            Text(
                                text = LocalizedStrings.get("clear_filters", lang),
                                fontSize = 12.sp,
                                color = PrimaryTeal
                            )
                        }
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(viewModel.allCategories) { cat ->
                        val isSelected = filterState.categoryId == cat.id
                        val displayName = if (lang == AppLanguage.TAMIL) cat.nameTa else cat.nameEn

                        Card(
                            onClick = {
                                if (isSelected) viewModel.selectCategoryFilter(null)
                                else viewModel.selectCategoryFilter(cat.id)
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) TealContainer else MaterialTheme.colorScheme.surface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) PrimaryTeal else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier
                                .width(120.dp)
                                .testTag("category_card_${cat.id}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) PrimaryTeal else DeepNavyLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val icon = when (cat.iconKey) {
                                        "plumbing" -> Icons.Default.Plumbing
                                        "electrical_services" -> Icons.Default.Bolt
                                        "carpenter" -> Icons.Default.Handyman
                                        "format_paint" -> Icons.Default.FormatPaint
                                        "cleaning_services" -> Icons.Default.CleaningServices
                                        "ac_unit" -> Icons.Default.AcUnit
                                        "kitchen" -> Icons.Default.Kitchen
                                        else -> Icons.Default.TwoWheeler
                                    }
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = cat.nameEn,
                                        tint = if (isSelected) Color.White else AccentAmber,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TealDark else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Filters & Sort Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Verified Only Toggle
                FilterChip(
                    selected = filterState.verifiedOnly,
                    onClick = { viewModel.toggleVerifiedOnly() },
                    label = { Text(LocalizedStrings.get("filter_verified_only", lang), fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.VerifiedUser, null, modifier = Modifier.size(14.dp))
                    },
                    modifier = Modifier.testTag("filter_verified_chip")
                )

                // Emergency Only
                FilterChip(
                    selected = filterState.emergencyOnly,
                    onClick = { viewModel.toggleEmergencyOnly() },
                    label = { Text(LocalizedStrings.get("filter_emergency", lang), fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Bolt, null, modifier = Modifier.size(14.dp))
                    },
                    modifier = Modifier.testTag("filter_emergency_chip")
                )

                // Available Now
                val isAvailNow = filterState.availability == AvailabilityStatus.AVAILABLE_NOW
                FilterChip(
                    selected = isAvailNow,
                    onClick = {
                        viewModel.setAvailabilityFilter(if (isAvailNow) null else AvailabilityStatus.AVAILABLE_NOW)
                    },
                    label = { Text(LocalizedStrings.get("status_available_now", lang), fontSize = 11.sp) },
                    modifier = Modifier.testTag("filter_avail_now_chip")
                )

                // Sort by Price / Rating
                val isPriceSort = filterState.sortBy == SortOption.PRICE_ASC
                FilterChip(
                    selected = isPriceSort,
                    onClick = {
                        viewModel.setSortOption(if (isPriceSort) SortOption.RATING_DESC else SortOption.PRICE_ASC)
                    },
                    label = {
                        Text(
                            text = if (isPriceSort) "₹ Lowest Visit Fee" else "★ Top Rated",
                            fontSize = 11.sp
                        )
                    },
                    modifier = Modifier.testTag("sort_price_chip")
                )
            }
        }

        // Section Title: Available Workers Count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${workers.size} Verified Workers in ${filterState.city}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Worker List Cards
        if (workers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Workers Match Your Current Filter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing category or city filters to view all verified service pros.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.clearAllFilters() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
                        ) {
                            Text(LocalizedStrings.get("clear_filters", lang))
                        }
                    }
                }
            }
        } else {
            items(workers) { worker ->
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                    WorkerCard(
                        worker = worker,
                        isSaved = savedWorkerIds.contains(worker.id),
                        language = lang,
                        onCardClick = { viewModel.navigateTo(Screen.WorkerProfileDetail(worker.id)) },
                        onBookClick = { viewModel.openBookingDialog(worker) },
                        onToggleFavorite = { viewModel.toggleFavorite(worker.id) }
                    )
                }
            }
        }

        // Trust & Transparency Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepNavyDark)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = AccentAmber,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Worker Connect Trust & Safety Guarantee",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "• Manual Review: Every professional's Govt ID & trade certification is vetted.\n• Upfront Inspection Charges: Visit fee clearly stated in INR before you book.\n• Direct Connect: No hidden middleman commissions or simulated contacts.\n• Community Ratings: Reviews permitted only from genuine completed bookings.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // How It Works Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = LocalizedStrings.get("how_it_works_title", lang),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                val steps = listOf(
                    Pair("how_step1", "how_step1_desc"),
                    Pair("how_step2", "how_step2_desc"),
                    Pair("how_step3", "how_step3_desc"),
                    Pair("how_step4", "how_step4_desc")
                )

                steps.forEachIndexed { index, (titleKey, descKey) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = LocalizedStrings.get(titleKey, lang),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = LocalizedStrings.get(descKey, lang),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Call to Action: Join as a Professional Worker
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TealContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = null,
                        tint = TealDark,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = LocalizedStrings.get("reg_wizard_title", lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Are you a skilled plumber, electrician, carpenter, or technician in Tamil Nadu? Expand your customer base and earn fair wages.",
                        fontSize = 12.sp,
                        color = TealDark.copy(alpha = 0.85f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.WorkerRegistrationWizard) },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("join_worker_cta_btn")
                    ) {
                        Text("Start Worker Registration (Free)")
                    }
                }
            }
        }
    }
}
