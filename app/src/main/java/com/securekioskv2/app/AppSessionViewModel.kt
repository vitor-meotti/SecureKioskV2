package com.securekioskv2.app

import androidx.lifecycle.ViewModel
import com.securekioskv2.core.security.AdminCredentialStore

class AppSessionViewModel : ViewModel() {
    fun startDestination(): String {
        return if (AdminCredentialStore.isConfigured()) "dashboard" else "onboarding"
    }
}
