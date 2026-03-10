package com.securekioskv2.feature.onboarding

import androidx.lifecycle.ViewModel
import com.securekioskv2.core.security.AdminSecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repo: AdminSecurityRepository
) : ViewModel() {
    fun savePin(pin: CharArray) = repo.configurePin(pin)
    fun isConfigured(): Boolean = repo.isConfigured()
}
