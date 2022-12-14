package com.igor101.kotlin.spring

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/webhooks")
class WebhookController {

    @PostMapping("/alert")
    fun alertManager(@RequestBody alert: Map<String, Any?>) {
        println("New alert received!")
        alert.forEach{ (k, v) -> println("$k-$v") }
        println()
    }
}