package com.example.storagedatabaseapp

import com.example.storagedatabaseapp.data.db.entities.*
import java.util.concurrent.TimeUnit

val materialList = listOf<Material>(
    Material("pants", Style.VERY_SMALL, 1, 1),
    Material("shoes", Style.SMALL, 2, 2),
    Material("heels", Style.MEDIUM, 3, 3),
    Material("jeans", Style.BIG, 4, 4),
    Material("jeans", Style.MEDIUM, 0, 5),

    Material("jeans", Style.BIG, 4, 228),
    Material("jeans", Style.VERY_BIG, -5, 228),
)

val takingRequestList = listOf<TakingRequest>(
    TakingRequest(1, 10, "ukraine", System.currentTimeMillis()),
    TakingRequest(1, 10, "ukraine", System.currentTimeMillis()),

    TakingRequest(2, 10, "america", System.currentTimeMillis() - TimeUnit.DAYS.toMillis(31)),

    TakingRequest(3, 10, "ukraine", System.currentTimeMillis() - TimeUnit.DAYS.toMillis(31)),
    TakingRequest(3, 10, "ukraine", System.currentTimeMillis() - TimeUnit.DAYS.toMillis(31)),
    TakingRequest(3, 10, "ukraine", System.currentTimeMillis() - TimeUnit.DAYS.toMillis(31)),

    TakingRequest(4, 10, "nowhere", System.currentTimeMillis() - TimeUnit.DAYS.toMillis(31)),

    TakingRequest(228, 10, "ukraine", System.currentTimeMillis()),
)

val givingRequestList = listOf<GivingRequest>(
    GivingRequest(1, 20, "ukraine", System.currentTimeMillis()),

    GivingRequest(2, 9, "america", System.currentTimeMillis()- TimeUnit.DAYS.toMillis(7)),
    GivingRequest(2, 90, "america", System.currentTimeMillis()- TimeUnit.DAYS.toMillis(7)),

    GivingRequest(3, 25, "america", System.currentTimeMillis()- TimeUnit.DAYS.toMillis(7)),
    GivingRequest(3, 10, "america", System.currentTimeMillis()),

    GivingRequest(4, 1, "nowhere", System.currentTimeMillis()),
    GivingRequest(228, 1, "america", System.currentTimeMillis()),
)

val materialTypeList = listOf<MaterialType>(
    MaterialType("pants"),
    MaterialType("shoes"),
    MaterialType("heels"),
    MaterialType("jeans")
)

val partnerNameList = listOf<PartnerName>(
    PartnerName("ukraine"),
    PartnerName("america")
)