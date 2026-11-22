package com.example.projectakhir.sensor

import android.os.SystemClock

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED,
    ABORTED
}

data class TimerStatus(
    val state: TimerState = TimerState.IDLE,
    val targetSeconds: Long = 1500L,
    val elapsedSeconds: Long = 0L,
    val remainingSeconds: Long = 1500L,
    val isTargetReached: Boolean = false
)

class TimerEngine(
    private val clockProvider: () -> Long = { SystemClock.elapsedRealtime() }
) {
    var state: TimerState = TimerState.IDLE
        private set

    var targetSeconds: Long = 1500L
        private set

    private var accumulatedMillis: Long = 0L
    private var segmentStartMillis: Long = 0L

    var onTimerTick: ((TimerStatus) -> Unit)? = null
    var onTargetReached: ((Long) -> Unit)? = null

    fun start(targetSec: Long) {
        targetSeconds = targetSec
        accumulatedMillis = 0L
        segmentStartMillis = clockProvider()
        state = TimerState.RUNNING
        notifyUpdate()
    }

    fun pause() {
        if (state == TimerState.RUNNING) {
            accumulatedMillis += clockProvider() - segmentStartMillis
            state = TimerState.PAUSED
            notifyUpdate()
        }
    }

    fun resume() {
        if (state == TimerState.PAUSED) {
            segmentStartMillis = clockProvider()
            state = TimerState.RUNNING
            notifyUpdate()
        }
    }

    fun finish(): Long {
        if (state == TimerState.RUNNING) {
            accumulatedMillis += clockProvider() - segmentStartMillis
        }
        state = TimerState.COMPLETED
        val totalSec = getElapsedSeconds()
        notifyUpdate()
        return totalSec
    }

    fun abort(): Long {
        if (state == TimerState.RUNNING) {
            accumulatedMillis += clockProvider() - segmentStartMillis
        }
        state = TimerState.ABORTED
        val totalSec = getElapsedSeconds()
        notifyUpdate()
        return totalSec
    }

    fun reset() {
        state = TimerState.IDLE
        accumulatedMillis = 0L
        segmentStartMillis = 0L
        notifyUpdate()
    }

    fun tick() {
        if (state != TimerState.RUNNING) return
        val currentElapsed = getElapsedSeconds()
        if (currentElapsed >= targetSeconds) {
            finish()
            onTargetReached?.invoke(targetSeconds)
        } else {
            notifyUpdate()
        }
    }

    fun getElapsedSeconds(): Long {
        val currentSegment = if (state == TimerState.RUNNING) {
            clockProvider() - segmentStartMillis
        } else 0L
        return (accumulatedMillis + currentSegment) / 1000L
    }

    fun getRemainingSeconds(): Long {
        val elapsed = getElapsedSeconds()
        val remaining = targetSeconds - elapsed
        return if (remaining > 0) remaining else 0L
    }

    fun getStatus(): TimerStatus {
        val elapsed = getElapsedSeconds()
        val remaining = getRemainingSeconds()
        val isReached = elapsed >= targetSeconds
        return TimerStatus(
            state = state,
            targetSeconds = targetSeconds,
            elapsedSeconds = elapsed,
            remainingSeconds = remaining,
            isTargetReached = isReached
        )
    }

    private fun notifyUpdate() {
        onTimerTick?.invoke(getStatus())
    }
}
