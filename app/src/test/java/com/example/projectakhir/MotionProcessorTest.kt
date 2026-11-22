package com.example.projectakhir

import com.example.projectakhir.domain.model.MovementState
import com.example.projectakhir.sensor.MotionProcessor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MotionProcessorTest {
    private lateinit var processor: MotionProcessor

    @Before
    fun setup() {
        processor = MotionProcessor(alpha = 0.8f, debounceMillis = 1500L)
    }

    @Test
    fun `test initial gravity baseline initialization`() {
        val result = processor.processReading(0f, 0f, 9.81f, timestampMillis = 2000L)
        assertEquals(MovementState.QUIET, result.movementState)
        assertEquals(0, result.movementEventsCount)
    }

    @Test
    fun `test quiet state under static gravity`() {
        var time = 2000L
        processor.processReading(0f, 0f, 9.81f, time)

        time += 100L
        val result = processor.processReading(0f, 0f, 9.81f, time)
        assertEquals(MovementState.QUIET, result.movementState)
        assertFalse(result.isNewEvent)
    }

    @Test
    fun `test movement classification and debouncing`() {
        var time = 2000L
        processor.processReading(0f, 0f, 9.81f, time)

        // Simulate sudden movement (linear mag between 0.4 and 2.5)
        time += 100L // 2100L
        val result1 = processor.processReading(1.5f, 0f, 9.81f, time)
        assertTrue(result1.movementState == MovementState.MOVING || result1.movementState == MovementState.HIGH_MOVEMENT)
        assertTrue(result1.isNewEvent)
        assertEquals(1, result1.movementEventsCount)

        // Rapid follow-up movement within 1500ms debounce window
        time += 500L // 2600L
        val result2 = processor.processReading(2.0f, 0f, 9.81f, time)
        assertFalse(result2.isNewEvent)
        assertEquals(1, result2.movementEventsCount)

        // Movement after debounce window (>= 1500ms later)
        time += 1600L // 4200L
        val result3 = processor.processReading(2.0f, 0f, 9.81f, time)
        assertTrue(result3.isNewEvent)
        assertEquals(2, result3.movementEventsCount)
    }
}
