package com.example.flo

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(){


        lateinit var binding: ActivityMainBinding
        private var gson : Gson= Gson()
        private var song :Song= Song()
        private var mediaPlayer:MediaPlayer?=null
        private var duration:Int =1
        private lateinit var player: MainActivity.Player
        private lateinit var  timer: MainActivity.Timer
        override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            timer=Timer(false)
            timer.start()


        if(intent.hasExtra("titleNow") && intent.hasExtra("SingerNow")){
            val nowTitle = intent.getIntExtra("titleNow", 0)
            val nowSinger= intent.getIntExtra("singerNow", 0)
            binding.mainMiniplayerTitleTv.setText(nowTitle)
            binding.mainMiniplayerSingerTv.setText(nowSinger)
            Log.d("intent!!", "happen")
        }


        binding.mainMiniplayerBtn.setOnClickListener{
            song.isPlaying=true
            player.isPlaying=true
            setMiniPlayer(song)
            mediaPlayer?.start()


        }
        binding.mainPauseBtn.setOnClickListener {
            if(song.isPlaying) mediaPlayer?.pause()
            song.isPlaying=false
            player.isPlaying=false
            setMiniPlayer(song)
            song.pos=mediaPlayer?.currentPosition!!
            Log.d("POS", song.pos.toString())

        }

        binding.mainPlayerLayout.setOnClickListener{


            Log.d("mytag", song.singer + " " + song.title)
            val intent =Intent(this, SongActivity::class.java)
            intent.putExtra("mtitle", song.title)
            intent.putExtra("msinger", song.singer)
            intent.putExtra("msecond", song.second)
            intent.putExtra("mplayTime", song.playTime)
            intent.putExtra("mpos",song.pos)
            intent.putExtra("misPlaying", song.isPlaying)
            intent.putExtra("mmusic", song.music)

            startActivity(intent)


        }
        binding.mainMiniplayerPreBtn.setOnClickListener {
            song.second=0
            song.pos=0
            binding.mainSeekbar.progress=0
            val music= resources.getIdentifier(song.music,"raw",this.packageName)
            if(mediaPlayer!=null){
                Log.d("interrupt", "다시 재생을 시작합니다.")
                mediaPlayer?.release()
                mediaPlayer= MediaPlayer.create(this,music)
                if(song.isPlaying)mediaPlayer?.start()
            }
        }

        initNavigation()


    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()


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
            window.setDecorFitsSystemWindows(false) //30이상 적용안됨 ..
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

    private fun setMiniPlayer(song: Song){

        Log.d("interrupt", "SetMiniPlayer 의 결과."+ song.second.toString())
        binding.mainMiniplayerTitleTv.text=song.title
        binding.mainMiniplayerSingerTv.text=song.singer
        //binding.mainSeekbar.progress=  song.second * 1000 / song.playTime
        binding.mainSeekbar.progress=  song.second * duration / song.playTime


        if(song.isPlaying){
            binding.mainPauseBtn.visibility=View.VISIBLE
            binding.mainMiniplayerBtn.visibility= View.GONE

        }else{
            binding.mainPauseBtn.visibility=View.GONE
            binding.mainMiniplayerBtn.visibility= View.VISIBLE
        }
    }



    inner class Timer(var isPlaying: Boolean): Thread(){
        override fun run() {
            try {
                if(isPlaying) runOnUiThread {
                    mediaPlayer?.start()
                }else{
                    mediaPlayer?.pause()
                }

            } catch (e : InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
                song.pos = mediaPlayer?.currentPosition!! // 내가 추가함
            }
        }
    }

    inner class Player(private val playTime: Int, var isPlaying: Boolean):Thread(){
        private var second  =song.second
        override fun run() {
          try{
              while(true) {
                  if (second >= playTime) {
                      song.isPlaying = false
                      song.second = 0
                      song.pos = 0
                      break;
                  }
                  if (isPlaying) {
                      sleep(1000)
                      second++
                      song.second=second
                      runOnUiThread {
                          binding.mainSeekbar.progress = second * duration / playTime
                          binding.mainSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                              override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                                  // Display the current progress of SeekBar
                                  binding.mainSeekbar.progress = i
                                  song.pos=i
                                 // second = i * song.playTime / 1000
                                  second = i * song.playTime / duration
                              }

                              override fun onStartTrackingTouch(seekBar: SeekBar) {
                              }

                              override fun onStopTrackingTouch(seekBar: SeekBar) {
                                  mediaPlayer?.seekTo(song.pos)
                                  song.second = song.pos * song.playTime / duration
                              }
                          })

                      }

                  }
              }


          }catch(e : InterruptedException){
              song.second=second
              Log.d("interrupt", "PALYER 쓰레드가 종료되었습니다.")

          }
        }
    }



    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsongSong = sharedPreferences.getString("song",null)
        if(jsongSong== null){
            song= Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(),0,210,0,false,"music_lilac")
        }else{
            song = gson.fromJson(jsongSong, Song::class.java)
        }

        Log.d("tag", "@@@@@@@@@@@start@@@@@@@@@@@" +song.isPlaying.toString())
        player=Player(song.playTime,song.isPlaying)
        player.start()


        val music= resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer= MediaPlayer.create(this,music)


        duration=mediaPlayer?.duration!! // duration
        binding.mainSeekbar.max=duration

        setMiniPlayer(song)
        mediaPlayer?.seekTo(song.pos)//
        if(song.isPlaying) mediaPlayer?.start() // 바로 시작하기

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("tag", "@@@@@@@@@@@restart@@@@@@@@@@@")
    }

    override fun onPause() {
        super.onPause()
        Log.d("tag", "@@@@@@@@@@@pause@@@@@@@@@@@")
        player.interrupt()

        if(song.isPlaying) song.pos=mediaPlayer?.currentPosition!! // added
        mediaPlayer?.stop()
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        val editor= sharedPreferences.edit() // 조작할 때 사용을 합니다
        //Gson
        val json = gson.toJson(song)
        editor.putString("song",json)
        editor.apply()
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("tag", "@@@@@@@@@@@Destroy@@@@@@@@@@@")
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null
    }


}



