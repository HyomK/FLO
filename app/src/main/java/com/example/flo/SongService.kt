package com.example.flo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongService {
    private lateinit var  lookView:LookView
    fun setLookView(lookView:LookView){
        this.lookView=lookView
    }


    fun getSongs(){
        val retrofit= getRetrofit()

        lookView.onGetSongsLoading()
        val songService= retrofit.create(SongRetrofitInterface::class.java)


        songService.getSongs().enqueue(object: Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                Log.d("GETSONGACT/API-RESPONSE",response.toString())

                val resp=response.body()!!

                Log.d("GETSONGACT/RESPONSE-FLO",resp.toString())
                when(resp.code){
                    1000->lookView.onGetSongSuccess(resp.result!!.songs)
                    else->{
                        lookView.onGetSongFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                Log.d("GETSONGACT/API-RESPONSE",t.message.toString())
                lookView.onGetSongFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
        Log.d("SIGNUPACT/ASYNC","Signed")
    }
}