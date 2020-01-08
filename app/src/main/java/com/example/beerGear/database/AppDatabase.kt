package com.example.beerGear.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BeerCraft::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}