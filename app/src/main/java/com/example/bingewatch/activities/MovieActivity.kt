package com.example.bingewatch.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.MovieAllDetails
import com.example.bingewatch.networks.RetroClient
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.content_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        setSupportActionBar(toolbar)

        val movieID = intent.getIntExtra("movieId", 0)
        launch {
            val movieBody = createDetails(movieID)
            tvTitle.text = movieBody?.title
            val year = movieBody?.release_date?.substring(0, 4)
            val genres = movieBody?.genres?.get(0)?.name + ", " + movieBody?.genres?.get(1)?.name
            tvYear.text = year
            tvGenres.text = genres
            tvRuntime.text = convertRuntime(movieBody?.runtime)
            tvOverview.text = movieBody?.overview
            tvtagLine.text = movieBody?.tagline
            tvRelease.text = convertDate(movieBody?.release_date)
            val runtime = movieBody?.runtime.toString() + " min"
            tvRuntimeDetails.text = runtime
        }


    }

    private suspend fun createDetails(id: Int): MovieAllDetails? {
        val detailsAPI = RetroClient.movieAPI
        val response = detailsAPI.getMoviesDetails(id)
        Log.d("TAG", "$response")
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    private fun convertRuntime(r: Int?): String {
        val hr = r?.div(60)
        val min = r?.rem(60)
        return "${hr.toString()}hr ${min.toString()}min"
    }

    private fun convertDate(str: String?): String {
        val month = str?.substring(5, 7)
        val year = str?.substring(0, 4)
        val date = str?.substring(8, 10)
        val monStr = when (month) {
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            else -> "December"
        }

        return "$monStr $date, $year"
    }
}
