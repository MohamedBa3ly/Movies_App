package com.example.moviesapp.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.network.MovieTopRatedApiService
import com.example.moviesapp.room.MoviesDataBase
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.model.RemoteKeys
import com.example.moviesapp.utils.Constants
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class RemoteMediator (private val movieTopRated:MovieTopRatedApiService, private val db:MoviesDataBase): RemoteMediator<Int,MovieDetails>() {

    private val allDao = db.moviesDao()
    private val remoteKeysDao = db.remoteDao()


    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - (db.remoteDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDetails>
    ): MediatorResult {
        return try {

            val currentPage = when(loadType){
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {

                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            Log.d("TAG", "current page: $currentPage")

            val response = movieTopRated.topRatedMovies(BuildConfig.API_KEY,Constants.language,currentPage)
            val endOfPaginationReached = response.body()!!.results.isEmpty()

            Log.d("TAG", "true or false: $endOfPaginationReached")

            val prevPage = if (currentPage == 1) null else currentPage-1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            Log.d("TAG", "previous page: $prevPage")
            Log.d("TAG", "next page: $nextPage")

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    allDao.deleteAllMovieDetails()
                    remoteKeysDao.deleteAllRemoteKeys()
                }


                val keys = response.body()!!.results.map {
                    RemoteKeys(
                        movieId = it.id,
                        prevPage = prevPage,
                        currentPage = currentPage,
                        nextPage = nextPage
                    )
                }


                remoteKeysDao.addRemoteKeys(keys)
                Log.d("TAG", "keys: $keys")

                allDao.addMovieDetails(response.body()!!.results.onEachIndexed { _, movieDetails -> movieDetails.page = currentPage })



            }

            Log.d("TAG", "true or false: $endOfPaginationReached")
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        }catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }



    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieDetails>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.readRemoteKeys(id = id)
            }
        }

    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieDetails>
    ): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { MovieDetails ->
                remoteKeysDao.readRemoteKeys(id = MovieDetails.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieDetails>
    ): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { MovieDetails ->
                remoteKeysDao.readRemoteKeys(id = MovieDetails.id)
            }
    }


}