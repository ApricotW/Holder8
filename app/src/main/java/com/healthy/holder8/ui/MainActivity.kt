package com.healthy.holder8.ui

//主界面，需要实现实时健康状况显示，同时具有侧滑统计数据功能。

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.widget.TextView
import com.healthy.holder8.R
import com.healthy.holder8.model.MyDataBase
import com.healthy.holder8.utils.SharedPreferencesUtils
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.params.TextParams
import com.mylhyl.circledialog.params.TitleParams
import com.special.ResideMenu.ResideMenu
import com.special.ResideMenu.ResideMenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_page.*
import kotlinx.android.synthetic.main.sos_page.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import java.util.*
import kotlin.random.Random


@TargetApi(Build.VERSION_CODES.M)
class MainActivity : BaseActivity() {

    companion object {
        lateinit var instance : MainActivity
    }

    val preferences by lazy { SharedPreferencesUtils(this) }
//    private var account =  MyDataBase.instance.getAccount(preferences.username!!)
    override fun getLayoutResId(): Int {
       return R.layout.activity_main
    }

    override fun initData() {
        super.initData()
        instance = this
        calThread = CalThread()
        calThread.start()

        // attach to current activity;
        resideMenu.setBackground(R.drawable.bg)
        resideMenu.attachToActivity(this)
        resideMenu.addIgnoredView(vp_main)
        resideMenu.setShadowVisible(false)

        // create menu items;
        val titles = arrayOf("我的联系人", "我的设备", "收藏", "个性装扮","我的会员","客户服务","退出登录")
        val icon = intArrayOf(
            R.mipmap.icon_contact,
            R.mipmap.icon_mydevice,
            R.mipmap.icon_mystar,
            R.mipmap.icon_skin,
            R.mipmap.icon_vip,
            R.mipmap.icon_service,
            android.R.drawable.ic_lock_power_off
        )

        val item1 = ResideMenuItem(this, icon[0], titles[0])
        val item2 = ResideMenuItem(this, icon[1], titles[1])
        val item3 = ResideMenuItem(this, icon[2], titles[2])
        val item4 = ResideMenuItem(this, icon[3], titles[3])
        val item5 = ResideMenuItem(this, icon[4], titles[4])
        val item6 = ResideMenuItem(this, icon[5], titles[5])
        val item7 = ResideMenuItem(this, icon[6], titles[6])
        val itemList = listOf<ResideMenuItem>(item1,item2,item3,item4,item5,item6,item7)

            // getting type of the field from superClass
        val privateTextView = ResideMenuItem::class.java.getDeclaredField("tv_title")
            // transform this field to public
        privateTextView.isAccessible = true
            // getting value from this field which is reference to a TextView

        for (i in titles.indices){
            val tv = privateTextView.get(itemList[i]) as TextView
            //finaly setting the Typface
            tv.textSize = 14f
            tv.textColor = getColor(R.color.colorGray)
            resideMenu.addMenuItem(itemList[i], ResideMenu.DIRECTION_LEFT) // or  ResideMenu.DIRECTION_RIGHT
        }
        item1.setOnClickListener {
            val cirtemp = CircleDialog.Builder()
            cirphone(cirtemp)
            cirtemp.show(supportFragmentManager)
        }
        item7.setOnClickListener { preferences.Login_state = false
            val cirdelete = CircleDialog.Builder()
            cirdelete(cirdelete).show(supportFragmentManager)
        }

        val mainAdapter = MainAdapter(supportFragmentManager)
        vp_main.adapter = mainAdapter
        vp_main.addOnAdapterChangeListener(mainAdapter)

        topbar.getTabView(1).isSelected = true
        topbar.setOnTabChangedListner {
            when (it) {
                0 -> resideMenu.openMenu(ResideMenu.DIRECTION_LEFT)
            }
        }


        alphaIndicator.setViewPager(vp_main)
        alphaIndicator.setOnTabChangedListner {
            when(it){
                2 -> alphaIndicator.currentItemView.removeShow()
            }
        }

        dotimer()
        alphaIndicator.getTabView(1).isSelected = true
        vp_main.currentItem = 1
    }

    private var a:String = "75"
    private var b:String = "120/70"
    private var c:String = "36.5"
    private var d:String = "160/120"
    private var e:String = "10/100"

    private var count = 0
    val resideMenu:ResideMenu by lazy { ResideMenu(this) }
    var linecolor = R.color.colorBlueLine
    private var totalcount = 0
    private var timer:Timer? = Timer()
    private var myTask: MyTask? = MyTask()
    val handler = Handler()
    fun circd(circd:CircleDialog.Builder):CircleDialog.Builder {
        circd.setCanceledOnTouchOutside(false)
        circd.setCancelable(false)//返回键关闭，默认true
        circd.setWidth(0.85f)//宽度
        circd.setMaxHeight(0.5f)//最大高度
        circd.setRadius(15)//圆角半径
//                    .setYoff()//对话框y坐标偏移
        circd.setTitle("风险提示")
//        circd.setTitleIcon(R.drawable.alert)
        circd.configTitle {
                params: TitleParams? -> params!!.textSize = 20
        }
        //标题字体颜值 0x909090 or Color.parseColor("#909090")
        circd.setTitleColor(Color.BLACK)
        circd.setText("检测到您的健康指标已超出正常范围\n驾驶存在安全风险，是否进行呼救")//内容
        circd.configText{ params: TextParams? -> params!!.textSize = 14 }
        circd.setTextColor(Color.BLACK)//内容字体色值
        circd.setNegative("稍后提醒") {dataRight(1)}
        circd.setPositive("立即呼救") { vp_main.currentItem=0
            sosTab.setTabCurrenItem(1)
            if(preferences.phone == null) preferences.phone = "120"
            callPhone(preferences.phone!!)
            dataRight(1)
        }
        return circd
    }
    private fun cirdelete(cirdelete:CircleDialog.Builder):CircleDialog.Builder{
        cirdelete.setCanceledOnTouchOutside(false)
        cirdelete.setCancelable(false)//返回键关闭，默认true
        cirdelete.setWidth(0.85f)//宽度
        cirdelete.setMaxHeight(0.5f)//最大高度
        cirdelete.setRadius(15)//圆角半径
//                    .setYoff()//对话框y坐标偏移
        cirdelete.setTitle("请注意")
//        circd.setTitleIcon(R.drawable.alert)
        cirdelete.configTitle {
                params: TitleParams? -> params!!.textSize = 20
        }
        //标题字体颜值 0x909090 or Color.parseColor("#909090")
        cirdelete.setTitleColor(Color.BLACK)
        cirdelete.setText("退出时是否清空相关账号的数据？")//内容
        cirdelete.configText{ params: TextParams? -> params!!.textSize = 14 }
        cirdelete.setTextColor(Color.BLACK)//内容字体色值
        cirdelete.setNegative("保留") {
            startActivity<LoginActivity>()
            finish()
        }
        cirdelete.setPositive("清空") {
            doAsync { MyDataBase.instance.deleteAccount(preferences.username!!)  }
            startActivity<LoginActivity>()
            finish()
        }
        return cirdelete
    }
    private fun cirphone(cirphone:CircleDialog.Builder):CircleDialog.Builder {
        //添加标题，参考普通对话框
        cirphone.setInputHint("请输入11位紧急联系人电话号码")//输入框默认文本，提示
        cirphone.setInputHeight(140)//输入框高度
        cirphone.setInputCounter(11)//输入框的最大字符数，默认格式在输入右下角例如：20
        cirphone.setInputCounterColor(getColor(R.color.colorPrimaryDark))//最大字符数文字的颜色值
        cirphone.setPositiveInput("确定") { text, v ->
            if (text.length == 11){
                handler.post { preferences.phone = text }
                true
            }else{
                false
            }

        }
        cirphone.setNegative("取消", null)
        return cirphone
    }

    val homePageFragment by lazy { HomePageFragment() }
    val sosPageFragment by lazy { SOSpageFragment() }
    val dataPageFragment by lazy { DataPageFragment() }

    fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL);
        val data = Uri . parse ("tel:" + phoneNum);
        intent.data = data;
        startActivity(intent);
    }
    fun updateUI(){
                tv_beatCurrent.text = a
                tv_bloodpressure.text = b
                tv_temperature.text = c
                tv_bloodoxy.text = d
                tv_alcohol.text = e
    }
    private var randcount = 0
    var i = 1
    internal lateinit var calThread: CalThread

    private fun rand():Float{
        randcount++
        return if (randcount<15) {
            35 * Random.nextFloat() + 10
        }else{
            if (randcount==50){
                randcount = 0
            }
            50f
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private fun dataWrong(i:Int){

        imageView3.setImageResource(R.mipmap.roundback_red)
        imageView6.setBackgroundColor(getColor(R.color.colorAccent))
        linecolor = R.color.colorAccent
        ecg_view.mPath.color = getColor(linecolor)
        topbar.background = getDrawable(R.color.colorAccent)
        alphaIndicator.getTabView(2).showNumber(alphaIndicator.getTabView(2).badgeNumber+1)
        when(i){
            5 ->{
                alcohol.setImageResource(R.mipmap.alcohol_red)
                tv_alcohol.textColor = getColor(R.color.colorAccent)
                textView5.text = "当前实时状态：酒精含量超标"
            }
            1 ->{
                bloodpressure.setImageResource(R.mipmap.bloodpressure_red)
                tv_bloodpressure.textColor = getColor(R.color.colorAccent)
                textView5.text = "当前实时状态：血压异常"
                val circd = CircleDialog.Builder()
                circd(circd).show(supportFragmentManager)
            }

        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    private fun dataRight(i: Int){
        imageView3.setImageResource(R.mipmap.middle_circle)
        imageView6.setBackgroundColor(getColor(R.color.colorPrimary))
        topbar.background = getDrawable(R.color.colorBlueLine)
        linecolor = R.color.colorBlueLine
        ecg_view.mPath.color = getColor(linecolor)
        textView5.text = "当前实时状态：正常"
        when(i){
            5 -> {
                alcohol.setImageResource(R.mipmap.alcohol)
                tv_alcohol.textColor = getColor(R.color.colorPrimary)
            }
            1 ->{
                bloodpressure.setImageResource(R.mipmap.bloodpresure)
                tv_bloodpressure.textColor = getColor(R.color.colorPrimary)

            }
        }
    }
    internal inner class CalThread:Thread(){
        lateinit var lineHandler:Handler
        override fun run() {
            Looper.prepare()
            lineHandler = object : Handler(){
                override fun handleMessage(msg: Message?) {
                    if(msg!!.what == 1001){
                        var r = rand().toInt()/2
                        i++
                        if (i == 10) {
                            r -= 80
                        }
                        if (i == 20) {
                            r = 55
                        }
                        if (i == 21) {
                            r = 25
                        }
                        if (i == 22) {
                            r = 20
                        }
                        if (i == 30) {
                            r = 0//心形位置
                        }
                        if (i == 40) {
                            i = 0
                        }
                        ecg_view.setData(r)
                    }
                }
            }
            Looper.loop()
        }
    }

    inner class MyTask : TimerTask() {
        override fun run() {
        }
    }

    internal inner class MainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm), ViewPager.OnPageChangeListener,
        ViewPager.OnAdapterChangeListener {
        override fun onAdapterChanged(p0: ViewPager, p1: PagerAdapter?, p2: PagerAdapter?) {
        }

        val fragments: List<Fragment> = listOf<Fragment>(sosPageFragment, homePageFragment, dataPageFragment)
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(position: Int) {
        }
    }
    override fun onStop() {
        super.onStop()
        timer!!.cancel()
        timer = null
        myTask = null
//        calThread.suspend()
    }
    override fun onResume() {
        super.onResume()
//        calThread.resume()
        if (timer == null) {
            timer = Timer()
            myTask = MyTask()
            dotimer()
        }

    }
    private fun dotimer(){
        timer!!.schedule(object : TimerTask() {
            override fun run() {
//                doAsync {
//                    account = Account(mutableMapOf("name" to preferences.username,"password" to preferences.password,
//                        "beat" to a,"bpressure" to b,"btemperature" to c,"boxy" to d))
//                    MyDataBase.instance.saveAccount(account)
//                }
                val msg = Message()
                msg.what = 1001
                val bundle =Bundle(1)
                msg.data = bundle
                calThread.lineHandler.sendMessage(msg)
                count++
                if (count==20) {
                    totalcount++
                    count = 0
                }
                when(totalcount){
                    1 -> {
                        if (count == 5){
                            a = "78"
                            b = "125/70"
                            c = "37.0"
                        }

                    }
                    2 -> {
                        if (count == 5){
                            a = "77"
                            c = "37.2"
                            d = "162/120"
                        }

                    }
                    3 -> {
                        if (count == 5){
                            a = "78"
                            c = "36.7"
                        }

                    }
                    4 -> {
                        if (count == 5){
                            a = "79"
                            b = "120/70"
                            c = "36.3"
                        }

                    }
                    5 -> {
                        if (count == 5){
                            a = "82"
                            b = "125/70"
                            c = "37.0"
                            d = "155/120"
                        }

                    }
                    7 -> {
                        if (count == 5){
                            a = "80"
                            b = "127/70"
                            c = "37.1"
                            d = "155/120"
                            e = "70/100"
                        }

                    }
                    10 -> {
                        if (count == 5){
                            a = "75"
                            b = "127/70"
                            c = "36.2"
                            d = "150/120"
                            e = "120/100"
                            handler.post{dataWrong(5)}
                        }
                    }
                    15 -> {
                        if (count == 5){
                            e = "15/100"

                            handler.post{dataRight(5)}
                        }

                    }
                    17 -> {
                        if (count == 5){
                            a = "78"
                            b = "125/70"
                            c = "37.0"
                        }

                    }
                    19 -> {
                        if (count == 5){
                            a = "77"
                            c = "37.2"
                            d = "162/120"
                        }

                    }
                    21 -> {
                        if (count == 5){
                            a = "78"
                            c = "36.7"
                        }

                    }
                    25 -> {
                        if (count == 5){
                            a = "79"
                            b = "120/70"
                            c = "36.3"
                        }

                    }
                    27 -> {
                        if (count == 5){
                            a = "82"
                            b = "125/70"
                            c = "37.0"
                            d = "155/120"
                        }

                    }
                    30 ->{
                        if (count == 5){
                            b = "220/120"
                            handler.post { dataWrong(1) }
                        }
                    }
//                    31 -> totalcount = 0
                }
                handler.post{updateUI()}
            }
        }, 1000, 50)
    }


//    private val mHandler: Handler = Handler();
//    private var mCountTime = 1
//    private val mDelay = 10L

//    private val countDown = object : Runnable {
//        override fun run() {
//            if (mCountTime>0){
//                mHandler.postDelayed(this,mDelay)
//            }else{
//                lineCharts.makeCharts(lineCV,rand())
//                mCountTime=2
//                start()
//            }
//            mCountTime--
//        }
//    }
//    private fun start(){
//        mHandler.postDelayed(countDown, 0)
//    }

//    private val lineCharts = LineCharts()
    /**
     * 心跳数据模拟生成函数，有点丑，不过频率还好
     */


//        lineCharts.initView(lineCV)
//        start()

//        val myDatabaseOpenHelper = MyDatabaseOpenHelper(this)

}

