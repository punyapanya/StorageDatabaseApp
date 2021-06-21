package com.example.storagedatabaseapp.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.storagedatabaseapp.data.db.entities.*
import com.example.storagedatabaseapp.data.db.entities.relations.MaterialWithGivingRequests
import com.example.storagedatabaseapp.data.db.entities.relations.MaterialWithTakingRequests

@Dao
interface MaterialDao {

    //Get simple data
    @Query("SELECT * FROM material WHERE quantity BETWEEN :minQuantity AND :maxQuantity")
    fun getMaterials(minQuantity: Int = 0, maxQuantity: Int = Int.MAX_VALUE): List<Material>

    @Query("SELECT * FROM givingrequest")
    fun getGivingRequest(): List<GivingRequest>

    @Query("SELECT * FROM takingrequest")
    fun getTakingRequest(): List<TakingRequest>

    @Query("SELECT type FROM materialtype")
    fun getMaterialTypes(): List<String>

    @Query("SELECT type FROM materialtype")
    fun getLiveMaterialTypes(): LiveData<List<String>>

    @Query("SELECT name FROM partnername")
    fun getPartnerNames(): List<String>

    @Query("SELECT * FROM material WHERE materialId = :id")
    fun getMaterial(id: Long): Material?

    //Get relation data
    @Transaction
    @Query("SELECT * FROM material")
    fun getMaterialsWithGivingRequests(): List<MaterialWithGivingRequests>

    @Transaction
    @Query("SELECT * FROM material")
    fun getMaterialsWithTakingRequests(): List<MaterialWithTakingRequests>

    @Transaction
    @Query("SELECT * FROM material WHERE materialId = :id")
    fun getMaterialWithGivingRequests(id: Long): MaterialWithGivingRequests?

    @Transaction
    @Query("SELECT * FROM material WHERE materialId = :id")
    fun getMaterialWithTakingRequests(id: Long): MaterialWithTakingRequests?

    //Insert data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: Material)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGivingRequest(request: GivingRequest)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTakingRequest(request: TakingRequest)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMaterialType(type: MaterialType)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPartnerName(name: PartnerName)
}