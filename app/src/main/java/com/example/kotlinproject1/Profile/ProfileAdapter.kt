@file:Suppress("DEPRECATION")

package com.example.kotlinproject1.Profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ProfileAdapter(fragmentManager: FragmentManager, private val titles: ArrayList<String>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {

       var fragment : Fragment? = null
        when (position) {
        0->   fragment = ProfileInfo()
        1->   fragment = ProfileUpdate()
        //2->   fragment = ThirdFragment()
        }

       return fragment!!

    }

    override fun getCount() = titles.size

    override fun getPageTitle(position: Int) = titles[position].decapitalize()
}