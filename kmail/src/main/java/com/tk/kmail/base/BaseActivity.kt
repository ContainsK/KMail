package com.tk.kmail.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.tk.kmail.model.utils.Evs

/**
 * Created by TangKai on 2018/12/14.
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {
    val mContentView: View? by lazy {
        var v: View? = getContentView()
        if (v == null && getLayoutId() > 0) {
            v = LayoutInflater.from(getContext()).inflate(getLayoutId(), null)
        }
        v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mContentView)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Evs.unreg(this)
    }
}




