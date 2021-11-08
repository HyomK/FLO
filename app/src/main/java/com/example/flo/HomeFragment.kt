package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
//
//        binding.homeSongCard01Cd.setOnClickListener {
////            (context as MainActivity).supportFragmentManager.beginTransaction()
////                    .replace(R.id.main_frm, AlbumFragment())
////                    .commitAllowingStateLoss()
//            setFragmentResult("requestKey",
//                bundleOf(
//                    "title" to binding.homeSongExp01Text01Tv.text.toString(),
//                    "singer" to binding.homeSongExp01Text02Tv.text.toString(),
//                    "image" to binding.homeSongEpx01Iv.drawable.toBitmap()
//                ))
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commit()
//
//        }
//
//        binding.homeSongCard02Cd.setOnClickListener {
//            setFragmentResult("requestKey",
//                bundleOf(
//                    "title" to binding.homeSongExp02Text01Tv.text.toString(),
//                    "singer" to binding.homeSongExp02Text02Tv.text.toString(),
//                    "image" to binding.homeSongEpx02Iv.drawable.toBitmap()
//                ))
//            parentFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, AlbumFragment())
//                .commit()
//        }
//
//        binding.homeSongCard03Cd.setOnClickListener {
//            setFragmentResult("requestKey",
//                bundleOf(
//                    "title" to binding.homeSongExp03Text01Tv.text.toString(),
//                    "singer" to binding.homeSongExp03Text02Tv.text.toString(),
//                    "image" to binding.homeSongEpx03Iv.drawable.toBitmap()
//                ))
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commit()
//        }

        //데이터 리스트 생성

        albumDatas.apply{
            var song :Song=Song("Savage","에스파",0,210,0,false)
            val songList : ArrayList<Song> = ArrayList<Song>();
            songList.add(song)
            add(Album("Savage","에스파",R.drawable.savage,songList))
            add(Album("쉬어","그레이노마",R.drawable.showme))
            add(Album("Strawberry moon","아이유(IU)",R.drawable.strawberry))
            add(Album("낙하","AKMU",R.drawable.nakha))
            add(Album("Butter","방탄소년단 (BTS)",R.drawable.img_album_exp))
            add(Album("Lilac","아이유(IU)",R.drawable.img_album_exp2)) // song 클래스 미추가 상태
        }

        // 어댑터 생성
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeRecycleviewRv.adapter=albumRVAdapter

        albumRVAdapter.setMyItemCLickLister(object : AlbumRVAdapter.MyItemClickLister{
            override fun onItemClick(album :Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        //레이아웃 매니저
        binding.homeRecycleviewRv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)




         val bannerAdapter=BannerViewpagerAdapter(this)
         bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL


        val backgroundAdapter = BackgroundViewpagerAdapter(this)
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_default_4_x_1, R.drawable.img_album_exp,R.drawable.img_album_exp2,"포근하게 덮어준는 꿈의 목소리"))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_default_4_x_1, R.drawable.img_album_exp2,R.drawable.img_album_exp2,"새벽에 듣기 좋은 음악"))
        backgroundAdapter.addFragment(BackgroundFragment(R.drawable.img_default_4_x_1, R.drawable.img_album_exp,R.drawable.img_album_exp,"운동할 때 신나게 듣기 좋은 음악"))
        binding.homeBackgroundVp.adapter=backgroundAdapter
        binding.homeBackgroundVp.orientation=ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.homeBackgroundTb, binding.homeBackgroundVp){
            tab,position->
        }.attach()

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {

        (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment().apply {
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val albumJson = gson.toJson(album)
                        putString("album",albumJson)

                    }
                })
                .commitAllowingStateLoss()
    }


}