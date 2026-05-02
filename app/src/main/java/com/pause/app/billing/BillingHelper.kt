package com.pause.app.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.pause.app.data.PauseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BillingHelper(private val context: Context, private val repository: PauseRepository) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var billingClient: BillingClient

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    init {
        setupBillingClient()
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }

    private fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()
        ) { _, purchases ->
            val isPremium = purchases.any { it.products.contains("pause_plus") && it.purchaseState == Purchase.PurchaseState.PURCHASED }
            scope.launch {
                val settings = repository.settings.first()
                repository.updateSettings(settings.copy(premiumActive = isPremium))
            }
        }
    }

    fun launchBillingFlow(activity: Activity) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("pause_plus")
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        billingClient.queryProductDetailsAsync(params) { _, productDetailsList ->
            if (productDetailsList.isNotEmpty()) {
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetailsList[0])
                        .build()
                )
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                billingClient.launchBillingFlow(activity, billingFlowParams)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        scope.launch {
                            val settings = repository.settings.first()
                            repository.updateSettings(settings.copy(premiumActive = true))
                        }
                    }
                }
            }
        }
    }
}
