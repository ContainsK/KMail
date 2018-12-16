package com.tk.kmail.base

import android.view.View

/**
 * Created by TangKai on 2018/12/14.
 */
interface IBuid {
    fun getLayoutId(): Int
    fun getContentView(): View?
    fun showWaitingDialog()
    fun hideWaitingDialog()
    fun getAppTitle(): String
    fun initView()
}