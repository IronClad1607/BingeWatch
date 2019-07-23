package com.example.bingewatch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.MoviesDetails
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cvmovies.view.*

class MoviesAdapter(private val popularMovies: ArrayList<MoviesDetails>, val context: Context) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.cvmovies, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount() = popularMovies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val popularMovie = popularMovies[position]
        holder.bind(popularMovie)
    }


    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(popularMovies: MoviesDetails) {
            with(itemView) {
                tvTitleMovie.text = popularMovies.title
                val year = popularMovies.release_date.substring(0, 4)
                tvYearMovie.text = year
                Picasso.get().load("https://image.tmdb.org/t/p/w500${popularMovies.poster_path}").fit()
                    .into(imagePoster)
            }
        }
    }
}