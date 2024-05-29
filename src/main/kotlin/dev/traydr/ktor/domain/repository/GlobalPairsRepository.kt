package dev.traydr.ktor.domain.repository

import dev.traydr.ktor.domain.GlobalPair
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal object GlobalPairs : LongIdTable() {
    val key = varchar("key", 255).uniqueIndex()
    val value: Column<String> = varchar("value", 255)

    fun toDomain(row: ResultRow): GlobalPair {
        return GlobalPair(
            id = row[id].value,
            key = row[key],
            value = row[value],
        )
    }
}

class GlobalPairsRepository {
    init {
        transaction {
            SchemaUtils.create(GlobalPairs)
        }
    }

    fun findByKey(keyIn: String): GlobalPair? {
        return transaction {
            GlobalPairs.select {
                GlobalPairs.key eq keyIn
            }
                .map { GlobalPairs.toDomain(it) }
                .firstOrNull()
        }
    }

    fun create(keyIn: String, valueIn: String): Long {
        return transaction {
            GlobalPairs.insertAndGetId { row ->
                row[key] = keyIn
                row[value] = valueIn
            }.value
        }
    }

    fun update(keyIn: String, valueIn: String) {
        transaction {
            GlobalPairs.update({ GlobalPairs.key eq keyIn }) { row ->
                row[value] = valueIn
            }
        }
    }
}