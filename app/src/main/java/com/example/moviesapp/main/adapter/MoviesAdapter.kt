package com.example.moviesapp.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.databinding.MovieCardBinding
import com.example.moviesapp.main.MainViewModel
import com.example.moviesapp.model.MovieDetails
import com.squareup.picasso.Picasso

class MoviesAdapter: RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    var oldMoviesList = emptyList<MovieDetails>()
    private lateinit var moviesViewModel: MainViewModel

    //make a Listener :
    private lateinit var movieListener: OnClick

    //Make an OnClick interface :
    interface OnClick {
        fun onItemClickListener(pos: Int)
    }

    fun setOnItemClickListenerMovie(listener: OnClick) {
        movieListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), movieListener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movieNow = oldMoviesList[position]
        holder.binding.textCard.text = movieNow.title
        if(movieNow.poster_path?.isNotEmpty() == true){
            Picasso.Builder(holder.binding.imageCard.context).build()
                .load(movieNow.urlImageMovie.toUri()).into(holder.binding.imageCard)
        }

    }

    override fun getItemCount(): Int {
        return oldMoviesList.size
    }

    class MyViewHolder(var binding: MovieCardBinding, listener: OnClick) :
        RecyclerView.ViewHolder(binding.root) {

        //init listener (when press on any position on card layout in recyclerView list) :
        init {
            binding.cardLayout.setOnClickListener {
                listener.onItemClickListener(bindingAdapterPosition)
            }
        }
    }

    fun setData(newMoviesList:List<MovieDetails>){
        val diffUtil = MyDiffUtil(oldMoviesList,newMoviesList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldMoviesList = newMoviesList
        diffResult.dispatchUpdatesTo(this)
    }

    //fun to Setup ViewModel in adapter :
    fun setUpViewModel(vm: MainViewModel) {
        moviesViewModel = vm
    }





}