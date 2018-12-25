package com.tk.kmail.mvp

import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
class UserManager private constructor() {

    interface View : IBase.View<Presenter> {
        fun addResult(isOk: Boolean)
        fun refreshUserList(arr: List<UserBean>)
    }

    interface Presenter : IBase.Presenter<View> {
        fun addUser(user: UserBean)
        fun getUserList(): List<UserBean>
        fun deleteUser(user: UserBean)
        fun refreshUserList()
    }
}