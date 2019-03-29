package com.healthy.holder8.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.healthy.holder8.R
import kotlinx.android.synthetic.main.sos_page.*
import org.jetbrains.anko.support.v4.startActivity


class SOSpageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity, R.layout.sos_page, null)

        return view
    }
    fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL);
        val data = Uri . parse ("tel:" + phoneNum);
        intent.data = data;
        startActivity(intent);
    }

    override fun onResume() {
        super.onResume()
        sosTab.setOnTabChangedListner {
            for (i in 0..2) sosTab.getTabView(i).setBackgroundResource(R.drawable.sos_btn_background)
            sosTab.getTabView(it).setBackgroundResource(R.mipmap.sos_shadow_red)
            if (it == 0)
                startActivity<MapActivity>()
            if (it == 1) {
                callPhone("120")
            }
        }
    }
}
