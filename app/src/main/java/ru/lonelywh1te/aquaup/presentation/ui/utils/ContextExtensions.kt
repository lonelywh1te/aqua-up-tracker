package ru.lonelywh1te.aquaup.presentation.ui.utils

import android.content.Context

fun Context.getAppVersion(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName ?: "N/A"
    } catch (e: Exception) {
        "N/A"
    }
}