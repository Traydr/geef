package dev.traydr.geef.utils

import at.favre.lib.crypto.bcrypt.BCrypt

object Cipher {
    fun encrypt(data: String): String = BCrypt.withDefaults().hashToString(12, data.toCharArray())

    fun validate(password: String, hashed: String): Boolean = BCrypt.verifyer().verify(password.toCharArray(), hashed).verified;
}
