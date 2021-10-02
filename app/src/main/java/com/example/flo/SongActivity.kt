package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity() {

    lateinit var binding:ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.songDownIc.setOnClickListener{
            finish()
        }
        binding.songPlayIc.setOnClickListener {
           setPlayerStatus(false)
        }
        binding.songPauseIc.setOnClickListener {
            setPlayerStatus(true)
        }



    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songPauseIc.visibility = View.GONE
            binding.songPlayIc.visibility= View.VISIBLE

        }else{
            binding.songPlayIc.visibility=View.GONE
            binding.songPauseIc.visibility = View.VISIBLE
        }
    }
}