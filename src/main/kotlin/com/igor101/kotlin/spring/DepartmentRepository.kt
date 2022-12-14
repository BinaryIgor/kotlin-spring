package com.igor101.kotlin.spring

interface DepartmentRepository {
    fun allDepartments(): List<Department>
}