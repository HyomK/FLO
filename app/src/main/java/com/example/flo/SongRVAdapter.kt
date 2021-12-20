package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemLikeBinding



class SongRVAdapter(val context : Context) :
        RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SongRVAdapter.ViewHolder {
        val binding: ItemLikeBinding = ItemLikeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.likeFrMenuBtn.setOnClickListener {

            Log.d("Unlike cancle", "cancle like position" +position)
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)


        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemLikeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            if(song.coverImgUrl == ""){
                Glide.with(context).load(song.coverImg!!).into(binding.likeItemCoverIv)
            }else{
                Glide.with(context).load(song.coverImgUrl).into(binding.likeItemCoverIv)
            }

            binding.likeItemSingerTv.text=song.singer
            binding.lockerItemTitleTv.text=song.title
        }
    }
}