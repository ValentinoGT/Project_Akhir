package com.example.projectakhir.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

data class SensorStatus(
    val isLightSensorAvailable: Boolean,
    val isAccelerometerAvailable: Boolean
)

object SensorAvailability {
    fun getSensorStatus(context: Context): SensorStatus {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        val accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        return SensorStatus(
            isLightSensorAvailable = lightSensor != null,
            isAccelerometerAvailable = accelSensor != null
        )
    }
}
