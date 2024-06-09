package dev.traydr.geef.web

import dev.traydr.geef.domain.Token
import dev.traydr.geef.domain.service.TokenService
import dev.traydr.geef.domain.service.UserService
import dev.traydr.geef.utils.Cipher
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.html.InputType
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

data class UserSession(val id: Long, val value: String) : Principal

fun Application.configureSecurity() {
    val tokenService by inject<TokenService>()
    val userService by inject<UserService>()

    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60 // 7 days
            cookie.extensions["SameSite"] = "strict"
            cookie.secure = true
        }
    }

    install(Authentication) {
        form("auth-form") {
            userParamName = "email"
            passwordParamName = "password"
            validate { credentials ->
                val user = userService.getUserbyEmail(credentials.name)
                if (credentials.name == (user?.email ?: "") && Cipher.validate(
                        credentials.password,
                        user?.password!!
                    )
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
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
