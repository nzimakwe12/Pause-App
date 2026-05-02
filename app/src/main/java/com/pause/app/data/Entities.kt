package com.pause.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captured_notifications")
data class CapturedNotification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val appName: String,
    val packageName: String,
    val title: String?,
    val text: String?,
    val iconBlob: ByteArray?,
    val pendingIntentData: String?, // Simplified for this example
    val isSent: Boolean = false
)

@Entity(tableName = "starred_contacts")
data class StarredContact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contactUri: String,
    val displayName: String
)

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val isPauseActive: Boolean = false,
    val premiumActive: Boolean = false,
    val extraSlotsEnabled: Boolean = false,
    val digestTimes: String = "08:00,13:00,18:00" // Comma separated
)
