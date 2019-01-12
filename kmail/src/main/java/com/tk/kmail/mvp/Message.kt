package com.tk.kmail.mvp

import com.tk.kmail.model.db_bean.ClassBean
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.mvp.base.IBase
import com.tk.kmail.mvp.base.ResultBean
import javax.mail.Folder

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
        fun getMessageArrs(folderName: String): MutableList<DataBean>
        fun getMessageArrs(folderName: String, start: Int, count: Int): MutableList<DataBean>
        fun getFolder(name: String): Folder?
        fun refreshList(folderName: String)
        fun refreshList(folderName: String, start: Int, count: Int)
        fun deleteMessage(folderName: String, uid: Long)
        fun sendMessage(folderName: String, bean: IGetData)
        fun setClassBean(clsBean: ClassBean)
        fun getClassBean(): ClassBean
        fun getCacheMessages(folderName: String, start: Int, count: Int): MutableList<DataBean>
    }
//
//    interface AddMessageView : IBase.View<AddMessagePresenter>, IBase.Result<ResultBean>, com.tk.kmail.base.IBase.IViewDialog
//
//    interface AddMessagePresenter : Presenter
}