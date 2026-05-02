package com.pause.app.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.pause.app.data.CapturedNotification
import com.pause.app.data.PauseDatabase
import com.pause.app.data.PauseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PauseNotificationListener : NotificationListenerService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var repository: PauseRepository

    override fun onCreate() {
        super.onCreate()
        val db = PauseDatabase.getDatabase(this)
        repository = PauseRepository(db)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        scope.launch {
            val settings = repository.getSettingsSync()
            if (!settings.isPauseActive) return@launch

            if (shouldIntercept(sbn)) {
                val notification = CapturedNotification(
                    timestamp = sbn.postTime,
                    appName = getAppName(sbn.packageName),
                    packageName = sbn.packageName,
                    title = sbn.notification.extras.getString("android.title"),
                    text = sbn.notification.extras.getCharSequence("android.text")?.toString(),
                    iconBlob = null, // In a real app, convert icon to byte array
                    pendingIntentData = sbn.notification.contentIntent?.toString()
                )
                repository.insertNotification(notification)
                cancelNotification(sbn.key)
                Log.d("PauseService", "Intercepted notification from ${sbn.packageName}")
            }
        }
    }

    private fun shouldIntercept(sbn: StatusBarNotification): Boolean {
        val isSystem = sbn.packageName == "android" || sbn.packageName == "com.android.systemui"
        val isOngoing = sbn.isOngoing
        val category = sbn.notification.category
        val isCritical = category == "call" || category == "alarm" || category == "err"
        
        // Simplified check for starred contacts - in real app, check extras for contact URI
        return !isSystem && !isOngoing && !isCritical
    }

    private fun getAppName(packageName: String): String {
        return try {
            val pm = packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(ai).toString()
        } catch (e: Exception) {
            packageName
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
