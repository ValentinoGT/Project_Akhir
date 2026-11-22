package com.example.projectakhir

import com.example.projectakhir.data.local.SessionEntity
import com.example.projectakhir.data.repository.toDomain
import com.example.projectakhir.data.repository.toEntity
import com.example.projectakhir.domain.model.SessionStatus
import com.example.projectakhir.domain.model.StudySession
import org.junit.Assert.assertEquals
import org.junit.Test

class SessionMapperTest {

    @Test
    fun `test SessionEntity to StudySession mapping`() {
        val entity = SessionEntity(
            id = 10L,
            startedAt = 1000000L,
            endedAt = 1001500L,
            plannedDurationSec = 1500,
            actualDurationSec = 1500,
            avgLux = 250f,
            minLux = 100f,
            maxLux = 400f,
            movementEvents = 3,
            movementDurationSec = 12,
            status = "COMPLETED",
            note = "Focus session on math"
        )

        val domain = entity.toDomain()

        assertEquals(10L, domain.id)
        assertEquals(1000000L, domain.startedAt)
        assertEquals(1001500L, domain.endedAt)
        assertEquals(1500, domain.plannedDurationSec)
        assertEquals(1500, domain.actualDurationSec)
        assertEquals(250f, domain.avgLux)
        assertEquals(100f, domain.minLux)
        assertEquals(400f, domain.maxLux)
        assertEquals(3, domain.movementEvents)
        assertEquals(12, domain.movementDurationSec)
        assertEquals(SessionStatus.COMPLETED, domain.status)
        assertEquals("Focus session on math", domain.note)
    }

    @Test
    fun `test StudySession to SessionEntity mapping`() {
        val domain = StudySession(
            id = 5L,
            startedAt = 2000000L,
            endedAt = 2000500L,
            plannedDurationSec = 1500,
            actualDurationSec = 500,
            avgLux = 80f,
            minLux = 50f,
            maxLux = 120f,
            movementEvents = 8,
            movementDurationSec = 45,
            status = SessionStatus.ABORTED,
            note = null
        )

        val entity = domain.toEntity()

        assertEquals(5L, entity.id)
        assertEquals(2000000L, entity.startedAt)
        assertEquals(2000500L, entity.endedAt)
        assertEquals(1500, entity.plannedDurationSec)
        assertEquals(500, entity.actualDurationSec)
        assertEquals(80f, entity.avgLux)
        assertEquals(50f, entity.minLux)
        assertEquals(120f, entity.maxLux)
        assertEquals(8, entity.movementEvents)
        assertEquals(45, entity.movementDurationSec)
        assertEquals("ABORTED", entity.status)
        assertEquals(null, entity.note)
    }
}
