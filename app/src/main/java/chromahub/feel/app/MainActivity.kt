package chromahub.feel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import chromahub.feel.app.navigation.NavigationItem
import chromahub.feel.app.ui.screens.*
import chromahub.feel.app.ui.theme.ThemeManager
import chromahub.feel.app.ui.theme.FeelTheme
import chromahub.feel.app.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            // Set initial dark mode based on system
            val isDarkTheme = isSystemInDarkTheme()
            ThemeManager.setDarkMode(isDarkTheme)
            
            FeelTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Create shared ViewModel instance
    val sharedViewModel: WeatherViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    NavigationItem.items.forEach { item ->
                        val selected = currentRoute == item.route
                        
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(26.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Today.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Today.route) {
                TodayScreen(viewModel = sharedViewModel)
            }
            composable(NavigationItem.Forecast.route) {
                ForecastScreen(viewModel = sharedViewModel)
            }
            composable(NavigationItem.Precipitation.route) {
                PrecipitationScreen(viewModel = sharedViewModel)
            }
            composable(NavigationItem.Location.route) {
                LocationScreen(viewModel = sharedViewModel)
            }
            composable(NavigationItem.Settings.route) {
                SettingsScreen(
                    viewModel = sharedViewModel,
                    onNavigateToUpdates = {
                        navController.navigate(NavigationItem.Updates.route)
                    }
                )
            }
            composable(NavigationItem.Updates.route) {
                UpdatesScreen(
                    onBackPressed = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
