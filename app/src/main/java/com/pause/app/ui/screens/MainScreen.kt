package com.pause.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pause.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel, onNavigateToSettings: () -> Unit) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pause") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (settings.isPauseActive) "Notifications Paused" else "Notifications Active",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Switch(
                checked = settings.isPauseActive,
                onCheckedChange = { viewModel.togglePause() },
                modifier = Modifier.scale(2f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "You'll receive a daily digest at your chosen times. Calls, alarms, and starred contacts still ring.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Helper for scaling switch
@Composable
fun Modifier.scale(scale: Float): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(
            (placeable.width * scale).toInt(),
            (placeable.height * scale).toInt()
        ) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
            }
        }
    }
)
