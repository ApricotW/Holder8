package com.healthy.holder8.ui

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import com.healthy.holder8.R
import org.jetbrains.anko.startActivity
import java.util.*

class WelcomeActivity : BaseActivity(){
    lateinit var vp: ViewPager
    var rg: RadioGroup? = null
    lateinit var btn: Button

    lateinit var ll: LinearLayout
    //把小圆点存到集合中
    lateinit var listDoc: MutableList<ImageView>

    lateinit var list: MutableList<ImageView>
    var imgArray = intArrayOf(R.mipmap.lunbo1, R.mipmap.lunbo2, R.mipmap.lunbo3)

    var addtime: Int = 0


    override fun getLayoutResId(): Int {
        return R.layout.activity_welcome
    }

    override fun initData() {
        super.initData()
        btn = findViewById(R.id.btn)

        // 1、初始化控件
        vp = findViewById<View>(R.id.vp) as ViewPager
        // 2、初始化数据
        initPageData()
        // 3、创建apdater对象
        val adapter = MyPagerAdapter()
        // 4、绑定
        vp.adapter = adapter

        //添加小圆点的方法
        addDoc()

        btn.setOnClickListener {
            startActivity<LoginActivity>()
            finish()
        }
        vp.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(arg0: Int) {
                // 通过vp的选中位置，来确定小圆点的联动
                val index = arg0 % list.size
                for (i in imgArray.indices) {
                    if (index == i) {
                        listDoc[i].setImageResource(R.mipmap.point_selected)
                    } else {
                        listDoc[i].setImageResource(R.mipmap.point_mormal)
                    }
                }
                //当index == 3的时候 说明显示的是第四张图片
                if (index == imgArray.size - 1) {
                    btn.visibility = View.VISIBLE
                } else {
                    btn.visibility = View.GONE
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        })

    }
    /**
     * 把小圆点按照导航图片的数量初始化出来
     */
    private fun addDoc() {
        listDoc = ArrayList()
        ll = findViewById<LinearLayout>(R.id.ll)

        //根据图片的数量创建小圆点对象
        for (i in imgArray.indices) {
            val iv = ImageView(this)
            if (i == 0) {
                iv.setImageResource(R.mipmap.point_selected)
            } else {
                iv.setImageResource(R.mipmap.point_mormal)
            }
            listDoc.add(iv)
            ll.addView(iv)
        }
    }

    private fun initPageData() {
        list = ArrayList()
        for (i in imgArray.indices) {
            val iv = ImageView(this)
            iv.setImageResource(imgArray[i])
            iv.scaleType = ImageView.ScaleType.FIT_XY

            list.add(iv)
        }
    }

    internal inner class MyPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            // TODO Auto-generated method stub
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // TODO Auto-generated method stub
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            if (addtime<3){
                container.addView(list[position])
                addtime++
            }
            return list[position]
        }

    }

}

