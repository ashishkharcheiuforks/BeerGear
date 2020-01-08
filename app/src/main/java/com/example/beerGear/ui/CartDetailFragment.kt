package com.example.beerGear.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.beerGear.R
import com.example.beerGear.binding.FragmentDataBindingComponent
import com.example.beerGear.database.BeerCraft
import com.example.beerGear.databinding.FragmentCartDetailBinding
import com.example.beerGear.di.Injectable
import com.example.beerGear.observer.DetailViewModel
import com.example.beerGear.repo.AppRepository.Companion.LOAD_TYPE_CART
import com.example.beerGear.util.AppExecutors
import com.example.beerGear.util.Status
import javax.inject.Inject

class CartDetailFragment : Fragment(), Injectable {
    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    private lateinit var binding: FragmentCartDetailBinding

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var cartAdapter: BeerCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<FragmentCartDetailBinding>(inflater,
            R.layout.fragment_cart_detail,
            container,
            false,
            dataBindingComponent)
            .also {
                it.lifecycleOwner = this
                it.networkStatus = Status.LOADING
                binding = it
                setUpToolbar()
            }.run {
                return this.root
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        BeerCartAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = executors,
            callback = this::onCartIconClick
        ).also {
            this.cartAdapter = it
            binding.recyclerViewCart.adapter = it
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DetailViewModel::class.java)
            .also { vm ->
                vm.setResId(LOAD_TYPE_CART)
                vm.result.observe(this, Observer { res ->
                    binding.networkStatus = res.status
                    res?.data.let { list ->
                        cartAdapter.submitList(list)
                        if (list.isNullOrEmpty())
                            binding.networkStatus = Status.ERROR
                    }
                })
            }
    }

    private fun onCartIconClick(item: BeerCraft) {
        viewModel.updateCardItem(item.inCart, item.id)
    }

    private fun setUpToolbar() {
        binding.detailToolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.avd_arrow_back)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.detailToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.cart)
        }
        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
