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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class AlbumFragment : Fragment() {


    lateinit var binding:FragmentAlbumBinding
    private var gson: Gson = Gson()
    var information= arrayListOf("수록곡", "상세정보", "영상")
    private var isLiked: Boolean=false

    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState:
            Bundle?): View? {

        binding= FragmentAlbumBinding.inflate(inflater, container,false)

        val songDB = SongDatabase.getInstance(mContext!!)!!
        val albumData= arguments?.getString("album")
        val albumId=arguments?.getInt("albumId")!!
        val album=gson.fromJson(albumData, Album::class.java)
        Log.d("Now album Point: ",album.toString())
        isLiked=isLikedAlbum(album.id)
        setInit(album)
        setCLickListeners(album)


        binding.albumBackIc.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
        }

        return binding.root
    }

    private fun setInit(album: Album) {

        if(album.coverImgUrl == ""){
            Glide.with(requireContext()).load(album.coverImg!!).into(binding.albumAlbumIv)
        }else{
            Glide.with(requireContext()).load(album.coverImgUrl).into(binding.albumAlbumIv)
        }
        binding.albumSongTitleTv.text = album.title
        binding.albumSongSingerTv.text = album.singer

        if(isLiked){
            binding.albumHeartBtn.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumHeartBtn.setImageResource(R.drawable.ic_my_like_off)
        }

        val albumAdapter = AlbumViewpagerAdapter(this, binding.albumSongTitleTv.text.toString(),  binding.albumSongSingerTv.text.toString())
        binding.albumContentVp.adapter= albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab,position->
            tab.text = information[position]
        }.attach()
    }

    private fun setCLickListeners(album:Album){
        val userId =getUserIdx(requireContext())
        val songDB=SongDatabase.getInstance(requireContext())!!
        binding.albumHeartBtn.setOnClickListener {
            if(isLiked){
                binding.albumHeartBtn.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId,album.id)
                Log.d("who are you",userId.toString()+" || "+album.id)
                Log.d("LIKES",songDB.albumDao().getlikes().toString())
                Log.d("isLikeAlbum",songDB.albumDao().getLikeAlbum(userId).toString())

            }else{
                binding.albumHeartBtn.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)
            }
        }
    }

    private fun isLikedAlbum(albumId: Int):Boolean{
        val songDB=SongDatabase.getInstance(requireContext())!!
        val userId= getUserIdx(requireContext())
        val likeId: Int?=songDB.albumDao().isLikeAlbum(userId,albumId)
        Log.d("isLikeAlbum",likeId.toString())
        return likeId!=null
    }


    private fun disLikedAlbum(userId: Int,albumId: Int){
        val songDB=SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId,albumId)
    }


    private fun likeAlbum(userId:Int, albumId: Int){
        val songDB=SongDatabase.getInstance(requireContext())!!
        val like=Like(userId, albumId)

        songDB.albumDao().likeAlbum((like))
    }


    private fun getJwt():Int{
        val spf= activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }

}


