package com.tk.kmail.project.Message

import com.sun.mail.imap.IMAPMessage
import com.tk.kmail.App
import com.tk.kmail.model.exception.DecodePassException
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.Subscribe
import javax.mail.Folder

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: Message.View) : Message.Presenter {
    override fun sendMessage(folder: Folder, bean: IGetData, password: String) {
        Observable.just(1)
                .runUI {
                    mView.showWaitingDialog("保存中，请稍候...")
                }.runIO {
                    val app = App.mails!!
                    app.sendMessage(app.openFolder(folder)!!, bean, password)
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.hideWaitingDialog()
                    mView.callResult(ResultBean(Message.TYPE_SEND, true, result = "保存成功！"))
                }, {
                    it.printStackTrace()
                    mView.hideWaitingDialog()
                    mView.callResult(ResultBean(Message.TYPE_SEND, false, result = "保存失败！"))
                })


    }

    override fun deleteMessage(msg: javax.mail.Message) {
        mView.runDialog("删除中...") {
            App.mails!!.deleteMessage(msg)
        }.doOnError {
            mView.callResult(ResultBean(Message.TYPE_DELETE, false, msg))
        }.runUI { mView.callResult(ResultBean(Message.TYPE_DELETE, true, msg)) }.subscribe()


    }

    private lateinit var folder: Folder

    init {
        Evs.reg(this)
    }

    @Subscribe(sticky = true)
    override fun setFolder(folder: Folder) {
        this.folder = folder
    }

    override fun getFolder(): Folder {
        return folder
    }

    override fun getFolder(name: String): Folder? {
        val email = App.mails!!
        return email.openFolder("Abcd")
    }

    override fun refreshList(folder: Folder, password: String) {
        mView.runDialog {
            getMessageArrs(App.mails!!.refreshFolder(folder)!!, password)
        }.subscribe({
            println("subscribe ${it.size}")
            mView.refreshList(it)
        })
    }


    override fun getMessageArrs(folder: Folder, password: String): MutableList<DataBean> {
        val email = App.mails!!
        val dataBeans = mutableListOf<DataBean>()

        val messages = folder.getMessages()
        folder.fetch(messages, email.getFetchProfile())
        for (v in messages) {
            val msg = v as IMAPMessage
            try {
                val dataBean = email.parseMessage(msg, password)
                println(dataBean)
                dataBeans.add(dataBean)
            } catch (e: DecodePassException) {
                mView.callResult(ResultBean(Message.TYPE_PASSERROR, false, null))
                throw e
            }
        }
        return dataBeans
    }

}