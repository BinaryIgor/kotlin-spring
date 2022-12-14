package com.igor101.kotlin.spring.inventory

data class Inventory(
    val id: String,
    val online: Boolean,
    val filter: InventoryFilter,
    val version: Int = 0
)

enum class InventoryFilterType { SKU, BRAND }

sealed class InventoryFilter(val type: InventoryFilterType)

data class InventorySkuFilter(val skus: List<String>) : InventoryFilter(InventoryFilterType.SKU)
data class InventoryBrandFilter(val brands: List<String>) : InventoryFilter(InventoryFilterType.BRAND)