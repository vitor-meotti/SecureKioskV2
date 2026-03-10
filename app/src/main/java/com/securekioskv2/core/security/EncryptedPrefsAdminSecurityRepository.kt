package com.securekioskv2.core.security

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedPrefsAdminSecurityRepository(context: Context) : AdminSecurityRepository {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_kiosk_admin",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val hasher = PinHasher()

    override fun isConfigured(): Boolean = prefs.contains(PIN_HASH) && prefs.contains(PIN_SALT)

    override fun configurePin(pin: CharArray) {
        val result = hasher.create(pin)
        prefs.edit()
            .putString(PIN_HASH, Base64.encodeToString(result.hash, Base64.NO_WRAP))
            .putString(PIN_SALT, Base64.encodeToString(result.salt, Base64.NO_WRAP))
            .putInt(FAILED_ATTEMPTS, 0)
            .putLong(LOCKOUT_UNTIL_MS, 0L)
            .apply()
    }

    override fun verifyPin(pin: CharArray): Boolean {
        val hash = prefs.getString(PIN_HASH, null)?.decodeBase64OrNull() ?: return false
        val salt = prefs.getString(PIN_SALT, null)?.decodeBase64OrNull() ?: return false
        return hasher.verify(pin, hash, salt)
    }

    override fun getFailedAttempts(): Int = prefs.getInt(FAILED_ATTEMPTS, 0)

    override fun setFailedAttempts(value: Int) {
        prefs.edit().putInt(FAILED_ATTEMPTS, value).apply()
    }

    override fun getLockoutUntilMs(): Long = prefs.getLong(LOCKOUT_UNTIL_MS, 0L)

    override fun setLockoutUntilMs(value: Long) {
        prefs.edit().putLong(LOCKOUT_UNTIL_MS, value).apply()
    }

    private fun String.decodeBase64OrNull(): ByteArray? =
        runCatching { Base64.decode(this, Base64.NO_WRAP) }.getOrNull()

    private companion object {
        const val PIN_HASH = "pin_hash"
        const val PIN_SALT = "pin_salt"
        const val FAILED_ATTEMPTS = "failed_attempts"
        const val LOCKOUT_UNTIL_MS = "lockout_until"
    }
}
