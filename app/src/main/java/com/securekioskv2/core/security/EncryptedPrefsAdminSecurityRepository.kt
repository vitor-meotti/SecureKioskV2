package com.securekioskv2.core.security

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedPrefsAdminSecurityRepository(context: Context) : AdminSecurityRepository {
    private val prefs = EncryptedSharedPreferences.create(
        "secure_kiosk_admin",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val hasher = PinHasher()

    override fun isConfigured(): Boolean = prefs.contains("pin_hash") && prefs.contains("pin_salt")

    override fun configurePin(pin: CharArray) {
        val result = hasher.create(pin)
        prefs.edit()
            .putString("pin_hash", Base64.encodeToString(result.hash, Base64.NO_WRAP))
            .putString("pin_salt", Base64.encodeToString(result.salt, Base64.NO_WRAP))
            .putInt("failed_attempts", 0)
            .putLong("lockout_until", 0L)
            .apply()
    }

    override fun verifyPin(pin: CharArray): Boolean {
        val hash = prefs.getString("pin_hash", null) ?: return false
        val salt = prefs.getString("pin_salt", null) ?: return false
        return hasher.verify(
            pin,
            Base64.decode(hash, Base64.NO_WRAP),
            Base64.decode(salt, Base64.NO_WRAP)
        )
    }

    override fun getFailedAttempts(): Int = prefs.getInt("failed_attempts", 0)
    override fun setFailedAttempts(value: Int) { prefs.edit().putInt("failed_attempts", value).apply() }
    override fun getLockoutUntilMs(): Long = prefs.getLong("lockout_until", 0L)
    override fun setLockoutUntilMs(value: Long) { prefs.edit().putLong("lockout_until", value).apply() }
}
