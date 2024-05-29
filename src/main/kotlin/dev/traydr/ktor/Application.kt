package dev.traydr.ktor

import dev.traydr.ktor.config.DbConfig
import dev.traydr.ktor.domain.repository.GlobalPairsRepository
import dev.traydr.ktor.domain.repository.TokenRepository
import dev.traydr.ktor.domain.repository.UserRepository
import dev.traydr.ktor.domain.service.GlobalPairsService
import dev.traydr.ktor.domain.service.TokenService
import dev.traydr.ktor.domain.service.UserService
import dev.traydr.ktor.web.configureRouting
import dev.traydr.ktor.web.configureSecurity
import dev.traydr.ktor.web.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin

fun main() {
    val port = (System.getenv("PORT")?: "8080").toInt()

    if (port < 1 || port > 65530) {
        println("Invalid port, please enter valid range from 1 to 65530")
        return
    }

    embeddedServer(
        Netty,
        port = port,
        host = "0.0.0.0",
        module = Application::module,
        watchPaths = listOf("classes", "resources")
    )
        .start(wait = true)
}

fun Application.module() {
    val repoModule = org.koin.dsl.module {
        single { UserRepository() }
        single { TokenRepository() }
        single { GlobalPairsRepository() }
    }

    val serviceModule = org.koin.dsl.module {
        single { TokenService(get(), get()) }
        single { UserService(get()) }
        single { GlobalPairsService(get()) }
    }

    install(Koin) {
        modules(repoModule, serviceModule)
    }

    val postgresIp = System.getenv("PG_IP")?: "0.0.0.0"
    val postgresUsername = System.getenv("PG_USERNAME")?: "postgres"
    val postgresPassword = System.getenv("PG_PASSWORD")?: ""
    DbConfig.setup(
        "jdbc:postgresql://$postgresIp:5432/postgres",
        postgresUsername,
        postgresPassword
    )

    configureSecurity()
    configureSerialization()
    configureRouting()
}
