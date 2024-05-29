package dev.traydr.ktor.domain.repository

import dev.traydr.ktor.domain.Token
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

internal object Tokens : LongIdTable() {
    val userId = reference("userId", Users.id).uniqueIndex()
    val value: Column<String> = varchar("value", 255).uniqueIndex()
    val expiryDate = datetime("expiryDate")

    fun toDomain(row: ResultRow): Token {
        return Token(
            id = row[Tokens.id].value,
            uid = row[userId].value,
            value = row[value],
            expiryDate = row[expiryDate]
        )
    }
}

class TokenRepository {
    init {
        transaction {
            SchemaUtils.create(Tokens)
        }
    }

    fun findTokenByEmail(email: String): Token? {
        return transaction {
            (Tokens innerJoin Users)
                .select { Users.email eq email }
                .map { Tokens.toDomain(it) }
                .firstOrNull()
        }
    }

    fun findTokenByToken(value: String): Token? {
        return transaction {
            Tokens.select {
                Tokens.value eq value
            }
                .map { Tokens.toDomain(it) }
                .firstOrNull()
        }
    }

    fun findTokenById(id: Long): Token? {
        return transaction {
            Tokens.select {
                Tokens.id eq id
            }
                .map { Tokens.toDomain(it) }
                .firstOrNull()
        }
    }

    fun create(token: Token): Long {
        return transaction {
            Tokens.insertAndGetId { row ->
                row[userId] = token.uid!!
                row[value] = token.value!!
                row[expiryDate] = token.expiryDate!!
            }.value
        }
    }

    fun update(id: Long, token: Token): Token? {
        transaction {
            Tokens.update({ Tokens.id eq id }) { row ->
                if (token.value != null) {
                    row[value] = token.value
                }

                if (token.expiryDate != null) {
                    row[expiryDate] = token.expiryDate
                }
            }
        }
        return findTokenById(id)
    }
}