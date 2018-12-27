package com.tk.kmail.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.tk.kmail.model.utils.Evs
import io.reactivex.Observable
import io.reactivex.Scheduler

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

    fun <T, R> Observable<T>.runUI(r: (T) -> R): Observable<R> {
        return this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).map(r)
    }

    fun <T, R> Observable<T>.runIO(r: (T) -> R): Observable<R> {
        return this.observeOn(io.reactivex.schedulers.Schedulers.io()).map(r)
    }

    fun <T, R> Observable<T>.run(scheduler: Scheduler, r: (T) -> R): Observable<R> {
        Observable.just(1)
        return this.observeOn(scheduler).map(r)
    }


}