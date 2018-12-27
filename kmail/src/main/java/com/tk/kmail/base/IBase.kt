package com.tk.kmail.base

import android.content.Context
import io.reactivex.disposables.Disposable

/**
 * Created by TangKai on 2018/12/25.
 */
class IBase private constructor() {
    interface IContext {
        fun getThisContext(): Context?
    }

    interface IViewDialog : IContext {
        fun showWaitingDialog()
        fun showWaitingDialog(text: String)
        fun hideWaitingDialog()
    }
}