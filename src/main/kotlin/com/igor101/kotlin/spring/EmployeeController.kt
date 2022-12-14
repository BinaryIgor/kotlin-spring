package com.igor101.kotlin.spring

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/employees")
class EmployeeController(val repository: EmployeeRepository) {

    @GetMapping
    fun employees() = repository.allEmployees()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEmployee(@RequestBody employee: Employee) {
        repository.createEmployee(employee)
    }
}