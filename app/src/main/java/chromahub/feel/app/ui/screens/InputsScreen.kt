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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Fields") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputSection(
                title = "Text Fields",
                description = "Various text input styles"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Standard TextField
                    var text by remember { mutableStateOf("") }
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Standard TextField") },
                        placeholder = { Text("Enter text") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Outlined TextField
                    var outlinedText by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = outlinedText,
                        onValueChange = { outlinedText = it },
                        label = { Text("Outlined TextField") },
                        placeholder = { Text("Enter text") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // TextField with Leading Icon
                    var nameText by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        label = { Text("Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Search TextField
                    var searchQuery by remember { mutableStateOf("") }
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        placeholder = { Text("Search...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Password TextField
                    var password by remember { mutableStateOf("") }
                    var passwordVisible by remember { mutableStateOf(false) }
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("Enter password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff
                            
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Helper Text TextField
                    var email by remember { mutableStateOf("") }
                    var isError by remember { mutableStateOf(false) }
                    
                    Column {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { 
                                email = it
                                isError = !it.contains('@')
                            },
                            label = { Text("Email") },
                            placeholder = { Text("example@domain.com") },
                            isError = isError,
                            supportingText = {
                                if (isError) {
                                    Text(
                                        text = "Email must contain '@'",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text("Enter a valid email address")
                                }
                            },
                            trailingIcon = {
                                if (isError) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Error",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            InputSection(
                title = "Selection Controls",
                description = "Checkboxes, radio buttons, and switches"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Checkboxes
                    Text(
                        text = "Checkboxes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    val checkboxItems = listOf("Option 1", "Option 2", "Option 3")
                    val checkedStates = remember { 
                        checkboxItems.map { mutableStateOf(false) }
                    }
                    
                    checkboxItems.forEachIndexed { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = checkedStates[index].value,
                                onCheckedChange = { checkedStates[index].value = it }
                            )
                            
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Radio Buttons
                    Text(
                        text = "Radio Buttons",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    val radioOptions = listOf("Option A", "Option B", "Option C")
                    var selectedOption by remember { mutableStateOf(radioOptions[0]) }
                    
                    Column {
                        radioOptions.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (option == selectedOption),
                                        onClick = { selectedOption = option },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (option == selectedOption),
                                    onClick = null
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Switches
                    Text(
                        text = "Switches",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    val switchLabels = listOf("Dark Mode", "Notifications", "Auto-sync")
                    val switchStates = remember { 
                        switchLabels.map { mutableStateOf(false) }
                    }
                    
                    switchLabels.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Switch(
                                checked = switchStates[index].value,
                                onCheckedChange = { switchStates[index].value = it }
                            )
                        }
                    }
                }
            }
            
            InputSection(
                title = "Sliders",
                description = "Adjust values with ranges"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Continuous Slider
                    Text(
                        text = "Continuous Slider",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    var sliderValue by remember { mutableFloatStateOf(0.5f) }
                    Column {
                        Slider(
                            value = sliderValue,
                            onValueChange = { sliderValue = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = "Value: ${(sliderValue * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Stepped Slider
                    Text(
                        text = "Stepped Slider",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    var steppedValue by remember { mutableIntStateOf(5) }
                    Column {
                        Slider(
                            value = steppedValue.toFloat(),
                            onValueChange = { steppedValue = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = "Level: $steppedValue",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Range Slider
                    Text(
                        text = "Range Slider",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    var rangeSliderValues by remember { mutableStateOf(20f..80f) }
                    Column {
                        RangeSlider(
                            value = rangeSliderValues,
                            onValueChange = { rangeSliderValues = it },
                            valueRange = 0f..100f,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Text(
                            text = "Range: ${rangeSliderValues.start.toInt()} - ${rangeSliderValues.endInclusive.toInt()}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
            
            InputSection(
                title = "Dropdown Menu",
                description = "Selection dropdown menu"
            ) {
                // Exposed Dropdown Menu
                var expanded by remember { mutableStateOf(false) }
                val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
                var selectedOption by remember { mutableStateOf(options[0]) }
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedOption,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    selectedOption = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Validation button
                var isValid by remember { mutableStateOf(false) }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FilledTonalButton(
                        onClick = { isValid = !isValid }
                    ) {
                        Text(if (isValid) "Validated!" else "Validate")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InputSection(
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
