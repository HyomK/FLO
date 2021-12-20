package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding:ActivityLoginBinding
    private lateinit var btnhandle:BtnThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
   
        binding.loginSignBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            login()
        }



    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun login(){
        if (binding.loginEmailEd.text.toString().isEmpty() || binding.loginIdEd.text.isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPwEd.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
            return
        }
        val email: String = binding.loginIdEd.text.toString() + "@" + binding.loginEmailEd.text.toString()
        val pwd: String = binding.loginPwEd.text.toString()

//        val songDB=SongDatabase.getInstance(this)!!
//        val user=songDB.userDao().getUser(email,pwd)
//
//        user?.let{
//            Log.d("LOGINACT/GET_USER","user id : ${user.id}, ${user}")

//            savename(user.email,user.password)
//        }
        val user= User(email,pwd,"")

        val authService=AuthService()
        authService.setLoginView(this)
        authService.login(user)
        savename(this,user.name)

    }
    private fun startActivityMain(){
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }





    /*
        private fun   saveUserIdx(userIdx: Int){
            val spf = getSharedPreferences("auth", MODE_PRIVATE)
            val editor =spf.edit()
            editor.putInt("jwt",userIdx)
            editor.apply()

        }*/

    inner class BtnThread:Thread(){
        override fun run() {
                while ( true) {
                    runOnUiThread {
                        if (( binding.loginPwEd.text.toString().isEmpty())) {
                            binding.loginSignBtn.setBackgroundResource((R.color.light_gray))
                        } else {
                            binding.loginSignBtn.setBackgroundResource((R.color.flo_main))
                        }
                    }
                }
            }
    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility= View.VISIBLE
    }

    override fun onLoginSuccess(auth: Auth) {
        binding.loginLoadingPb.visibility= View.GONE

        //saveJwt(auth.jwt)
        Log.d("LOGINSUCCESS- ",auth.toString())
        saveUserIdx(this,auth.userIdx)
        saveJwt(this,auth.jwt)
        startActivityMain()



    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility= View.GONE

        when(code){
            2015,2019,3014->{
                binding.loginEmailErrorTv.visibility=View.VISIBLE
                binding.loginEmailErrorTv.text=message
            }

        }
    }


}