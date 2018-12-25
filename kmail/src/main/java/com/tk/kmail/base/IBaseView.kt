package com.tk.kmail.base

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import com.tk.kmail.model.utils.Evs

interface IBaseView : IBase.IContext {

    fun getAppTitle(): String {
        return ""
    }

    fun getContentView(): View? {
        return null
    }


    fun getLayoutId(): Int
    fun initView()

    fun recycler() {
        Evs.unreg(this)
    }

    fun getViewDialog(): IBase.IViewDialog {
        return object : IBase.IViewDialog {
            val p: ProgressDialog by lazy { ProgressDialog(getContext()) }
            override fun showWaitingDialog() {
                showWaitingDialog("加载中，请稍候...")
            }

            override fun getContext(): Context? {
                return this@IBaseView.getContext()
            }

            override fun showWaitingDialog(text: String) {
                p.apply { setMessage(text) }.show()
            }

            override fun hideWaitingDialog() {
                p.dismiss()
            }

        }
    }
}