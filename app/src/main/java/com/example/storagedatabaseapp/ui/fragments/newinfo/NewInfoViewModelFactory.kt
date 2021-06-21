package com.example.storagedatabaseapp.ui.fragments.newinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storagedatabaseapp.data.datasource.LocalDataSource

class NewInfoViewModelFactory(
    private val localDataSource: LocalDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewInfoViewModel(localDataSource) as T
    }
}