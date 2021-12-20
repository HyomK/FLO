package com.example.flo

interface SignUpView {
    fun onSignUpLoading()
    fun onSignUpSuccess(userIdx:Int, jwt:String?)
    fun onSignUpFailure(code : Int, message: String)
}