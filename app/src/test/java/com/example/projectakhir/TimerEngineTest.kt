package com.example.projectakhir

import com.example.projectakhir.sensor.TimerEngine
import com.example.projectakhir.sensor.TimerState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimerEngineTest {
    private var currentTime = 1000L
    private val timer = TimerEngine { currentTime }

    @Test
    fun `test initial state is IDLE`() {
        assertEquals(TimerState.IDLE, timer.state)
        assertEquals(0L, timer.getElapsedSeconds())
    }

    @Test
    fun `test start transitions to RUNNING and tracks time`() {
        timer.start(1500L)
        assertEquals(TimerState.RUNNING, timer.state)

        currentTime += 5000L // 5 seconds later
        assertEquals(5L, timer.getElapsedSeconds())
        assertEquals(1495L, timer.getRemainingSeconds())
    }

    @Test
    fun `test pause and resume duration calculation`() {
        timer.start(100L)
        currentTime += 10000L // 10s running
        assertEquals(10L, timer.getElapsedSeconds())

        timer.pause()
        assertEquals(TimerState.PAUSED, timer.state)

        currentTime += 15000L // 15s elapsed while paused
        assertEquals(10L, timer.getElapsedSeconds()) // Elapsed time should NOT increase while paused

        timer.resume()
        assertEquals(TimerState.RUNNING, timer.state)

        currentTime += 5000L // 5s running after resume
        assertEquals(15L, timer.getElapsedSeconds())
    }

    @Test
    fun `test auto-stop when target reached`() {
        var targetReachedTriggered = false
        timer.onTargetReached = { targetReachedTriggered = true }

        timer.start(10L) // 10s target
        currentTime += 11000L // 11s passed
        timer.tick()

        assertEquals(TimerState.COMPLETED, timer.state)
        assertTrue(targetReachedTriggered)
    }

    @Test
    fun `test finish returns correct elapsed seconds`() {
        timer.start(300L)
        currentTime += 45000L // 45s
        val actualSec = timer.finish()
        assertEquals(TimerState.COMPLETED, timer.state)
        assertEquals(45L, actualSec)
    }
}
