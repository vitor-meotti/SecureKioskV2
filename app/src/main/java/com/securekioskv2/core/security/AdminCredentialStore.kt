package com.securekioskv2.core.security

import java.util.concurrent.atomic.AtomicReference

interface AdminCredentialVerifier {
    fun isConfigured(): Boolean
    fun verify(pin: String): Boolean
}

object AdminCredentialStore : AdminCredentialVerifier {
    private val hasher = PinHasher()
    private val reference = AtomicReference<PinHashResult?>(null)

    override fun isConfigured(): Boolean = reference.get() != null

    fun configure(pin: String) {
        reference.set(hasher.create(pin))
    }

    override fun verify(pin: String): Boolean {
        val secret = reference.get() ?: return false
        return hasher.verify(pin, secret.salt, secret.hash)
    }

    fun clearForTests() {
        reference.set(null)
    }
}
