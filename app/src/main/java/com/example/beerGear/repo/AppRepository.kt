package com.example.beerGear.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.beerGear.database.AppDao
import com.example.beerGear.database.BeerCraft
import com.example.beerGear.util.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val webservice: WebService,
    private val executor: AppExecutors,
    private val dao: AppDao
) {

    companion object {
        const val LOAD_TYPE_CART = 0
        const val LOAD_TYPE_ALL = 1
    }

    fun fetchBeers(type: Int): LiveData<Resource<List<BeerCraft>>> {
        return object : NetworkBoundResource<List<BeerCraft>, List<BeerCraft>>(executor) {
            override fun saveCallResult(item: List<BeerCraft>) {
                Log.d(Thread.currentThread().name, "fetch_beer_list ${Gson().toJson(item)}")
                dao.insertBeers(item)
            }

            override fun shouldFetch(data: List<BeerCraft>?): Boolean {
                return data.isNullOrEmpty() && type == LOAD_TYPE_ALL
            }

            override fun loadFromDb(): LiveData<List<BeerCraft>> {
                return when (type) {
                    LOAD_TYPE_ALL -> dao.loadBeers()
                    else -> dao.loadBeerCart(true)
                }
             }

            override fun createCall(): LiveData<ApiResponse<List<BeerCraft>>> {
                return webservice.fetchCategories("beercraft")
            }

        }.asLiveData()
    }

    lateinit var id: Unit
    fun updateCart(flag: Boolean, id: Int) {
        executor.diskIO().execute {
            val _id = dao.updateCart(flag = flag, beerId = id)

            executor.diskIO().execute { this.id = _id }
        }
    }

}