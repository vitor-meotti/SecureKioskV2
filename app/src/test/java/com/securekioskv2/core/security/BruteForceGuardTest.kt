package com.securekioskv2.core.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BruteForceGuardTest {
    @Test
    fun growsAndBlocks() {
        val guard = BruteForceGuard()
        assertEquals(1_000L, guard.delayMsForAttempt(1))
        assertEquals(2_000L, guard.delayMsForAttempt(2))
        assertTrue(guard.shouldBlock(7))
    }

    @Test
    fun delayIsCappedForVeryLargeAttempts() {
        val guard = BruteForceGuard()
        assertEquals(30_000L, guard.delayMsForAttempt(128))
    }
}
