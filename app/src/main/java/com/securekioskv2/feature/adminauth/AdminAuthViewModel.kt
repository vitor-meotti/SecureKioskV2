package com.securekioskv2.feature.adminauth

import androidx.lifecycle.ViewModel
import com.securekioskv2.core.security.AdminCredentialStore
import com.securekioskv2.core.security.AdminCredentialVerifier
import com.securekioskv2.core.security.BruteForceGuard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

fun interface ClockProvider {
    fun nowMs(): Long
}

class AdminAuthViewModel(
    private val guard: BruteForceGuard = BruteForceGuard(),
    private val credentialVerifier: AdminCredentialVerifier = AdminCredentialStore,
    private val clockProvider: ClockProvider = ClockProvider { System.currentTimeMillis() }
) : ViewModel() {

    private val _state = MutableStateFlow(AdminAuthState())
    val state: StateFlow<AdminAuthState> = _state.asStateFlow()

    private var lockoutUntilMs: Long = 0

    fun refreshLockoutState() {
        val remaining = (lockoutUntilMs - clockProvider.nowMs()).coerceAtLeast(0)
        if (remaining == 0L && _state.value.lockoutRemainingMs > 0) {
            _state.value = _state.value.copy(lockoutRemainingMs = 0, message = "")
            return
        }
        if (remaining > 0 && remaining != _state.value.lockoutRemainingMs) {
            _state.value = _state.value.copy(lockoutRemainingMs = remaining)
        }
    }

    fun authenticate(pin: String) {
        refreshLockoutState()
        val current = _state.value

        if (current.lockoutRemainingMs > 0) {
            _state.value = current.copy(message = "Aguarde o bloqueio temporário expirar.")
            return
        }

        if (!credentialVerifier.isConfigured()) {
            _state.value = current.copy(message = "Credencial admin não configurada. Conclua o onboarding.")
            return
        }

        val valid = credentialVerifier.verify(pin)
        if (valid) {
            lockoutUntilMs = 0
            _state.value = current.copy(
                authenticated = true,
                failedAttempts = 0,
                lockoutRemainingMs = 0,
                message = "Autenticado com sucesso"
            )
            return
        }

        val failed = current.failedAttempts + 1
        val delay = guard.calculateDelayMs(failed)
        val blocked = guard.isTemporarilyBlocked(failed)
        val remaining = if (blocked) {
            lockoutUntilMs = clockProvider.nowMs() + delay
            delay
        } else {
            lockoutUntilMs = 0
            0
        }

        _state.value = current.copy(
            authenticated = false,
            failedAttempts = failed,
            lockoutRemainingMs = remaining,
            message = if (blocked) "Bloqueado temporariamente por segurança." else "PIN inválido"
        )
    }
}
