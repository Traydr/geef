package dev.traydr.geef.domain.repository

import dev.traydr.geef.domain.Post
import dev.traydr.geef.domain.User
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

internal object Posts : LongIdTable() {
    val userId = reference("userId", Users.id)
    val publicUUID: Column<String> = varchar("publicUUID", 36).uniqueIndex()
    val extension: Column<String> = varchar("extension", 64)
    val etag: Column<String> = varchar("etag", 255).uniqueIndex()
    val title: Column<String> = varchar("title", 40)
    val body: Column<String> = varchar("body", 255)

    fun toDomain(row: ResultRow): Post {
        return Post(
            id = row[Posts.id].value,
            author = row[Posts.userId].value,
            publicUUID = row[publicUUID],
            extension = row[extension],
            etag = row[etag],
            title = row[title],
            body = row[body],
        )
    }
}

class PostRepository {
    init {
        transaction {
            SchemaUtils.create(Posts)
        }
    }

    fun findAllPostsByUserId(id: Long): List<Post> {
        return transaction {
            Posts.select { Posts.userId eq id }
                .map { Posts.toDomain(it) }
        }
    }

    fun findPostById(id: Long): Post? {
        return transaction {
            Posts.select(Posts.id eq id)
                .map { Posts.toDomain(it) }
                .firstOrNull()
        }
    }

    fun create(post: Post): Long {
        return transaction {
            Posts.insertAndGetId { row ->
                row[userId] = post.author!!
                row[publicUUID] = post.publicUUID
                row[extension] = post.extension!!
                row[etag] = post.etag!!
                row[title] = post.title!!
                row[body] = post.body!!
            }.value
        }
    }
}