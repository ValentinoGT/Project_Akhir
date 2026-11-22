package com.example.projectakhir

import com.example.projectakhir.domain.model.LightState
import org.junit.Assert.assertEquals
import org.junit.Test

class LuxClassifierTest {

    @Test
    fun `test null lux returns UNKNOWN`() {
        assertEquals(LightState.UNKNOWN, LightState.fromLux(null))
    }

    @Test
    fun `test lux less than 100 returns REDUP`() {
        assertEquals(LightState.REDUP, LightState.fromLux(0f))
        assertEquals(LightState.REDUP, LightState.fromLux(50f))
        assertEquals(LightState.REDUP, LightState.fromLux(99.9f))
    }

    @Test
    fun `test lux between 100 and 299 returns SEDANG`() {
        assertEquals(LightState.SEDANG, LightState.fromLux(100f))
        assertEquals(LightState.SEDANG, LightState.fromLux(200f))
        assertEquals(LightState.SEDANG, LightState.fromLux(299.9f))
    }

    @Test
    fun `test lux 300 or greater returns TERANG`() {
        assertEquals(LightState.TERANG, LightState.fromLux(300f))
        assertEquals(LightState.TERANG, LightState.fromLux(500f))
        assertEquals(LightState.TERANG, LightState.fromLux(1000f))
    }

    @Test
    fun `test custom threshold support`() {
        assertEquals(LightState.REDUP, LightState.fromLux(120f, redupThreshold = 150f, sedangThreshold = 400f))
        assertEquals(LightState.SEDANG, LightState.fromLux(250f, redupThreshold = 150f, sedangThreshold = 400f))
        assertEquals(LightState.TERANG, LightState.fromLux(450f, redupThreshold = 150f, sedangThreshold = 400f))
    }
}
