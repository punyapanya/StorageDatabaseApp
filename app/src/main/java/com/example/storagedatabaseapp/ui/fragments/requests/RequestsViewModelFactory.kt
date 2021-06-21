package com.example.storagedatabaseapp.ui.fragments.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storagedatabaseapp.data.datasource.LocalDataSource

class RequestsViewModelFactory(
    private val localDataSource: LocalDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RequestsViewModel(localDataSource) as T
    }
}