package com.example.storagedatabaseapp.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.storagedatabaseapp.data.db.entities.GivingRequest
import com.example.storagedatabaseapp.data.db.entities.Material

data class MaterialWithGivingRequests(
    @Embedded
    val material: Material,

    @Relation(parentColumn = "materialId", entityColumn = "requestedMaterialId")
    var requests: List<GivingRequest>
)