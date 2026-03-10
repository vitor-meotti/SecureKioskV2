package com.securekioskv2.core.security

class BruteForceGuard {
    fun delayMsForAttempt(failedAttempts: Int): Long =
        (1000L * (1L shl (failedAttempts - 1).coerceAtLeast(0))).coerceAtMost(30_000L)

    fun shouldBlock(failedAttempts: Int): Boolean = failedAttempts >= 7
}
