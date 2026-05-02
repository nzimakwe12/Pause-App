package com.pause.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pause.app.data.AppSettings
import com.pause.app.data.CapturedNotification
import com.pause.app.data.PauseRepository
import com.pause.app.data.StarredContact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PauseRepository) : ViewModel() {
    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    fun togglePause() {
        viewModelScope.launch {
            val current = settings.value
            repository.updateSettings(current.copy(isPauseActive = !current.isPauseActive))
        }
    }
}

class DigestViewModel(private val repository: PauseRepository) : ViewModel() {
    val notifications: StateFlow<List<CapturedNotification>> = repository.unsentNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAllNotifications()
        }
    }

    fun deleteNotification(id: Long) {
        viewModelScope.launch {
            repository.deleteNotification(id)
        }
    }
}

class SettingsViewModel(private val repository: PauseRepository) : ViewModel() {
    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())
    
    val starredContacts: StateFlow<List<StarredContact>> = repository.starredContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addContact(name: String, uri: String) {
        viewModelScope.launch {
            repository.addStarredContact(StarredContact(displayName = name, contactUri = uri))
        }
    }

    fun removeContact(contact: StarredContact) {
        viewModelScope.launch {
            repository.removeStarredContact(contact)
        }
    }
}
