package com.example.beerGear.di

import android.app.Application
import com.example.beerGear.di.module.ActivityModule
import com.example.beerGear.di.module.AppModule
import com.example.beerGear.util.AppController
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    AndroidSupportInjectionModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: AppController)
}