package com.igor101.kotlin.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

//@Configuration
class JdbcConfig {

    @Bean
    fun jdbcTemplate(source: DataSource) = JdbcTemplate(source)
}