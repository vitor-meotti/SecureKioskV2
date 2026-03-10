package com.securekioskv2.core.kiosk

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.util.Log

class KioskLockTaskManager(private val context: Context) {
    fun startIfPermitted(activity: Activity) {
        val activityManager = context.getSystemService(ActivityManager::class.java) ?: return
        val devicePolicyManager = context.getSystemService(DevicePolicyManager::class.java)

        if (activityManager.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE) return

        val packageName = context.packageName
        val isDeviceOwner = devicePolicyManager?.isDeviceOwnerApp(packageName) == true
        val isLockTaskPermitted = devicePolicyManager?.isLockTaskPermitted(packageName) == true

        if (isDeviceOwner || isLockTaskPermitted) {
            runCatching { activity.startLockTask() }
                .onFailure { Log.w(TAG, "Unable to start lock task mode", it) }
        }
    }

    private companion object {
        const val TAG = "KioskLockTaskManager"
    }
}
