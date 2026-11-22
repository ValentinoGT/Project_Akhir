package com.example.projectakhir.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.projectakhir.domain.model.MovementState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MotionData(
    val currentMagnitude: Float? = null,
    val movementState: MovementState = MovementState.UNKNOWN,
    val movementEventsCount: Int = 0,
    val movementDurationSec: Int = 0,
    val isAvailable: Boolean = false
)

class MotionSensorMonitor(context: Context) : SensorEventListener {
    private val sensorManager =
        context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val motionProcessor = MotionProcessor()

    private val _motionData = MutableStateFlow(
        MotionData(isAvailable = accelSensor != null)
    )
    val motionData: StateFlow<MotionData> = _motionData.asStateFlow()

    fun startListening() {
        motionProcessor.reset()
        if (accelSensor != null && sensorManager != null) {
            sensorManager.registerListener(
                this,
                accelSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            _motionData.value = MotionData(isAvailable = false)
        }
    }

    fun stopListening() {
        if (accelSensor != null && sensorManager != null) {
            sensorManager.unregisterListener(this)
        }
    }

    fun resetAggregation() {
        motionProcessor.reset()
        _motionData.value = MotionData(isAvailable = accelSensor != null)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val now = System.currentTimeMillis()
        val result = motionProcessor.processReading(
            x = event.values[0],
            y = event.values[1],
            z = event.values[2],
            timestampMillis = now
        )

        _motionData.value = MotionData(
            currentMagnitude = result.linearMagnitude,
            movementState = result.movementState,
            movementEventsCount = result.movementEventsCount,
            movementDurationSec = result.movementDurationSec,
            isAvailable = true
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
