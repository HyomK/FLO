package com.example.flo

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.databinding.FragmentBannerBinding

class AlbumViewpagerAdapter(fragment: Fragment, val title: String, val singer : String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->  SongFragment()
            1 -> DetailFragment(title,singer)
            else -> VideoFragment()
        }
    }

}