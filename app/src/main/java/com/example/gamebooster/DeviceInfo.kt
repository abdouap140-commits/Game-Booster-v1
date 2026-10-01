package com.example.gamebooster

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import java.util.Locale

data class DeviceSpecs(
    val deviceName: String = "",
    val manufacturer: String = "",
    val totalRam: String = "",
    val availableRam: String = "",
    val cpuCores: Int = 0,
    val cpuArch: String = "",
    val totalStorage: String = "",
    val availableStorage: String = "",
    val batteryLevel: Int = 0,
    val batteryTemp: String = "",
    val gpuRenderer: String = "Unknown"
)

fun getDeviceSpecs(context: Context): DeviceSpecs {
    val deviceName = Build.MODEL
    val manufacturer = Build.MANUFACTURER
    val cpuArch = Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"

    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    val totalRam = formatSize(memoryInfo.totalMem)
    val availableRam = formatSize(memoryInfo.availMem)

    val cpuCores = Runtime.getRuntime().availableProcessors()

    val statFs = StatFs(Environment.getDataDirectory().path)
    val totalStorage = formatSize(statFs.blockCountLong * statFs.blockSizeLong)
    val availableStorage = formatSize(statFs.availableBlocksLong * statFs.blockSizeLong)

    val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val batteryLevel = if (level != -1 && scale != -1) (level * 100 / scale) else 0
    val temp = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1
    val batteryTemp = if (temp != -1) String.format(Locale.US, "%.1f°C", temp / 10f) else "Unknown"

    val gpuRenderer = Build.HARDWARE

    return DeviceSpecs(
        deviceName = deviceName,
        manufacturer = manufacturer,
        totalRam = totalRam,
        availableRam = availableRam,
        cpuCores = cpuCores,
        cpuArch = cpuArch,
        totalStorage = totalStorage,
        availableStorage = availableStorage,
        batteryLevel = batteryLevel,
        batteryTemp = batteryTemp,
        gpuRenderer = gpuRenderer
    )
}

fun formatSize(size: Long): String {
    val kb = size / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1 -> String.format(Locale.US, "%.2f GB", gb)
        mb >= 1 -> String.format(Locale.US, "%.2f MB", mb)
        else -> String.format(Locale.US, "%.2f KB", kb)
    }
}
