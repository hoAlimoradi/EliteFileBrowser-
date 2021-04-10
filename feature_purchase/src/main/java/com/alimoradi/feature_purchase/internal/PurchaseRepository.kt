@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.alimoradi.feature_purchase.internal

internal interface PurchaseRepository {

    fun addPurchased(sku: String): Boolean

    fun isPurchased(sku: String): Boolean

    fun isEmpty(): Boolean
}
