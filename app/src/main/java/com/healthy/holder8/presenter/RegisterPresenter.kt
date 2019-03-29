package com.healthy.holder8.presenter

import com.healthy.holder8.contract.RegisterContract
import com.healthy.holder8.ui.LoginActivity.Companion.MAX_PASSWORD_L
import com.healthy.holder8.ui.LoginActivity.Companion.MIN_PASSWORD_L

class RegisterPresenter(val view: RegisterContract.View):RegisterContract.Presenter {
    override fun register(username: String, password: String, password2:String) {
        when{
//            username in MyDataBase.instance.getAllAccount().toString() -> view.onUserNameRepeat()
            password.length>MAX_PASSWORD_L||password.length<MIN_PASSWORD_L -> view.onPassWordInvaild()
            password != password2 -> view.onReInputWrong()
            else -> view.onRegingIn()
        }
    }
}