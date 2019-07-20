package com.example.bingewatch.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.bingewatch.fragments.CelebrityFragment
import com.example.bingewatch.fragments.MovieFragment
import com.example.bingewatch.fragments.TVSeriesFragment

class ViewPageAdapter(fragmentManager: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> MovieFragment()
            1 -> TVSeriesFragment()
            2 -> CelebrityFragment()
            else -> null
        }
    }

    override fun getCount() = totalTabs

}