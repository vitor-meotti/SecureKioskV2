package com.securekioskv2.core.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PinHasherTest {
    @Test fun verifyWorks() {
        val h = PinHasher(); val res = h.create("123456".toCharArray())
        assertTrue(h.verify("123456".toCharArray(), res.hash, res.salt))
        assertFalse(h.verify("000000".toCharArray(), res.hash, res.salt))
    }
}
