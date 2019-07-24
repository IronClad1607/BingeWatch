package com.example.bingewatch.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.Crew
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cvcastcrew.view.*

class CrewAdapter(private val crew: ArrayList<Crew>?, val context: Context) :
    RecyclerView.Adapter<CrewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.cvcastcrew, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount(): Int = crew?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crew = crew?.get(position)
        holder.bind(crew!!)

    }

    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(crew: Crew) {
            with(itemView)
            {
                tvName.text = crew.name
                if (crew.department == "Writing") {
                    tvCharacter.text = "${crew.department} / ${crew.job}"
                } else {
                    tvCharacter.text = crew.job
                }
                Picasso.get().load("https://image.tmdb.org/t/p/w500${crew.profile_path}").fit().into(imagePoster)
            }
        }
    }
}