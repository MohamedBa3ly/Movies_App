package com.example.moviesapp.main



import androidx.lifecycle.*
import androidx.paging.*
import com.example.moviesapp.model.MovieDetails
import com.example.moviesapp.paging.MoviesPagingSource
import com.example.moviesapp.repository.MoviesRepo
import com.example.moviesapp.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var repository: MoviesRepo
): ViewModel() {

    var customMovie: MutableLiveData<State<List<MovieDetails?>>> = MutableLiveData()

    //Fun to get movie from search:
    fun getMovieFinally(apiKey: String, movieName: String?){
        viewModelScope.launch {
            val result = repository.getMovieDetails(apiKey,movieName)
            result.collect{
                customMovie.postValue(it)
            }
        }
    }

    //Paging via paging source only :
    fun getPagingFinally() : Flow<PagingData<MovieDetails>> {
        return repository.getPaging().cachedIn(viewModelScope)
        }

    //Paging (remote mediator):
    fun getMoviesPagingDb():Flow<PagingData<MovieDetails>>{
        return repository.getMoviesPaging().cachedIn(viewModelScope)
    }
}