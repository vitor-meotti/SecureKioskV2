package com.securekioskv2.core.kiosk

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.Context

class KioskLockTaskManager(private val context: Context) {
    fun startIfPermitted(activity: Activity) {
        val dpm = context.getSystemService(DevicePolicyManager::class.java)
        val am = context.getSystemService(ActivityManager::class.java)
        val isLocked = am.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
        if (isLocked) return
        val deviceOwner = dpm?.isDeviceOwnerApp(context.packageName) == true
        if (deviceOwner) runCatching { activity.startLockTask() }
    }
}
