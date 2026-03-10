package com.securekioskv2.core.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BruteForceGuardTest {
    private val subject = BruteForceGuard(baseDelayMs = 1_000, maxDelayMs = 8_000, blockThreshold = 5)

    @Test
    fun `delay deve crescer progressivamente`() {
        assertEquals(0, subject.calculateDelayMs(0))
        assertEquals(1_000, subject.calculateDelayMs(1))
        assertEquals(2_000, subject.calculateDelayMs(2))
        assertEquals(4_000, subject.calculateDelayMs(3))
        assertEquals(8_000, subject.calculateDelayMs(4))
    }

    @Test
    fun `delay deve respeitar teto maximo`() {
        assertEquals(8_000, subject.calculateDelayMs(10))
    }

    @Test
    fun `bloqueio temporario respeita threshold`() {
        assertFalse(subject.isTemporarilyBlocked(4))
        assertTrue(subject.isTemporarilyBlocked(5))
    }
}
