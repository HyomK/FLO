package com.example.flo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLookBinding


class LookFragment: Fragment() ,LookView{

    private lateinit var binding: FragmentLookBinding
    private lateinit var songRVAdapter: ChartSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        initRecyclerView()
        getSongs()

        return binding.root
    }

    private fun initRecyclerView(){
        songRVAdapter=ChartSongRVAdapter(requireContext())
        binding.lookFloChartRv.adapter=songRVAdapter
    }
    private fun getSongs(){
        val songService=SongService()
        songService.setLookView(this)

        songService.getSongs()
    }


    override fun onGetSongsLoading() {
        binding.lookLoadingPb.visibility=View.VISIBLE
    }

    override fun onGetSongSuccess(songs: ArrayList<Song>) {
        binding.lookLoadingPb.visibility=View.GONE
        songRVAdapter.addSongs(songs)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding.lookLoadingPb.visibility=View.GONE
        when(code){
            400-> Log.d("LOOKFRAG/API"," - ERROR")
        }
    }

}