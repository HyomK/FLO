package com.example.flo

interface LookView {
    fun onGetSongsLoading()
    fun onGetSongSuccess(songs : ArrayList<Song>)
    fun onGetSongFailure(code: Int, message: String)
}