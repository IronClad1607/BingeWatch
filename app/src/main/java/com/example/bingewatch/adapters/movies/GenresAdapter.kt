package com.example.bingewatch.adapters.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.Genres
import kotlinx.android.synthetic.main.cvgenremovie.view.*

class GenresAdapter(private val genres: ArrayList<Genres>?, val context: Context) :
    RecyclerView.Adapter<GenresAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.cvgenremovie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = genres?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres?.get(position)
        holder.binf(genre!!)
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun binf(genres: Genres) {
            with(itemView)
            {
                gen.text = genres.name
            }
        }
    }
}