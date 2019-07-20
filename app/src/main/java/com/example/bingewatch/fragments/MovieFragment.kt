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
import com.example.bingewatch.adapters.PopularMoviesAdapter
import com.example.bingewatch.networks.getPopularMovies
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {
            val popularMovies = getPopularMovies()
            Log.d("PUI","$popularMovies")
            rvPopularMovies.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvPopularMovies.adapter = PopularMoviesAdapter(popularMovies!!.results, requireContext())

        }
    }
}
