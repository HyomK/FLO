package com.example.flo

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity.apply
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    var information= arrayListOf("내 리스트", "좋아요", "저장앨범", "많이 들은")


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerAdapter(this)
        binding.lockerVp.adapter=lockerAdapter




        TabLayoutMediator(binding.lockerTb, binding.lockerVp){
            tab, position->
            tab.text = information[position]
        }.attach()


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initView()

    }




    private fun initView(){
        val spf= activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val jwt=getUserIdx(requireContext())
        Log.d("Login-API",jwt.toString())
        if(jwt==0) {
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener{
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }else{
            binding.lockerLoginTv.text="로그아웃"
            binding.lockerUserTv.text=spf!!.getString("name","none")
            binding.lockerLoginTv.setOnClickListener{
                logout()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun logout(){

        val spf= activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val editor=spf!!.edit()
        editor.remove("jwt")
        editor.remove("userIdx")
        editor.apply()
    }

    private fun getJwt():Int{
        val spf= activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt",0)
    }


}