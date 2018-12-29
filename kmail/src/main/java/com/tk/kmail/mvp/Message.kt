package com.tk.kmail.mvp

import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.mvp.base.IBase
import com.tk.kmail.mvp.base.ResultBean
import javax.mail.Folder
import javax.mail.Message

/**
 * Created by TangKai on 2018/12/28.
 */
class Message private constructor() {
    companion object {
        val TYPE_DELETE = 0x01
    }

    interface View : IBase.View<Presenter>, IBase.Result<ResultBean>, com.tk.kmail.base.IBase.IViewDialog {
        fun refreshList(list: MutableList<DataBean>)
    }

    interface Presenter : IBase.Presenter<View> {
        fun getMessageArrs(folder: Folder): MutableList<DataBean>
        fun getFolder(name: String): Folder?
        fun refreshList(folder: Folder)
        fun deleteMessage(msg: Message)

        fun setFolder(folder: Folder)

        fun getFolder(): Folder
    }

    interface AddMessageView : IBase.View<AddMessagePresenter>, IBase.Result<ResultBean>, com.tk.kmail.base.IBase.IViewDialog

    interface AddMessagePresenter : IBase.Presenter<AddMessageView> {
        fun sendMessage(folder: Folder, bean: IGetData)

        fun setFolder(folder: Folder)

        fun getFolder(): Folder
    }
}