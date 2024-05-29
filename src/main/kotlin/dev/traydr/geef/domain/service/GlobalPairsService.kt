package dev.traydr.geef.domain.service

import dev.traydr.geef.domain.GlobalPair
import dev.traydr.geef.domain.repository.GlobalPairsRepository

class GlobalPairsService(private val globalPairsRepository: GlobalPairsRepository) {

    fun get(key: String): GlobalPair? {
        return globalPairsRepository.findByKey(key)
    }

    fun create(key: String, value: String): Long {
        return globalPairsRepository.create(key, value)
    }

    fun update(key: String, value: String) {
        globalPairsRepository.update(key, value)
    }
}