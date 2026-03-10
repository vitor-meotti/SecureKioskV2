package com.securekioskv2.feature.adminauth

data class AdminAuthState(
    val failedAttempts: Int = 0,
    val lockoutRemainingMs: Long = 0,
    val authenticated: Boolean = false,
    val message: String = ""
)
