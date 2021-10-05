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
import com.example.flo.databinding.FragmentHomeBinding


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

        return binding.root
    }


}