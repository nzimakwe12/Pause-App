package com.pause.app

import android.app.Application
import com.pause.app.data.PauseDatabase
import com.pause.app.data.PauseRepository

class PauseApplication : Application() {
    val database by lazy { PauseDatabase.getDatabase(this) }
    val repository by lazy { PauseRepository(database) }
}
