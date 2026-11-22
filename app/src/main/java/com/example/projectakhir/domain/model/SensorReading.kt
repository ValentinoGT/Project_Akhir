package com.example.projectakhir.domain.model

data class SensorReading(
    val currentLux: Float? = null,
    val minLux: Float? = null,
    val maxLux: Float? = null,
    val avgLux: Float? = null,
    val movementEvents: Int = 0,
    val movementDurationSec: Int = 0,
    val currentMagnitude: Float? = null,
    val lightState: LightState = LightState.UNKNOWN,
    val movementState: MovementState = MovementState.UNKNOWN
)
