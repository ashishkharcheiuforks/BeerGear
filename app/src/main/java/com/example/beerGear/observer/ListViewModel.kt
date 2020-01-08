package com.example.beerGear.observer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.beerGear.database.BeerCraft
import com.example.beerGear.repo.AppRepository
import com.example.beerGear.util.AbsentedLiveData
import com.example.beerGear.util.Resource
import javax.inject.Inject

class ListViewModel @Inject constructor(
    val repo: AppRepository
): ViewModel() {

    private val _resId: MutableLiveData<Int> = MutableLiveData()
    val resId: LiveData<Int>
        get() = _resId
    fun setResId(resId: Int) {
        Log.e(Thread.currentThread().name, "${this.resId.value} $resId")
        if (this.resId.value == resId) return
        _resId.value = resId
    }

    val result: LiveData<Resource<List<BeerCraft>>> = Transformations
        .switchMap(_resId) {
            when(it) {
                null -> AbsentedLiveData.create()
                else -> repo.fetchBeers(it)
            }
        }

    fun updateCardItem(flag: Boolean, id: Int) {
        repo.updateCart(flag, id)
    }
}