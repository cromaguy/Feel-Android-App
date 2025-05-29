package chromahub.feel.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import chromahub.feel.app.ui.components.*
import chromahub.feel.app.ui.viewmodel.WeatherUiState
import chromahub.feel.app.ui.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { 
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Today's Weather",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Current conditions and forecast",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f)
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            
            LocationSearchBar(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onSearch = { 
                    if (viewModel.searchQuery.isNotEmpty()) {
                        viewModel.updateLocation(viewModel.searchQuery)
                    }
                }
            )
            
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
                
                is WeatherUiState.Success -> {
                    val data = state.data
                    
                    // Primary weather card with elevated style
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 8.dp,
                        shadowElevation = 4.dp
                    ) {
                        CurrentWeatherCard(
                            location = data.location,
                            currentWeather = data.current,
                            useCelsius = viewModel.useCelsius,
                            onToggleUnit = { viewModel.toggleTemperatureUnit() }
                        )
                    }
                    
                    if (data.forecast.forecastday.isNotEmpty()) {
                        val todayForecast = data.forecast.forecastday.first()
                        
                        // Precipitation card with filled style
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            tonalElevation = 4.dp
                        ) {
                            PrecipitationInfoCard(
                                dayForecast = todayForecast,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                        
                        // Sun and Moon information with improved Material 3 styling
                        var expanded by remember { mutableStateOf(true) }
                        
                        Surface(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            tonalElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.tertiaryContainer,
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Rounded.WbTwilight,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                        }
                                        
                                        Column(
                                            modifier = Modifier.padding(start = 16.dp)
                                        ) {
                                            Text(
                                                text = "Sun & Moon",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "Daily celestial schedule",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                
                                AnimatedVisibility(
                                    visible = expanded,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp)
                                    ) {
                                        // Sun information with improved visual styling
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {
                                                Text(
                                                    text = "Sunlight",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                )
                                                
                                                SunMoonItem(
                                                    icon = Icons.Rounded.WbSunny,
                                                    text = "Sunrise: ${todayForecast.astro.sunrise}",
                                                    tint = Color(0xFFFFA000)
                                                )
                                                
                                                Spacer(modifier = Modifier.height(8.dp))
                                                
                                                SunMoonItem(
                                                    icon = Icons.Rounded.WbSunny,
                                                    text = "Sunset: ${todayForecast.astro.sunset}",
                                                    tint = Color(0xFFF57C00)
                                                )
                                            }
                                        }
                                        
                                        Spacer(modifier = Modifier.height(12.dp))
                                        
                                        // Moon information with improved visual styling
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {
                                                Text(
                                                    text = "Moonlight",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                )
                                                
                                                SunMoonItem(
                                                    icon = Icons.Rounded.NightsStay,
                                                    text = "Moonrise: ${todayForecast.astro.moonrise}",
                                                    tint = Color(0xFF7986CB)
                                                )
                                                
                                                Spacer(modifier = Modifier.height(8.dp))
                                                
                                                SunMoonItem(
                                                    icon = Icons.Rounded.NightsStay,
                                                    text = "Moonset: ${todayForecast.astro.moonset}",
                                                    tint = Color(0xFF5C6BC0)
                                                )
                                            }
                                        }
                                        
                                        Spacer(modifier = Modifier.height(12.dp))
                                        
                                        // Moon phase with improved Material 3 styling
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier.size(40.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.NightsStay,
                                                            contentDescription = null,
                                                            tint = MaterialTheme.colorScheme.secondary,
                                                            modifier = Modifier.size(24.dp)
                                                        )
                                                    }
                                                }
                                                
                                                Column(
                                                    modifier = Modifier.padding(start = 16.dp)
                                                ) {
                                                    Text(
                                                        text = "Moon Phase",
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = todayForecast.astro.moonPhase,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // Add bottom spacing
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                is WeatherUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.retryFetch() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SunMoonItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = tint.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = tint.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
} 
