package com.example.bingewatch.adapters.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.Review
import kotlinx.android.synthetic.main.cvreviews.view.*

class ReviewAdapter(private val reviews: ArrayList<Review>, val context: Context) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.cvreviews, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[0]
        holder.bind(review)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(review: Review) {
            with(itemView) {
                tvAuthor.text = review.author
                tvContent.text = review.content
            }
        }
    }
}