package com.securekioskv2.core.platform

import android.app.admin.DevicePolicyManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class KioskCapabilityChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isDeviceOwnerActive(): Boolean {
        val dpm = context.getSystemService(DevicePolicyManager::class.java) ?: return false
        return dpm.isDeviceOwnerApp(context.packageName)
    }
}
