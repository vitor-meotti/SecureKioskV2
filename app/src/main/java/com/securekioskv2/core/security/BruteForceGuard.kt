package com.securekioskv2.core.security

class BruteForceGuard(
    private val baseDelayMs: Long = 1_000L,
    private val maxDelayMs: Long = 30_000L,
    private val blockThreshold: Int = 7
) {
    fun delayMsForAttempt(failedAttempts: Int): Long {
        val safeAttempts = failedAttempts.coerceAtLeast(1)
        val exponent = (safeAttempts - 1).coerceAtMost(MAX_SHIFT)
        val multiplier = 1L shl exponent
        return (baseDelayMs * multiplier).coerceAtMost(maxDelayMs)
    }

    fun shouldBlock(failedAttempts: Int): Boolean = failedAttempts >= blockThreshold

    private companion object {
        const val MAX_SHIFT = 20
    }
}
