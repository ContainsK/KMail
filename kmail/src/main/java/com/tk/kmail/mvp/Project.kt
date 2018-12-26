package com.tk.kmail.mvp

import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/25.
 */
class Project private constructor() {
    interface View : IBase.View<Presenter> {
        fun refreshList()
    }

    interface Presenter : IBase.Presenter<View> {
        fun getMessageArrs(): MutableList<DataBean>
    }

}