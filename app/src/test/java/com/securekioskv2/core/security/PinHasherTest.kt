package com.securekioskv2.core.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PinHasherTest {
    private val subject = PinHasher()

    @Test
    fun `deve validar pin correto`() {
        val result = subject.create("123456")
        assertTrue(subject.verify("123456", result.salt, result.hash))
    }

    @Test
    fun `deve rejeitar pin incorreto`() {
        val result = subject.create("123456")
        assertFalse(subject.verify("111111", result.salt, result.hash))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `deve falhar para pin curto`() {
        subject.create("123")
    }
}
