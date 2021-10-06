package com.example.flo

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
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


        setFragmentResultListener("requestKey") { requestKey, bundle ->
            if(! bundle.isEmpty){
                binding.albumSongTitleTv.text=bundle.getString("title")
                binding.albumSongSingerTv.text=bundle.getString("singer")
                binding.albumAlbumIv.setImageBitmap(bundle.getParcelable<Bitmap>("image"))
                Log.d("bundle Okay",bundle.getString("title").toString() )
            }

        }




        binding.albumBackIc.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
        }

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

        binding.albumHeartBtn.setOnClickListener {
            val OffStatus :Drawable = resources.getDrawable(R.drawable.ic_my_like_off, null)

            if(OffStatus.constantState==(binding.albumHeartBtn.drawable.constantState)){
                binding.albumHeartBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_my_like_on, null))
            }
            else{
                binding.albumHeartBtn.setImageDrawable(OffStatus)
            }
        }

        val barOff : Drawable =  resources.getDrawable(R.drawable.btn_bar_slim, null)
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

        binding.albumWholeSelectBtn.setOnClickListener {
            val ALL ="전체선택"
            val NONE="선택해제"
            if(binding.albumWholeSelectBtn.text.equals(NONE)){
                binding.albumWholeSelectBtn.setTextColor(resources.getColor(R.color.black,null))
                binding.albumWholeSelectCheckBtn.clearColorFilter()
                binding.albumWholeSelectBtn.text=ALL
                binding.albumListLayout.background=null
            }else{
                binding.albumWholeSelectBtn.setTextColor(resources.getColor(R.color.blue,null))
                binding.albumWholeSelectCheckBtn.setColorFilter(R.color.blue)
                binding.albumWholeSelectBtn.text=NONE
                binding.albumListLayout.setBackgroundColor(resources.getColor(R.color.silver,null))
            }
        }



        return binding.root
    }



}


