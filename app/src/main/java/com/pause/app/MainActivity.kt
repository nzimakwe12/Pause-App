package com.pause.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.nativead.NativeAd
import com.pause.app.ads.AdManager
import com.pause.app.billing.BillingHelper
import com.pause.app.ui.screens.DigestScreen
import com.pause.app.ui.screens.MainScreen
import com.pause.app.ui.screens.SettingsScreen
import com.pause.app.viewmodel.DigestViewModel
import com.pause.app.viewmodel.MainViewModel
import com.pause.app.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private lateinit var billingHelper: BillingHelper
    private lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = (application as PauseApplication).repository
        billingHelper = BillingHelper(this, repository)
        adManager = AdManager(this)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
                val settings by repository.settings.collectAsState(initial = null)

                LaunchedEffect(Unit) {
                    adManager.loadNativeAd { nativeAd = it }
                }

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = MainViewModel(repository),
                            onNavigateToSettings = { navController.navigate("settings") }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            viewModel = SettingsViewModel(repository),
                            onBuyCoffee = { billingHelper.launchBillingFlow(this@MainActivity) }
                        )
                    }
                    composable("digest") {
                        DigestScreen(
                            viewModel = DigestViewModel(repository),
                            nativeAd = nativeAd,
                            isPremium = settings?.premiumActive ?: false
                        )
                    }
                }

                // Handle intent to open digest
                LaunchedEffect(intent) {
                    if (intent?.getBooleanExtra("open_digest", false) == true) {
                        navController.navigate("digest")
                    }
                }
            }
        }
    }
}
