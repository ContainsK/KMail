package com.tk.kmail.project.Message

import com.kmail.greendao.gen.MsgBeanDao
import com.sun.mail.imap.IMAPFolder
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
    fun cacheMessage(folder: Folder, start: Int, end: Int) {
        val msgDao = App.daoSession.msgBeanDao
        val count = folder.messageCount
        val dataBeans = mutableListOf<DataBean>()
        if (count < 1)
            return

        val es = if (end > count) count else end
        val messages = folder.getMessages(start, es)
        val imapFolder = folder as IMAPFolder
        val cacheMsgs = msgDao.queryBuilder().orderAsc(MsgBeanDao.Properties.Uid).offset(start).limit(es - start).list()

        if (!cacheMsgs.first().equals(imapFolder.getUID(messages.first()))
                || !cacheMsgs.last().uid.equals(imapFolder.getUID(messages.last()))) {
            //顺序不对,删除本地的，在添加新的
            //TODO 这里函数需要对不同分类的UID数据进行处理
            msgDao.deleteInTx(cacheMsgs)
            messages.forEach {
                msgDao.save(App.mails!!.parseMessage2MsgBean(it, mView.getPassword()))
            }
        }

    }

    override fun getMessageArrs(folder: Folder, start: Int, end: Int): MutableList<DataBean> {
        val email = App.mails!!
        val count = folder.messageCount
        val dataBeans = mutableListOf<DataBean>()
        if (count < 1)
            return dataBeans
        val messages = folder.getMessages(start, if (end > count) count else end)
        folder.fetch(messages, email.getFetchProfile())
        for (v in messages) {
            println("message number ${v.messageNumber}")
            val msg = v as IMAPMessage
            try {
                val dataBean = email.parseMessage(msg, mView.getPassword())
                println(dataBean)
                dataBeans.add(dataBean)
            } catch (e: DecodePassException) {
                mView.callResult(ResultBean(Message.TYPE_PASSERROR, false, null))
                throw e
            }
        }
        return dataBeans

    }

    override fun sendMessage(folder: Folder, bean: IGetData) {
        Observable.just(1)
                .runUI {
                    mView.showWaitingDialog("保存中，请稍候...")
                }.runIO {
                    val app = App.mails!!
                    app.sendMessage(app.openFolder(folder)!!, bean, mView.getPassword())
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
            println("msg1 ${msg.flags} ${msg.isExpunged}")
            App.mails!!.deleteMessage(msg)
            msg.folder.fetch(arrayOf(msg), App.mails!!.getFetchProfile())
            println("msg2 ${msg.flags} ${msg.isExpunged}")

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

    override fun refreshList(folder: Folder) {
        mView.runDialog {
            getMessageArrs(App.mails!!.refreshFolder(folder)!!)
        }.subscribe({
            println("subscribe ${it.size}")
            mView.refreshList(it)
        })
    }


    override fun getMessageArrs(folder: Folder): MutableList<DataBean> {
        return getMessageArrs(folder, 1, Int.MAX_VALUE)
    }

}