package com.example.storagedatabaseapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MaterialType(
    @PrimaryKey(autoGenerate = false)
    val type: String
)