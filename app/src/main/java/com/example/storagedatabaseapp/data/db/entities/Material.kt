package com.example.storagedatabaseapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Material(
    val type: String,
    val style: Style,
    var quantity: Int,

    @PrimaryKey(autoGenerate = true)
    var materialId: Long = 0L
)