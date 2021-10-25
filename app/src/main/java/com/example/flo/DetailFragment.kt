package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class DetailFragment(val title : String, val singer : String):Fragment() {

    lateinit var binding:FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentDetailBinding.inflate(inflater,container,false)
        //Toast.makeText(activity,title+singer,Toast.LENGTH_SHORT).show()
        binding.detailAlbumTv.text=title
        binding.detailArtistTv.text=singer
        return binding.root
    }

}