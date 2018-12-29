package com.tk.kmail.mvp

import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.mvp.base.IBase
import com.tk.kmail.mvp.base.ResultBean
import io.reactivex.Observable

/**
 * Created by TangKai on 2018/12/14.
 */
class Login private constructor() {
    companion object {
        val TYPE_LOGIN = 0x01
        val TYPE_OUT = 0x02
    }

    interface View : IBase.View<Presenter>, com.tk.kmail.base.IBase.IViewDialog, IBase.Result<ResultBean> {

    }

    interface Presenter : IBase.Presenter<View> {
        fun login(bean: UserBean)
        fun loginOut(): Observable<*>
    }

}

