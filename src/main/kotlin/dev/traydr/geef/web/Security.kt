package dev.traydr.geef.web

import dev.traydr.geef.domain.Token
import dev.traydr.geef.domain.service.TokenService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Application.configureSecurity() {
    val tokenService by inject<TokenService>()

    data class UserSession(val id: Long, val value: String) : Principal
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60 // 7 days
            cookie.extensions["SameSite"] = "strict"
            cookie.secure = true
        }
    }

    install(Authentication) {
        session<UserSession>("auth-session") {
            validate { session ->
                val token: Token? = tokenService.getTokenById(session.id)

                if (token == null) {
                    null
                } else if (token.expiryDate?.isBefore(LocalDateTime.now()) == true) {
                    null
                } else if (token.value != session.value) {
                    null
                } else {
                    tokenService.refreshToken(token)
                    session
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}
