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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()


               binding.songDownIc.setOnClickListener{
                   finish()
               }



               binding.songPlayIc.setOnClickListener {

//                   if(song.pos==null)  mediaPlayer?.seekTo(0)
//                   else mediaPlayer?.seekTo(song.pos!!)

                   player.isPlaying=true
                   setPlayerStatus(true)
                   song.isPlaying=true
                   mediaPlayer?.start()
               }
               binding.songPauseIc.setOnClickListener {
                   pos = mediaPlayer?.currentPosition
                   song.pos=pos!!

                   player.isPlaying=false
                   setPlayerStatus(false)
                   song.isPlaying=false
                   mediaPlayer?.pause()
               }

               binding.songPreviousIc.setOnClickListener {
                   song.second=0
                   song.pos=0
                   binding.songStartTimeTv.text = String.format("%02d:%02d", 0, 0)
                   binding.songSeekbar.progress=0
                   val music= resources.getIdentifier(song.music,"raw",this.packageName)
                   if(mediaPlayer!=null){
                       Log.d("interrupt", "다시 재생을 시작합니다.")

                        mediaPlayer?.release()
                        mediaPlayer= MediaPlayer.create(this,music)
                       if(song.isPlaying)mediaPlayer?.start()
                   }
               }

              binding.songHeartIc.setOnClickListener {

                  binding.songHeartIc.setImageDrawable(
                          switchBtn(resources.getDrawable(R.drawable.ic_my_like_off, null),
                                  resources.getDrawable(R.drawable.ic_my_like_on, null),
                                  binding.songHeartIc.drawable))
                }

                binding.songUnlikeIc.setOnClickListener {

                    binding.songUnlikeIc.setImageDrawable(
                            switchBtn(resources.getDrawable(R.drawable.btn_player_unlike_off, null),
                                    resources.getDrawable(R.drawable.btn_player_unlike_on, null),
                                    binding.songUnlikeIc.drawable))

                }
                binding.songShuffleIc.setOnClickListener {

                    val ON : Drawable =  resources.getDrawable(R.drawable.btn_playlist_random_on, null)
                    val OFF : Drawable =  resources.getDrawable(R.drawable.nugu_btn_random_inactive, null)

                    if(binding.songShuffleIc.drawable.constantState == ON.constantState){
                        binding.songShuffleIc.setImageDrawable(OFF);
                        CustomToast.createToast(this, "재생목록을 순서대로 재생합니다")?.show()
                    }else{
                        binding.songShuffleIc.setImageDrawable(ON);
                        CustomToast.createToast(this, "재생목록을 랜덤으로 재생합니다")?.show()
                    }
                }

                binding.songRepeatIc.setOnClickListener {


                    val all : Drawable =  resources.getDrawable(R.drawable.btn_playlist_repeat_on, null)
                    val none : Drawable =  resources.getDrawable(R.drawable.nugu_btn_repeat_inactive, null)
                    val one :Drawable = resources.getDrawable(R.drawable.btn_playlist_repeat_on1, null)

                    if(binding.songRepeatIc.drawable.constantState == none.constantState){
                        binding.songRepeatIc.setImageDrawable(all);
                        CustomToast.createToast(this, "전체 음악을 반복합니다")?.show()
                    }else if(binding.songRepeatIc.drawable.constantState == all.constantState){
                        binding.songRepeatIc.setImageDrawable(one);
                        CustomToast.createToast(this, "현재 음악을 반복합니다")?.show()
                    }else{
                        binding.songRepeatIc.setImageDrawable(none);
                        CustomToast.createToast(this, "반복을 사용하지 않습니다")?.show()
                    }

                }





    }






    private fun initSong(){
        if(intent.hasExtra("mtitle") && intent.hasExtra("msinger")&& intent.hasExtra("msecond")
                &&intent.hasExtra("mplayTime")&&intent.hasExtra("mpos")&&intent.hasExtra("misPlaying")
                && intent.hasExtra("mmusic")){

            song.title=intent.getStringExtra("mtitle")!!
            song.singer= intent.getStringExtra("msinger")!!
            song.second=intent.getIntExtra("msecond",0)
            song.playTime=intent.getIntExtra("mplayTime",0)
            song.pos=intent.getIntExtra("mos",0)
            song.isPlaying=intent.getBooleanExtra("misPlaying",false)
            song.music=intent.getStringExtra("mmusic")!!
            Log.d("interrupt", "Song class " +intent.getStringExtra("mtitle")!!+intent.getStringExtra("msinger")!!)
            binding.songSongTitleTv.text= song.title
            binding.songSongSingerTv.text= song.singer
            binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
            //song.isPlaying=true // 바로 재
            setPlayerStatus(song.isPlaying)

        }


    }



    fun setPlayerStatus(isPlaying: Boolean){
        if(isPlaying){
            binding.songPlayIc.visibility=View.GONE
            binding.songPauseIc.visibility = View.VISIBLE

        }else{
            binding.songPauseIc.visibility = View.GONE
            binding.songPlayIc.visibility= View.VISIBLE

        }
    }


    fun switchBtn(off: Drawable, on: Drawable, status: Drawable) : Drawable {
        if(off.constantState==(status.constantState)){
            return on
        }
        return off
    }

    fun updateSeekbar(){
        binding.songSeekbar.setProgress(mediaPlayer?.currentPosition!!)
        handler.postDelayed(runnable,1000)
    }

    inner class Player(private val playTime: Int, var isPlaying: Boolean): Thread(){
        private var second =song.second
        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {
                        song.pos=0;
                        song.second=0;
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
                                    song.pos=i
                                   // second=i*song.playTime/1000
                                   // binding.songStartTimeTv.text =String.format("%02d:%02d",(i*song.playTime/1000)/60,(i*song.playTime/1000)%60)
                                    second=i*song.playTime/duration
                                    binding.songStartTimeTv.text =String.format("%02d:%02d",(i*song.playTime/duration)/60,(i*song.playTime/duration)%60)

                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar) {
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar) {
                                    mediaPlayer?.seekTo(song.pos)
                                   // binding.songStartTimeTv.text =String.format("%02d:%02d",(song.pos*song.playTime/duration)/60,(song.pos*song.playTime/duration)%60)
                                }
                            })
                        }
                    }
                }
            } catch (e : InterruptedException) {
                song.second=second
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }




    override fun onStart() {
        super.onStart()
        Log.d("tag", "##########start###############")
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsongSong = sharedPreferences.getString("song",null)
        val music= resources.getIdentifier(song.music,"raw",this.packageName)


        if(jsongSong== null){

        }else{
            song =gson.fromJson(jsongSong, Song::class.java)
        }

        mediaPlayer= MediaPlayer.create(this,music)
        duration=mediaPlayer?.duration!!
        binding.songSeekbar.max=duration
        Log.d("POS IN SONG IN START ", song.pos.toString() + " || " +
                song.second.toString()+" || " + duration.toString())


        mediaPlayer?.seekTo(song.pos)
        setPlayerStatus(song.isPlaying)

        //binding.songSeekbar.progress=song.second* 1000 / song.playTime
        //binding.songSeekbar.progress=song.second* duration / song.playTime
        binding.songSeekbar.progress=song.pos

        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60,song. second % 60)

        player=Player(song.playTime,song.isPlaying)
        player.start()
        if(song.isPlaying ) mediaPlayer?.start()

        if(song.second==0){ // 처음 시작이라면
            song.isPlaying=true
            setPlayerStatus(song.isPlaying)
            player.isPlaying=true
            mediaPlayer?.start()
        }

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("tag", "##########restart###############")

    }


    override fun onPause() {
        super.onPause()
        Log.d("tag", "########pause###########")
        song.pos=mediaPlayer?.currentPosition!!
        Log.d("POS", song.pos.toString())
        if(mediaPlayer!=null) mediaPlayer?.stop()

        song.second=(binding.songSeekbar.progress * song.playTime)/duration
        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        val editor= sharedPreferences.edit() // 조작할 때 사용을 합니다
        //Gson
        val json = gson.toJson(song)
        editor.putString("song",json)
        editor.apply()
        player.interrupt()
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null


    }

    override fun onDestroy(){
        Log.d("tag", "########DESTROY###########")
        player.interrupt()
        super.onDestroy()
        mediaPlayer?.release() // 미디어 플레어가 갖고 있더 리소스 해제
        mediaPlayer=null
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


