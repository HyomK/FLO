package com.example.flo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class HomeFragment : Fragment(),AlbumListView {

    lateinit var binding: FragmentHomeBinding
    private lateinit var albumDatas :List<Album>
    private var mContext: Context? = null
    private lateinit var  albumRVAdapter :AlbumRVAdapter
    private lateinit var todayAlbumRVAdapter: AlbumResponseAdapter
    private lateinit var   bannerAdapter:BannerViewpagerAdapter
    private lateinit var backgroundAdapter:BackgroundViewpagerAdapter
    private val gson=Gson()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initRoomRv()
        initBackgroundVP()
        initAPIRv()

        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL

        binding.homeBackgroundVp.adapter=backgroundAdapter
        binding.homeBackgroundVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.homeBackgroundTb, binding.homeBackgroundVp){ tab, position->
        }.attach()
        getAlbums()
        return binding.root
    }
    private fun initBackgroundVP(){
        bannerAdapter=BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))


       backgroundAdapter = BackgroundViewpagerAdapter(this)
        backgroundAdapter.addFragment(
                BackgroundFragment(
                        R.drawable.img_default_4_x_1,
                        R.drawable.img_album_exp,
                        R.drawable.img_album_exp2,
                        "포근하게 덮어준는 꿈의 목소리"
                )
        )
        backgroundAdapter.addFragment(
                BackgroundFragment(
                        R.drawable.img_default_4_x_1,
                        R.drawable.img_album_exp2,
                        R.drawable.img_album_exp2,
                        "새벽에 듣기 좋은 음악"
                )
        )
        backgroundAdapter.addFragment(
                BackgroundFragment(
                        R.drawable.img_default_4_x_1,
                        R.drawable.img_album_exp,
                        R.drawable.img_album_exp,
                        "운동할 때 신나게 듣기 좋은 음악"
                )
        )
    }
    private fun initRoomRv(){

        val songDB = SongDatabase.getInstance(mContext!!)!!
        albumDatas = songDB.albumDao().getAlbums()
        //데이터 리스트 생성

        // 어댑터 생성
        var arr : ArrayList<Album> =ArrayList<Album>();
        arr.addAll(albumDatas)
        albumRVAdapter = AlbumRVAdapter(arr)
        binding.homeRecycleviewRv.adapter=albumRVAdapter

        albumRVAdapter.setMyItemCLickLister(object : AlbumRVAdapter.MyItemClickLister {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        //레이아웃 매니저
        binding.homeRecycleviewRv.layoutManager=LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
    }


    private fun initAPIRv(){

        todayAlbumRVAdapter = AlbumResponseAdapter(requireContext())

        binding.homeTodayRecycleviewRv.adapter=todayAlbumRVAdapter


        binding.homeTodayRecycleviewRv.layoutManager=LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )

        todayAlbumRVAdapter.setMyItemCLickLister(object :AlbumResponseAdapter.MyItemClickLister {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                todayAlbumRVAdapter.removeItem(position)
            }
        })
    }




    private fun changeAlbumFragment(album: Album) {

        (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment().apply {
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val albumJson = gson.toJson(album)
                        putString("album",albumJson)
                        putInt("albumId",album.id)

                    }
                })
                .commitAllowingStateLoss()
    }

    private fun getAlbums(){
        val albumListService=AlbumListService()
        albumListService.setAlbumListView((this))
        albumListService.getAlbums()
    }

    override fun onAlbumListLoading() {
    }

    override fun onAlbumListSuccess(albums: ArrayList<Album>) {
        todayAlbumRVAdapter.addAlbums(albums)
        Log.d("HOMEFR-API"," : SUCCESS")
    }

    override fun onAlbumListFailure(code: Int, message: String) {
        when(code){
            400-> Log.d("HOMEFRAG/API"," - ERROR")
            else ->Log.d("HOMEFRAG/API"," - ERROR2")
        }
    }


}