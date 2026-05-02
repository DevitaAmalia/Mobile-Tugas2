package com.example.tugas2.sqlite

data class WaterHistory(
    val id: Int,            // COL_ID
    val inputAmount: Int,   // COL_INPUT
    val totalAfter: Int,    // COL_TOTAL
    val timestamp: String   // COL_TIMESTAMP
)