package dev.traydr.geef.domain.service

import dev.traydr.geef.domain.User
import dev.traydr.geef.domain.repository.UserRepository
import dev.traydr.geef.utils.Cipher

class UserService(private val userRepository: UserRepository) {

    fun create(user: User): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Email already registered!" }
        }
        userRepository.create(user.copy(password = user.password?.let { Cipher.encrypt(it) }))
        return user.copy() // token = generateJwtToken(user)
    }

    fun getAllUsers(): List<User> {
        return userRepository.getAll()
    }
}