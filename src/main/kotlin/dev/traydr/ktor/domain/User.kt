package dev.traydr.ktor.domain

data class User(
    val id: Long? = null,
    val token: String? = null,
    val email: String,
    val username: String? = null,
    val password: String? = null,
    val bio: String? = null
)