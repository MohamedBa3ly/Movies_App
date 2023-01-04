package com.example.moviesapp.network

import com.example.moviesapp.model.ApiDetailsResponse
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") movieName: String?
    ): Response<ApiDetailsResponse>
}

interface MovieTopRatedApiService {
    @GET("3/movie/top_rated")
    suspend fun topRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page:Int
    ): Response<ApiDetailsResponse>
}

