package com.example.flo

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeSongCard01Cd.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, AlbumFragment())
//                    .commitAllowingStateLoss()
            setFragmentResult("requestKey",
                bundleOf(
                    "title" to binding.homeSongExp01Text01Tv.text.toString(),
                    "singer" to binding.homeSongExp01Text02Tv.text.toString(),
                    "image" to binding.homeSongEpx01Iv.drawable.toBitmap()
                ))
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commit()

        }

        binding.homeSongCard02Cd.setOnClickListener {
            setFragmentResult("requestKey",
                bundleOf(
                    "title" to binding.homeSongExp02Text01Tv.text.toString(),
                    "singer" to binding.homeSongExp02Text02Tv.text.toString(),
                    "image" to binding.homeSongEpx02Iv.drawable.toBitmap()
                ))
            parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment())
                .commit()
        }

        binding.homeSongCard03Cd.setOnClickListener {
            setFragmentResult("requestKey",
                bundleOf(
                    "title" to binding.homeSongExp03Text01Tv.text.toString(),
                    "singer" to binding.homeSongExp03Text02Tv.text.toString(),
                    "image" to binding.homeSongEpx03Iv.drawable.toBitmap()
                ))
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commit()
        }


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


}