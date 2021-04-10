@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.feature_purchase

import android.content.Context
import com.alimoradi.feature_purchase.internal.PlayBillingManagerImpl
import com.alimoradi.feature_purchase.internal.PurchaseManagerImpl
import com.alimoradi.feature_purchase.internal.PurchaseRepository
import com.alimoradi.feature_purchase.internal.PurchaseRepositoryImpl

class PurchaseModule(
    private val context: Context
) {

    fun createPurchaseManager(
        purchaseAnalyticsManager: PurchaseAnalyticsManager? = null
    ): PurchaseManager {
        val playBillingManager = createPlayBillingManager()
        val purchaseRepository = createPurchaseRepository()
        return PurchaseManagerImpl(
            playBillingManager,
            purchaseRepository,
            purchaseAnalyticsManager
        )
    }

    private fun createPlayBillingManager() = PlayBillingManagerImpl(
        context.applicationContext
    )

    private fun createPurchaseRepository(): PurchaseRepository {
        val sharedPreferences = context.getSharedPreferences(
            PurchaseRepositoryImpl.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        return PurchaseRepositoryImpl(
            sharedPreferences
        )
    }
}
