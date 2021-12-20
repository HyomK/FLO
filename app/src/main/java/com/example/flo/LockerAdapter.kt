package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->  StoredMusicFragment()
            1 -> SavedSongFragment()
            2 ->StoredAlbumFragment()
            3 ->EmptyFragment()
            else -> EmptyFragment()
        }
    }

}