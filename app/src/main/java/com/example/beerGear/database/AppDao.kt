package com.example.beerGear.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBeers(list: List<BeerCraft>?)

    /**
     * limiting results for gc memory reasons
     */
    @Query("SELECT * from beer_craft LIMIT 40")
    fun loadBeers(): LiveData<List<BeerCraft>>

    @Query("SELECT * from beer_craft WHERE inCart = :flag")
    fun loadBeerCart(flag: Boolean): LiveData<List<BeerCraft>>

    @Query("UPDATE beer_craft SET inCart = :flag WHERE id = :beerId")
    fun updateCart(flag: Boolean, beerId: Int)

}