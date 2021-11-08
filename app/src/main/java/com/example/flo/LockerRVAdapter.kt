package com.example.flo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemLockerBinding

class LockerRVAdapter(private val storedList: ArrayList<Album>) : RecyclerView.Adapter<LockerRVAdapter.ViewHolder>() {


    interface LockerItemClickLister{

        fun onRemoveSong(position: Int){

        }
    }
    private lateinit var myItemClickLister: LockerItemClickLister

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LockerRVAdapter.ViewHolder {
        val binding: ItemLockerBinding = ItemLockerBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    fun setLockerItemClickListener(itemClickLister: LockerItemClickLister){
        myItemClickLister=itemClickLister
    }

    fun removeItem(position: Int){

        storedList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LockerRVAdapter.ViewHolder, position: Int) {
       holder.bind(storedList[position])
        holder.binding.lockerFrMenuBtn.setOnClickListener {
        myItemClickLister.onRemoveSong(position)
        }
    }

    override fun getItemCount(): Int = storedList.size

    inner class ViewHolder(val binding:ItemLockerBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.lockerItemCoverIv.setImageResource(album.coverImg!!)
            binding.lockerItemSingerTv.text=album.singer
            binding.lockerItemTitleTv.text=album.title
        }
    }

}