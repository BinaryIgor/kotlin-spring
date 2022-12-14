package com.igor101.kotlin.spring.inventory

interface InventoryRepository {

    fun existsById(id: String): Boolean

    fun save(inventory: Inventory)

    fun findById(id: String): Inventory?
}