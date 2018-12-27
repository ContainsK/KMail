package com.tk.kmail.mvp

import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.mvp.base.IBase
import com.tk.kmail.mvp.base.ResuleBean

/**
 * Created by TangKai on 2018/12/25.
 */
class ProjectMng private constructor() {
    companion object {
        val TYPE_NOT_LOGIN = 0x01
    }

    interface View : IBase.View<Presenter>, IBase.Result<ResuleBean> {
        fun refreshList(list: MutableList<DataBean>)
    }

    interface Presenter : IBase.Presenter<View> {
        fun getMessageArrs(): MutableList<DataBean>
        fun refreshList()
    }

}