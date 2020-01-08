package com.example.beerGear.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.beerGear.R
import com.example.beerGear.database.BeerCraft
import com.example.beerGear.databinding.ListItemCartBinding
import com.example.beerGear.util.AppExecutors
import com.example.beerGear.util.DataBoundListAdapter

class BeerCartAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((BeerCraft) -> Unit)?
) : DataBoundListAdapter<BeerCraft, ListItemCartBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<BeerCraft>() {
        override fun areContentsTheSame(oldItem: BeerCraft, newItem: BeerCraft): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: BeerCraft, newItem: BeerCraft): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ListItemCartBinding {
        DataBindingUtil.inflate<ListItemCartBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_cart,
            parent,
            false,
            dataBindingComponent
        ).also {
            return it
        }
    }

    override fun bind(binding: ListItemCartBinding, item: BeerCraft, position: Int) {
        binding.beer = item
        binding.addRemoveBtn.setOnClickListener {
            item.inCart = !item.inCart
            notifyDataSetChanged()
            binding.beer.let {
                callback?.invoke(it!!)
            }
        }
    }
}