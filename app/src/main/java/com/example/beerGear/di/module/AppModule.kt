package com.example.beerGear.di.module

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.beerGear.database.AppDao
import com.example.beerGear.database.AppDatabase
import com.example.beerGear.repo.AppRepository
import com.example.beerGear.util.AppExecutors
import com.example.beerGear.util.LiveDataCallAdapterFactory
import com.example.beerGear.util.WebService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "beer_craft_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideExecutor(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase) = database.appDao()

    @Provides
    @Singleton
    fun provideAppRepository(webservice: WebService, executor: AppExecutors, dao: AppDao): AppRepository {
        return AppRepository(webservice, executor, dao)
    }

    private val apiKey = "97fa5b00c30b31fe61fab18d5f35d3ea"
    private val baseUrl = "http://starlord.hackerearth.com/"

    @SuppressLint("NewApi")
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS) .addInterceptor { chain ->
                val original = chain.request()
                Log.e(Thread.currentThread().toString(), "api called - ${chain.request().url()} ${chain.request().body().toString()}")

                // add request headers
                val request = original.newBuilder()
                    .header("user-key", apiKey)
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        okHttpClient.sslSocketFactory()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiWebservice(restAdapter: Retrofit): WebService {
        return restAdapter.create(WebService::class.java)
    }
}