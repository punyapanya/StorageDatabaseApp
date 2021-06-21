package com.example.storagedatabaseapp.ui.fragments.materials

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.entities.Material
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MaterialsViewModel(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _materials = MutableLiveData<List<Material>>(listOf())
    val materials: LiveData<List<Material>>
        get() = _materials

    private val _btnGroupStates = MutableLiveData<BtnGroupState>(BtnGroupState.NOTHING)
    val btnGroupStates: LiveData<BtnGroupState>
        get() = _btnGroupStates

    fun getMaterials(minQuantity: Int = 0, maxQuantity: Int = Int.MAX_VALUE) {
        _btnGroupStates.value = BtnGroupState.LOADING

        viewModelScope.launch(Dispatchers.IO) {
            _materials.postValue(localDataSource.getMaterials(minQuantity, maxQuantity))

            if (minQuantity == 0 && maxQuantity == Int.MAX_VALUE)
                _btnGroupStates.postValue(BtnGroupState.ALL)
            else if (minQuantity == 1 && maxQuantity == Int.MAX_VALUE)
                _btnGroupStates.postValue(BtnGroupState.IN_STOCK)
            else if (minQuantity == 0 && maxQuantity == 0)
                _btnGroupStates.postValue(BtnGroupState.NOT_IN_STOCK)
        }
    }

    fun getMaterialsNotOrdered(
        start: Long = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30),
        end: Long = System.currentTimeMillis()
    ) {
        _btnGroupStates.value = BtnGroupState.LOADING

        viewModelScope.launch(Dispatchers.IO) {
            _materials.postValue(localDataSource.getMaterialsNotGiving(start, end))
            _btnGroupStates.postValue(BtnGroupState.NOT_ORDERED)
        }
    }

    fun getMaterialsWithRefuses(
        start: Long = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30),
        end: Long = System.currentTimeMillis()
    ) {
        _btnGroupStates.value = BtnGroupState.LOADING

        viewModelScope.launch(Dispatchers.IO) {
            _materials.postValue(localDataSource.getMaterialsWithGivingStatus(start, end, false))
            _btnGroupStates.postValue(BtnGroupState.WITH_REFUSES)
        }
    }
}