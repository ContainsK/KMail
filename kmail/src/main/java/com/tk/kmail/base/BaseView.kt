package com.tk.kmail.base

import android.content.Context
import android.view.View

abstract class BaseView : IBuid {

    var mContentView: View? = null
        get() {
            if (field == null)
                return getContentView()
            return field
        }

//    if (mContentView == null && getLayoutId() > 0)
//    mContentView = View.inflate(mContext, getLayoutId(), null)

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

     abstract fun getContext(): Context
}