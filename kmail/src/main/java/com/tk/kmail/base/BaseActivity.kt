package com.tk.kmail.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
abstract class BaseActivity : AppCompatActivity(),IBuid {
    var mContentView: View? = null
        get() {
            if (field == null)
                return getContentView()
            return field
        }
    override final fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mContentView == null && getLayoutId() > 0)
            mContentView = View.inflate(this, getLayoutId(), null)
        initView()
        setContentView(mContentView)
    }

}
