package com.example.storagedatabaseapp.ui.fragments.newinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.entities.GivingRequest
import com.example.storagedatabaseapp.data.db.entities.Material
import com.example.storagedatabaseapp.data.db.entities.Style
import com.example.storagedatabaseapp.data.db.entities.TakingRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewInfoViewModel(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    val materialStyles: List<String> = Style.values().map { it.name }.toList()

    private val _insertionMaterialState = MutableLiveData<Boolean>()
    val insertionMaterialState: LiveData<Boolean>
        get() = _insertionMaterialState

    private val _insertionRequestState = MutableLiveData<Boolean>()
    val insertionRequestState: LiveData<Boolean>
        get() = _insertionRequestState

    private val _partners = MutableLiveData<List<String>>()
    val partners: LiveData<List<String>>
        get() = _partners

    var materialTypes: LiveData<List<String>> = localDataSource.getLiveMaterialTypes()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _partners.postValue(localDataSource.getPartnerNames())
        }
    }

    fun insertMaterial(material: Material) {
        viewModelScope.launch(Dispatchers.IO) {
            _insertionMaterialState.postValue(localDataSource.insertMaterial(material))
        }
    }

    fun insertGivingRequest(request: GivingRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _insertionRequestState.postValue(localDataSource.insertGivingRequest(request))
        }
    }

    fun insertTakingRequest(request: TakingRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _insertionRequestState.postValue(localDataSource.insertTakingRequest(request))
        }
    }
}