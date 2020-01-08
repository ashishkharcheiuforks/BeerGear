package com.example.beerGear.binding

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.beerGear.util.Status
import com.example.beerGear.util.then
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import javax.inject.Inject
import androidx.core.view.ViewCompat.setElevation
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.NestedScrollView
import com.google.android.material.appbar.AppBarLayout


/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {

    @BindingAdapter("viewVisible")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter(value = ["drawableFlag", "positiveDrawable", "negativeDrawable"], requireAll = true)
    fun setImageDrawable(imageView: ImageView, flag: Boolean, positiveDrawable: Drawable, negativeDrawable: Drawable) {
        val drawable = when (!flag) {
            true -> positiveDrawable
            else -> negativeDrawable
        }

        Log.e(Thread.currentThread().name, "image_drawable_is: $flag")

        imageView.setImageDrawable(drawable)
    }

    @BindingAdapter(value = ["lottieAnimate", "lottieEmptyState", "lottieLoadingState"], requireAll = false)
    fun showLottiAnimation(view: View?, status: Status?, emptyState: String?, loadingState: String?) {
        val empty = emptyState ?: "bag-error.json"
        val loading = loadingState ?: "beer-icon.json"

        val animation = (status == Status.LOADING) then loading ?: empty
        (view as LottieAnimationView).setAnimation(animation)
        if (status == Status.ERROR || status == Status.SUCCESS) view.loop(false) else view.loop(true)
        view.playAnimation()
    }

    @BindingAdapter(value = ["appBarElevate"], requireAll = true)
    fun elevateAppbar(recyclerView: RecyclerView, appBarView: AppBarLayout) {
        val elevation = 4 * fragment.resources.displayMetrics.density + 0.5f
        setElevation(appBarView, 0f)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.computeVerticalScrollOffset() == 0) {
                    setElevation(appBarView, 0f)
                } else {
                    setElevation(appBarView, elevation)
                }
            }
        })
    }

    @BindingAdapter(value = ["viewElevate"], requireAll = true)
    fun elevateAppbar(view: View, nestedScrollView: NestedScrollView?) {
        val elevation = 4 * fragment.resources.displayMetrics.density + 0.5f
        nestedScrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
        { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == 0)
                view.elevation = 0f
            else
                view.elevation = elevation
        })
    }

    @BindingAdapter(value = ["chipList"], requireAll = true)
    fun bindChips(group: ChipGroup, list: List<String>?) {
        group.removeAllViews()
        if (list.isNullOrEmpty()) return
        for ((index, item) in list.withIndex()) {
            val chip = Chip(this.fragment.requireContext())
            chip.id = index
            chip.text = item
            chip.isCheckable = true
            chip.isCheckedIconVisible = true
            group.addView(chip)
        }
    }

}