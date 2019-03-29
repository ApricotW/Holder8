package com.healthy.holder8.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.healthy.holder8.R
import kotlinx.android.synthetic.main.home_page.*
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.*
import kotlin.random.Random

class HomePageFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =View.inflate(activity, R.layout.home_page,null)
        return view
    }


}