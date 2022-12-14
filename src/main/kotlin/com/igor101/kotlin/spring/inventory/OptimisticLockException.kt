package com.igor101.kotlin.spring.inventory

class OptimisticLockException(override val message: String) : RuntimeException(message)