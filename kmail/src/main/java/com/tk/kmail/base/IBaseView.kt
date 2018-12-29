package com.tk.kmail.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.tk.kmail.model.utils.Evs

interface IBaseView<T : com.tk.kmail.mvp.base.IBase.View<*>> : IBase.IContext {

    fun getAppTitle(): String {
        return ""
    }

    fun getContentView(): View? {
        return null
    }

    fun getViewP(): T

    fun getLayoutId(): Int
    fun initView()

    fun recycler() {
        Evs.unreg(this)
    }

    fun getViewDialog(): IBase.IViewDialog {
        return object : IBase.IViewDialog {
            val p: ProgressDialog by lazy { ProgressDialog(getThisContext()) }
            override fun showWaitingDialog() {
                showWaitingDialog("加载中，请稍候...")
            }

            override fun getThisContext(): Context? {
                return this@IBaseView.getThisContext()
            }

            override fun showWaitingDialog(text: String) {
                p.apply { setMessage(text) }.show()
            }

            override fun hideWaitingDialog() {
                p.dismiss()
            }

        }
    }

    override fun getThisContext(): Context? {
        if (this is Activity)
            return this
        if (this is Fragment)
            return this.context
        return null
    }


}