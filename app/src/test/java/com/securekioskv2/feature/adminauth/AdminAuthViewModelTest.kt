package com.securekioskv2.feature.adminauth

import com.securekioskv2.core.security.AdminCredentialStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminAuthViewModelTest {
    @Test
    fun `autenticacao correta deve liberar sessao`() {
        AdminCredentialStore.clearForTests()
        AdminCredentialStore.configure("123456")
        val vm = AdminAuthViewModel()

        vm.authenticate("123456")

        assertTrue(vm.state.value.authenticated)
        assertEquals(0, vm.state.value.failedAttempts)
    }

    @Test
    fun `falhas consecutivas devem gerar bloqueio temporario com expiracao`() {
        AdminCredentialStore.clearForTests()
        AdminCredentialStore.configure("123456")

        var now = 1_000L
        val vm = AdminAuthViewModel(clockProvider = ClockProvider { now })

        repeat(7) { vm.authenticate("000000") }
        val initialLock = vm.state.value.lockoutRemainingMs

        assertTrue(initialLock > 0)
        assertEquals(7, vm.state.value.failedAttempts)
        assertFalse(vm.state.value.authenticated)

        now += initialLock + 1
        vm.refreshLockoutState()

        assertEquals(0, vm.state.value.lockoutRemainingMs)
    }

    @Test
    fun `sem credencial configurada deve bloquear autenticacao`() {
        AdminCredentialStore.clearForTests()
        val vm = AdminAuthViewModel()

        vm.authenticate("123456")

        assertFalse(vm.state.value.authenticated)
        assertEquals(0, vm.state.value.failedAttempts)
        assertTrue(vm.state.value.message.contains("não configurada"))
    }
}
