package com.example.flo

import com.google.gson.annotations.SerializedName

data class Albums(
        @SerializedName("albums")val albums: ArrayList<Album>)

data class AlbumsListResponse(@SerializedName( "isSuccess")val isSuccess: Boolean,
                              @SerializedName("code")val code : Int,
                              @SerializedName("message")val message: String,
                              @SerializedName("result")val result : Albums?)