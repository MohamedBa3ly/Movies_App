package com.example.moviesapp.di

import com.example.moviesapp.repository.MoviesRepo
import com.example.moviesapp.repository.MoviesRepoImpl
import dagger.Binds
import dagger.Component.Factory
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindMyRepository(moviesRepoImpl:MoviesRepoImpl): MoviesRepo
}