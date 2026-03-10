package com.securekioskv2.feature.adminauth

import com.securekioskv2.core.security.AdminSecurityRepository
import com.securekioskv2.core.security.BruteForceGuard
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    @Test
    fun lockoutIsPersistedInRepo() {
        val repo = FakeRepo()
        var now = 1_000L
        val vm = AdminAuthViewModel(repo, BruteForceGuard(), TimeProvider { now })

        repeat(7) { attempt ->
            vm.authenticate("000000".toCharArray())
            if (attempt < 6) {
                now += vm.state.value.lockoutRemainingMs + 1
            }
        }

        assertTrue(repo.getLockoutUntilMs() > now)
        assertTrue(vm.state.value.lockoutRemainingMs > 0)
    }

    @Test
    fun loadsPersistedLockoutOnInit() {
        val repo = FakeRepo().apply {
            failed = 3
            lockoutUntil = 2_000L
        }
        val vm = AdminAuthViewModel(repo, BruteForceGuard(), TimeProvider { 1_000L })

        assertEquals(3, vm.state.value.failedAttempts)
        assertEquals(1_000L, vm.state.value.lockoutRemainingMs)
    }

    @Test
    fun successfulAuthResetsCounters() {
        val repo = FakeRepo().apply {
            failed = 4
            lockoutUntil = 0
        }
        val vm = AdminAuthViewModel(repo, BruteForceGuard(), TimeProvider { 10_000L })

        vm.authenticate("123456".toCharArray())

        assertTrue(vm.state.value.authenticated)
        assertEquals(0, repo.getFailedAttempts())
        assertEquals(0L, repo.getLockoutUntilMs())
    }

    @Test
    fun lockoutExpiresWhenTimeAdvances() {
        val repo = FakeRepo().apply {
            failed = 7
            lockoutUntil = 2_000L
        }
        var now = 1_000L
        val vm = AdminAuthViewModel(repo, BruteForceGuard(), TimeProvider { now })

        vm.refreshLockout()
        assertTrue(vm.state.value.lockoutRemainingMs > 0)

        now = 2_100L
        vm.refreshLockout()

        assertEquals(0L, vm.state.value.lockoutRemainingMs)
    }

    @Test
    fun notConfiguredBlocksAuth() {
        val repo = FakeRepo().apply { configured = false }
        val vm = AdminAuthViewModel(repo, BruteForceGuard(), TimeProvider { 1_000L })

        vm.authenticate("123456".toCharArray())

        assertFalse(vm.state.value.authenticated)
        assertTrue(vm.state.value.message.contains("não configurada"))
    }
}
