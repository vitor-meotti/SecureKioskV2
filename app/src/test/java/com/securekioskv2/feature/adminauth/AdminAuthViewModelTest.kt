package com.securekioskv2.feature.adminauth

import com.securekioskv2.core.security.AdminSecurityRepository
import com.securekioskv2.core.security.BruteForceGuard
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakeRepo : AdminSecurityRepository {
    var configured = true
    var failed = 0
    var lockoutUntil = 0L
    override fun isConfigured() = configured
    override fun configurePin(pin: CharArray) {}
    override fun verifyPin(pin: CharArray) = pin.concatToString() == "123456"
    override fun getFailedAttempts() = failed
    override fun setFailedAttempts(value: Int) { failed = value }
    override fun getLockoutUntilMs() = lockoutUntil
    override fun setLockoutUntilMs(value: Long) { lockoutUntil = value }
}

class AdminAuthViewModelTest {
    @Test fun lockoutIsPersistedInRepo() {
        val repo = FakeRepo(); val vm = AdminAuthViewModel(repo, BruteForceGuard())
        repeat(7) { vm.authenticate("000000".toCharArray()) }
        assertTrue(repo.getLockoutUntilMs() > 0)
    }
}
