package com.tk.kmail.mvp.base

/**
 * Created by TangKai on 2018/12/14.
 */
class IBase private constructor() {
    interface View<T> {
        val mPresenter: T
            get() = getPresenter()


        fun getPresenter(): T
    }

    interface Presenter<T> {
        val mView: T

    }

    interface Result<T> {
        fun callResult(result: T)
    }
}