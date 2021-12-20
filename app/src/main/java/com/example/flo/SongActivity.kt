package com.example.flo

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.*
import android.os.health.TimerStat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.databinding.ToastCustomBinding
import com.google.gson.Gson
import java.sql.Time
import java.util.*

class SongActivity: AppCompatActivity() {

    lateinit var binding:ActivitySongBinding
    private var song = Song()
    private lateinit var player:Player
    private var mediaPlayer:MediaPlayer?=null
    var pos : Int ?=0
    private  var duration:Int =1
    private var gson: Gson= Gson()
    private val handler= Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()



    }

    private fun initClickListener() {
        binding.songDownIc.setOnClickListener {
            finish()
        }


        binding.songPlayIc.setOnClickListener {

            player.isPlaying = true
            setPlayerStatus(true)
            songs[nowPos].isPlaying = true
            mediaPlayer?.start()
        }
        binding.songPauseIc.setOnClickListener {
            pos = mediaPlayer?.currentPosition
            songs[nowPos].pos = pos!!

            player.isPlaying = false
            setPlayerStatus(false)
            songs[nowPos].isPlaying = false
            mediaPlayer?.pause()
        }

        binding.songPreviousIc.setOnClickListener {

            val music = resources.getIdentifier(songs[nowPos].music, "raw", this.packageName)
            if (mediaPlayer != null) {
                Log.d("interrupt", "다시 재생을 시작합니다.")
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(this, music)
                if (songs[nowPos].isPlaying) mediaPlayer?.start()
            }
            moveSong(-1)
        }

        binding.songNextIc.setOnClickListener {
            moveSong(+1)
        }

        binding.songHeartIc.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

        binding.songUnlikeIc.setOnClickListener {

            setLike(songs[nowPos].isLike)

        }
        binding.songShuffleIc.setOnClickListener {

            val ON: Drawable = resources.getDrawable(R.drawable.btn_playlist_random_on, null)
            val OFF: Drawable = resources.getDrawable(R.drawable.nugu_btn_random_inactive, null)

            if (binding.songShuffleIc.drawable.constantState == ON.constantState) {
                binding.songShuffleIc.setImageDrawable(OFF);
                CustomToast.createToast(this, "재생목록을 순서대로 재생합니다")?.show()
            } else {
                binding.songShuffleIc.setImageDrawable(ON);
                CustomToast.createToast(this, "재생목록을 랜덤으로 재생합니다")?.show()
            }
        }

        binding.songRepeatIc.setOnClickListener {


            val all: Drawable = resources.getDrawable(R.drawable.btn_playlist_repeat_on, null)
            val none: Drawable = resources.getDrawable(R.drawable.nugu_btn_repeat_inactive, null)
            val one: Drawable = resources.getDrawable(R.drawable.btn_playlist_repeat_on1, null)

            if (binding.songRepeatIc.drawable.constantState == none.constantState) {
                binding.songRepeatIc.setImageDrawable(all);
                CustomToast.createToast(this, "전체 음악을 반복합니다")?.show()
            } else if (binding.songRepeatIc.drawable.constantState == all.constantState) {
                binding.songRepeatIc.setImageDrawable(one);
                CustomToast.createToast(this, "현재 음악을 반복합니다")?.show()
            } else {
                binding.songRepeatIc.setImageDrawable(none);
                CustomToast.createToast(this, "반복을 사용하지 않습니다")?.show()
            }

        }
    }


    private fun initSong(){
        val spf=getSharedPreferences("song", MODE_PRIVATE)
        val songId=spf.getInt("songId",0)
        nowPos=getPlayingSongPosition(songId)
        Log.d("[Song activity]now",songId.toString()+songs[nowPos].title)
        //startTimer()
        setPlayer(songs[nowPos])
        Log.d("[Song activity IsLike]",songDB.songDao().getSong(songs[nowPos].id).isLike.toString())

    }
    private fun initPlayList(){

        songs.clear()
        songDB= SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
        Log.d("[Song InitPlayList]",songs.size.toString())

    }

    private fun setPlayer(song:Song){

        binding.songSongTitleTv.text= songs[nowPos].title
        binding.songSongSingerTv.text= songs[nowPos].singer
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)

        setPlayerStatus(songs[nowPos].isPlaying)

        if (songs[nowPos].isLike) {
            binding.songHeartIc.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songHeartIc.setImageResource(R.drawable.ic_my_like_off)
        }
    }



    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }


    private fun moveSong(direct: Int){

        if (nowPos + direct < 0){
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct
        player.interrupt()


        val music= resources.getIdentifier( songs[nowPos].music,"raw",this.packageName)
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        mediaPlayer= MediaPlayer.create(this,music)
        duration=mediaPlayer?.duration!!
        binding.songSeekbar.max=duration


        setPlayer(songs[nowPos])
        songs[nowPos].isPlaying=true
        setPlayerStatus( songs[nowPos].isPlaying)
        mediaPlayer?.start()

        startTimer()

    }

    fun setPlayerStatus(isPlaying: Boolean){

        binding.songSeekbar.progress= songs[nowPos].pos
        binding.songStartTimeTv.text = String.format("%02d:%02d",  songs[nowPos].second / 60, songs[nowPos]. second % 60)

        songs[nowPos].isPlaying = isPlaying//check

        if(isPlaying){
            binding.songPlayIc.visibility=View.GONE
            binding.songPauseIc.visibility = View.VISIBLE

        }else{
            binding.songPauseIc.visibility = View.GONE
            binding.songPlayIc.visibility= View.VISIBLE

        }
    }



    inner class Player(private val playTime: Int, var isPlaying: Boolean): Thread(){
        private var second = songs[nowPos].second
        override fun run() {
            try {
                while (!this.isInterrupted && mediaPlayer!=null) {
                    if (second >= playTime) {
                        songs[nowPos].pos=0;
                        songs[nowPos].second=0;
                        break
                    }
                    if (isPlaying) {
                        sleep(1000)
                        second++
                        runOnUiThread {
                            //binding.songSeekbar.progress = second * duration / playTime
                            binding.songSeekbar.progress =  mediaPlayer?.currentPosition!!
                            binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)

                            binding.songSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                                    // Display the current progress of SeekBar

                                    binding.songSeekbar.progress=mediaPlayer?.currentPosition!!
                                    songs[nowPos].pos=i
                                    second=i* songs[nowPos].playTime/duration
                                    binding.songStartTimeTv.text =String.format("%02d:%02d",(i* songs[nowPos].playTime/duration)/60,(i* songs[nowPos].playTime/duration)%60)

                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar) {
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar) {
                                    mediaPlayer?.seekTo( songs[nowPos].pos)
                                   // binding.songStartTimeTv.text =String.format("%02d:%02d",(song.pos*song.playTime/duration)/60,(song.pos*song.playTime/duration)%60)
                                }
                            })
                        }
                    }
                }
            } catch (e : InterruptedException) {
                songs[nowPos].second=second
                Log.d("PLAYER interrupt", "PLAYER 쓰레드가 종료되었습니다.")
            }
        }
    }





    override fun onResume() {
        super.onResume()
        initPlayList()
        initSong()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val music= resources.getIdentifier( songs[nowPos].music,"raw",this.packageName)
        Log.d("Star_ttag", "##########start###############"+song.music)


        mediaPlayer= MediaPlayer.create(this,music)
        duration=mediaPlayer?.duration!!
        binding.songSeekbar.max=duration


        mediaPlayer?.seekTo( songs[nowPos].pos)
        setPlayerStatus( songs[nowPos].isPlaying)

        startTimer()

        if( songs[nowPos].isPlaying ) mediaPlayer?.start()

        if( songs[nowPos].second==0){ // 처음 시작이라면
            songs[nowPos].isPlaying=true
            setPlayerStatus( songs[nowPos].isPlaying)
            player.isPlaying=true
            mediaPlayer?.start()
        }

    }

    override fun onRestart() {
        super.onRestart()
    }



    override fun onPause() {
        super.onPause()
        player.interrupt()
        songs[nowPos].pos=mediaPlayer?.currentPosition!!
        Log.d("POS",  songs[nowPos].pos.toString())
        if(mediaPlayer!=null) mediaPlayer?.stop()

        songs[nowPos].second=(binding.songSeekbar.progress *  songs[nowPos].playTime)/duration
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        val editor= sharedPreferences.edit() // 조작할 때 사용을 합니다

        editor.putInt("songId",songs[nowPos].id)
        editor.apply()

        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null
        songDB.songDao().update(songs[nowPos])


    }

    override fun onDestroy(){

        player.interrupt()
        Log.d("SONG destroy",  songs[nowPos].pos.toString())
        super.onDestroy()
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null
    }



    private fun startTimer(){
        player=Player(songs[nowPos].playTime,songs[nowPos].isPlaying)
        player.start()
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        Log.d("Like change","${songs[nowPos].id} is now .${!isLike}")
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (isLike){
            binding.songHeartIc.setImageResource(R.drawable.ic_my_like_off)

        }else{
            binding.songHeartIc.setImageResource(R.drawable.ic_my_like_on)
        }
        //songDB.songDao().update(songs[nowPos])
    }


    object CustomToast {

        fun createToast(context: Context, message: String): Toast? {
            val inflater = LayoutInflater.from(context)
            val binding: ToastCustomBinding = DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

            binding.tvSample.text = message
            return Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 130.toPx())
                duration = Toast.LENGTH_SHORT
                view = binding.root
            }
        }

        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }

}


