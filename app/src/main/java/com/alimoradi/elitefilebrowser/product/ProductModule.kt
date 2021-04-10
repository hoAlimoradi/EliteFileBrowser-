package com.alimoradi.elitefilebrowser.product

import android.content.Context
import com.alimoradi.elitefilebrowser.main.ApplicationGraph
import com.alimoradi.feature_purchase.PurchaseManager
import com.alimoradi.feature_purchase.PurchaseModule

class ProductModule(
    private val context: Context
) {

    fun createProductManager(): ProductManager {
        val developerManager = ApplicationGraph.getDeveloperManager()
        val purchaseManager = createPurchaseManager()
        val remoteConfig = ApplicationGraph.getRemoteConfig()
        return ProductManagerImpl(
            developerManager,
            purchaseManager,
            remoteConfig
        )
    }

    private fun createPurchaseManager(): PurchaseManager {
        val purchaseModule = PurchaseModule(context)
        return purchaseModule.createPurchaseManager()
    }
}
