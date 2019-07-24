package com.example.bingewatch.adapters.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bingewatch.R
import com.example.bingewatch.model_movies.ProductionCompanies
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cvproduction.view.*

class CompanyAdapter(private val companies: ArrayList<ProductionCompanies>?, val context: Context) :
    RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.cvproduction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = companies?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = companies?.get(position)
        holder.bind(company!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(companies: ProductionCompanies) {
            with(itemView) {
                tvCompany.text = companies.name
                Picasso.get().load("https://image.tmdb.org/t/p/w500${companies.logo_path}").into(imagePoster)
            }
        }
    }
}