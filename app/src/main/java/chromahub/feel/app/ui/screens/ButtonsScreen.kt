package chromahub.feel.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.HorizontalDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ButtonsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buttons") },
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
            ButtonSection(
                title = "Filled Buttons",
                description = "Standard buttons with high emphasis"
            ) {
                Button(onClick = {}) {
                    Text("Filled Button")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Button(
                    onClick = {},
                    enabled = false
                ) {
                    Text("Disabled")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Button(onClick = {}) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("With Icon")
                }
            }
            
            ButtonSection(
                title = "Outlined Buttons",
                description = "Medium emphasis buttons with borders"
            ) {
                OutlinedButton(onClick = {}) {
                    Text("Outlined Button")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                OutlinedButton(
                    onClick = {},
                    enabled = false
                ) {
                    Text("Disabled")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                OutlinedButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Create,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("With Icon")
                }
            }
            
            ButtonSection(
                title = "Text Buttons",
                description = "Low emphasis buttons without borders"
            ) {
                TextButton(onClick = {}) {
                    Text("Text Button")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                TextButton(
                    onClick = {},
                    enabled = false
                ) {
                    Text("Disabled")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                TextButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("With Icon")
                }
            }
            
            ButtonSection(
                title = "Elevated and Tonal Buttons",
                description = "Buttons with elevation and tonal variants"
            ) {
                ElevatedButton(onClick = {}) {
                    Text("Elevated")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                FilledTonalButton(onClick = {}) {
                    Text("Tonal")
                }
            }
            
            ButtonSection(
                title = "Icon Buttons",
                description = "Buttons that display only icons"
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                FilledIconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorite"
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                FilledTonalIconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Check"
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                OutlinedIconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorite"
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                var selected by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { selected = !selected },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        if (selected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Toggle Favorite"
                    )
                }
            }
            
            ButtonSection(
                title = "Floating Action Buttons",
                description = "Floating action buttons for primary actions"
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallFloatingActionButton(
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                    
                    FloatingActionButton(
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                    
                    LargeFloatingActionButton(
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ExtendedFloatingActionButton(
                    onClick = {},
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text("Extended FAB") },
                )
            }
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ButtonSection(
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
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
    }
} 
