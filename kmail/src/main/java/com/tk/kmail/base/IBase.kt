package com.tk.kmail.base

import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by TangKai on 2018/12/25.
 */
class IBase private constructor() {
    interface IContext {
        fun getThisContext(): Context?
    }

    interface IRecycler {
        fun recycler()
    }

    interface IViewDialog : IContext {
        fun showWaitingDialog()
        fun showWaitingDialog(text: String)
        fun hideWaitingDialog()


        fun <T> runDialog(t: String? = null, v: () -> T): Observable<T> {
            return Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        if (t == null) showWaitingDialog()
                        else
                            showWaitingDialog(t)
                    }
                    .observeOn(Schedulers.io())
                    .map { v() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        it.printStackTrace()
                        hideWaitingDialog()
                    }.observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        println("complete")
                        hideWaitingDialog()
                    }

        }

    }
}