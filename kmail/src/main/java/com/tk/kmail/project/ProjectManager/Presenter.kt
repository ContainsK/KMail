package com.tk.kmail.project.ProjectManager

import com.sun.mail.imap.IMAPMessage
import com.tk.kmail.App
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.mvp.ProjectMng

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: ProjectMng.View) : ProjectMng.Presenter {
    override fun refreshList() {
        mView.refreshList(getMessageArrs())
    }

    override fun getMessageArrs(): MutableList<DataBean> {
        val email = App.mails!!
        val dataBeans = mutableListOf<DataBean>()
        val abcd = email.openFolder("Abcd")
        if (abcd != null) {
            val messages = abcd.getMessages()
            abcd.fetch(messages, email.getFetchProfile())

            for (v in messages) {
                val msg = v as IMAPMessage
                println(msg.flags.toString() + " flags")
                val dataBean = email.parseMessage(msg)
                println(dataBean)
                dataBeans.add(dataBean)
            }
        }
        return dataBeans
    }

}