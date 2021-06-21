package com.example.storagedatabaseapp.di

import android.content.Context
import com.example.storagedatabaseapp.data.datasource.LocalDataSource
import com.example.storagedatabaseapp.data.db.AppDatabase
import com.example.storagedatabaseapp.data.db.daos.MaterialDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(
    private val mAppContext: Context
) {

    @Provides
    fun provideAppContext(): Context {
        return mAppContext
    }

    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    @Provides
    @Singleton
    fun provideMaterialDao(appDatabase: AppDatabase): MaterialDao {
        return appDatabase.materialDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(materialDao: MaterialDao): LocalDataSource {
        return LocalDataSource(materialDao)
    }
}