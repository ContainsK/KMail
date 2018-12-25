package com.tk.kmail.mvp

import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.mvp.base.IBase
import io.reactivex.Observable

/**
 * Created by TangKai on 2018/12/14.
 */
class Login private constructor() {
    interface View : IBase.View<Presenter>, com.tk.kmail.base.IBase.IViewDialog {
        fun loginResult(bean: UserBean?, b: Boolean)

    }

    interface Presenter : IBase.Presenter<View> {
        fun login(bean: UserBean)
        fun loginOut(): Observable<*>
    }

}

