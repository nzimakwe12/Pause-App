package com.pause.app.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.pause.app.data.PauseDatabase
import com.pause.app.data.PauseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PauseWidget : GlanceAppWidget() {
    override suspend fun provideContent(context: Context, id: GlanceId) {
        val repository = PauseRepository(PauseDatabase.getDatabase(context))
        
        provideContent {
            val settings by repository.settings.collectAsState(initial = null)
            val isActive = settings?.isPauseActive ?: false
            
            Column(
                modifier = GlanceModifier.fillMaxSize().padding(8.dp),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Text(text = if (isActive) "Paused" else "Active")
                Button(
                    text = "Toggle",
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val current = repository.settings.first()
                            repository.updateSettings(current.copy(isPauseActive = !current.isPauseActive))
                            updateAll(context)
                        }
                    }
                )
            }
        }
    }
}

class PauseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PauseWidget()
}
