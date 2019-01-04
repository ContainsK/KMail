package com.tk.kmail.mvp

import com.tk.kmail.mvp.base.IBase
import com.tk.kmail.mvp.base.ResultBean
import javax.mail.Folder

/**
 * Created by TangKai on 2018/12/25.
 */
class ProjectMng private constructor() {
    companion object {
        val TYPE_PROJECT_ADD = 0x10
        val TYPE_PROJECT_DELETE = 0x11
    }

    interface View : IBase.View<Presenter>, IBase.Result<ResultBean>, com.tk.kmail.base.IBase.IViewDialog {
        fun refreshList(list: MutableList<Folder>)
    }

    interface Presenter : IBase.Presenter<View> {
        fun refreshList()
        fun createFolder(name: String)
        fun deleteFolder(name: String)
    }


    interface AddView {

    }

    interface AddPresenter {

    }
}