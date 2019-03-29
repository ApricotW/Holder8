package com.healthy.holder8.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    val progressDialog by lazy {
        ProgressDialog(this)
    }
    fun showProgress(message:String){
        progressDialog.setMessage(message)
        progressDialog.show()
    }
    fun dissmissProgress(){
        progressDialog.dismiss()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initData()
    }

   open fun initData() {
    }

    abstract fun getLayoutResId(): Int
}