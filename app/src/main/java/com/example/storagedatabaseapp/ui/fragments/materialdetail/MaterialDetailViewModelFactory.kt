package com.example.storagedatabaseapp.ui.fragments.materialdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storagedatabaseapp.data.datasource.LocalDataSource

class MaterialDetailViewModelFactory(
    private val localDataSource: LocalDataSource,
    private val materialId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MaterialDetailViewModel(localDataSource, materialId) as T
    }
}