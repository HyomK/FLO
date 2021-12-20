package com.example.flo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 안씁니다.
    var title: String?="",
    var singer: String? = "",
    var coverImg: Int?=null,
    val coverImgUrl:String? = null

) :Parcelable