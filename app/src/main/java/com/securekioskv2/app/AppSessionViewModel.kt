package com.securekioskv2.app

import androidx.lifecycle.ViewModel
import com.securekioskv2.core.security.AdminSecurityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppSessionViewModel @Inject constructor(
    private val repo: AdminSecurityRepository
) : ViewModel() {
    fun startDestination(): String = if (repo.isConfigured()) "dashboard" else "onboarding"
}
