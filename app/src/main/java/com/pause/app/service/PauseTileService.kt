package com.pause.app.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.pause.app.data.PauseDatabase
import com.pause.app.data.PauseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PauseTileService : TileService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var repository: PauseRepository

    override fun onCreate() {
        super.onCreate()
        val db = PauseDatabase.getDatabase(this)
        repository = PauseRepository(db)
    }

    override fun onStartListening() {
        super.onStartListening()
        scope.launch {
            val settings = repository.settings.first()
            updateTile(settings.isPauseActive)
        }
    }

    override fun onClick() {
        super.onClick()
        scope.launch {
            val settings = repository.settings.first()
            val newState = !settings.isPauseActive
            repository.updateSettings(settings.copy(isPauseActive = newState))
            updateTile(newState)
        }
    }

    private fun updateTile(isActive: Boolean) {
        val tile = qsTile ?: return
        tile.state = if (isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = "Pause"
        tile.updateTile()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
