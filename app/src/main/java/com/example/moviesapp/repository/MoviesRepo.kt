package com.example.moviesapp.repository


import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.moviesapp.model.ApiDetailsResponse
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.utils.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface MoviesRepo {

    //Fun to get movies from search :
    suspend fun getMovieDetails(apiKey: String, movieName: String?): Flow<State<List<MovieDetails>>>

    //Fun for Room Data Base: read,add and delete :)
    fun readAllMoviesDetails():LiveData<List<MovieDetails>>
    suspend fun addMovieDetails(movieDetails: List<MovieDetails>)
    suspend fun deleteAllMoviesDetails()
    //Fun to delete remote keys from room data base :
    suspend fun deleteAllRemoteKeys()

    //Fun to get Top Rated movies :)
    suspend fun getTopRatedMovies(apiKey: String, language: String, page:Int): Flow<State<List<MovieDetails>>>

    //Paging
    fun getPaging():Flow<PagingData<MovieDetails>>

    fun getMoviesPaging(): Flow<PagingData<MovieDetails>>

}