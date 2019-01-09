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
        val TYPE_PASSERROR = 0x02
        val TYPE_SEND = 0x03
    }

    interface View : IBase.View<Presenter>, IBase.Result<ResultBean>, com.tk.kmail.base.IBase.IViewDialog {
        fun refreshList(list: MutableList<DataBean>)
        fun getPassword(): String
    }

    interface Presenter : IBase.Presenter<View> {
        fun getMessageArrs(folder: Folder): MutableList<DataBean>
        fun getMessageArrs(folder: Folder, start: Int, end: Int): MutableList<DataBean>
        fun getFolder(name: String): Folder?
        fun refreshList(folder: Folder)
        fun deleteMessage(msg: Message)
        fun sendMessage(folder: Folder, bean: IGetData)
        fun setFolder(folder: Folder)

        fun getFolder(): Folder
    }
//
//    interface AddMessageView : IBase.View<AddMessagePresenter>, IBase.Result<ResultBean>, com.tk.kmail.base.IBase.IViewDialog
//
//    interface AddMessagePresenter : Presenter
}