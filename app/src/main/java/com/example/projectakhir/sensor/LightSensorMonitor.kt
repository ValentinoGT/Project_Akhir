package com.example.projectakhir.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.projectakhir.domain.model.LightState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max
import kotlin.math.min

data class LightData(
    val currentLux: Float? = null,
    val minLux: Float? = null,
    val maxLux: Float? = null,
    val avgLux: Float? = null,
    val lightState: LightState = LightState.UNKNOWN,
    val isAvailable: Boolean = false
)

class LightSensorMonitor(context: Context) : SensorEventListener {
    private val sensorManager =
        context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

    private val _lightData = MutableStateFlow(
        LightData(isAvailable = lightSensor != null)
    )
    val lightData: StateFlow<LightData> = _lightData.asStateFlow()

    private var lastUpdateTimestamp: Long = 0L
    private var luxCount: Long = 0L
    private var luxSum: Double = 0.0
    private var minLuxVal: Float? = null
    private var maxLuxVal: Float? = null

    var redupThreshold: Float = 100f
    var sedangThreshold: Float = 300f

    fun startListening() {
        resetAggregation()
        if (lightSensor != null && sensorManager != null) {
            sensorManager.registerListener(
                this,
                lightSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            _lightData.value = LightData(isAvailable = false)
        }
    }

    fun stopListening() {
        if (lightSensor != null && sensorManager != null) {
            sensorManager.unregisterListener(this)
        }
    }

    fun resetAggregation() {
        luxCount = 0L
        luxSum = 0.0
        minLuxVal = null
        maxLuxVal = null
        _lightData.value = LightData(isAvailable = lightSensor != null)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_LIGHT) return

        val now = System.currentTimeMillis()
        if (now - lastUpdateTimestamp < 500L) {
            return // Throttled updates (max 2 times per second)
        }
        lastUpdateTimestamp = now

        val lux = event.values[0]
        luxCount++
        luxSum += lux.toDouble()

        minLuxVal = if (minLuxVal == null) lux else min(minLuxVal!!, lux)
        maxLuxVal = if (maxLuxVal == null) lux else max(maxLuxVal!!, lux)

        val avgLux = (luxSum / luxCount).toFloat()
        val state = LightState.fromLux(lux, redupThreshold, sedangThreshold)

        _lightData.value = LightData(
            currentLux = lux,
            minLux = minLuxVal,
            maxLux = maxLuxVal,
            avgLux = avgLux,
            lightState = state,
            isAvailable = true
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
