package com.example.beerGear.di.module

import com.example.beerGear.ui.BeerListFragment
import com.example.beerGear.ui.CartDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeListFragment(): BeerListFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): CartDetailFragment
}