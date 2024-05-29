package dev.traydr.geef.domain.service

import dev.traydr.geef.domain.Token
import dev.traydr.geef.domain.User
import dev.traydr.geef.domain.exceptions.UserNotFoundException
import dev.traydr.geef.domain.repository.TokenRepository
import dev.traydr.geef.domain.repository.UserRepository
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*

class TokenService(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) {
    fun createToken(): String {
        val base64Encoder: Base64.Encoder? = Base64.getEncoder()
        val secureRandom: SecureRandom = SecureRandom()
        var randomBytes: ByteArray = ByteArray(30)
        var stringToken: String

        do {
            // Not best practice, need to be in separate file, because secure random no resets on each
            // call to createToken()
            secureRandom.nextBytes(randomBytes)
            stringToken = base64Encoder!!.encodeToString(randomBytes)

        } while (tokenRepository.findTokenByToken(stringToken) != null)

        return stringToken
    }

    fun getTokenForEmail(email: String): Token {
        val user: User? = userRepository.findByEmail(email)
        if (user == null) {
            throw UserNotFoundException("Not able to find $email")
        }

        var token: Token? = tokenRepository.findTokenByEmail(email)
        if (token == null) {
            val tokenTemp: Token = Token(
                uid = user.id,
                value = createToken(),
                expiryDate = LocalDateTime.now().plusDays(7)
            )

            token = tokenRepository.findTokenById(tokenRepository.create(tokenTemp))
        }

        return token!!
    }

    fun refreshToken(token: Token) {
        var tokenCopy = token.copy()
        if (tokenCopy.expiryDate?.isBefore(LocalDateTime.now()) == true) {
            tokenCopy = tokenCopy.copy(value = createToken())
        }

        tokenCopy.id?.let {
            tokenRepository.update(
                it,
                tokenCopy.copy(expiryDate = LocalDateTime.now().plusDays(7))
            )
        }
    }

    fun getTokenById(id: Long): Token? {
        return tokenRepository.findTokenById(id)
    }
}