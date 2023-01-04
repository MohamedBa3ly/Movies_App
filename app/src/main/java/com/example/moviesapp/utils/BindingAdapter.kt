package com.example.moviesapp.utils

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.example.moviesapp.model.MovieDetails
import com.squareup.picasso.Picasso


@BindingAdapter("app:showImage")
fun showImageMovie(imageView: ImageView,poster_path:String?){
    Picasso.Builder(imageView.context).build().load("https://image.tmdb.org/t/p/w500${poster_path}".toUri()).into(imageView)
}

@BindingAdapter("app:showWhenLoading")
fun<T> showWhenLoading(view: View, state: State<T>?){
    if (state is State.Loading){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:showWhenError")
fun<T> showWhenError(view: View, state: State<T>?){
    if (state is State.Error){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:showWhenSuccess")
fun<T> showWhenSuccess(view: View, state: State<T>?){
    if (state is State.Success){
        view.visibility = View.VISIBLE
    }else{
        view.visibility = View.GONE
    }
}