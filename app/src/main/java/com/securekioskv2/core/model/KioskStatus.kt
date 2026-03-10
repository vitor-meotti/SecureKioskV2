package com.securekioskv2.core.model

enum class OperationMode {
    NORMAL,
    SCREEN_PINNING,
    LOCK_TASK,
    DEVICE_OWNER
}

data class KioskStatus(
    val profileName: String,
    val targetPackage: String,
    val kioskActive: Boolean,
    val operationMode: OperationMode,
    val deviceOwner: Boolean,
    val lockTaskAvailable: Boolean,
    val lastError: String? = null
)
