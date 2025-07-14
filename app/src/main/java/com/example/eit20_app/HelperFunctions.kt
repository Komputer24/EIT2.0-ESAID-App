package com.example.eit20_app

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager

fun saveSliderValue(context: Context, key: String, value: Float) {
    val prefs = context.getSharedPreferences("slider_prefs", Context.MODE_PRIVATE)
    prefs.edit().putFloat(key, value).apply()
}

fun loadSliderValue(context: Context, key: String, defaultValue: Float = 0.5f): Float {
    val prefs = context.getSharedPreferences("slider_prefs", Context.MODE_PRIVATE)
    return prefs.getFloat(key, defaultValue)
}

private fun findActivity(context: Context): Activity? {
    var currentContext = context
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun setWindowBrightness(context: Context, brightness: Float) {
    val activity = findActivity(context) ?: return
    val layoutParams: WindowManager.LayoutParams = activity.window.attributes
    layoutParams.screenBrightness = brightness
    activity.window.attributes = layoutParams
}