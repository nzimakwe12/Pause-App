package com.pause.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.pause.app.data.CapturedNotification
import com.pause.app.viewmodel.DigestViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigestScreen(viewModel: DigestViewModel, nativeAd: NativeAd?, isPremium: Boolean) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Digest") },
                actions = {
                    if (notifications.isNotEmpty()) {
                        TextButton(onClick = { viewModel.clearAll() }) {
                            Text("Clear All")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications captured yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(notifications) { notification ->
                    NotificationRow(notification, onDelete = { viewModel.deleteNotification(notification.id) })
                    HorizontalDivider()
                }
                
                if (!isPremium && nativeAd != null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sponsored", style = MaterialTheme.typography.labelSmall)
                        NativeAdComposable(nativeAd)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationRow(notification: CapturedNotification, onDelete: () -> Unit) {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(notification.timestamp))
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(notification.appName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(notification.title ?: "No Title", style = MaterialTheme.typography.titleMedium)
            Text(notification.text ?: "", style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Text(time, style = MaterialTheme.typography.labelSmall)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun NativeAdComposable(nativeAd: NativeAd) {
    AndroidView(
        factory = { context ->
            NativeAdView(context).apply {
                // In a real app, you'd inflate a layout and bind nativeAd properties
            }
        },
        update = { view ->
            view.setNativeAd(nativeAd)
        },
        modifier = Modifier.fillMaxWidth().height(100.dp)
    )
}
