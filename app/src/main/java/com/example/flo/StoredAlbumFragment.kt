package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentStoredalbumBinding

class StoredAlbumFragment:Fragment() {

    lateinit var binding:FragmentStoredalbumBinding
    lateinit var songDB: SongDatabase
    lateinit var albumRVAdapter :LikeAlbumRVAdpater

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {

        binding= FragmentStoredalbumBinding.inflate(inflater,container,false)

        songDB=SongDatabase.getInstance(requireContext()!!)!!
        val albums=songDB.albumDao().getAlbums()

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    override fun onResume(){
        super.onResume()
        albumRVAdapter.addAlbums(songDB.albumDao().getLikeAlbum(getUserIdx(requireContext())) as ArrayList)
    }

    private fun initRecyclerview(){
        binding.albumStoredFrListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var temp=songDB.albumDao().getLikeAlbum(getUserIdx(requireContext()))
        var arr=ArrayList<Album>()
        arr.addAll(temp)
        Log.d("like talbe", arr.toString())
        albumRVAdapter= LikeAlbumRVAdpater()
        //리스너 객체 생성 및 전달
        binding.albumStoredFrListRv.adapter = albumRVAdapter
        albumRVAdapter.addAlbums(arr)

        albumRVAdapter.setMyItemCLickLister(object:LikeAlbumRVAdpater.MyItemClickLister{
            override fun onRemoveAlbum(albumId: Int) {
                val userId: Int=getUserIdx(requireContext())
                songDB.albumDao().disLikeAlbum(userId,albumId)
            }
        })

    }


    private fun getJwt():Int{
        val spf= activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }
}