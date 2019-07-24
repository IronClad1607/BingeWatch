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
import com.example.bingewatch.adapters.movies.MoviesAdapter
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
    var iUpcoming: Int = 2
    var iTopRated: Int = 2
    var loadingMorePopular: Boolean = false
    var loadingMoreCinema: Boolean = false
    var loadingMoreUpcoming: Boolean = false
    var loadingMoreToRated: Boolean = false
    var lastVisibleItemIdPopular: Int = 0
    var lastVisibleItemIdCinema: Int = 0
    var lastVisibleItemIdUpcoming: Int = 0
    var lastVisibleItemIdTopRated: Int = 0
    var mMoviesPopular = ArrayList<MoviesDetails>()
    var mMoviesCinema = ArrayList<MoviesDetails>()
    var mMoviesUpcoming = ArrayList<MoviesDetails>()
    var mMoviesTopRated = ArrayList<MoviesDetails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        launch {
            val layoutManagerPopular = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            val layoutManagerCinema = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            val layoutManagerUpcoming = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            val layoutManagerTopRated = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvPopularMovies.layoutManager = layoutManagerPopular
            rvCinemas.layoutManager = layoutManagerCinema
            rvComingSoon.layoutManager = layoutManagerUpcoming
            rvTopRated.layoutManager = layoutManagerTopRated

            createPopularMovies(1, 0)
            createInCinema(1, 0)
            createUpcoming(1, 0)
            createTopRated(1, 0)

            rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdPopular = layoutManagerPopular.findLastVisibleItemPosition()
                    if (lastVisibleItemIdPopular == mMoviesPopular.size - 1 && !loadingMorePopular) {
                        loadMorePopular(iPopular++)
                    }
                }
            })


            rvCinemas.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdCinema = layoutManagerCinema.findLastVisibleItemPosition()
                    if (lastVisibleItemIdCinema == mMoviesCinema.size - 1 && !loadingMoreCinema) {
                        loadMoreCinema(iCinema++)
                    }
                }
            })


            rvComingSoon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdUpcoming = layoutManagerUpcoming.findLastVisibleItemPosition()
                    if (lastVisibleItemIdUpcoming == mMoviesUpcoming.size - 1 && !loadingMoreUpcoming) {
                        loadMoreUpcoming(iUpcoming++)
                    }

                }
            })


            rvTopRated.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdTopRated = layoutManagerTopRated.findLastVisibleItemPosition()
                    if (lastVisibleItemIdTopRated == mMoviesTopRated.size - 1 && !loadingMoreToRated) {
                        loadMoreTopRated(iTopRated++)
                    }
                }
            })
        }

        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    private fun loadMoreTopRated(i: Int) {
        loadingMoreToRated = true
        launch {
            createTopRated(i, lastVisibleItemIdTopRated)
        }
    }

    private suspend fun createTopRated(page: Int, last: Int) {
        val topRatedAPI = RetroClient.movieAPI
        val response = topRatedAPI.getTopRated(page)
        if (response.isSuccessful) {
            val nMovies: ArrayList<MoviesDetails>? = response.body()?.results
            if (loadingMoreToRated) {
                mMoviesTopRated.addAll(nMovies!!)
                rvTopRated.scrollToPosition(last)
            } else {
                mMoviesTopRated = nMovies!!
                rvTopRated.adapter = MoviesAdapter(
                    mMoviesTopRated,
                    requireContext()
                ).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMoreToRated = false
        }
    }

    private fun loadMoreUpcoming(i: Int) {
        loadingMoreUpcoming = true
        launch {
            createUpcoming(i, lastVisibleItemIdUpcoming)
        }
    }

    private suspend fun createUpcoming(page: Int, last: Int) {
        val upcomingAPI = RetroClient.movieAPI
        val response = upcomingAPI.getUpcoming(page)
        if (response.isSuccessful) {
            val nMovies: ArrayList<MoviesDetails>? = response.body()?.results
            if (loadingMoreUpcoming) {
                mMoviesUpcoming.addAll(nMovies!!)
                rvComingSoon.scrollToPosition(last)
            } else {
                mMoviesUpcoming = nMovies!!
                rvComingSoon.adapter = MoviesAdapter(
                    mMoviesUpcoming,
                    requireContext()
                ).apply {
                    notifyDataSetChanged()
                }
            }

            loadingMoreUpcoming = false
        }
    }

    private fun loadMoreCinema(i: Int) {
        loadingMoreCinema = true
        launch {
            Log.d("CinemaCall", "Creating Data")
            createInCinema(i, lastVisibleItemIdCinema)
        }
    }

    private suspend fun createInCinema(page: Int, last: Int) {
        Log.d("CinemaCall", "createInCinema")
        val cinemaAPI = RetroClient.movieAPI
        val response = cinemaAPI.getInCinema(page)
        Log.d("CinemaCall", "${response.body()}")
        if (response.isSuccessful) {
            val nMovies: ArrayList<MoviesDetails>? = response.body()?.results
            if (loadingMoreCinema) {
                Log.d(
                    "CinemaCall", """
                    $page
                    $last
                    LoadingMore
                """.trimIndent()
                )
                mMoviesCinema.addAll(nMovies!!)
                rvCinemas.scrollToPosition(last)
            } else {
                mMoviesCinema = nMovies!!
                Log.d("CinemaCall", "$mMoviesCinema")
                rvCinemas.adapter = MoviesAdapter(
                    mMoviesCinema,
                    requireContext()
                ).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMoreCinema = false
        } else {
            Log.d("CinemaCall", "Call Failed!")
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
                rvPopularMovies.adapter = MoviesAdapter(
                    mMoviesPopular,
                    requireContext()
                ).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMorePopular = false
        }
    }
}