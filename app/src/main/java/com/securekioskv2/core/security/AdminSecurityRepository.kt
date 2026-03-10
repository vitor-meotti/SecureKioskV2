package com.securekioskv2.core.security

interface AdminSecurityRepository {
    fun isConfigured(): Boolean
    fun configurePin(pin: CharArray)
    fun verifyPin(pin: CharArray): Boolean
    fun getFailedAttempts(): Int
    fun setFailedAttempts(value: Int)
    fun getLockoutUntilMs(): Long
    fun setLockoutUntilMs(value: Long)
}
