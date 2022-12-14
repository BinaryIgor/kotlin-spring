package com.igor101.kotlin.spring.inventory

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import java.util.*

interface InventoryEntityRepository : CrudRepository<InventoryEntity, UUID> {

    @Query("SELECT value FROM inventory_filter_list WHERE inventory_id = :id")
    fun findFilterListByInventoryId(id: UUID): List<String>
}

@Table("inventory")
data class InventoryEntity(
    @Id val id: UUID,
    val online: Boolean,
    val filterType: String,
    @MappedCollection(idColumn = "inventory_id", keyColumn = "inventory_id")
    val filterList: Set<InventoryFilterList>,
    @Version
    val version: Int
) {

    fun toInventory(): Inventory {
        val filterValues = this.filterList.map { it.value }

        return Inventory(
            id = this.id.toString(),
            online = this.online,
            filter = if (this.filterType == InventoryFilterType.BRAND.name) {
                InventoryBrandFilter(filterValues)
            } else {
                InventorySkuFilter(filterValues)
            },
            version = this.version
        )
    }
}

@Table("inventory_filter_list")
data class InventoryFilterList(
    val value: String
)