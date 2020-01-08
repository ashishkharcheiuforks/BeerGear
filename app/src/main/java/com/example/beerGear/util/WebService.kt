package com.example.beerGear.util

import androidx.lifecycle.LiveData
import com.example.beerGear.database.BeerCraft
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * contains all api calls
 */
interface WebService {

    @GET("{arg}")
    fun fetchCategories(@Path("arg") arg: String): LiveData<ApiResponse<List<BeerCraft>>>

}