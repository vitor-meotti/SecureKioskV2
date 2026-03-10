package com.securekioskv2.feature.adminauth

data class AdminAuthState(
    val authenticated: Boolean = false,
    val failedAttempts: Int = 0,
    val lockoutRemainingMs: Long = 0,
    val message: String = ""
)
