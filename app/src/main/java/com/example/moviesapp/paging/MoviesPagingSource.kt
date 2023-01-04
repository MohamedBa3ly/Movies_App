package com.example.moviesapp.paging


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.network.MovieTopRatedApiService
import com.example.moviesapp.utils.Constants


class MoviesPagingSource(private var topRatedApi:MovieTopRatedApiService): PagingSource<Int,MovieDetails>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDetails>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetails> {
        return try {
            val currentPage = params.key ?: Constants.Page
            val response = topRatedApi.topRatedMovies(BuildConfig.API_KEY,Constants.language,currentPage)
            val movies = response.body()?.results
            val nextKey = if (movies!!.isEmpty()){
                null
            }else{
                currentPage + 1
            }
            Log.d("TAG", "current page: $currentPage")

            LoadResult.Page(
                data = movies,
                prevKey = if (currentPage==Constants.Page) null else currentPage,
                nextKey = nextKey
            )

        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

}