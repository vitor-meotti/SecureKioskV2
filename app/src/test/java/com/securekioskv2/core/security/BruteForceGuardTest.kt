package com.securekioskv2.core.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BruteForceGuardTest {
    @Test fun growsAndBlocks() {
        val g = BruteForceGuard()
        assertEquals(1000, g.delayMsForAttempt(1))
        assertTrue(g.shouldBlock(7))
    }
}
