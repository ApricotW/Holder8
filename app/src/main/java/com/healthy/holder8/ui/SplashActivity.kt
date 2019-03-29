package com.healthy.holder8.ui

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import com.healthy.holder8.R
import com.healthy.holder8.utils.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

//程序启动界面。
//调用SplashContract对是否完成登录进行校验。
//有启动动画，也可以加入特性展示列表。
//根据情况拉起登陆页面，或者拉起主页面。
class SplashActivity : BaseActivity(),ViewPropertyAnimatorListener{
    private val preferences by lazy { SharedPreferencesUtils(this) }
    companion object {
        const val DELAY = 1000L
        var needFinish:Boolean = false
        //伴生变量：控制动画效果持续时间。
    }
    private val handler by lazy {
        android.os.Handler()
        //懒加载Handler
    }

    override fun initData() {

        super.initData()
        ViewCompat.animate(splashImage).scaleX(1.0f).scaleY(1.0f).setDuration(DELAY).setListener(this)
        //启动界面动画。
    }

    override fun onAnimationStart(p0: View?) {
        var count = preferences.count
//        if(mBluetoothUtils.isSupported()){
//            toast("蓝牙自检状态：可用")
//            mBluetoothUtils.openBluetooth(this,120)
//        }else{
//            toast("本机不支持蓝牙，程序将退出。")
//            needFinish = true
//        }
}

    override fun onAnimationEnd(p0: View?) {

        var count = preferences.count
        //显示程序以前使用过的次数

        //动画结束时，拉起handler，运行run()
        if (needFinish){
            finish()
        }else {
            //读取SharedPreferences中的count数据

            toast("程序以前被使用了" + count + "次。")
            preferences.count++

            handler.post(object : Runnable {
                override fun run() { checkLoginStatus() }
            })
        }
    }
    override fun onAnimationCancel(p0: View?) {
    }


    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }
    fun checkLoginStatus() {
//        当该acitivty启动的时候，先从SharedPreferences中获取登录状态的值，如果为true，那么直接调转到主页
            if (preferences.Login_state) {
                startActivity<MainActivity>()
                finish()
            }else {
                startActivity<WelcomeActivity>()
//                  startActivity<BluetoothClientActivity>()
                finish()
        }

    }

}