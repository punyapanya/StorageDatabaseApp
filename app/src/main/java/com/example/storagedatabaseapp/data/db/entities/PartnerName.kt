package com.example.storagedatabaseapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PartnerName(
    @PrimaryKey(autoGenerate = false)
    val name: String
)
