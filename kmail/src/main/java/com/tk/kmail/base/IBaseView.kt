package com.tk.kmail.base

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast

interface IBaseView : IViewDialog {
    override fun showWaitingDialog() {
        Toast.makeText(getContext(), "show", Toast.LENGTH_SHORT).show()
    }

    override fun hideWaitingDialog() {
        Toast.makeText(getContext(), "hide", Toast.LENGTH_SHORT).show()
    }

    fun getAppTitle(): String {
        return ""
    }

    fun getContentView(): View? {
        return null
    }


    fun getLayoutId(): Int
    fun initView()
    fun getContext(): Context? {
        if (this is Activity)
            return this
        if (this is Fragment)
            return this.getContext()
        return null
    }
}