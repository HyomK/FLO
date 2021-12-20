package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemAlbumBinding
import retrofit2.http.Url


class AlbumResponseAdapter(val context: Context) :
    RecyclerView.Adapter<AlbumResponseAdapter.ViewHolder>() {
    private val  albumList= ArrayList<Album>()
    interface MyItemClickLister{
        fun onItemClick(album:Album){

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
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):AlbumResponseAdapter.ViewHolder {
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

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) {
        this.albumList.clear()
        this.albumList.addAll(albums)
        notifyDataSetChanged()
    }


    //뷰홀더에 데이터를 바인딩해줘야 할때마다 호출되는 함수
    override fun onBindViewHolder(holder: AlbumResponseAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener{mItemClickLister.onItemClick(albumList[position])}
        holder.binding.itemAlbumTitleTv.setOnClickListener{mItemClickLister.onRemoveAlbum(position)}
    }

    // 데이터 세트 크기를 할려주는 함수 -? 리사이클러뷰가 마지막이 언제인지를 알게된다

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            if(album.coverImgUrl == ""){
                Glide.with(context).load(album.coverImg!!).into(binding.itemAlbumIv)
            }else{
                Glide.with(context).load(album.coverImgUrl).into(binding.itemAlbumIv)
            }

            binding.itemAlbumTitleTv.text=album.title
            binding.itemAlbumSingerTv.text=album.singer

        }
    }



}
