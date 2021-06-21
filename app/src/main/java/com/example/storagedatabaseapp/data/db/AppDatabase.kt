package com.example.storagedatabaseapp.data.db

import android.content.Context
import androidx.room.*
import com.example.storagedatabaseapp.data.db.daos.MaterialDao
import com.example.storagedatabaseapp.data.db.entities.*
import com.example.storagedatabaseapp.data.db.typeconverters.StyleTypeConverter

@Database(
    entities = [Material::class, GivingRequest::class, TakingRequest::class, MaterialType::class, PartnerName::class],
    version = 1
)
@TypeConverters(StyleTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materialDao(): MaterialDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .build()
    }
}