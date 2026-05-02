package com.pause.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PauseRepository(private val db: PauseDatabase) {
    val unsentNotifications: Flow<List<CapturedNotification>> = db.notificationDao().getUnsentNotifications()
    val starredContacts: Flow<List<StarredContact>> = db.contactDao().getStarredContacts()
    val settings: Flow<AppSettings> = db.settingsDao().getSettings().map { it ?: AppSettings() }

    suspend fun insertNotification(notification: CapturedNotification) = db.notificationDao().insert(notification)
    suspend fun markNotificationsAsSent(ids: List<Long>) = db.notificationDao().markAsSent(ids)
    suspend fun deleteNotification(id: Long) = db.notificationDao().delete(id)
    suspend fun clearAllNotifications() = db.notificationDao().deleteAll()

    suspend fun addStarredContact(contact: StarredContact) = db.contactDao().insert(contact)
    suspend fun removeStarredContact(contact: StarredContact) = db.contactDao().delete(contact)

    suspend fun updateSettings(settings: AppSettings) = db.settingsDao().updateSettings(settings)
    suspend fun getSettingsSync() = db.settingsDao().getSettingsSync() ?: AppSettings()
}
