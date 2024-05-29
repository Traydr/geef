package dev.traydr.ktor.domain

import java.time.LocalDateTime

data class Token(
    val id: Long? = null,
    val uid: Long? = null,
    val value: String? = null,
    val expiryDate: LocalDateTime? = null,
)