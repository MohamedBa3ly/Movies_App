package com.example.moviesapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.network.MovieApiService
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.network.MovieTopRatedApiService
import com.example.moviesapp.paging.MoviesPagingSource
import com.example.moviesapp.paging.RemoteMediator
import com.example.moviesapp.room.MoviesDao
import com.example.moviesapp.room.MoviesDataBase
import com.example.moviesapp.room.RemoteDao
import com.example.moviesapp.utils.Constants
import com.example.moviesapp.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MoviesRepoImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val remoteDao: RemoteDao,
    private val api:MovieApiService,
    private val topRatedApi:MovieTopRatedApiService,
    private val db: MoviesDataBase
): MoviesRepo{


    override fun readAllMoviesDetails(): LiveData<List<MovieDetails>> {
        return moviesDao.readAllMovieDetails()
    }

    override suspend fun getMovieDetails(
        apiKey: String,
        movieName: String?
    ): Flow<State<List<MovieDetails>>> {
        return flow {
            emit(State.Loading)
            val result = api.searchMovies(apiKey,movieName)
            if (result.isSuccessful) {
                emit(State.Success(result.body()!!.results))
            } else {
                emit(State.Error(result.message()))
            }
        }

    }

    override suspend fun addMovieDetails(movieDetails: List<MovieDetails>) {
        moviesDao.addMovieDetails(movieDetails)
    }

    override suspend fun deleteAllMoviesDetails() {
        moviesDao.deleteAllMovieDetails()
    }

    override suspend fun deleteAllRemoteKeys() {
        remoteDao.deleteAllRemoteKeys()
    }

    override suspend fun getTopRatedMovies(
        apiKey: String,
        language: String,
        page: Int
    ): Flow<State<List<MovieDetails>>> {
        return flow {
            emit(State.Loading)
            val result = topRatedApi.topRatedMovies(apiKey,language,page)
            if (result.isSuccessful) {
                emit(State.Success(result.body()!!.results))
            } else {
                emit(State.Error(result.message()))
            }
        }
    }

    //Paging via paging source only :
    override fun getPaging(): Flow<PagingData<MovieDetails>> {
        return Pager(
            config = PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {MoviesPagingSource(topRatedApi)}
        ).flow
    }

    //Paging (remote mediator):
    @OptIn(ExperimentalPagingApi::class)
    override fun getMoviesPaging(): Flow<PagingData<MovieDetails>> {
        val pagingSourceFactory = { db.moviesDao().readAllMovieDetailsPaging() }
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_SIZE, prefetchDistance = 2, initialLoadSize = 10),
            remoteMediator = RemoteMediator(topRatedApi,db),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}