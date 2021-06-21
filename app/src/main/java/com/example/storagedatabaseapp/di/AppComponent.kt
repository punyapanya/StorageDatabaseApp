package com.example.storagedatabaseapp.di

import com.example.storagedatabaseapp.ui.fragments.materialdetail.MaterialDetailFragment
import com.example.storagedatabaseapp.ui.fragments.materials.MaterialsFragment
import com.example.storagedatabaseapp.ui.fragments.newinfo.NewInfoFragment
import com.example.storagedatabaseapp.ui.fragments.requests.RequestsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun inject(fragment: MaterialsFragment)
    fun inject(fragment: MaterialDetailFragment)
    fun inject(fragment: RequestsFragment)
    fun inject(fragment: NewInfoFragment)
}