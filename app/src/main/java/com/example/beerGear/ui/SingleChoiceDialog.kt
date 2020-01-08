package com.example.beerGear.ui

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import com.example.beerGear.R

open class SingleChoiceDialog<T>(
    @get:JvmName("getContext_") val context: Context,
    private val title: String? = "Alert",
    private val selectedPosition: Int = 0,
    private val args: MutableList<T>,
    private val positiveActionTitle: String? = "OK",
    private val actionPositive: ((T, Int) -> Unit)? = null
) {
    fun show() {
        var cur = selectedPosition
        val arrayAdapter = ArrayAdapter(context, R.layout.select_dialog_singlechoice_material, args)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setSingleChoiceItems(arrayAdapter, cur) { _, i ->
                cur = i
            }
            .setPositiveButton(positiveActionTitle) { dialogInterface, _ ->
                arrayAdapter.getItem(cur)?.let {
                    actionPositive?.invoke(it, cur)
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }
}