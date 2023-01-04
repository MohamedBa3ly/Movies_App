package com.example.moviesapp.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.moviesapp.model.MovieDetails

@Dao
interface MoviesDao {

    //To insert data in Movies data base :
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieDetails(movieDetails:List<MovieDetails>)

    //To read all data in Movies data base :
    @Query("SELECT * FROM movies_table")
    fun readAllMovieDetails() : LiveData<List<MovieDetails>>

    //To update data in Movies data base :
    @Update
    suspend fun updateMovieDetails(movieDetails: MovieDetails)

    //To delete data in Movies data base :
    @Delete
    suspend fun deleteMovieDetails(movieDetails: MovieDetails)

    //To delete all data in Movies data base :
    @Query("DELETE FROM movies_table")
    suspend fun deleteAllMovieDetails()


    //Paging :
    //To read all data in Movies data base :
    @Query("SELECT * FROM movies_table ORDER BY page")
    fun readAllMovieDetailsPaging() : PagingSource<Int,MovieDetails>


}