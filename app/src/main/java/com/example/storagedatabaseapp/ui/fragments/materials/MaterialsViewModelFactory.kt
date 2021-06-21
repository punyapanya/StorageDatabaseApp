package com.example.storagedatabaseapp.ui.fragments.materials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storagedatabaseapp.data.datasource.LocalDataSource

class MaterialsViewModelFactory(
    private val localDataSource: LocalDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MaterialsViewModel(localDataSource) as T
    }
}