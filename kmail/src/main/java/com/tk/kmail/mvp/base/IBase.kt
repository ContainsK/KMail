package com.tk.kmail.mvp.base

import com.tk.kmail.model.utils.Evs
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by TangKai on 2018/12/14.
 */
class IBase private constructor() {
    interface Base {
        fun <T, R> Observable<T>.runUI(r: (T) -> R): Observable<R> {
            return this.observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread()).map(r)
        }

        fun <T, R> Observable<T>.runIO(r: (T) -> R): Observable<R> {
            return this.observeOn(io.reactivex.schedulers.Schedulers.io()).map(r)
        }

        fun <T, R> Observable<T>.run(scheduler: Scheduler, r: (T) -> R): Observable<R> {
            return this.observeOn(scheduler).map(r)
        }

        fun callRecycler() {
            Evs.unreg(this)
        }
    }

    interface View<T:Presenter<*>> : Base {
        val mPresenter: T
            get() = getPresenter()


        fun getPresenter(): T
    }

    interface Presenter<T> : Base {
        val mView: T

    }

    interface Result<T> {
        fun callResult(result: T)
    }
}