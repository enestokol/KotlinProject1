package com.example.kotlinproject1.Profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinproject1.R
import kotlinx.android.synthetic.main.profile_activity_layout.*

class ProfileActivity: AppCompatActivity() {
    private val tabTitles = arrayListOf("Profile", "Change Password")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile_activity_layout)
        val tabsAdapter=ProfileAdapter(supportFragmentManager,tabTitles)
        with(tabsActivityVP){
            adapter=tabsAdapter
            offscreenPageLimit=2
            tabsActivityTL.setupWithViewPager(this)
        }
    }
}