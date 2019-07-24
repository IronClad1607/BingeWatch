package com.example.bingewatch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.Cast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cvcastcrew.view.*

class CastAdapter(private val cast: ArrayList<Cast>?, val context: Context) :
    RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.cvcastcrew, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount(): Int = cast?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cast = cast?.get(position)
        holder.bind(cast!!)
    }

    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        fun bind(cast: Cast) {
            with(itemView)
            {
                tvName.text = cast.name
                tvCharacter.text = cast.character
                Picasso.get().load("https://image.tmdb.org/t/p/w500${cast.profile_path}").fit().into(imagePoster)
            }
        }
    }
}