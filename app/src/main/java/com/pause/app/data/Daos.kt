package com.pause.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM captured_notifications WHERE isSent = 0 ORDER BY timestamp DESC")
    fun getUnsentNotifications(): Flow<List<CapturedNotification>>

    @Query("SELECT * FROM captured_notifications WHERE isSent = 0")
    suspend fun getUnsentNotificationsList(): List<CapturedNotification>

    @Insert
    suspend fun insert(notification: CapturedNotification)

    @Query("UPDATE captured_notifications SET isSent = 1 WHERE id IN (:ids)")
    suspend fun markAsSent(ids: List<Long>)

    @Query("DELETE FROM captured_notifications WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM captured_notifications")
    suspend fun deleteAll()
}

@Dao
interface ContactDao {
    @Query("SELECT * FROM starred_contacts")
    fun getStarredContacts(): Flow<List<StarredContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: StarredContact)

    @Delete
    suspend fun delete(contact: StarredContact)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettings(): Flow<AppSettings?>

    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun getSettingsSync(): AppSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: AppSettings)
}
