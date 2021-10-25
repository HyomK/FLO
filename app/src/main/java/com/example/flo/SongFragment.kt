package com.example.flo

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment: Fragment() {
    lateinit var binding : FragmentSongBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentSongBinding.inflate(inflater,container,false)


        binding.songList02Layout.setOnClickListener {
            Toast.makeText(activity,binding.albumSongTitle02Tv.text,Toast.LENGTH_SHORT).show()
        }

        binding.songListLayout.setOnClickListener {
            Toast.makeText(activity,binding.albumSongTitle01Tv.text,Toast.LENGTH_SHORT).show()
        }
        binding.albumMixBtn.setOnClickListener {

            val OffStatus :Drawable = resources.getDrawable(R.drawable.btn_toggle_off, null)

            if(OffStatus.constantState==(binding.albumMixSwitchBtn.drawable.constantState)){
                binding.albumMixSwitchBtn.setImageDrawable(resources.getDrawable(R.drawable.btn_toggle_on, null))
            }
            else{
                binding.albumMixSwitchBtn.setImageDrawable(OffStatus)
            }

        }



        binding.albumWholeSelectBtn.setOnClickListener {
            val ALL ="전체선택"
            val NONE="선택해제"
            if(binding.albumWholeSelectBtn.text.equals(NONE)){
                binding.albumWholeSelectBtn.setTextColor(resources.getColor(R.color.black,null))
                binding.albumWholeSelectCheckBtn.clearColorFilter()
                binding.albumWholeSelectBtn.text=ALL
                binding.frSongListLayout.background=null
            }else{
                binding.albumWholeSelectBtn.setTextColor(resources.getColor(R.color.blue,null))
                binding.albumWholeSelectCheckBtn.setColorFilter(R.color.blue)
                binding.albumWholeSelectBtn.text=NONE
                binding.frSongListLayout.setBackgroundColor(resources.getColor(R.color.silver,null))
            }
        }



        return binding.root
    }
}