package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding


class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickLister{
        fun onItemClick(album: Album){

        }

        fun onRemoveAlbum(position :Int ){

        }
    }
    //리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickLister: MyItemClickLister

    fun setMyItemCLickLister(itemClickLister: MyItemClickLister){
        mItemClickLister=itemClickLister
    }

    // 뷰홀더를 생성해줘야 할때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }


    //뷰홀더에 데이터를 바인딩해줘야 할때마다 호출되는 함수
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{mItemClickLister.onItemClick(albumList[position])}
        holder.binding.itemAlbumTitleTv.setOnClickListener{mItemClickLister.onRemoveAlbum(position)}
    }

    // 데이터 세트 크기를 할려주는 함수 -? 리사이클러뷰가 마지막이 언제인지를 알게된다

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            binding.itemAlbumTitleTv.text=album.title
            binding.itemAlbumSingerTv.text=album.singer
            binding.itemAlbumIv.setImageResource(album.coverImg!!)
        }
    }



}




