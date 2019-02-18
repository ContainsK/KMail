package com.tk.kmail.project.Message

import com.kmail.greendao.gen.MsgBeanDao
import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPMessage
import com.tk.kmail.App
import com.tk.kmail.model.db_bean.ClassBean
import com.tk.kmail.model.db_bean.Flag
import com.tk.kmail.model.exception.DecodePassException
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.NetUtils
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import org.greenrobot.eventbus.Subscribe
import javax.mail.Folder

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: Message.View) : Message.Presenter {
    override fun getClassBean(): ClassBean {
        return classBean
    }

    @Subscribe(sticky = true)
    override fun setClassBean(clsBean: ClassBean) {
        classBean = clsBean
    }

    override fun refreshList(folderName: String, start: Int, count: Int) {
        mView.runDialog {
            getCacheMessages(folderName, start, count)
        }.doOnError {
            if (it is DecodePassException)
                mView.callResult(ResultBean(Message.TYPE_PASSERROR, false, null))
        }.subscribe({
            println("subscribe ${it.size}")
            mView.refreshList(it)
        })
    }

    override fun getCacheMessages(folderName: String, start: Int, countA: Int): MutableList<DataBean> {
        val msgDao = App.daoSession.msgBeanDao
        val dataBeans = mutableListOf<DataBean>()
        val netStatus = NetUtils.isNetworkAvailable()
        var folder: Folder? = null
        val count: Int = if (netStatus) {
            folder = getFolder(folderName)
            folder?.messageCount ?: 0
        } else msgDao.queryBuilder().listLazy().size
        if (count < 1 || start > count)
            return dataBeans

        val aStart = start.coerceIn(1, count)
        val aEnd = (aStart - 1 + countA).coerceIn(aStart, count)
        val cacheMsgs = msgDao.queryBuilder()
                .where(MsgBeanDao.Properties.ClassName.eq(folderName), MsgBeanDao.Properties.Flag.notEq(Flag.FLAG_DELETE))
                .orderAsc(MsgBeanDao.Properties.SendTime).offset(aStart - 1).limit(countA).list()
        if (netStatus) {
            val messages = folder!!.getMessages(aStart, aEnd)
            val imapFolder = folder as IMAPFolder
            val w = imapFolder.getUID(messages.first())
            println(w.toString())

            if (App.mails!!.isGetMessage() && (cacheMsgs.isEmpty() || cacheMsgs.size != messages.size || cacheMsgs.first().uid != imapFolder.getUID(messages.first())
                            || cacheMsgs.last().uid != imapFolder.getUID(messages.last()))) {
                //顺序不对,删除本地的，在添加新的
                //TODO 这里函数需要对不同分类的UID数据进行处理

                //TODO 对 未反馈给服务器数据 处理，例如本地删除后，没有反馈到服务器，并且从服务器更新数据后，应该是不对本地数据做比较,交集

                //TODO 云回收站，将删除的数据存储，方便下次回收，或者增加隐藏功能，等等...

                folder.fetch(messages, App.mails!!.getFetchProfile())

                msgDao.deleteInTx(cacheMsgs)

                messages.forEach {
                    Mails.parseMessage2MsgBean(it).apply {
                        msgDao.insert(this)
                        dataBeans.add(Mails.parseMsgB2DataB(this, mView.getPassword()))
                    }
                }
                return dataBeans
            }
        }
        cacheMsgs.forEach {

            dataBeans.add(Mails.parseMsgB2DataB(it, mView.getPassword()).also { println(it.title) })
        }
        return dataBeans

    }

    override fun getMessageArrs(folderName: String, start: Int, end: Int): MutableList<DataBean> {
        val email = App.mails!!
        val folder = getFolder(folderName)
        val count = folder?.messageCount ?: 0
        val dataBeans = mutableListOf<DataBean>()
        if (count < 1)
            return dataBeans
        val messages = folder!!.getMessages(start, if (end > count) count else end)
        folder.fetch(messages, email.getFetchProfile())
        for (v in messages) {
            println("message number ${v.messageNumber}")
            val msg = v as IMAPMessage
            val dataBean = Mails.parseMessage(msg, mView.getPassword())
            println(dataBean)
            dataBeans.add(dataBean)
        }
        return dataBeans

    }

    override fun sendMessage(folderName: String, bean: IGetData) {
        mView.runDialog("保存中，请稍候...") {
            if (!NetUtils.isNetworkAvailable()) {
                Mails.parseIGetData2MsgBean(bean, mView.getPassword()).apply {
                    className = folderName
                    flag = Flag.FLAG_CREATE
                    App.daoSession.msgBeanDao.insert(this)
                }
                return@runDialog
            }
            val app = App.mails!!
            val b = app.sendMessage(getFolder(folderName)!!, bean, mView.getPassword())
            App.daoSession.msgBeanDao.insert(b)
        }.subscribe({
            mView.hideWaitingDialog()
            mView.callResult(ResultBean(Message.TYPE_SEND, true, result = "保存成功！"))
        }, {
            it.printStackTrace()

            mView.hideWaitingDialog()
            mView.callResult(ResultBean(Message.TYPE_SEND, false, result = "保存失败！"))
        })


    }

    override fun deleteMessage(folderName: String, uid: Long) {
        mView.runDialog("删除中...") {
            val build = App.daoSession.msgBeanDao.queryBuilder()
            build.where(MsgBeanDao.Properties.ClassName.eq(folderName)
                    , MsgBeanDao.Properties.Uid.eq(uid))


            if (!NetUtils.isNetworkAvailable()) {
                val ls = build.list()
                if (ls.size > 0) {
                    if (ls[0].flag == Flag.FLAG_DEFAULT) {
                        ls[0].flag = Flag.FLAG_DELETE
                        App.daoSession.msgBeanDao.update(ls[0])
                    } else if (ls[0].flag == Flag.FLAG_CREATE) {
                        App.daoSession.msgBeanDao.delete(ls[0])
                    }
                }
                return@runDialog
            }


            val folder = getFolder(folderName) as IMAPFolder
            val msg = folder.getMessageByUID(uid)
//            println("msg1 ${msg.flags} ${msg.isExpunged}")
            App.mails!!.deleteMessage(msg)
            build.buildDelete().executeDeleteWithoutDetachingEntities()

//            msg.folder.fetch(arrayOf(msg), App.mails!!.getFetchProfile())
//            println("msg2 ${msg.flags} ${msg.isExpunged}")

        }.doOnError {
            mView.callResult(ResultBean(Message.TYPE_DELETE, false, null))
        }.runUI { mView.callResult(ResultBean(Message.TYPE_DELETE, true, null)) }.subscribe()


    }

    private lateinit var classBean: ClassBean

    init {
        Evs.reg(this)
    }


    override fun getFolder(name: String): Folder? {
        return App.mails?.openFolder(name, false)
    }

    override fun refreshList(folderName: String) {
        mView.runDialog {
            getMessageArrs(folderName)
        }.doOnError {
            if (it is DecodePassException)
                mView.callResult(ResultBean(Message.TYPE_PASSERROR, false, null))
        }.subscribe({
            println("subscribe ${it.size}")
            mView.refreshList(it)
        })
    }


    override fun getMessageArrs(folderName: String): MutableList<DataBean> {
        return getMessageArrs(folderName, 1, Int.MAX_VALUE)
    }

}