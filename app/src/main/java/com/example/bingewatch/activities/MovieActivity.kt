package com.example.bingewatch.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.adapters.CastAdapter
import com.example.bingewatch.adapters.CrewAdapter
import com.example.bingewatch.model_movies.CreditsResponse
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
            val creditBody = createCredits(movieID)
            tvTitle.text = movieBody?.title
            val year = movieBody?.release_date?.substring(0, 4)
            val genres = movieBody?.genres?.get(0)?.name + ", " + movieBody?.genres?.get(1)?.name
            tvYear.text = year
            tvGenres.text = genres
            tvBudget.text = convertBudget(movieBody?.budget)
            tvRuntime.text = convertRuntime(movieBody?.runtime)
            tvOverview.text = movieBody?.overview
            tvtagLine.text = movieBody?.tagline
            tvRelease.text = convertDate(movieBody?.release_date)
            val runtime = movieBody?.runtime.toString() + " min"
            tvRuntimeDetails.text = runtime
            rvCast.layoutManager = LinearLayoutManager(this@MovieActivity,RecyclerView.HORIZONTAL,false)
            rvCrew.layoutManager = LinearLayoutManager(this@MovieActivity,RecyclerView.HORIZONTAL,false)
            rvCast.adapter = CastAdapter(creditBody?.cast,this@MovieActivity)
            rvCrew.adapter = CrewAdapter(creditBody?.crew,this@MovieActivity)
        }


    }

    private suspend fun createDetails(id: Int): MovieAllDetails? {
        val detailsAPI = RetroClient.movieAPI
        val response = detailsAPI.getMoviesDetails(id)
        Log.d("TAG", "${response.body()}")
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    private suspend fun createCredits(id: Int): CreditsResponse? {
        val creditsAPI = RetroClient.movieAPI
        val response = creditsAPI.getCreditsDetails(id)
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

    private fun convertBudget(b: Int?): String {
        val million = 1000000L
        val billion = 1000000000L
        val trillion = 1000000000000L

        val number = Math.round(b!!.toDouble())
        if (number in million until billion) {
            val fraction = calculateFraction(number, million)
            return fraction.toString() + "Million"
        } else if (number in billion until trillion) {
            val fraction = calculateFraction(number, billion)
            return fraction.toString() + "Billion"
        }
        return number.toString()
    }

    private fun calculateFraction(number: Long, divisor: Long): Float {
        val truncate = (number * 10L + divisor / 2L) / divisor
        return truncate.toFloat() * 0.10f
    }
}
