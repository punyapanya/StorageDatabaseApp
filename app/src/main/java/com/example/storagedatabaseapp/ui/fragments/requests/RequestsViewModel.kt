package com.example.storagedatabaseapp.ui.fragments.requests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.entities.GivingRequest
import com.example.storagedatabaseapp.ui.fragments.materialdetail.BtnRequestGroupState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RequestsViewModel(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _btnGroupState = MutableLiveData(BtnRequestGroupState.NOTHING)
    val btnGroupState: LiveData<BtnRequestGroupState>
        get() = _btnGroupState

    private val _requests = MutableLiveData<List<GivingRequest>>()
    val requests: LiveData<List<GivingRequest>>
        get() = _requests

    fun getRequestsOrder() {
        _btnGroupState.value = BtnRequestGroupState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val data = localDataSource.getGivingRequest()
            _requests.postValue(data)

            _btnGroupState.postValue(BtnRequestGroupState.ORDER)
        }
    }

    fun getRequestsReceiving() {
        _btnGroupState.value = BtnRequestGroupState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val data = localDataSource.getTakingRequest()
            _requests.postValue(data.map {
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
}