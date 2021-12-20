package com.example.flo

interface AlbumListView {
    fun onAlbumListLoading()
    fun onAlbumListSuccess(albums : ArrayList<Album>)
    fun onAlbumListFailure(code: Int, message: String)
}