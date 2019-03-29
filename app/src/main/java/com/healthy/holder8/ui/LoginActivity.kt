package com.healthy.holder8.ui

import com.healthy.holder8.R
import com.healthy.holder8.contract.LoginContract
import com.healthy.holder8.model.Account
import com.healthy.holder8.model.MyDataBase
import com.healthy.holder8.presenter.LoginPresenter
import com.healthy.holder8.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

//登陆界面，需要实现对不同用户数据库的访问
//最好能做出加密效果。
//保存密码，基本检查功能等。

class LoginActivity: BaseActivity(),LoginContract.View {
    companion object {
        const val MIN_PASSWORD_L = 6
        const val MAX_PASSWORD_L = 12
    }

    private val preferences by lazy { SharedPreferencesUtils(this) }
    override fun onPasswordWrong() {
        log_passWord.error = getString(R.string.onPassWordWrong)
    }

    override fun onLoggingIn() {
        showProgress(getString(R.string.logging))
        if (checkBox.isChecked) {
            preferences.password_temp = log_passWord.text.toString()
        }
//        if (log_passWord.text.toString() == MyDataBase.instance.getAccount(log_userName.text.toString()).toString()) {
//            onLoginSuccess()
//            preferences.Login_state = true
//            startActivity<MainActivity>()
//        } else {
//            onLoginFailed()
//        }

       if (preferences.password== log_passWord.text.toString()){
            onLoginSuccess()
            preferences.Login_state = true
           startActivity<MainActivity>()
        }else{
            onLoginFailed()
       }
    }

    override fun onUserNameWrong() {
        log_userName.error = getString(R.string.onUserNameWrong)
    }

    override fun onLoginSuccess() {
        doAsync {
            val account = Account(mutableMapOf("name" to preferences.username))
            MyDataBase.instance.saveAccount(account)
        }
        dissmissProgress()
        startActivity<MainActivity>()
    }

    override fun onLoginFailed() {
        dissmissProgress()
        toast(getString(R.string.failedToLogin))
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        super.initData()
        log_passWord.hint = getString(R.string.login_hint).format(
            MIN_PASSWORD_L,
            MAX_PASSWORD_L
        )
        checkBox.isChecked = preferences.checkbox_state
        if (preferences.password_temp!="0"&&checkBox.isChecked){
            log_passWord.setText(preferences.password_temp)
        }
        btn_reg.setOnClickListener {
            startActivity<RegisterActivity>()
        }
        checkBox.setOnClickListener { if (checkBox.isChecked){
            preferences.password_temp = log_passWord.text.toString()
            preferences.checkbox_state = true
        }else{
                preferences.checkbox_state = false
            }

        }
        btn_login.setOnClickListener {
            val presenter = LoginPresenter(this)
            presenter.login(log_userName.text.toString(),log_passWord.text.toString())
        }
        tv_guest.setOnClickListener {
            startActivity<MainActivity>()
            finish()
        }

    }

}