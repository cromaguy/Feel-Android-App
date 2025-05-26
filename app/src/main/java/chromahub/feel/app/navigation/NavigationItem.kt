package chromahub.feel.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Today : NavigationItem(
        route = "today",
        title = "Today",
        icon = Icons.Rounded.WbSunny
    )
    
    object Forecast : NavigationItem(
        route = "forecast",
        title = "Forecast",
        icon = Icons.Rounded.Cloud
    )
    
    object Precipitation : NavigationItem(
        route = "precipitation",
        title = "Rain",
        icon = Icons.Rounded.WaterDrop
    )
    
    object Location : NavigationItem(
        route = "location",
        title = "Locations",
        icon = Icons.Rounded.LocationOn
    )
    
    object Settings : NavigationItem(
        route = "settings",
        title = "Settings",
        icon = Icons.Rounded.Settings
    )

    object Updates : NavigationItem(
        route = "updates",
        title = "Updates",
        icon = Icons.Rounded.SystemUpdate
    )
    
    companion object {
        val items = listOf(Today, Forecast, Precipitation, Location, Settings)
    }
} 
