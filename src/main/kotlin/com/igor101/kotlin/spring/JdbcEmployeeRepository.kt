package com.igor101.kotlin.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcEmployeeRepository(val jdbcTemplate: JdbcTemplate) : EmployeeRepository {

    override fun allEmployees(): List<Employee> = jdbcTemplate.query("SELECT * FROM employee") { it, _ ->
        Employee(
            it.getLong("id"),
            it.getString("name"),
            Seniority.valueOf(it.getString("seniority")),
            it.getInt("salary")
        )
    }

    override fun createEmployee(employee: Employee) {
        jdbcTemplate.update(
            "INSERT INTO employee (name, seniority, salary) values (?, ?, ?)",
            employee.name, employee.seniority.name, employee.salary
        )
    }
}