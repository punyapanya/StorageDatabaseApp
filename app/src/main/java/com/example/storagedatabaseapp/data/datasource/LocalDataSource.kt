package com.example.storagedatabaseapp.data.datasource

import androidx.lifecycle.LiveData
import com.example.storagedatabaseapp.data.db.daos.MaterialDao
import com.example.storagedatabaseapp.data.db.entities.*
import com.example.storagedatabaseapp.data.db.entities.relations.MaterialWithGivingRequests
import com.example.storagedatabaseapp.data.db.entities.relations.MaterialWithTakingRequests

class LocalDataSource(
    private val materialDao: MaterialDao
) {

    //GET SIMPLE DATA
    fun getMaterials(minQuantity: Int = 0, maxQuantity: Int = Int.MAX_VALUE): List<Material> =
        materialDao.getMaterials(minQuantity, maxQuantity)

    fun getGivingRequest(): List<GivingRequest> = materialDao.getGivingRequest()

    fun getTakingRequest(): List<TakingRequest> = materialDao.getTakingRequest()

    fun getMaterialTypes(): List<String> = materialDao.getMaterialTypes()

    fun getLiveMaterialTypes(): LiveData<List<String>> = materialDao.getLiveMaterialTypes()

    fun getPartnerNames(): List<String> = materialDao.getPartnerNames()

    fun getMaterial(id: Long): Material? = materialDao.getMaterial(id)

    //GET CUSTOM RELATION DATA
    fun getMaterialsNotGiving(start: Long, end: Long): List<Material> {
        return materialDao.getMaterialsWithGivingRequests().filter {
            for (request in it.requests) {
                if (request.date in start..end) return@filter false
            }
            true
        }.map { it.material }
    }

    fun getMaterialsWithGivingStatus(start: Long, end: Long, status: Boolean): List<Material> {
        return materialDao.getMaterialsWithGivingRequests().filter {
            for (request in it.requests) {
                if (request.date in start..end && request.status == status) return@filter true
            }
            false
        }.map { it.material }
    }

    //GET RELATION DATA
    fun getMaterialsWithGivingRequests(): List<MaterialWithGivingRequests> {
        return materialDao.getMaterialsWithGivingRequests()
    }

    fun getMaterialsWithTakingRequests(): List<MaterialWithTakingRequests> {
        return materialDao.getMaterialsWithTakingRequests()
    }

    fun getMaterialWithGivingRequests(id: Long): MaterialWithGivingRequests? {
        return materialDao.getMaterialWithGivingRequests(id)
    }

    fun getMaterialWithTakingRequests(id: Long): MaterialWithTakingRequests? {
        return materialDao.getMaterialWithTakingRequests(id)
    }

    //INSERT DATA
    suspend fun insertMaterial(material: Material): Boolean {
        if (material.quantity < 0) {
            return false
        }
        val allMaterials = getMaterials()
        val allTypes = getMaterialTypes()
        if (material.type !in allTypes) {
            insertMaterialType(MaterialType(material.type))
        }
        for (item in allMaterials) {
            if (material.type == item.type && material.style == item.style) {
                return false
            }
        }
        materialDao.insertMaterial(material)
        return true
    }

    suspend fun insertGivingRequest(request: GivingRequest): Boolean {
        val partners = getPartnerNames()
        if (request.requestedFrom !in partners) {
            return false
        }

        val material = getMaterial(request.requestedMaterialId) ?: return false
        if (material.quantity < request.quantity) {
            request.status = false
        } else {
            material.quantity -= request.quantity
            materialDao.insertMaterial(material)
        }

        materialDao.insertGivingRequest(request)
        return true
    }

    suspend fun insertTakingRequest(request: TakingRequest): Boolean {
        val partners = getPartnerNames()
        if (request.requestedFrom !in partners) {
            return false
        }

        val material = getMaterial(request.requestedMaterialId) ?: return false
        material.quantity += request.quantity

        materialDao.insertMaterial(material)
        materialDao.insertTakingRequest(request)
        return true
    }

    suspend fun insertMaterialType(type: MaterialType) {
        materialDao.insertMaterialType(type)
    }

    suspend fun insertPartnerName(name: PartnerName) {
        materialDao.insertPartnerName(name)
    }
}