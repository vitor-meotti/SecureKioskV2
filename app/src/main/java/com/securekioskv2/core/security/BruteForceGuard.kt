package com.securekioskv2.core.security

class BruteForceGuard(
    private val baseDelayMs: Long = 1_000,
    private val maxDelayMs: Long = 30_000,
    private val blockThreshold: Int = 7
) {
    fun calculateDelayMs(failedAttempts: Int): Long {
        if (failedAttempts <= 0) return 0
        val exp = (failedAttempts - 1).coerceAtMost(10)
        val delay = baseDelayMs * (1L shl exp)
        return delay.coerceAtMost(maxDelayMs)
    }

    fun isTemporarilyBlocked(failedAttempts: Int): Boolean = failedAttempts >= blockThreshold
}
