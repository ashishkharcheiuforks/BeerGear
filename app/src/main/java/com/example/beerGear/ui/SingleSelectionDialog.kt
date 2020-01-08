package com.example.beerGear.ui

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import com.example.beerGear.R

open class SingleSelectionDialog<T>(
    @get:JvmName("getContext_") val context: Context,
    private var title: String? = "Alert",
    private var selectedPosition: Int = 0,
    private var args: MutableList<T>,
    private var positiveActionTitle: String? = "OK",
    private var actionPositive: ((T, Int) -> Unit)? = null
) {
    companion object {
        class Builder<T>() {
            @get:JvmName("getContext_") private var context: Context? = null
            private var title: String? = "Alert"
            private var selectedPosition: Int = 0
            private var args: MutableList<T>? = null
            private var positiveActionTitle: String? = "OK"
            private var actionPositive: ((T, Int) -> Unit)? = null

            fun setContext(context: Context): Builder<T> {
                this.context = context
                return this
            }

            fun setTitle(title: String): Builder<T> {
                this.title = title
                return this
            }

            fun setSelectedPostion(position: Int): Builder<T> {
                this.selectedPosition = position
                return this
            }

            fun setArgs(args: MutableList<T>): Builder<T> {
                this.args = args
                return this
            }

            fun setPositiveActionTitle(buttonTitle: String) : Builder<T> {
                this.positiveActionTitle = title
                return this
            }

            fun setPositiveAction(actionPositive: ((T, Int) -> Unit)) : Builder<T> {
                this.actionPositive = actionPositive
                return this
            }

            fun build(): SingleSelectionDialog<T> {
                return SingleSelectionDialog(context!!, title, selectedPosition, args!!, positiveActionTitle, actionPositive)
            }
        }
    }


    fun show() {
        var cur = selectedPosition
        val arrayAdapter = ArrayAdapter(context, R.layout.select_dialog_singlechoice_material, args)

        AlertDialog.Builder(context)
            .setTitle(title)
            .setSingleChoiceItems(arrayAdapter, cur) { _, i ->
                cur = i
            }
            .setPositiveButton(positiveActionTitle) { thisInterface, _ ->
                arrayAdapter.getItem(cur)?.let {
                    actionPositive?.invoke(it, cur)
                }
                thisInterface.dismiss()
            }
            .setNegativeButton(context.getString(R.string.cancel)) { thisInterface, _ ->
                thisInterface.dismiss()
            }
            .create()
            .show()
    }
}