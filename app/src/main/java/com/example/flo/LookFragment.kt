package com.example.flo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment() {

    lateinit var binding: FragmentLookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

}