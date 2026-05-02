package com.pause.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pause.app.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onBuyCoffee: () -> Unit) {
    val settings by viewModel.settings.collectAsState()
    val contacts by viewModel.starredContacts.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text("Digest Times", style = MaterialTheme.typography.titleLarge)
                settings.digestTimes.split(",").forEach { time ->
                    ListItem(headlineContent = { Text(time) })
                }
                Button(onClick = { /* Show Time Picker */ }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Add Time")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text("Starred Contacts (VIP)", style = MaterialTheme.typography.titleLarge)
            }
            
            items(contacts) { contact ->
                ListItem(
                    headlineContent = { Text(contact.displayName) },
                    trailingContent = {
                        IconButton(onClick = { viewModel.removeContact(contact) }) {
                            Text("Remove")
                        }
                    }
                )
            }

            item {
                Button(onClick = { /* Open Contact Picker */ }) {
                    Text("Add Contact")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text("Support Pause", style = MaterialTheme.typography.titleLarge)
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    onClick = onBuyCoffee
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Buy us a coffee – $0.20", style = MaterialTheme.typography.titleMedium)
                            if (settings.premiumActive) {
                                Text("Thank you for your support! ❤️", color = MaterialTheme.colorScheme.primary)
                            } else {
                                Text("Unlocks extra delivery slot & removes ads")
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text("App Version: 1.0.0", style = MaterialTheme.typography.labelSmall)
                Text("Developer: Pause Team", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
