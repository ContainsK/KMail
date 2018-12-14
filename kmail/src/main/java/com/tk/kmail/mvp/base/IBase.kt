package com.tk.kmail.mvp.base

/**
 * Created by TangKai on 2018/12/14.
 */
class IBase private constructor() {
    interface View {
        val mPresenter: Presenter<*>?
            get() = getPresenter()


        fun getPresenter(): Presenter<*>
    }

    interface Presenter<T : IBase.View> {
        val mView: T

    }
}