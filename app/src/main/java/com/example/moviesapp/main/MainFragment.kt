package com.example.moviesapp.main

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentMainBinding
import com.example.moviesapp.main.adapter.MoviesAdapter
import com.example.moviesapp.paging.MoviesPagingAdapter
import com.example.moviesapp.model.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var movieViewModel: MainViewModel
    private val adapter = MoviesAdapter()
    private val pagingAdapter = MoviesPagingAdapter()

//    private val movieViewModel :MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializing:
        movieViewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        movieViewModel
        initialize()

        //Fun to add search menu :
        addSearch()

        //Show movies at Start:
        remoteMediatorPaging()
        //Show movies with pagingSource fun if  u want ( just put it here at start if u want but now i am using remote mediator :)

        //Transfer to Movies Details:
        detailFragmentItems()

        //Transfer to movie details when press on paging recycler:
        detailFragmentWhenPressInPaging()

    }

    //Fun Initializing :
    private fun initialize() {
        binding.lifecycleOwner = this
        binding.viewModel = movieViewModel
    }

    //Fun to Click on any items in the list to go to Detail Fragment :
    private fun detailFragmentItems() {
        adapter.setOnItemClickListenerMovie(object : MoviesAdapter.OnClick {
            override fun onItemClickListener(pos: Int) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailFragment(
                        adapter.oldMoviesList[pos]
                    )
                )
            }
        })
    }

    //Fun to click on any items in paging recycler view :
    private fun detailFragmentWhenPressInPaging() {
        pagingAdapter.setOnItemClickListenerMovie(object : MoviesPagingAdapter.OnClickPaging {
            override fun onItemClickListenerPaging(pos: Int) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailFragment(
                        pagingAdapter.currentSelected(pos)!!
                    )
                )
            }
        })
    }

    //Fun to show Top Rated Movies when open app :
    private fun remoteMediatorPaging() {
        binding.rv.adapter = pagingAdapter
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.getMoviesPagingDb().collect {
                pagingAdapter.submitData(it)
            }
        }
    }

    //Fun to show Top Rated Movies when open app (Use it only to show top rated movies without caching):
    private fun pagingSource() {
        binding.rv.adapter = pagingAdapter
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.getPagingFinally().collect {
                pagingAdapter.submitData(it)
            }
        }
    }

    //Fun to add search in toolbar :
    private fun addSearch() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                //Search view :
                val item = menu.findItem(R.id.search)
                (item.actionView as SearchView).apply {

                    maxWidth = Integer.MAX_VALUE
                    queryHint = "Search for movies"

                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean {

                            if (p0 != null) {
                                setDataForSearchOnTyping(p0)
                            }
                            return true
                        }

                        override fun onQueryTextChange(p0: String): Boolean {

                            setDataForSearchOnTyping(p0)

                            return true
                        }
                    })

                    callbackFlow<String> {

                        awaitClose { setOnQueryTextListener(null) }

                    }.debounce(300)
                        .distinctUntilChanged()
                        .flowOn(Dispatchers.IO)
                        .launchIn(lifecycleScope)

                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun setDataForSearchOnTyping(textSearch: String) {
        //setup view model in adapter :
        adapter.setUpViewModel(movieViewModel)

        //setup adapter :
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)


        if (textSearch.isNotEmpty()) {
            movieViewModel.apply {
                getMovieFinally(BuildConfig.API_KEY, textSearch)
                customMovie.observe(viewLifecycleOwner, Observer {

                    if (it.toData()?.isNotEmpty() == true) {
                        adapter.setData(it.toData() as List<MovieDetails>)
                    }
                })
            }
        } else {
            //If user close search, it will show first page that show top rated movies :
            remoteMediatorPaging()
        }
    }
}