package com.example.bingewatch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.bingewatch.R
import com.example.bingewatch.adapters.ViewPageAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_major.*

class MajorActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Movies"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.vpMain)


        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.movies))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.tvSeries))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.celebs))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPageAdapter(supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                viewPager!!.currentItem = p0!!.position
                toolbar.title = p0.text
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.account -> {
            true
        }
        android.R.id.home -> {
            finishAffinity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
