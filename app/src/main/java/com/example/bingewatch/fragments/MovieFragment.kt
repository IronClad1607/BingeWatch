package com.example.bingewatch.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.adapters.MoviesAdapter
import com.example.bingewatch.model_movies.MoviesDetails
import com.example.bingewatch.networks.RetroClient
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieFragment : Fragment(), CoroutineScope {
    private val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisor

    var iPopular: Int = 2
    var iCinema: Int = 2
    var loadingMorePopular: Boolean = false
    var loadingMoreCinema: Boolean = false
    var lastVisibleItemIdPopular: Int = 0
    var lastVisibleItemIdCinema: Int = 0
    var mMoviesPopular = ArrayList<MoviesDetails>()
    var mMoviesCinema = ArrayList<MoviesDetails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        launch {
            val layoutManagerPopular = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            val layoutManagerCinema = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvPopularMovies.layoutManager = layoutManagerPopular
            rvCinemas.layoutManager = layoutManagerCinema
            createPopularMovies(1, 0)
            createInCinema(1, 0)


            rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdPopular = layoutManagerPopular.findLastVisibleItemPosition()
                    Log.d("LastItem", "$lastVisibleItemIdPopular ${mMoviesPopular.size} $loadingMorePopular")
                    if (lastVisibleItemIdPopular == mMoviesPopular.size - 1 && !loadingMorePopular) {
                        Log.d("LastItem", "LastitemReached!")
                        Log.d("LastItem", "$iPopular")
                        loadMorePopular(iPopular++)
                    }
                }
            })


            rvCinemas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdCinema = layoutManagerCinema.findLastVisibleItemPosition()
                    Log.d("CinemaCall", "$lastVisibleItemIdCinema ${mMoviesCinema.size} $loadingMoreCinema")
                    if (lastVisibleItemIdCinema == mMoviesCinema.size -1 && !loadingMoreCinema){
                        Log.d("CinemaCall", "LastitemReached!")
                        Log.d("CinemaCall", "$iCinema")
                        loadMoreCinema(iCinema++)
                    }
                }
            })
        }

        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    private fun loadMoreCinema(i: Int) {
        loadingMoreCinema = true
        launch {
            Log.d("CinemaCall", "Creating Data")
            createInCinema(i,lastVisibleItemIdCinema)
        }
    }

    private suspend fun createInCinema(page: Int, last: Int) {
        Log.d("CinemaCall","createInCinema")
        val cinemaAPI = RetroClient.movieAPI
        val response = cinemaAPI.getInCinema(page)
        Log.d("CinemaCall","${response.body()}")
        if(response.isSuccessful){
            val nMovies : ArrayList<MoviesDetails>? = response.body()?.results
            if(loadingMoreCinema){
                Log.d(
                    "CinemaCall", """
                    $page
                    $last
                    LoadingMore
                """.trimIndent()
                )
                mMoviesCinema.addAll(nMovies!!)
                rvCinemas.scrollToPosition(last)
            }
            else{
                mMoviesCinema = nMovies!!
                Log.d("CinemaCall", "$mMoviesCinema")
                rvCinemas.adapter = MoviesAdapter(mMoviesCinema,requireContext()).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMoreCinema = false
        }
        else{
            Log.d("CinemaCall","Call Failed!")
        }
    }

    private fun loadMorePopular(i: Int) {
        loadingMorePopular = true
        launch {
            Log.d("LastItem", "Creating Data")
            createPopularMovies(i, lastVisibleItemIdPopular)
        }
    }

    private suspend fun createPopularMovies(page: Int, last: Int) {
        val popularAPI = RetroClient.movieAPI
        val response = popularAPI.getPopularMovies(page)

        if (response.isSuccessful) {
            val nMovies: ArrayList<MoviesDetails>? = response.body()?.results
            if (loadingMorePopular) {
                Log.d(
                    "LastItem", """
                    $page
                    $last
                    LoadingMore
                """.trimIndent()
                )
                mMoviesPopular.addAll(nMovies!!)
                rvPopularMovies.scrollToPosition(last)
            } else {
                mMoviesPopular = nMovies!!
                Log.d("PTAG", "$mMoviesPopular")
                rvPopularMovies.adapter = MoviesAdapter(mMoviesPopular, requireContext()).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMorePopular = false
        }
    }
}