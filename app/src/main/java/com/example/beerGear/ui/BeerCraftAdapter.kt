package com.example.beerGear.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.beerGear.R
import com.example.beerGear.database.BeerCraft
import com.example.beerGear.databinding.ListItemBeerCraftBinding
import com.example.beerGear.util.AppExecutors
import com.example.beerGear.util.DataBoundListAdapter

class BeerCraftAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((BeerCraft) -> Unit)?
) : DataBoundListAdapter<BeerCraft, ListItemBeerCraftBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<BeerCraft>() {
        override fun areContentsTheSame(oldItem: BeerCraft, newItem: BeerCraft): Boolean {
            return oldItem.inCart == newItem.inCart
        }

        override fun areItemsTheSame(oldItem: BeerCraft, newItem: BeerCraft): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ListItemBeerCraftBinding {
        DataBindingUtil.inflate<ListItemBeerCraftBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_beer_craft,
            parent,
            false,
            dataBindingComponent
        ).also {
            return it
        }
    }

    override fun bind(binding: ListItemBeerCraftBinding, item: BeerCraft, position: Int) {
        binding.beer = item
        binding.addRemoveBtn.setOnClickListener {
            item.inCart = !item.inCart
            notifyDataSetChanged()
            binding.beer.let {
                callback?.invoke(it!!)
            }
        }
        binding.root.setOnClickListener {
            binding.beer.let {
                callback?.invoke(it!!)
            }
        }
    }
}