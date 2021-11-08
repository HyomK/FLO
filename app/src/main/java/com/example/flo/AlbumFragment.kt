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
import android.widget.TableLayout
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
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class AlbumFragment : Fragment() {


    lateinit var binding:FragmentAlbumBinding
    private var gson: Gson = Gson()
    var information= arrayListOf("수록곡", "상세정보", "영상")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState:
            Bundle?): View? {

        binding= FragmentAlbumBinding.inflate(inflater, container,false)

        val albumData= arguments?.getString("album")
        val album=gson.fromJson(albumData, Album::class.java)
        setInit(album)


//        setFragmentResultListener("requestKey") { requestKey, bundle ->
//            if(! bundle.isEmpty){
//                binding.albumSongTitleTv.text=bundle.getString("title")
//                binding.albumSongSingerTv.text=bundle.getString("singer")
//                binding.albumAlbumIv.setImageBitmap(bundle.getParcelable<Bitmap>("image"))
//
//
//
//                Log.d("bundle Okay",bundle.getString("title").toString() )
//            }
//
//        }






        binding.albumBackIc.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
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



        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumSongTitleTv.text = album.title
        binding.albumSongSingerTv.text = album.singer


        val albumAdapter = AlbumViewpagerAdapter(this, binding.albumSongTitleTv.text.toString(),  binding.albumSongSingerTv.text.toString())
        binding.albumContentVp.adapter= albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab,position->
            tab.text = information[position]
        }.attach()
    }

}


