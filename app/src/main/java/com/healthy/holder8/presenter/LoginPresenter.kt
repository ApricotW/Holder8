package com.healthy.holder8.presenter

import com.healthy.holder8.contract.LoginContract
import java.net.PasswordAuthentication

class LoginPresenter(val view:LoginContract.View):LoginContract.Presenter {
    override fun login(userName: String, password: String) {
        when{
            '@' !in userName -> view.onUserNameWrong()
            password.length>16 -> view.onPasswordWrong()
            else -> view.onLoggingIn()
        }
    }

}