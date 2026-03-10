package com.securekioskv2.core.security

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PinHasher {
    fun create(pin: CharArray): PinHashResult {
        require(pin.size >= 6)
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hash = derive(pin, salt)
        return PinHashResult(hash, salt)
    }

    fun verify(pin: CharArray, expectedHash: ByteArray, salt: ByteArray): Boolean {
        val computed = derive(pin, salt)
        return MessageDigest.isEqual(computed, expectedHash)
    }

    private fun derive(pin: CharArray, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(pin, salt, 120_000, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
            pin.fill('\u0000')
        }
    }
}

data class PinHashResult(val hash: ByteArray, val salt: ByteArray)
