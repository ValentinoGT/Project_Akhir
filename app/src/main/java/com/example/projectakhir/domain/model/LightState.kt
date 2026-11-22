package com.example.projectakhir.domain.model

enum class LightState(val label: String, val description: String) {
    REDUP("Redup", "Cahaya kurang terang, disarankan menambah penerangan."),
    SEDANG("Sedang", "Kondisi pencahayaan cukup baik untuk belajar."),
    TERANG("Terang", "Pencahayaan sangat baik dan optimal untuk membaca."),
    UNKNOWN("Tidak Tersedia", "Sensor cahaya tidak tersedia pada perangkat.");

    companion object {
        fun fromLux(
            lux: Float?,
            redupThreshold: Float = 100f,
            sedangThreshold: Float = 300f
        ): LightState {
            if (lux == null) return UNKNOWN
            return when {
                lux < redupThreshold -> REDUP
                lux < sedangThreshold -> SEDANG
                else -> TERANG
            }
        }
    }
}
