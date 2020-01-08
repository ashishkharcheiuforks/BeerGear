package com.example.beerGear.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import com.example.beerGear.databinding.FragmentBeerListBinding
import com.example.beerGear.di.Injectable
import com.example.beerGear.observer.ListViewModel
import com.example.beerGear.repo.AppRepository.Companion.LOAD_TYPE_ALL
import com.example.beerGear.util.AppExecutors
import com.example.beerGear.util.Status
import com.example.beerGear.util.then
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.gson.Gson
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BeerListFragment : Fragment(), Injectable {

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListViewModel

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var binding: FragmentBeerListBinding
    private lateinit var adapter: BeerCraftAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var beerList: List<BeerCraft>

    private var cnt = 0
    val callback = object : OnBackPressedCallback(true /* enabled by default */) {
        override fun handleOnBackPressed() {
            if (cnt == 5)
                findNavController().popBackStack()
            else
                cnt++
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        DataBindingUtil.inflate<FragmentBeerListBinding>(
            inflater,
            R.layout.fragment_beer_list,
            container,
            false,
            dataBindingComponent
        ).also {
            it.lifecycleOwner = this
            it.networkStatus = Status.LOADING
            (requireActivity() as AppCompatActivity).setSupportActionBar(it.bottomAppBar)
            binding = it
//            filterDialog =
//                FilterDialog(requireContext(), dataBindingComponent, listOf("Ascending ABU", "Descending ABU"))
        }.run {
            return this.root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        BeerCraftAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = executors
        ) { beer ->
            viewModel.updateCardItem(beer.inCart, beer.id)
        }.also {
            this.adapter = it
            binding.recyclerViewBeer.adapter = adapter
        }

        ViewModelProviders.of(this, viewModelFactory)
            .get(ListViewModel::class.java)
            .also { vm ->
                this.viewModel = vm
                vm.setResId(LOAD_TYPE_ALL)
                vm.result.observe(this, Observer { res ->
                    Log.e(Thread.currentThread().name, "res_is: ${Gson().toJson(res)}")
                    binding.networkStatus = res.status
                    res.data?.let { this.beerList = it }
                    adapter.submitList(res.data)
                    val list = res.data?.distinctBy { it.style }!!.map { it.style!! }
                    filterDialog = FilterDialog(
                        requireContext(),
                        dataBindingComponent,
                        sortOrderList = listOf("Ascending ABU", "Descending ABU"),
                        styleList = list,
                        onOrderChange = this::onFilterOrderChange,
                        onStyleChange = this::onFilterStyleChange
                    )
                })
            }

        binding.fab.setOnClickListener {
            val act = BeerListFragmentDirections.actionListFragmentToDetailFragment()
            findNavController().navigate(act)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun onFilterOrderChange(orderBy: Int) {
        adapter.submitList((orderBy == 0) then beerList.sortedBy { it.abv } ?: beerList.sortedByDescending { it.abv })
    }

    private fun onFilterStyleChange(styles: List<String>) {
        adapter.submitList(styles.flatMap { style -> beerList.filter { beer -> style == beer.style } })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item?.itemId == R.id.filter) {
//            centerToEndAnimation()
//        } else {
//            endToCenterAnimation()
//        }
//        binding?.let {
//            it.notifyChange()
//        }
        filterDialog.show()
        return super.onOptionsItemSelected(item)
    }

    fun centerToEndAnimation() {
        detachFab()
        moveToEnd()
    }

    //fab animation from end to center
    fun endToCenterAnimation() {
        detachFab()
        moveToCenter()
    }

    fun detachFab() {
        val field = BottomAppBar::class.java.getDeclaredField("fabAttached")
        field.isAccessible = true
        field.set(binding.bottomAppBar, false)
    }

    fun moveToEnd() {
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        attachFab()
    }

    fun moveToCenter() {
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        attachFab()
    }

    fun attachFab() {
        val runnable = Runnable {
            val field = BottomAppBar::class.java.getDeclaredField("fabAttached")
            field.isAccessible = true
            field.set(binding.bottomAppBar, true)
        }
        Handler().postDelayed(runnable, 150)
    }


}
