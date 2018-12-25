package com.tk.kmail.base

import android.content.Context

/**
 * Created by TangKai on 2018/12/25.
 */
class IBase private constructor() {
    interface IContext {
        fun getContext(): Context?
    }

    interface IViewDialog : IContext {
        fun showWaitingDialog()
        fun showWaitingDialog(text: String)
        fun hideWaitingDialog()
    }
}