package com.igor101.kotlin.spring

import com.igor101.kotlin.spring.inventory.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.sql.DataSource

@ActiveProfiles("integration")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [TestConfig::class]
)
class KotlinSpringApplicationTests {

    @Autowired
    lateinit var repository: SqlInventoryRepository

    @Autowired
    lateinit var entityRepository: InventoryEntityRepository

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var transactionManager: TestPlatformTransactionManager

    @AfterEach
    fun clear() {
        transactionManager.clear()
    }

    @Test
    fun `should upsert inventory and increment version in transaction`() {
        val inventoryId = randomInventoryId()
        val newInventory = Inventory(
            id = inventoryId,
            online = true,
            filter = InventorySkuFilter(listOf("12", "23", "31")),
            version = 0
        )

        transactionManager.afterCommit {
            Assertions.assertThat(repository.findById(inventoryId))
                .isEqualTo(newInventory.copy(version = 1))
        }

        repository.save(newInventory)

        Assertions.assertThat(transactionManager.afterCommitCalled).isTrue

        val updatedInventory = newInventory.copy(
            filter = InventoryBrandFilter(listOf("Nike", "Adidas")),
            version = 1
        )

        transactionManager.afterCommit {
            Assertions.assertThat(repository.findById(inventoryId))
                .isEqualTo(updatedInventory.copy(version = 2))
        }

        Assertions.assertThat(transactionManager.afterCommitCalled).isFalse

        repository.save(updatedInventory)

        Assertions.assertThat(transactionManager.afterCommitCalled).isTrue
    }

    @Test
    fun `should throw exception with optimistic lock conflict`() {
        val inventoryId = randomInventoryId()
        val newInventory = Inventory(
            id = inventoryId,
            online = true,
            filter = InventorySkuFilter(listOf("12", "23", "31")),
            version = 0
        )

        repository.save(newInventory)
        repository.save(newInventory.copy(version = 1, online = false))

        Assertions.assertThatThrownBy { repository.save(newInventory.copy(version = 1)) }
            .isInstanceOf(OptimisticLockingFailureException::class.java)
    }

    private fun randomInventoryId() = UUID.randomUUID().toString()

}

@TestConfiguration
class TestConfig {

    @Bean
    fun transactionManager(dataSource: DataSource): TestPlatformTransactionManager {
        return TestPlatformTransactionManager(dataSource)
    }
}
