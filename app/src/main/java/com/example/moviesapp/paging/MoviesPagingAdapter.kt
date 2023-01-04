package com.example.moviesapp.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.moviesapp.databinding.MovieCardBinding
import com.example.moviesapp.main.adapter.MoviesAdapter
import com.example.moviesapp.paging.MoviesPagingAdapter.*
import com.example.moviesapp.model.MovieDetails
import com.squareup.picasso.Picasso

class MoviesPagingAdapter: PagingDataAdapter<MovieDetails, MyPagingViewHolder>(diffCallback) {

    //make a Listener :
    private lateinit var movieListenerPaging: OnClickPaging

    //Make an OnClick interface :
    interface OnClickPaging {
        fun onItemClickListenerPaging(pos: Int)
    }

    fun setOnItemClickListenerMovie(listener: OnClickPaging) {
        movieListenerPaging = listener
    }


    inner class MyPagingViewHolder (val binding: MovieCardBinding,listener: OnClickPaging): ViewHolder(binding.root){
        //init listener (when press on any position on card layout in recyclerView list) :
        init {
            binding.cardLayout.setOnClickListener {
                listener.onItemClickListenerPaging(bindingAdapterPosition)
            }
        }
    }



    companion object {
        val diffCallback = object :DiffUtil.ItemCallback<MovieDetails>(){
            override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: MyPagingViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.textCard.text = currentItem?.title
        if (currentItem?.poster_path?.isNotEmpty() == true) {
            Picasso.Builder(holder.binding.imageCard.context).build()
                .load(currentItem.urlImageMovie.toUri()).into(holder.binding.imageCard)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPagingViewHolder {
        return MyPagingViewHolder(MovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false),movieListenerPaging)
    }

    //Fun used when select any item in paging recycler :
    fun currentSelected(pos:Int):MovieDetails?{
        return getItem(pos)
    }
}