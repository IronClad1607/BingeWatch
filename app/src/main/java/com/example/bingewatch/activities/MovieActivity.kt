package com.example.bingewatch.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.adapters.movies.*
import com.example.bingewatch.model_movies.CreditsResponse
import com.example.bingewatch.model_movies.MovieAllDetails
import com.example.bingewatch.model_movies.MoviesDetails
import com.example.bingewatch.model_movies.Review
import com.example.bingewatch.networks.RetroClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.content_movie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MovieActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    var iReview: Int = 2
    var iSimilar: Int = 2
    var loadingMoreReview: Boolean = false
    var loadingMoreSimilar: Boolean = false
    var lastVisibleItemIdReview: Int = 0
    var lastVisibleItemIdSimilar: Int = 0
    var mReview = ArrayList<Review>()
    var mSimilar = ArrayList<MoviesDetails>()
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieID = intent.getIntExtra("movieId", 0)
        launch {
            val movieBody = createDetails(movieID)
            val creditBody = createCredits(movieID)
            Log.d("Cast","${creditBody?.cast}")
            tvTitle.text = movieBody?.title
            val year = movieBody?.release_date?.substring(0, 4)
            val genres = if (movieBody?.genres?.size!! >= 2) {
                movieBody.genres[0].name + ", " + movieBody.genres[1].name
            } else {
                movieBody.genres[0].name
            }
            tvYear.text = year
            Picasso.get().load("https://image.tmdb.org/t/p/w500${movieBody.backdrop_path}").fit().into(backdropImage)
            tvGenres.text = genres
            tvBudget.text = convertBudget(movieBody.budget)
            tvRuntime.text = convertRuntime(movieBody.runtime)
            tvOverview.text = movieBody.overview
            tvtagLine.text = movieBody.tagline
            tvRelease.text = convertDate(movieBody.release_date)
            val runtime = movieBody.runtime.toString() + " min"
            tvRuntimeDetails.text = runtime
            rvCast.layoutManager = LinearLayoutManager(this@MovieActivity, RecyclerView.HORIZONTAL, false)
            rvCrew.layoutManager = LinearLayoutManager(this@MovieActivity, RecyclerView.HORIZONTAL, false)
            rvGenresInMovies.layoutManager = LinearLayoutManager(this@MovieActivity, RecyclerView.HORIZONTAL, false)
            rvPC.layoutManager = LinearLayoutManager(this@MovieActivity, RecyclerView.HORIZONTAL, false)
            val layoutManagerReview = LinearLayoutManager(this@MovieActivity, RecyclerView.HORIZONTAL, false)
            val layoutManagerSimilar = LinearLayoutManager(this@MovieActivity, RecyclerView.HORIZONTAL, false)
            rvReviews.layoutManager = layoutManagerReview
            rvSimilar.layoutManager = layoutManagerSimilar
            rvCast.adapter = CastAdapter(creditBody?.cast, this@MovieActivity)
            rvCrew.adapter = CrewAdapter(creditBody?.crew, this@MovieActivity)
            rvGenresInMovies.adapter =
                GenresAdapter(movieBody.genres, this@MovieActivity)
            rvPC.adapter = CompanyAdapter(movieBody.production_companies, this@MovieActivity)


            createReviews(1, movieID, 0)
            createSimilar(1, movieID, 0)

            rvReviews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdReview = layoutManagerReview.findLastVisibleItemPosition()
                    if (lastVisibleItemIdReview == mReview.size - 1 && !loadingMoreReview) {
                        loadMoreReview(iReview++, movieID)
                    }

                }
            })

            rvSimilar.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastVisibleItemIdSimilar = layoutManagerSimilar.findLastVisibleItemPosition()
                    Log.d(
                        "Similar", """
                        $lastVisibleItemIdSimilar
                        ${mSimilar.size}
                    """.trimIndent()
                    )
                    if (lastVisibleItemIdSimilar == mSimilar.size - 1 && !loadingMoreSimilar) {
                        loadMoreSimilar(iSimilar++, movieID)
                    }
                }
            })
        }


    }

    private fun loadMoreSimilar(i: Int, movieID: Int) {
        loadingMoreSimilar = true
        launch {
            Log.d(
                "Similar", """
                $loadingMoreSimilar
                Creating Similar
            """.trimIndent()
            )
            createSimilar(i, movieID, lastVisibleItemIdSimilar)
        }
    }

    private suspend fun createSimilar(page: Int, movieID: Int, last: Int) {
        val similarAPI = RetroClient.movieAPI
        val response = similarAPI.getSimilar(movieID, page)
        Log.d("Similar", "$response")
        if (response.isSuccessful) {
            val nSimilar: ArrayList<MoviesDetails>? = response.body()?.results
            if (loadingMoreSimilar) {
                mSimilar.addAll(nSimilar!!)
                rvSimilar.scrollToPosition(last)
            } else {
                mSimilar = nSimilar!!
                rvSimilar.adapter = MoviesAdapter(mSimilar, this).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMoreSimilar = false
        }
    }

    private fun loadMoreReview(i: Int, movieID: Int) {
        loadingMoreReview = true
        launch {
            createReviews(i, movieID, lastVisibleItemIdReview)
        }
    }

    private suspend fun createReviews(page: Int, movieID: Int, last: Int) {
        val reviewAPI = RetroClient.movieAPI
        val response = reviewAPI.getReviews(movieID, page)

        if (response.isSuccessful) {
            val nReview: ArrayList<Review>? = response.body()?.results
            Log.d("TAG", "$nReview")
            if (loadingMoreReview) {
                mReview.addAll(nReview!!)
                rvReviews.scrollToPosition(last)
            } else {
                mReview = nReview!!
                rvReviews.adapter = ReviewAdapter(mReview, this).apply {
                    notifyDataSetChanged()
                }
            }
            loadingMoreReview = false

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
        Log.d(
            "PIU", """
            ${response.body()?.crew}
        """.trimIndent()
        )
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
