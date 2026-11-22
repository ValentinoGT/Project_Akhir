package com.example.projectakhir.data.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(getThemeMode())
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _defaultTargetDurationSec = MutableStateFlow(getDefaultTargetDurationSec())
    val defaultTargetDurationSec: StateFlow<Long> = _defaultTargetDurationSec.asStateFlow()

    private val _luxRedupThreshold = MutableStateFlow(getLuxRedupThreshold())
    val luxRedupThreshold: StateFlow<Float> = _luxRedupThreshold.asStateFlow()

    private val _luxSedangThreshold = MutableStateFlow(getLuxSedangThreshold())
    val luxSedangThreshold: StateFlow<Float> = _luxSedangThreshold.asStateFlow()

    private val _luxPresetName = MutableStateFlow(getLuxPresetName())
    val luxPresetName: StateFlow<String> = _luxPresetName.asStateFlow()

    private val _notificationEnabled = MutableStateFlow(isNotificationEnabled())
    val notificationEnabled: StateFlow<Boolean> = _notificationEnabled.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(isVibrationEnabled())
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    fun getDefaultTargetDurationSec(): Long {
        return prefs.getLong(KEY_TARGET_DURATION, 1500L)
    }

    fun setDefaultTargetDurationSec(seconds: Long) {
        prefs.edit().putLong(KEY_TARGET_DURATION, seconds).apply()
        _defaultTargetDurationSec.value = seconds
    }

    fun getLuxRedupThreshold(): Float {
        return prefs.getFloat(KEY_LUX_REDUP, 100f)
    }

    fun getLuxSedangThreshold(): Float {
        return prefs.getFloat(KEY_LUX_SEDANG, 300f)
    }

    fun setLuxThresholds(redup: Float, sedang: Float, presetName: String) {
        prefs.edit()
            .putFloat(KEY_LUX_REDUP, redup)
            .putFloat(KEY_LUX_SEDANG, sedang)
            .putString(KEY_LUX_PRESET, presetName)
            .apply()
        _luxRedupThreshold.value = redup
        _luxSedangThreshold.value = sedang
        _luxPresetName.value = presetName
    }

    fun getLuxPresetName(): String {
        return prefs.getString(KEY_LUX_PRESET, "STANDARD") ?: "STANDARD"
    }

    fun isNotificationEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION, true)
    }

    fun setNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATION, enabled).apply()
        _notificationEnabled.value = enabled
    }

    fun isVibrationEnabled(): Boolean {
        return prefs.getBoolean(KEY_VIBRATION, true)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIBRATION, enabled).apply()
        _vibrationEnabled.value = enabled
    }

    fun getThemeMode(): String {
        return prefs.getString(KEY_THEME_MODE, "SYSTEM") ?: "SYSTEM"
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString(KEY_THEME_MODE, mode).apply()
        _themeMode.value = mode
    }

    companion object {
        private const val PREFS_NAME = "focus_sense_prefs"
        private const val KEY_TARGET_DURATION = "default_target_duration_sec"
        private const val KEY_LUX_REDUP = "lux_redup_threshold"
        private const val KEY_LUX_SEDANG = "lux_sedang_threshold"
        private const val KEY_LUX_PRESET = "lux_preset_name"
        private const val KEY_NOTIFICATION = "notification_enabled"
        private const val KEY_VIBRATION = "vibration_enabled"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}
