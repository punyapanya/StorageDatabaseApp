package com.example.storagedatabaseapp.ui.fragments.materialdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.entities.GivingRequest
import com.example.storagedatabaseapp.data.db.entities.Material
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MaterialDetailViewModel(
    private val localDataSource: LocalDataSource,
    private val materialId: Long
) : ViewModel() {

    private val _material = MutableLiveData<Material>()
    val material: LiveData<Material>
        get() = _material

    private val _btnGroupState = MutableLiveData(BtnRequestGroupState.NOTHING)
    val btnGroupState: LiveData<BtnRequestGroupState>
        get() = _btnGroupState

    private val _requests = MutableLiveData<List<GivingRequest>>()
    val requests: LiveData<List<GivingRequest>>
        get() = _requests

    fun getRequestsOrder() {
        _btnGroupState.value = BtnRequestGroupState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val data = localDataSource.getMaterialWithGivingRequests(materialId)
                ?: throw IllegalStateException("Material cannot be null")
            _requests.postValue(data.requests)

            _btnGroupState.postValue(BtnRequestGroupState.ORDER)
        }
    }

    fun getRequestsReceiving() {
        _btnGroupState.value = BtnRequestGroupState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val data = localDataSource.getMaterialWithTakingRequests(materialId)
                ?: throw IllegalStateException("Material cannot be null")
            _requests.postValue(data.requests.map {
                GivingRequest(
                    it.requestedMaterialId,
                    it.quantity,
                    it.requestedFrom,
                    it.date,
                    true,
                    it.id
                )
            })
            _btnGroupState.postValue(BtnRequestGroupState.RECEIVING)
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val data = localDataSource.getMaterial(materialId)
                ?: throw IllegalStateException("Material cannot be null")
            _material.postValue(data)
        }
    }
}