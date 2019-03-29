package com.healthy.holder8.contract

import com.healthy.holder8.presenter.BasePresenter

interface LoginContract {
    interface Presenter : BasePresenter {

        fun login (userName:String,password:String)

    }
    interface  View{
        fun onLoginSuccess()
        fun onLoginFailed()
        fun onUserNameWrong()
        fun onPasswordWrong()
        fun onLoggingIn()
    }
}