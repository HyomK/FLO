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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.util.ArrayList

class MainActivity : AppCompatActivity(){


        lateinit var binding: ActivityMainBinding

        private var nowPos=0
        private var mediaPlayer:MediaPlayer?=null
        private var duration:Int =1
        private lateinit var player: MainActivity.Player
        private lateinit var  timer: MainActivity.Timer
        private var songs =ArrayList<Song>()
        private lateinit var song:Song
        private lateinit var songDB:SongDatabase


        override fun onCreate(savedInstanceState: Bundle?) {
            Log.d("ONCREATE", "ONCREATE FLO")

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            timer=Timer(false)
            timer.start()

         initNavigation()
         inputDummyAlbums()
        inputDummySongs()


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



            val editor=getSharedPreferences("song",MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()
            val intent=Intent(this@MainActivity,SongActivity::class.java)
            startActivity(intent)


        }
        binding.mainMiniplayerPreBtn.setOnClickListener {
            player.interrupt()
            songInit(song)
            Log.d("interrupt", "이전 곡으로 돌아갑니다 ." + -1+song.id)
            nowPos = song.id
            songDB.songDao().update(song)
            nowPos--
            moveSong(-1)

        }
        binding.mainMiniplayerPnxtbtn.setOnClickListener {
            player.interrupt()
            songInit(song)
            Log.d("interrupt", "다음 곡으로 이동합니다 ." + song.id+1)
            nowPos = song.id
            songDB.songDao().update(song)
            nowPos--
            moveSong(+1)
        }

    }
    private fun songInit(song: Song){
        song.second = 0
        song.pos = 0
        binding.mainSeekbar.progress = 0
    }


    private fun moveSong(direct:Int){
        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }


        song=songs[nowPos+direct]

        val music= resources.getIdentifier(song.music,"raw",this.packageName)
        if(mediaPlayer!=null){
            Log.d("interrupt", "이전 곡:."+song.id)
            mediaPlayer?.release()
            mediaPlayer=null

            mediaPlayer= MediaPlayer.create(this,music)
            duration=mediaPlayer?.duration!!
            binding.mainSeekbar.max=duration
            song.isPlaying=true
            setMiniPlayer(song)
            mediaPlayer?.start()
            startTimer()

        }
    }

    private fun startTimer(){
        player=Player(songs[nowPos].playTime,songs[nowPos].isPlaying)
        player.start()
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

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        val songDB = SongDatabase.getInstance(this)!!


        if(songId == 0){
            song= songDB.songDao().getSong(1)
        }else{
            song =  songDB.songDao().getSong(songId)
        }

        Log.d("tag", "@@@@@@@@@@@start@@@@@@@@@@@" +song.isPlaying.toString())
        player=Player(song.playTime,song.isPlaying)
        player.start()


        val music= resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer= MediaPlayer.create(this,music)


        duration=mediaPlayer?.duration!! // duration
        binding.mainSeekbar.max=duration
        Log.d("song ID", song.id.toString())
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


        editor.putInt("songId",song.id)
        editor.apply()
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null
        songDB.songDao().update(song)



    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("tag", "@@@@@@@@@@@Destroy@@@@@@@@@@@")
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null

    }




private fun inputDummyAlbums() {
    songDB = SongDatabase.getInstance(this)!!
    val albums = songDB.albumDao().getAlbums()

    if (albums.isNotEmpty()){
        Log.d("SONG DB INIT", "NOT EMPTY")
        return
    }

    songDB.albumDao().insert(
        Album(
            1,
            "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2 ,""
        )
    )

    songDB.albumDao().insert(
        Album(
            2,
            "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp,""
        )
    )

    songDB.albumDao().insert(
        Album(
            3,
            "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3,""
        )
    )

    songDB.albumDao().insert(
        Album(
            4,
            "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4,""
        )
    )

    songDB.albumDao().insert(
        Album(
            5,
            "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5,""
        )
    )

}

//ROOM_DB
private fun inputDummySongs() {
    val songDB = SongDatabase.getInstance(this)!!
    songs.addAll(songDB.songDao().getSongs())

    if (songs.isNotEmpty()) return

    songDB.songDao().insert(
            Song(
                    "Strawberry moon",
                    "아이유 (IU)",
                    0,
                    200,
                    0,
                    false,
                    "music_strawberrymoon",
                    R.drawable.img_album_exp2,
                    "",
                    false,
                    1
            )
    )

    songDB.songDao().insert(
        Song(
            "Lilac",
            "아이유 (IU)",
            0,
            200,
            0,
            false,
            "music_lilac",
              R.drawable.img_album_exp2,
                "",
            false,
            1
        )
    )


    songDB.songDao().insert(
        Song(
            "Butter",
            "방탄소년단 (BTS)",
            0,
            190,
            0,
            false,
            "butter",
            R.drawable.img_album_exp,
                "",
            false,
            2
        )
    )

    songDB.songDao().insert(
        Song(
            "Butter (Hotter Remix)",
            "방탄소년단 (BTS)",
            0,
            190,
            0,
            false,
            "butter",
            R.drawable.img_album_exp,
                "",
            false,
            2
        )
    )

    songDB.songDao().insert(
        Song(
            "Butter (Sweeter Remix)",
            "방탄소년단 (BTS)",
            0,
            190,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp,
                "",
            false,
            2
        )
    )

    songDB.songDao().insert(
        Song(
            "Next Level",
            "에스파 (AESPA)",
            0,
            210,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp3,
                "",
            false,
            3
        )
    )

    songDB.songDao().insert(
        Song(
            "Next Level (IMLAY Remix)",
            "에스파 (AESPA)",
            0,
            210,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp3,
                "",
            false,
            3
        )
    )

    songDB.songDao().insert(
        Song(
            "Boy with Luv",
            "방탄소년단 (BTS)",
            0,
            230,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp4,
                "",
            false,
            4
        )
    )

    songDB.songDao().insert(
        Song(
            "소우주 (Mikrokosmos)",
            "방탄소년단 (BTS)",
            0,
            230,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp4,
                "",
            false,
            4
        )
    )

    songDB.songDao().insert(
        Song(
            "Make It Right",
            "방탄소년단 (BTS)",
            0,
            230,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp4,
                "",
            false,
            4
        )
    )

    songDB.songDao().insert(
        Song(
            "BBoom BBoom",
            "모모랜드 (MOMOLAND)",
            0,
            240,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp5,
                "",
            false,
            5
        )
    )

    songDB.songDao().insert(
        Song(
            "궁금해",
            "모모랜드 (MOMOLAND)",
            0,
            240,
            0,
            false,
            "music_lilac",
            R.drawable.img_album_exp5,
                "",
            false,
            5
        )
    )


    val _songs = songDB.songDao().getSongs()
    Log.d("DB DATA", _songs.toString())

    }
}


