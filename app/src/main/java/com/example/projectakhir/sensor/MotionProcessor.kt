package com.example.projectakhir.sensor

import com.example.projectakhir.domain.model.MovementState
import kotlin.math.sqrt

data class MotionResult(
    val linearMagnitude: Float,
    val movementState: MovementState,
    val isNewEvent: Boolean,
    val movementEventsCount: Int,
    val movementDurationSec: Int
)

class MotionProcessor(
    private val alpha: Float = 0.8f,
    private val debounceMillis: Long = 1500L
) {
    private var gravityX = 0f
    private var gravityY = 0f
    private var gravityZ = 0f
    private var isGravityInitialized = false

    private var lastEventTimestamp: Long = 0L
    private var movementEventsCount = 0
    private var movementDurationMillis = 0L
    private var lastReadingTimestamp: Long = 0L

    fun processReading(
        x: Float,
        y: Float,
        z: Float,
        timestampMillis: Long
    ): MotionResult {
        if (!isGravityInitialized) {
            gravityX = x
            gravityY = y
            gravityZ = z
            isGravityInitialized = true
            lastReadingTimestamp = timestampMillis
            return MotionResult(
                linearMagnitude = 0f,
                movementState = MovementState.QUIET,
                isNewEvent = false,
                movementEventsCount = 0,
                movementDurationSec = 0
            )
        }

        // Low-pass filter for gravity
        gravityX = alpha * gravityX + (1 - alpha) * x
        gravityY = alpha * gravityY + (1 - alpha) * y
        gravityZ = alpha * gravityZ + (1 - alpha) * z

        // Linear acceleration
        val linearX = x - gravityX
        val linearY = y - gravityY
        val linearZ = z - gravityZ
        val linearMagnitude = sqrt((linearX * linearX + linearY * linearY + linearZ * linearZ).toDouble()).toFloat()

        val state = MovementState.fromMagnitude(linearMagnitude)

        val deltaMillis = if (lastReadingTimestamp > 0L) timestampMillis - lastReadingTimestamp else 0L
        lastReadingTimestamp = timestampMillis

        if (state == MovementState.MOVING || state == MovementState.HIGH_MOVEMENT) {
            movementDurationMillis += deltaMillis
        }

        var isNewEvent = false
        if (state == MovementState.MOVING || state == MovementState.HIGH_MOVEMENT) {
            if (timestampMillis - lastEventTimestamp >= debounceMillis) {
                movementEventsCount++
                lastEventTimestamp = timestampMillis
                isNewEvent = true
            }
        }

        return MotionResult(
            linearMagnitude = linearMagnitude,
            movementState = state,
            isNewEvent = isNewEvent,
            movementEventsCount = movementEventsCount,
            movementDurationSec = (movementDurationMillis / 1000L).toInt()
        )
    }

    fun reset() {
        gravityX = 0f
        gravityY = 0f
        gravityZ = 0f
        isGravityInitialized = false
        lastEventTimestamp = 0L
        movementEventsCount = 0
        movementDurationMillis = 0L
        lastReadingTimestamp = 0L
    }
}
