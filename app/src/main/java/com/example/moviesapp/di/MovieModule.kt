package com.example.moviesapp.di

import android.app.Application
import com.example.moviesapp.network.MovieApiService
import com.example.moviesapp.network.MovieTopRatedApiService
import com.example.moviesapp.repository.MoviesRepo
import com.example.moviesapp.repository.MoviesRepoImpl
import com.example.moviesapp.room.MoviesDao
import com.example.moviesapp.room.MoviesDataBase
import com.example.moviesapp.room.RemoteDao
import com.example.moviesapp.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    @Provides
    @Singleton
    fun provideMyApi():MovieApiService{
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyApiTopRated():MovieTopRatedApiService{
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(MovieTopRatedApiService::class.java)
    }

    @Provides
    @Singleton
    fun getInstanceDB(context:Application): MoviesDataBase{
        return MoviesDataBase.getInstance(context)
    }

    @Provides
    @Singleton
    fun getMoviesDao(appDao: MoviesDataBase): MoviesDao{
        return appDao.moviesDao()
    }

    @Provides
    @Singleton
    fun getRemoteDao(appDao: MoviesDataBase): RemoteDao{
        return appDao.remoteDao()
    }




}