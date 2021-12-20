package com.example.flo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumListRetrofitInterface {
    @GET("/albums")
    fun loadAlbums() : Call<AlbumsListResponse>
}