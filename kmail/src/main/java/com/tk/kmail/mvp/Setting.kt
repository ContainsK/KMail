package com.tk.kmail.mvp

import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
class Setting private constructor() {
    interface View : IBase.View<Presenter> {
    }

    interface Presenter : IBase.Presenter<View> {
        fun saveUser(username: String, password: String)
    }
}