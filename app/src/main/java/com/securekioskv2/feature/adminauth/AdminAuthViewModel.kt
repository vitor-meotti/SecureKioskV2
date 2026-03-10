package com.securekioskv2.feature.adminauth

import androidx.lifecycle.ViewModel
import com.securekioskv2.core.security.AdminSecurityRepository
import com.securekioskv2.core.security.BruteForceGuard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AdminAuthViewModel @Inject constructor(
    private val repo: AdminSecurityRepository,
    private val guard: BruteForceGuard
) : ViewModel() {
    private val _state = MutableStateFlow(AdminAuthState())
    val state: StateFlow<AdminAuthState> = _state.asStateFlow()

    fun refreshLockout() {
        val remaining = (repo.getLockoutUntilMs() - System.currentTimeMillis()).coerceAtLeast(0)
        _state.value = _state.value.copy(lockoutRemainingMs = remaining, failedAttempts = repo.getFailedAttempts())
    }

    fun authenticate(pin: CharArray) {
        refreshLockout()
        if (_state.value.lockoutRemainingMs > 0) {
            pin.fill('\u0000')
            _state.value = _state.value.copy(message = "Aguarde o bloqueio temporário expirar.")
            return
        }
        if (!repo.isConfigured()) {
            pin.fill('\u0000')
            _state.value = _state.value.copy(message = "Credencial admin não configurada.")
            return
        }
        if (repo.verifyPin(pin)) {
            repo.setFailedAttempts(0); repo.setLockoutUntilMs(0)
            _state.value = AdminAuthState(authenticated = true, message = "Autenticado")
            return
        }
        val failed = repo.getFailedAttempts() + 1
        repo.setFailedAttempts(failed)
        if (guard.shouldBlock(failed)) {
            val delay = guard.delayMsForAttempt(failed)
            repo.setLockoutUntilMs(System.currentTimeMillis() + delay)
            _state.value = _state.value.copy(failedAttempts = failed, lockoutRemainingMs = delay, message = "Bloqueado temporariamente")
        } else {
            _state.value = _state.value.copy(failedAttempts = failed, message = "PIN inválido")
        }
    }
}
