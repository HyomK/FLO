package com.example.flo

import com.example.flo.databinding.ChartItemBinding


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemLikeBinding



class ChartSongRVAdapter(val context : Context) :
        RecyclerView.Adapter<ChartSongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()
    private var Index=1;
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChartSongRVAdapter.ViewHolder {
        val binding: ChartItemBinding = ChartItemBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])

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

    inner class ViewHolder(val binding: ChartItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            if(song.coverImgUrl == ""){
                Glide.with(context).load(song.coverImg!!).into(binding.chartItemCoverIv)
            }else{
                Glide.with(context).load(song.coverImgUrl).into(binding.chartItemCoverIv)
            }

            binding.chartItemTitleTv.text=song.title
            binding.chartItemSingerTv.text=song.singer
            binding.chartItemPositionTv.text=Index.toString()
            Index++;
        }
    }
}