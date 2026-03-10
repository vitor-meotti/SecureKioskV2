package com.securekioskv2.core.security

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PinHasher(
    private val iterations: Int = 120_000,
    private val keyLengthBits: Int = 256,
    private val saltSize: Int = 16
) {
    fun create(pin: String): PinHashResult {
        require(pin.length >= 6) { "PIN deve possuir ao menos 6 dígitos." }
        val salt = ByteArray(saltSize).also { SecureRandom().nextBytes(it) }
        val hash = derive(pin.toCharArray(), salt)
        return PinHashResult(salt = salt, hash = hash, iterations = iterations)
    }

    fun verify(pin: String, salt: ByteArray, expectedHash: ByteArray): Boolean {
        val computed = derive(pin.toCharArray(), salt)
        return MessageDigest.isEqual(computed, expectedHash)
    }

    private fun derive(value: CharArray, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(value, salt, iterations, keyLengthBits)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
            value.fill('\u0000')
        }
    }
}

data class PinHashResult(
    val salt: ByteArray,
    val hash: ByteArray,
    val iterations: Int
)
