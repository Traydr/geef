package dev.traydr.geef.domain.repository

import dev.traydr.geef.domain.User
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal object Users : LongIdTable() {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val username: Column<String> = varchar("username", 100).uniqueIndex()
    val password: Column<String> = varchar("password", 150)
    val bio: Column<String?> = varchar("bio", 255).nullable()

    fun toDomain(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            email = row[email],
            username = row[username],
            password = row[password],
            bio = row[bio]
        )
    }
}

class UserRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun getAll(): List<User> {
        return transaction {
            Users.selectAll()
                .map { Users.toDomain(it) }
        }
    }

    fun findByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun findByUsername(username: String): User? {
        return transaction {
            Users.select { Users.username eq username }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun create(user: User): Long? {
        return transaction {
            Users.insertAndGetId { row ->
                row[email] = user.email
                row[username] = user.username!!
                row[password] = user.password!!
                row[bio] = user.bio
            }.value
        }
    }

    fun update(email: String, user: User): User? {
        transaction {
            Users.update({ Users.email eq email }) { row ->
                row[Users.email] = user.email
                if (user.username != null) {
                    row[username] = user.username
                }
                if (user.password != null) {
                    row[password] = user.password
                }
                if (user.bio != null) {
                    row[bio] = user.bio
                }
            }
        }
        return findByEmail(user.email)
    }
}