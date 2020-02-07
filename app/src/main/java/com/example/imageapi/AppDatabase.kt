package com.example.imageapi

import android.app.Application
import androidx.room.*

@Database(entities = [ImageList::class], version = 1)
@TypeConverters(MyConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageListDao(): ImageListDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(applicationContext: Application) : AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "AppDB"
                )
                    .allowMainThreadQueries() // caution dont do this in real app
                    .build()
            }

            return INSTANCE as AppDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}