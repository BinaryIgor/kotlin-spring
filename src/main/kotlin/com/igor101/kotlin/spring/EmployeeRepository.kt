package com.igor101.kotlin.spring

interface EmployeeRepository {

    fun allEmployees(): List<Employee>

    fun createEmployee(employee: Employee)
}