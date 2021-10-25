package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBackgroundBinding


class BackgroundFragment( val imgBg : Int ,val  album1 : Int, val album2 : Int, val text: String): Fragment() {

    lateinit var binding: FragmentBackgroundBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        binding= FragmentBackgroundBinding.inflate(inflater, container, false)
        binding.bgFrImgIv.setImageResource(imgBg)
        binding.bgHomeTitleTv.text=text
        binding.bgHomeAlbumEpx01Iv.setImageResource(album1)
        binding.bgHomeAlbumEpx02Iv.setImageResource(album2)
        return binding.root
    }

}

