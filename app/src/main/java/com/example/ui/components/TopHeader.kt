package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.UserRole
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeader(
    viewModel: WorkerConnectViewModel,
    onOpenAwsGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    var roleMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = DeepNavy,
        tonalElevation = 6.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Row 1: Logo, Brand Name, Language Toggle, Role Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { viewModel.navigateTo(Screen.Home) }
                        .testTag("brand_logo_title")
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Handyman,
                            contentDescription = "Worker Connect Logo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = LocalizedStrings.get("app_name", lang),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 0.3.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Location",
                                tint = AccentAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Tamil Nadu, IN",
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Controls: Language Toggle + Role Switcher Pill + AWS Guide
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Language Switcher (EN / தமிழ்)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (lang == AppLanguage.TAMIL) PrimaryTeal else DeepNavyLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier
                            .clickable { viewModel.toggleLanguage() }
                            .testTag("language_toggle_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (lang == AppLanguage.ENGLISH) "தமிழ்" else "EN",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Role Switcher Dropdown
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = DeepNavyLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                            modifier = Modifier
                                .clickable { roleMenuExpanded = true }
                                .testTag("role_menu_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                val roleIcon = when (role) {
                                    UserRole.CUSTOMER -> Icons.Default.Person
                                    UserRole.WORKER -> Icons.Default.Engineering
                                    UserRole.ADMINISTRATOR -> Icons.Default.AdminPanelSettings
                                }
                                Icon(
                                    imageVector = roleIcon,
                                    contentDescription = "Role",
                                    tint = when (role) {
                                        UserRole.CUSTOMER -> PrimaryTeal
                                        UserRole.WORKER -> AccentAmber
                                        UserRole.ADMINISTRATOR -> Color(0xFF90CDF4)
                                    },
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = when (role) {
                                        UserRole.CUSTOMER -> LocalizedStrings.get("role_customer", lang)
                                        UserRole.WORKER -> LocalizedStrings.get("role_worker", lang)
                                        UserRole.ADMINISTRATOR -> LocalizedStrings.get("role_admin", lang)
                                    },
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = roleMenuExpanded,
                            onDismissRequest = { roleMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(LocalizedStrings.get("role_customer", lang)) },
                                leadingIcon = { Icon(Icons.Default.Person, null, tint = PrimaryTeal) },
                                onClick = {
                                    viewModel.setRole(UserRole.CUSTOMER)
                                    roleMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LocalizedStrings.get("role_worker", lang)) },
                                leadingIcon = { Icon(Icons.Default.Engineering, null, tint = AccentAmber) },
                                onClick = {
                                    viewModel.setRole(UserRole.WORKER)
                                    roleMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LocalizedStrings.get("role_admin", lang)) },
                                leadingIcon = { Icon(Icons.Default.AdminPanelSettings, null, tint = Color(0xFF3B82F6)) },
                                onClick = {
                                    viewModel.setRole(UserRole.ADMINISTRATOR)
                                    roleMenuExpanded = false
                                }
                            )
                        }
                    }

                    // AWS Cloud Blueprint Link
                    IconButton(
                        onClick = onOpenAwsGuide,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("aws_blueprint_header_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudQueue,
                            contentDescription = "AWS Architecture Guide",
                            tint = if (currentScreen is Screen.AwsCloudGuide) AccentAmber else Color.White
                        )
                    }
                }
            }
        }
    }
}
