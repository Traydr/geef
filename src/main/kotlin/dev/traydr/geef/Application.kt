package dev.traydr.geef

import dev.traydr.geef.config.DbConfig
import dev.traydr.geef.domain.repository.FileRepository
import dev.traydr.geef.domain.repository.TokenRepository
import dev.traydr.geef.domain.repository.UserRepository
import dev.traydr.geef.domain.service.TokenService
import dev.traydr.geef.domain.service.UserService
import dev.traydr.geef.web.configureRouting
import dev.traydr.geef.web.configureSecurity
import dev.traydr.geef.web.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.minio.MakeBucketArgs
import org.koin.ktor.plugin.Koin
import io.minio.MinioClient;
import java.util.UUID

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
    }

    val serviceModule = org.koin.dsl.module {
        single { TokenService(get(), get()) }
        single { UserService(get()) }
    }

    val minioUrl = System.getenv("MINIO_URL")?: "0.0.0.0"
    val minioUsername = System.getenv("MINIO_USERNAME")?: "minio"
    val minioPassword = System.getenv("MINIO_PASSWORD")?: ""

    val minioClient: MinioClient = MinioClient.builder()
        .endpoint(minioUrl)
        .credentials(minioUsername, minioPassword)
        .build()

    val fileModule = org.koin.dsl.module {
        single { FileRepository(minioClient) }
    }

    install(Koin) {
        modules(repoModule, serviceModule, fileModule)
    }

    val postgresIp = System.getenv("PG_IP")?: "0.0.0.0"
    val postgresPort = System.getenv("PG_PORT")?: "5432"
    val postgresUsername = System.getenv("PG_USERNAME")?: "postgres"
    val postgresPassword = System.getenv("PG_PASSWORD")?: ""
    DbConfig.setup(
        "jdbc:postgresql://$postgresIp:$postgresPort/postgres",
        postgresUsername,
        postgresPassword
    )

    configureSecurity()
    configureSerialization()
    configureRouting()
}
