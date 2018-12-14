package com.tk.kmail.mvp

import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
class Login private constructor() {
    interface View : IBase.View {
        fun loginOk()
        fun loginNo()
    }

    interface Presenter : IBase.Presenter<View> {
        fun login(u: String, p: String)
    }

}

