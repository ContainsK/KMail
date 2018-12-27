package com.tk.kmail.mvp

import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.mvp.base.IBase
import com.tk.kmail.mvp.base.ResuleBean

/**
 * Created by TangKai on 2018/12/14.
 */
class UserManager private constructor() {
    companion object {
        val TYPE_ADD = 0x01
        val TYPE_DELETE = 0x02
        val TYPE_UPDATE = 0x03
    }

    interface View : IBase.View<Presenter>, IBase.Result<ResuleBean> {
        fun refreshUserList(arr: List<UserBean>)
    }

    interface Presenter : IBase.Presenter<View> {
        fun addUser(user: UserBean)
        fun getUserList(): List<UserBean>
        fun deleteUser(user: UserBean)
        fun refreshUserList()
    }
}