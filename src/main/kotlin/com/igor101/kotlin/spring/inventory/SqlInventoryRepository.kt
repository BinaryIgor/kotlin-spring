package com.igor101.kotlin.spring.inventory

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SqlInventoryRepository(
    private val inventoryEntityRepository: InventoryEntityRepository
) : InventoryRepository {

    override fun existsById(id: String) = inventoryEntityRepository.existsById(UUID.fromString(id))

    override fun save(inventory: Inventory) {
        val entity = toInventoryEntity(inventory)
        inventoryEntityRepository.save(entity)
    }

    private fun toInventoryEntity(inventory: Inventory): InventoryEntity {
        val inventoryId = UUID.fromString(inventory.id)

        val inventoryFilterList = when (inventory.filter) {
            is InventorySkuFilter -> inventory.filter.skus.map { InventoryFilterList(it) }.toSet()
            is InventoryBrandFilter -> inventory.filter.brands.map { InventoryFilterList(it) }.toSet()
        }

        return InventoryEntity(
            id = inventoryId,
            online = inventory.online,
            filterType = inventory.filter.type.name,
            filterList = inventoryFilterList,
            version = inventory.version
        )
    }

    override fun findById(id: String): Inventory? {
        return inventoryEntityRepository.findByIdOrNull(UUID.fromString(id))?.toInventory()
    }
}