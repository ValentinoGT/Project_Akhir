package com.example.projectakhir.domain.model

enum class MovementState(val label: String, val description: String) {
    QUIET("Tenang", "Perangkat stabil dan tidak bergerak."),
    MOVING("Bergerak", "Perangkat bergerak sedang."),
    HIGH_MOVEMENT("Guncangan Tinggi", "Terdeteksi gerakan atau guncangan signifikan."),
    UNKNOWN("Tidak Tersedia", "Sensor akselerometer tidak tersedia pada perangkat.");

    companion object {
        fun fromMagnitude(magnitude: Float?): MovementState {
            if (magnitude == null) return UNKNOWN
            return when {
                magnitude < 0.4f -> QUIET
                magnitude < 2.5f -> MOVING
                else -> HIGH_MOVEMENT
            }
        }
    }
}
