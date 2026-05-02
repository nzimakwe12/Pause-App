package com.pause.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CapturedNotification::class, StarredContact::class, AppSettings::class],
    version = 1,
    exportSchema = false
)
abstract class PauseDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun contactDao(): ContactDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: PauseDatabase? = null

        fun getDatabase(context: Context): PauseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PauseDatabase::class.java,
                    "pause_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
