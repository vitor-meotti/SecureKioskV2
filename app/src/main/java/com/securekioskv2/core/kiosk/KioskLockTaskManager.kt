package com.securekioskv2.core.kiosk

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.Context

class KioskLockTaskManager(private val context: Context) {
    fun startIfPermitted(activity: Activity) {
        val am = context.getSystemService(ActivityManager::class.java)
        val dpm = context.getSystemService(DevicePolicyManager::class.java)
        if (am.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE) return

        val packageName = context.packageName
        val deviceOwner = dpm?.isDeviceOwnerApp(packageName) == true
        val lockTaskPermitted = dpm?.isLockTaskPermitted(packageName) == true

        if (deviceOwner || lockTaskPermitted) {
            runCatching { activity.startLockTask() }
        }
    }
}
