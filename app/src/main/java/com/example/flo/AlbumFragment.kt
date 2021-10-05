package com.example.flo

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.databinding.ToastCustomBinding

class AlbumFragment : Fragment() {
    lateinit var binding:FragmentAlbumBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState:
            Bundle?): View? {

        binding= FragmentAlbumBinding.inflate(inflater, container,false)

        binding.albumBackIc.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
        }

        binding.songList02Layout.setOnClickListener {
            Toast.makeText(activity,binding.songList01TitleLayout.text,Toast.LENGTH_SHORT).show()
        }

        binding.songListLayout.setOnClickListener {
            Toast.makeText(activity,binding.albumSongTitle02Tv.text,Toast.LENGTH_SHORT).show()
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

        binding.albumHeartBtn.setOnClickListener {
            val OffStatus :Drawable = resources.getDrawable(R.drawable.ic_my_like_off, null)

            if(OffStatus.constantState==(binding.albumHeartBtn.drawable.constantState)){
                binding.albumHeartBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_my_like_on, null))
            }
            else{
                binding.albumHeartBtn.setImageDrawable(OffStatus)
            }
        }

        val barOff : Drawable =  resources.getDrawable(R.drawable.btn_bar_off, null)
        val barOn : Drawable =  resources.getDrawable(R.drawable.btn_bar_on, null)

        binding.albumSubTitleBtn.setOnClickListener {
            val btnStatus: Drawable = binding.albumSubTitleBtn.background
            if(btnStatus.constantState ==  barOff.constantState){
                binding.albumSubTitleBtn.background=barOn
                binding.albumDetailBtn.background=barOff
                binding.albumVideoBtn.background=barOff
            }else{
                binding.albumSubTitleBtn.background= barOff
            }

        }
        binding.albumDetailBtn.setOnClickListener{
            val btnStatus: Drawable =   binding.albumDetailBtn.background
            if(btnStatus.constantState ==  barOff.constantState){
                binding.albumSubTitleBtn.background=barOff
                binding.albumDetailBtn.background=barOn
                binding.albumVideoBtn.background=barOff
            }else{
                binding.albumDetailBtn.background= barOff
            }
        }
        binding.albumVideoBtn.setOnClickListener{
            val btnStatus: Drawable = binding.albumVideoBtn.background
            if(btnStatus.constantState ==  barOff.constantState){
                binding.albumSubTitleBtn.background=barOff
                binding.albumDetailBtn.background=barOff
                binding.albumVideoBtn.background=barOn
            }else{
                binding.albumVideoBtn.background= barOff
            }
        }




        return binding.root
    }



}


