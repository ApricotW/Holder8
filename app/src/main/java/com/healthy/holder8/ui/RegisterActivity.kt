package com.healthy.holder8.ui

import com.healthy.holder8.R
import com.healthy.holder8.contract.RegisterContract
import com.healthy.holder8.presenter.RegisterPresenter
import com.healthy.holder8.ui.LoginActivity.Companion.MAX_PASSWORD_L
import com.healthy.holder8.ui.LoginActivity.Companion.MIN_PASSWORD_L
import com.healthy.holder8.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_reg.*
import org.jetbrains.anko.toast

class RegisterActivity: BaseActivity(),RegisterContract.View {
    private val preferences by lazy { SharedPreferencesUtils(this) }

    override fun onRegingIn() {
        showProgress(getString(R.string.regging))
        if (phoneInput.text.count() == 11) {
            preferences.phone = phoneInput.text.toString()
            onRegInSuccess()
        }else{
            onRegInFailed()
        }
    }

    override fun onRegInSuccess() {
        preferences.username = reg_userName.text.toString()
        preferences.password = reg_passWord.text.toString()
//        doAsync {
//            val account = Account(mutableMapOf("name" to preferences.username,"password" to preferences.password))
//            MyDataBase.instance.saveAccount(account)
//        }
        dissmissProgress()
        finish()
    }

    override fun onRegInFailed() {
        dissmissProgress()
        toast(getString(R.string.failedToReg))
    }
    override fun onUserNameInvaild() {
        reg_userName.error = getString(R.string.onUserNameInvaild)
    }

    override fun onPassWordInvaild() {
        reg_passWord.error = getString(R.string.onPassWordInvaild).format(MIN_PASSWORD_L,MAX_PASSWORD_L)
    }

    override fun onReInputWrong() {
        passWord2.error = getString(R.string.onReInputWrong)
    }

    override fun onUserNameRepeat() {
        reg_userName.error = getString(R.string.onUserNameRepeat)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_reg
    }

    override fun initData() {
        super.initData()
        textView12.text = getString(R.string.login_hint).format(MIN_PASSWORD_L, MAX_PASSWORD_L)
        btn_regin.setOnClickListener {
            val presenter = RegisterPresenter(this)
            presenter.register(reg_userName.text.toString(),reg_passWord.text.toString(),passWord2.text.toString())
        }
    }

}