package dev.traydr.geef.domain

data class Post(
    val id: Long? = null,
    val publicUUID: String,
    val extension: String? = null,
    val etag: String? = null,
    val author: Long? = null,
    val title: String? = null,
    val body: String? = null,
)