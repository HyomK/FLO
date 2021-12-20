package com.example.flo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumListService  {

    private lateinit var albumView: AlbumListView

    fun setAlbumListView(albumView: AlbumListView){
        this.albumView=albumView
    }


    fun getAlbums(){
        val retrofit= getRetrofit()

        albumView.onAlbumListLoading()
        val albumSerivce= retrofit.create(AlbumListRetrofitInterface::class.java)


        albumSerivce.loadAlbums().enqueue(object: Callback<AlbumsListResponse> {
            override fun onResponse(call: Call<AlbumsListResponse>, response: Response<AlbumsListResponse>) {
                Log.d("GETSONGACT/API-RESPONSE",response.toString())

                val resp=response.body()!!

                Log.d("GETALBUMACT/RESP-FLO",resp.toString())
                when(resp.code){
                    1000->albumView.onAlbumListSuccess(resp.result!!.albums)
                    else->{
                        albumView.onAlbumListFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<AlbumsListResponse>, t: Throwable) {
                Log.d("GETALBUMACT/RESP-FLO",t.message.toString())
                albumView.onAlbumListFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
        Log.d("GETALBUMACT/ASYNC","Signed")
    }
}