package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentMusicfileBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentMusicfileBinding
    lateinit var songDB: SongDatabase
    lateinit var songRVAdapter :SongRVAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding =FragmentMusicfileBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    override fun onResume(){
        super.onResume()
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)
    }

    private fun initRecyclerview(){
        binding.musicFrListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        songRVAdapter = SongRVAdapter(requireContext()!!)
        //리스너 객체 생성 및 전달

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                Log.d("Unlike", "좋아요 취소"  + songId)
                songDB.songDao().updateIsLikeById(false,songId)
                Log.d("Unlike", "좋아요 취소"  +  songDB.songDao().getSong(songId).isLike)
            }
        })

        binding.musicFrListRv.adapter = songRVAdapter
        var temp=songDB.songDao().getLikedSongs(true)
        var arr=ArrayList<Song>()
        arr.addAll(temp)
        songRVAdapter.addSongs(arr)

    }
}