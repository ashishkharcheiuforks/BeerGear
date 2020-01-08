package com.example.beerGear.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.example.beerGear.R
import com.example.beerGear.databinding.LayoutFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson

class FilterDialog(
    @get:JvmName("getContext_") val context: Context,
    private val dataBindingComponent: DataBindingComponent,
    var styleList: List<String>? = ArrayList(),
    var sortOrderList: List<String>? = ArrayList(),
    private val onOrderChange: ((Int) -> Unit)? = null,
    private val onStyleChange: ((List<String>) -> Unit)? = null
) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    var orderBy: Int = 0
    var binding: LayoutFilterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_filter,
            null,
            false,
            dataBindingComponent)
        binding?.dialog = this
        setContentView(binding?.root!!)
    }

    fun setStyles(styleList: List<String>) {
        this.styleList = sortOrderList
        Log.i(Thread.currentThread().name, "styleList: ${Gson().toJson(styleList)} $binding")
        binding?.notifyChange()
    }

    fun setSortList(sortOrderList: List<String>) {
        this.sortOrderList = sortOrderList
    }

    fun onOrderChange(chipGroup: ChipGroup, id: Int) {
        onOrderChange?.invoke(id)
    }

    private var styleSelection: MutableList<String> = ArrayList()
    fun onStyleChange(chipGroup: ChipGroup, id: Int) {
        Log.e(Thread.currentThread().name, "style_change: $id")
//        onOrderChange?.invoke(id)
    }
}