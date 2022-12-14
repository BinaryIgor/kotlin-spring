package com.igor101.kotlin.spring

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcDepartmentRepository(val jdbcTemplate: JdbcTemplate) : DepartmentRepository {

    override fun allDepartments(): List<Department> {
        val departmentsEmployees = HashMap<String, MutableList<Employee>>()

        jdbcTemplate.query(
            """
            SELECT d.*, e.id, e.name, e.seniority, e.salary FROM department d
            INNER JOIN employee e
            ON d.name = e.department
        """.trimIndent()
        ) { rs ->
            val employee = Employee(
                rs.getLong("e.id"),
                rs.getString("e.name"),
                Seniority.valueOf(rs.getString("e.seniority")),
                rs.getInt("salary")
            )

            departmentsEmployees.computeIfAbsent(rs.getString("d.name"),
                { k -> ArrayList() }).add(employee)
        }

        return departmentsEmployees.toList()
            .map { Department(it.first, it.second) }
    }
}