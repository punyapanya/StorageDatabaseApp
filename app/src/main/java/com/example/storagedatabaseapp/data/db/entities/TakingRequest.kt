package com.example.storagedatabaseapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TakingRequest(
    val requestedMaterialId: Long,
    val quantity: Int,
    val requestedFrom: String,
    val date: Long,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)