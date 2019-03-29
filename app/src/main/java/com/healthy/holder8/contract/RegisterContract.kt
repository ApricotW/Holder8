package com.healthy.holder8.contract

import com.healthy.holder8.presenter.BasePresenter

interface RegisterContract {
    interface Presenter : BasePresenter{
        fun register(username: String, password: String, password2: String)
    }
    interface View{
        fun onUserNameInvaild()
        fun onPassWordInvaild()
        fun onReInputWrong()
        fun onUserNameRepeat()
        fun onRegingIn()
        fun onRegInSuccess()
        fun onRegInFailed()
    }
}