package com.example.bingewatch.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.bingewatch.movie_fragments.FavMovies
import com.example.bingewatch.movie_fragments.PopularMovies
import com.example.bingewatch.movie_fragments.UpcomingMovie

class ViewPagerAdapter(val myContext: Context, fm: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                PopularMovies()
            }
            1 -> {
                UpcomingMovie()
            }
            2 -> {
                FavMovies()
            }
            else -> null
        }
    }

    override fun getCount() = totalTabs

}