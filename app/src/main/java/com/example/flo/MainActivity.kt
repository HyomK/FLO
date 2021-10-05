package com.example.flo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.example.flo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if(intent.hasExtra("titleNow") && intent.hasExtra("SingerNow")){
            val nowTitle = intent.getIntExtra("titleNow", 0)
            val nowSinger= intent.getIntExtra("singerNow", 0)
            binding.mainMiniplayerTitleTv.setText(nowTitle)
            binding.mainMiniplayerSingerTv.setText(nowSinger)
            Log.d("intent!!","happen")
        }


        val song = Song(binding.mainMiniplayerTitleTv.text.toString() , binding.mainMiniplayerSingerTv.text.toString() )

        binding.mainPlayerLayout.setOnClickListener{
            // startActivity(Intent(this, SongActivity::class.java))
            Log.d("mytag",song.singer + " " + song.title)
            val intent =Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            startActivity(intent)


        }


        initNavigation()


        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activit
            // var w: Window = window
            ////            w.setDecorFitsSystemWindows(false)y
//
            window.setDecorFitsSystemWindows( false) //30이상 적용안됨 ..
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, HomeFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, LookFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, SearchFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, LockerFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()

    }

    fun receiveData(song :Song){
        binding.mainMiniplayerTitleTv.setText(song.title)
        binding.mainMiniplayerSingerTv.setText(song.singer)
        Log.d("Mytag",song.title.toString() + " " + song.singer.toString() )
    }

}






/*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if(intent.hasExtra("titleNow") && intent.hasExtra("SingerNow")){
            val nowTitle = intent.getIntExtra("titleNow", 0)
            val nowSinger= intent.getIntExtra("singerNow", 0)
            binding.mainMiniplayerTitleTv.setText(nowTitle)
            binding.mainMiniplayerSingerTv.setText(nowSinger)
            Log.d("intent!!","happen")
        }


        val song = Song(binding.mainMiniplayerTitleTv.text.toString() , binding.mainMiniplayerSingerTv.text.toString() )

        binding.mainPlayerLayout.setOnClickListener{
           // startActivity(Intent(this, SongActivity::class.java))
            Log.d("mytag",song.singer + " " + song.title)
            val intent =Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title)
            intent.putExtra("singer",song.singer)
            startActivity(intent)


        }


        initNavigation()


        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activit
            // var w: Window = window
            ////            w.setDecorFitsSystemWindows(false)y
//
            window.setDecorFitsSystemWindows( false) //30이상 적용안됨 ..
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, HomeFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, LookFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, SearchFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, LockerFragment())
                            .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    fun receiveData(song :Song){
        binding.mainMiniplayerTitleTv.setText(song.title)
        binding.mainMiniplayerSingerTv.setText(song.singer)
        Log.d("Mytag",song.title.toString() + " " + song.singer.toString() )
    }

}

*/