package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.LocalizedStrings
import com.example.ui.Screen
import com.example.ui.WorkerConnectViewModel
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.TealContainer
import com.example.ui.theme.TealDark

data class NavDestination(
    val screen: Screen,
    val titleKey: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    language: AppLanguage,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavDestination(Screen.Home, "nav_home", Icons.Default.Search, "nav_item_home"),
        NavDestination(Screen.CustomerDashboard, "nav_requests", Icons.Default.Assignment, "nav_item_requests"),
        NavDestination(Screen.WorkerDashboard, "nav_worker_hub", Icons.Default.Engineering, "nav_item_worker_hub"),
        NavDestination(Screen.AdminDashboard, "nav_admin", Icons.Default.AdminPanelSettings, "nav_item_admin"),
        NavDestination(Screen.AwsCloudGuide, "nav_aws_guide", Icons.Default.CloudQueue, "nav_item_aws")
    )

    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        items.forEach { dest ->
            val isSelected = when (dest.screen) {
                is Screen.Home -> currentScreen is Screen.Home
                is Screen.CustomerDashboard -> currentScreen is Screen.CustomerDashboard
                is Screen.WorkerDashboard -> currentScreen is Screen.WorkerDashboard
                is Screen.AdminDashboard -> currentScreen is Screen.AdminDashboard
                is Screen.AwsCloudGuide -> currentScreen is Screen.AwsCloudGuide
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(dest.screen) },
                icon = {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = LocalizedStrings.get(dest.titleKey, language)
                    )
                },
                label = {
                    Text(
                        text = LocalizedStrings.get(dest.titleKey, language),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TealDark,
                    selectedTextColor = TealDark,
                    indicatorColor = TealContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(dest.testTag)
            )
        }
    }
}
