package dev.traydr.geef.domain

data class User(
    val id: Long? = null,
    val publicUUID: String,
    val email: String,
    val username: String? = null,
    val password: String? = null,
    val bio: String? = null
)