package com.example.flo

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.databinding.ToastCustomBinding

class SongActivity: AppCompatActivity() {

    lateinit var binding:ActivitySongBinding

    private val song = Song()
    private lateinit var player:Player
    private val handler= Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initSong()
        player=Player(song.playTime,song.isPlaying)
        player.start()


               binding.songDownIc.setOnClickListener{
                   finish()
               }
               binding.songPlayIc.setOnClickListener {
                   player.isPlaying=true
                   setPlayerStatus(true)
               }
               binding.songPauseIc.setOnClickListener {

                   player.isPlaying=false
                   setPlayerStatus(false)

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
        if(intent.hasExtra("title") && intent.hasExtra("singer")
                &&intent.hasExtra("playTime")&&intent.hasExtra("isPlaying") ){

            song.title=intent.getStringExtra("title")!!
            song.singer= intent.getStringExtra("singer")!!
            song.playTime=intent.getIntExtra("playTime",0)
            song.isPlaying=intent.getBooleanExtra("isPlaying",false)

            binding.songSongTitleTv.text= song.title
            binding.songSongSingerTv.text= song.singer
            binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
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

    inner class Player(private val playTime: Int, var isPlaying: Boolean): Thread(){
        private var second =0

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(1000)
                        second++
                        runOnUiThread {
                            binding.songSeekbar.progress = second * 1000 / playTime
                            binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)

                            binding.songSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                                    // Display the current progress of SeekBar
                                    binding.songSeekbar.progress=i
                                    second=i*song.playTime/1000
                                    binding.songStartTimeTv.text =String.format("%02d:%02d",(i*song.playTime/1000)/60,(i*song.playTime/1000)%60)
                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar) {
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar) {
                                }
                            })
                        }
                    }
                }
            } catch (e : InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

    override fun onDestroy(){
        player.interrupt()
        super.onDestroy()
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