package com.example.moviesapp.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.moviesapp.model.MovieDetails

class MyDiffUtil(
    private val oldMoviesList: List<MovieDetails>,
    private val newMoviesList: List<MovieDetails>
): DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldMoviesList.size
    }

    override fun getNewListSize(): Int {
        return newMoviesList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMoviesList[oldItemPosition].id == newMoviesList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldMoviesList[oldItemPosition].id != newMoviesList[newItemPosition].id ->{
                false
            }
            oldMoviesList[oldItemPosition].title != newMoviesList[newItemPosition].title ->{
                false
            }
            oldMoviesList[oldItemPosition].urlImageMovie != newMoviesList[newItemPosition].urlImageMovie ->{
                false
            }
            else -> true
        }
    }
}