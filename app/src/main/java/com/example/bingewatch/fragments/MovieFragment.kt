package com.example.bingewatch.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.adapters.PopularMoviesAdapter
import com.example.bingewatch.model_movies.MoviesDetails
import com.example.bingewatch.networks.RetroClient
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var i: Int = 2
    var loadingMore: Boolean = false
    var lastVisibleItemId: Int = 0
    var mMoviesList = ArrayList<MoviesDetails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        launch {
            val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvPopularMovies.layoutManager = layoutManager
            createData(1, 0)
            rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemId = layoutManager.findLastVisibleItemPosition()
                    Log.d("LastItem","$lastVisibleItemId ${mMoviesList.size} $loadingMore")
                    if (lastVisibleItemId == mMoviesList.size - 1 && !loadingMore) {
                        Log.d("LastItem","LastitemReached!")
                        Log.d("LastItem","$i")
                        loadMoreData(i++)
                    }
                }
            })
        }

        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    private fun loadMoreData(i: Int) {
        loadingMore = true
        launch {
            Log.d("LastItem","Creating Data")
            createData(i, lastVisibleItemId)
        }
    }

    private suspend fun createData(page: Int, last: Int) {
        val popularAPI = RetroClient.movieAPI
        val response = popularAPI.getPopularMovies(page)

        if (response.isSuccessful) {
            val nMovies: ArrayList<MoviesDetails>? = response.body()?.results
            if (loadingMore) {
                Log.d("LastItem","""
                    $page
                    $last
                    LoadingMore
                """.trimIndent())
                mMoviesList.addAll(nMovies!!)
                rvPopularMovies.scrollToPosition(last)
            } else {
                mMoviesList = nMovies!!
                Toast.makeText(requireContext(), "Activity", Toast.LENGTH_LONG).show()
                Log.d("PTAG", "$mMoviesList")
                rvPopularMovies.adapter = PopularMoviesAdapter(mMoviesList, requireContext()).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMore = false
        }
    }
}