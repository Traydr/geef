package dev.traydr.ktor.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.h2.tools.Server
import org.jetbrains.exposed.sql.Database

object DbConfig {
    fun setup(jdbcUrl: String, username: String, password: String) {
        Server.createPgServer().start()
        val config = HikariConfig().also { config ->
            config.jdbcUrl = jdbcUrl
            config.username = username
            config.password = password
            config.driverClassName = org.postgresql.Driver::class.java.canonicalName
            config.initializationFailTimeout = 3000
        }
        Database.connect(HikariDataSource(config))
    }
}