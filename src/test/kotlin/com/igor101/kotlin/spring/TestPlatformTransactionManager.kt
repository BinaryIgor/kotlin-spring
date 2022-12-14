package com.igor101.kotlin.spring

import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.DefaultTransactionStatus
import javax.sql.DataSource

class TestPlatformTransactionManager(dataSource: DataSource) : DataSourceTransactionManager(dataSource) {

    private var afterCommit: () -> Unit = {}
    var afterCommitCalled = false
        private set

    fun afterCommit(check: () -> Unit) {
        afterCommit = check
        afterCommitCalled = false
    }

    override fun doCommit(status: DefaultTransactionStatus) {
        super.doCommit(status)
        afterCommit()
        afterCommitCalled = true
    }

    fun clear() {
        afterCommit = {}
    }
}