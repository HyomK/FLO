package com.example.flo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.flo.databinding.ActivitySongBinding
import com.example.flo.databinding.ToastCustomBinding
 class SongActivity: AppCompatActivity() {

    lateinit var binding:ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("title") && intent.hasExtra("singer")){

            binding.songSongTitleTv.text= intent.getStringExtra("title")
            binding.songSongSingerTv.text= intent.getStringExtra("singer")


        }else{
            Toast.makeText(this,"false", Toast.LENGTH_SHORT).show();
        }

               binding.songDownIc.setOnClickListener{
                   finish()
               }
               binding.songPlayIc.setOnClickListener {
                  setPlayerStatus(false)
               }
               binding.songPauseIc.setOnClickListener {
                   setPlayerStatus(true)
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
                        SampleToast.createToast(this,"재생목록을 순서대로 재생합니다")?.show()
                    }else{
                        binding.songShuffleIc.setImageDrawable(ON);
                        SampleToast.createToast(this,"재생목록을 랜덤으로 재생합니다")?.show()
                    }
                }

                binding.songRepeatIc.setOnClickListener {


                    val all : Drawable =  resources.getDrawable(R.drawable.btn_playlist_repeat_on, null)
                    val none : Drawable =  resources.getDrawable(R.drawable.nugu_btn_repeat_inactive, null)
                    val one :Drawable = resources.getDrawable(R.drawable.btn_playlist_repeat_on1, null)

                    if(binding.songRepeatIc.drawable.constantState == none.constantState){
                        binding.songRepeatIc.setImageDrawable(all);
                    }else if(binding.songRepeatIc.drawable.constantState == all.constantState){
                        binding.songRepeatIc.setImageDrawable(one);
                    }else{
                        binding.songRepeatIc.setImageDrawable(none);
                    }

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

    fun switchBtn( off : Drawable , on : Drawable , status : Drawable) : Drawable {
        if(off.constantState==(status.constantState)){
            return on
        }
        return off
    }
    object SampleToast {

        fun createToast(context: Context, message: String): Toast? {
            val inflater = LayoutInflater.from(context)
            val binding: ToastCustomBinding =
                    DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

            binding.tvSample.text = message
            return Toast(context).apply {
                setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 100.toPx())
                duration = Toast.LENGTH_LONG
                view = binding.root
            }
        }

        private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    }

}