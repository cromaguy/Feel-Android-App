package chromahub.feel.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogsScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dialogs") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogSection(
                title = "Alert Dialogs",
                description = "Alerts with various configurations"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Basic Alert Dialog
                    var showBasicDialog by remember { mutableStateOf(false) }
                    
                    Button(
                        onClick = { showBasicDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Basic Alert Dialog")
                    }
                    
                    if (showBasicDialog) {
                        AlertDialog(
                            onDismissRequest = { showBasicDialog = false },
                            title = { Text("Dialog Title") },
                            text = { Text("This is a basic alert dialog with a message and confirm/dismiss buttons.") },
                            confirmButton = {
                                TextButton(onClick = { 
                                    showBasicDialog = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Confirmed action")
                                    }
                                }) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showBasicDialog = false }) {
                                    Text("Dismiss")
                                }
                            }
                        )
                    }
                    
                    // Icon Alert Dialog
                    var showIconDialog by remember { mutableStateOf(false) }
                    
                    ElevatedButton(
                        onClick = { showIconDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Icon Alert Dialog")
                    }
                    
                    if (showIconDialog) {
                        AlertDialog(
                            onDismissRequest = { showIconDialog = false },
                            icon = { Icon(Icons.Default.Info, contentDescription = null) },
                            title = { Text("Information") },
                            text = { Text("This dialog includes an icon to emphasize the alert type or message importance.") },
                            confirmButton = {
                                TextButton(onClick = { showIconDialog = false }) {
                                    Text("Acknowledge")
                                }
                            }
                        )
                    }
                    
                    // Warning Dialog
                    var showWarningDialog by remember { mutableStateOf(false) }
                    
                    FilledTonalButton(
                        onClick = { showWarningDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Warning Dialog")
                    }
                    
                    if (showWarningDialog) {
                        AlertDialog(
                            onDismissRequest = { showWarningDialog = false },
                            icon = { 
                                Icon(
                                    Icons.Default.Warning, 
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                ) 
                            },
                            title = { Text("Warning") },
                            text = { Text("This action cannot be undone. Are you sure you want to proceed?") },
                            confirmButton = {
                                TextButton(
                                    onClick = { 
                                        showWarningDialog = false
                                        scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Action in progress...",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Short
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                snackbarHostState.showSnackbar("Action undone")
                                            }
                                        }
                                    }
                                ) {
                                    Text("Proceed")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showWarningDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
            }
            
            DialogSection(
                title = "Custom Dialogs",
                description = "Custom dialog implementations"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Custom Content Dialog
                    var showCustomDialog by remember { mutableStateOf(false) }
                    
                    Button(
                        onClick = { showCustomDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Custom Dialog")
                    }
                    
                    if (showCustomDialog) {
                        Dialog(
                            onDismissRequest = { showCustomDialog = false },
                            properties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true
                            )
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Custom Dialog Content",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Text(
                                        "This is a completely custom dialog with any content you want to include.",
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        for (i in 1..5) {
                                            FilledTonalButton(
                                                onClick = {
                                                    showCustomDialog = false
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("Selected option $i")
                                                    }
                                                },
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            ) {
                                                Text("$i")
                                            }
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    OutlinedButton(
                                        onClick = { showCustomDialog = false },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Close")
                                    }
                                }
                            }
                        }
                    }
                    
                    // Full-screen Dialog example button
                    var showFullscreenDialog by remember { mutableStateOf(false) }
                    
                    ElevatedButton(
                        onClick = { showFullscreenDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Fullscreen Dialog")
                    }
                    
                    if (showFullscreenDialog) {
                        Dialog(
                            onDismissRequest = { showFullscreenDialog = false },
                            properties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = false,
                                usePlatformDefaultWidth = false
                            )
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp),
                                shape = MaterialTheme.shapes.extraSmall
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Dialog "toolbar"
                                    TopAppBar(
                                        title = { Text("Fullscreen Dialog") },
                                        navigationIcon = {
                                            TextButton(onClick = { showFullscreenDialog = false }) {
                                                Text("Close")
                                            }
                                        },
                                        actions = {
                                            TextButton(onClick = {
                                                showFullscreenDialog = false
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Changes saved")
                                                }
                                            }) {
                                                Text("Save")
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                    
                                    // Dialog content
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "This is a fullscreen dialog that provides an immersive experience for complex tasks or forms.",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        
                                        Spacer(modifier = Modifier.height(32.dp))
                                        
                                        Button(
                                            onClick = { showFullscreenDialog = false },
                                            modifier = Modifier.fillMaxWidth(0.5f)
                                        ) {
                                            Text("Close Dialog")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            DialogSection(
                title = "Date & Time Pickers",
                description = "Date and time selection dialogs"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Date Picker Dialog
                    var showDatePicker by remember { mutableStateOf(false) }
                    var selectedDate by remember { mutableStateOf<Long?>(null) }
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = System.currentTimeMillis()
                    )
                    
                    Button(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(
                            if (selectedDate == null) "Select Date" 
                            else "Selected: ${formatDate(selectedDate!!)}"
                        )
                    }
                    
                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    selectedDate = datePickerState.selectedDateMillis
                                    showDatePicker = false
                                }) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("Cancel")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                    
                    // Time Picker Dialog
                    var showTimePicker by remember { mutableStateOf(false) }
                    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }
                    val timePickerState = rememberTimePickerState(
                        initialHour = 12, 
                        initialMinute = 30
                    )
                    
                    Button(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(
                            if (selectedTime == null) "Select Time" 
                            else "Selected: ${formatTime(selectedTime!!.first, selectedTime!!.second)}"
                        )
                    }
                    
                    if (showTimePicker) {
                        Dialog(
                            onDismissRequest = { showTimePicker = false }
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Select Time",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    TimePicker(
                                        state = timePickerState,
                                        colors = TimePickerDefaults.colors(
                                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { showTimePicker = false }) {
                                            Text("Cancel")
                                        }
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        TextButton(
                                            onClick = {
                                                selectedTime = timePickerState.hour to timePickerState.minute
                                                showTimePicker = false
                                            }
                                        ) {
                                            Text("Confirm")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DialogSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            content()
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    
    return DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
        .format(date)
}

private fun formatTime(hour: Int, minute: Int): String {
    val time = LocalTime.of(hour, minute)
    
    return DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        .format(time)
} 
