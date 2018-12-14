package com.tk.kmail.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
abstract class BaseActivity : AppCompatActivity(), IBase.View, IBuid {
    override final fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun showWaitingDialog() {
    }

    override fun hideWaitingDialog() {
    }

    override fun getAppTitle(): String {
        return ""
    }

    override fun getContentView(): View? {
        return null
    }
}