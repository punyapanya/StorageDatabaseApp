package com.example.storagedatabaseapp.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.storagedatabaseapp.data.db.entities.Material
import com.example.storagedatabaseapp.data.db.entities.TakingRequest

data class MaterialWithTakingRequests(
    @Embedded
    val material: Material,

    @Relation(parentColumn = "materialId", entityColumn = "requestedMaterialId")
    var requests: List<TakingRequest>
)