package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.IntentCompat
import com.example.flo.databinding.ActivitySignupBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignupActivity : AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignupBinding
    var isLook=false
    var isLookCheck=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            signUp()

        }

        initView()



    }

    override fun onStart() {
        super.onStart()

    }

    private fun initView(){

        var emailData=arrayOf("선택","gmail.com", "naver.com", "daum.net","직접입력")
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,emailData)
        binding.signupEmailSp.adapter=adapter
        binding.signupEmailSp.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                 if(position==4){
//                    binding.signupEmailEd.visibility=VISIBLE
//                    binding.signupEmailSp.visibility= INVISIBLE
//                    binding.signupEmailEd.setText("")
//                    return
//                }
//                 else if(position==0){
//                     binding.signupEmailEd.setText("")
//                     return
//                 }
//                 else binding.signupEmailEd.setText(emailData[position])
                binding.signupEmailEd.setText("gmail.com")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun getUser(): User {
        val email: String = binding.signupIdEd.text.toString() + "@" + binding.signupEmailEd.text.toString()
        val pwd: String = binding.signupPwEd.text.toString()
        val name : String = binding.signupNameEd.text.toString()
        Log.d("SIGNUPACT/API-RESPONSE"," ${email} | ${pwd} | ${name} ")
        return User(email, pwd,name)
    }

//    private fun signUp() : Boolean {
//        if (binding.signupIdEd.text.toString().isEmpty() || binding.signupEmailEd.text.isEmpty()) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (binding.signupPwEd.text.toString() != (binding.signupPwcheckEd.text.toString())) {
//            Toast.makeText(this, "비밀번호 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        val userDB= SongDatabase.getInstance(this)!!
//        val user=getUser()
//        userDB.userDao().insert(user)
//
//        var spf = getSharedPreferences("name", MODE_PRIVATE)
//        val editor =spf.edit()
//        editor.clear()
//        editor.putString("name",user.name)
//        editor.apply()
//
//        val users=userDB.userDao().getUsers()
//        Log.d("SGINUPACT",users.toString())
//        return true
//    }

    private fun signUp() {

        if (binding.signupIdEd.text.toString().isEmpty() || binding.signupEmailEd.text.isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signupPwEd.text.toString() != (binding.signupPwcheckEd.text.toString())) {
            Toast.makeText(this, "비밀번호 형식이 잘못되었습니다", Toast.LENGTH_SHORT).show()
            return
        }
        val user= getUser()
        val authService=AuthService()
        authService.setSignUpView(this)
        authService.signUp(user)

        savename(this,user.name) // 이름 추가
        Log.d("SIGNUPACT/ASYNC","Signed")
    }

    override fun onSignUpLoading() {
        binding.signupLoadingPb.visibility=View.VISIBLE
    }

    override fun onSignUpSuccess(userIdx:Int, jwt:String? ) {
        binding.signupLoadingPb.visibility=View.GONE
        saveUserIdx(this,userIdx)
        saveJwt(this,jwt!!)
        startActivity(Intent(this,LoginActivity::class.java))
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signupLoadingPb.visibility=View.GONE
        when(code){
            2016,2017,2000->{
                binding.signupEmailErrorTv.visibility=View.VISIBLE
                binding.signupEmailErrorTv.text=message
            }
        }
    }


}