package com.tk.kmail.model.mails

import com.google.gson.JsonSyntaxException
import com.sun.mail.imap.IMAPFolder
import com.tk.kmail.model.bean.MessageDataBean
import com.tk.kmail.model.db_bean.MsgBean
import com.tk.kmail.model.exception.DecodePassException
import com.tk.kmail.model.utils.DesUtil
import com.tk.kmail.model.utils.GsonUtils
import com.tk.kmail.model.utils.NetUtils
import java.util.*
import javax.mail.*
import javax.mail.event.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeUtility

/**
 * Created by TangKai on 2018/12/13.
 */

class Mails(val server: IServer) {
    var mSession: Session
    lateinit var store: Store
    private var delTim: Long = 0
//    lateinit var transport: Transport

    companion object {
        val PROJECT_NAME = "Tan9-X"
        val PROJECT_NAME_S = Mails.PROJECT_NAME + "_"
        val KEY_HEAD_CONTENT = "MK-CONTENT"
        val KEY_HEAD_DESCRIBE = "MK-DESCRIBE"
        val KEY_HEAD_SUBJECT = "Subject"

        fun parseMessage2MsgBean(msg: Message): MsgBean {
            val folder = msg.folder as IMAPFolder
            return MsgBean().apply {
                content = MimeUtility.decodeText(MimeUtility.unfold(msg.getHeader(KEY_HEAD_CONTENT)[0]))
                uid = folder.getUID(msg)
                title = MimeUtility.decodeText(MimeUtility.unfold(msg.getHeader(KEY_HEAD_SUBJECT)[0]))
                dec = MimeUtility.decodeText(MimeUtility.unfold(msg.getHeader(KEY_HEAD_DESCRIBE)[0]))
                sendTime = msg.receivedDate
                className = msg.folder.name
            }
        }

        fun parseIGetData2MsgBean(msg: IGetData, password: String): MsgBean {
            val bean = MsgBean()
            val j = GsonUtils.gson().toJson(com.tk.kmail.model.bean.MessageDataBean(msg.getMsgContent()))
            val p = DesUtil.encrypt(password, j)
            bean.content = p
            bean.title = msg.getMsgTitle()
            bean.dec = msg.getMsgDescribe()
            bean.sendTime = Date()
            return bean
        }

        fun parseMessage(msg: Message, password: String): DataBean {
            return parseMessage2MsgBean(msg).let {
                parseMsgB2DataB(it, password)
            }
        }

        fun parseMsgB2DataB(msgB: MsgBean, password: String): DataBean {
            return GsonUtils.build(GsonUtils.gson().toJson(msgB)).getClassObj(DataBean::class.java)
                    .apply {
                        content = parseJson2MessageDataBean(msgB.content, password).mail_content
                    }
        }

        /**
         * 判断密码，异常抛出
         */
        private fun parseJson2MessageDataBean(encodeText: String, password: String): MessageDataBean {
            val exc = DecodePassException("pass error !")
            val p = DesUtil.decrypt(password, encodeText)
            try {
                val bean = GsonUtils.gson().fromJson(p, com.tk.kmail.model.bean.MessageDataBean::class.java)
                if (!GsonUtils.isJson(p) || bean == null)
                    throw exc
                return bean
            } catch (ex: JsonSyntaxException) {
                throw exc
            }
        }
    }


    init {
        val properties = Properties()
        var protocol = IServer.TYPE_IMAP
        properties["mail.$protocol.host"] = protocol + ".qq.com"
        properties["mail.$protocol.port"] = "993"
        properties["mail.$protocol.ssl.enable"] = "true"
        properties["mail.$protocol.statuscachetimeout"] = -1
        properties["mail.$protocol.connectionpoolsize"] = 10
        properties["mail.$protocol.timeout"] = 5000
        properties["mail.$protocol.auth"] = "true"
        protocol = IServer.TYPE_SMTP
        properties["mail.$protocol.host"] = protocol + ".qq.com"
        properties["mail.$protocol.port"] = "465"
        properties["mail.$protocol.statuscachetimeout"] = -1
        properties["mail.$protocol.ssl.enable"] = "true"
        properties["mail.$protocol.auth"] = "true"
        mSession = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(server.e_username, server.e_password)
            }
        }).apply {
            debug = true

            this@Mails.store = getStore(IServer.TYPE_IMAP).apply {
                addStoreListener {
                    println("Store :: ${it.message} ${it.messageType}")
                }
                addFolderListener(object : FolderListener {
                    override fun folderCreated(e: FolderEvent?) {
                        println("folderCreated")
                    }

                    override fun folderRenamed(e: FolderEvent?) {
                        println("folderRenamed")
                    }

                    override fun folderDeleted(e: FolderEvent?) {
                        println("folderDeleted")
                    }

                })
                addConnectionListener(object : ConnectionListener {
                    override fun opened(e: ConnectionEvent?) {
                        println("store opened")
                    }

                    override fun closed(e: ConnectionEvent?) {
                        println("store closed")
                    }

                    override fun disconnected(e: ConnectionEvent?) {
                        println("store disconnected")
                    }

                })

            }

//            this@Mails.transport = getTransport(IServer.TYPE_SMTP).apply {
//                addConnectionListener(object : ConnectionListener {
//                    override fun opened(e: ConnectionEvent?) {
//                        println("transport opened")
//                    }
//
//                    override fun closed(e: ConnectionEvent?) {
//                        println("transport closed")
//                    }
//
//                    override fun disconnected(e: ConnectionEvent?) {
//                        println("transport disconnected")
//                    }
//
//                })
//
//                addTransportListener(object : TransportListener {
//                    override fun messageNotDelivered(e: TransportEvent?) {
//                        println("messageNotDelivered")
//                    }
//
//                    override fun messageDelivered(e: TransportEvent?) {
//                        println("messageDelivered")
//                    }
//
//                    override fun messagePartiallyDelivered(e: TransportEvent?) {
//                        println("messagePartiallyDelivered")
//                    }
//
//                })
//            }
        }
    }

    fun isConnected(): Boolean {
        if (!::store.isInitialized || !store.isConnected)
            return false
//        if (transport == null || !transport.isConnected)
//            return false
        return true
    }

    fun connected(): Boolean {
        try {
            if (!store.isConnected)
                store.connect()
//            if (!transport.isConnected)
//                transport.connect()
            return true
        } catch (a: Exception) {
            a.printStackTrace()
        }
        return false
    }

    fun closeConnected() {
        if (NetUtils.isNetworkAvailable() && store.isConnected)
            store.close()
//        if (transport.isConnected)
//            transport.close()
    }

    fun openFolder(name: String, autoCreate: Boolean = true): Folder {
        return openFolder(store.getFolder(name), autoCreate)
    }

    fun openFolder(folder: Folder, autoCreate: Boolean = true): Folder {
        if (NetUtils.isNetworkAvailable() && (folder.exists() || (autoCreate && folder.create(Folder.HOLDS_MESSAGES)))) {
            if (!folder.isOpen) {
                folder.open(Folder.READ_WRITE)

                folder.addMessageChangedListener { println("message change ... " + it + " -> " + it.messageChangeType) }

                folder.addMessageCountListener(object : MessageCountListener {
                    override fun messagesAdded(e: MessageCountEvent) {
                        println("messagesAdded " + e.messages.size)
                    }

                    override fun messagesRemoved(e: MessageCountEvent) {
                        println("messagesRemoved " + e.messages.size)
                    }
                })
            }
        }
        return folder
    }


    fun closeFolder(folder: Folder) {
        if (NetUtils.isNetworkAvailable() && folder.isOpen)
            folder.close(false)
    }

    fun refreshFolder(folder: Folder): Folder? {
        closeFolder(folder)
        return openFolder(folder)
    }

    fun renameFolder(folder: Folder, name: String) {
        folder.renameTo(folder.parent.getFolder(name))
    }

    fun deleteFolder(folder: Folder) {
        val message = folder.getMessages()
        message.forEach {
            deleteMessage(it)
        }
        closeFolder(folder)
        folder.delete(true)
    }

    fun isGetMessage(): Boolean {
        return (System.currentTimeMillis() - delTim) > 15 * 1000
    }

    fun deleteMessage(msg: Message): Boolean {
        val folder = openFolder(msg.folder)
        if (folder is IMAPFolder) {
            val uid = folder.getUID(msg)
            val messageByUID = folder.getMessageByUID(uid)
            messageByUID.setFlag(Flags.Flag.DELETED, true)
            folder.expunge()
            delTim = System.currentTimeMillis()
            return true
        }
        return false
    }

    fun sendMessage(folder: Folder, msg: IGetData, password: String): MsgBean {
        return sendMessage(folder, parseIGetData2MsgBean(msg, password))
    }

    fun sendMessage(folder: Folder, bean: MsgBean): MsgBean {
        val mimeMessage = MimeMessage(mSession).apply {
            setFrom(InternetAddress("ppx@ppx.com", "TanX"))
            bean.className = folder.name
            setHeader(KEY_HEAD_SUBJECT, MimeUtility.fold(9,
                    MimeUtility.encodeText(bean.title, null, null)))
            setHeader(KEY_HEAD_CONTENT, MimeUtility.fold(9,
                    MimeUtility.encodeText(bean.content, null, null)))
            setHeader(KEY_HEAD_DESCRIBE, MimeUtility.fold(9,
                    MimeUtility.encodeText(bean.dec, null, null)))
            sentDate = bean.sendTime
            setContent("!!!", "text/application")
            //smtpTran.sendMessage(mimeMessage,new Address[]{new InternetAddress(username, "Tan33")});

        }
        bean.uid = (folder as IMAPFolder).uidNext
        folder.appendMessages(arrayOf(mimeMessage))
//        println("新发送的UID：" + (folder as IMAPFolder).getUID(mimeMessage))
        return bean
    }

    fun getFolderList(): Array<out Folder> {
        return store.defaultFolder.list()
    }

    fun getFetchProfile(): FetchProfile {
        val fetchProfile = FetchProfile()
        fetchProfile.add(KEY_HEAD_SUBJECT)
        fetchProfile.add(KEY_HEAD_CONTENT)
        fetchProfile.add(KEY_HEAD_DESCRIBE)
        fetchProfile.add(FetchProfile.Item.FLAGS)
        fetchProfile.add(FetchProfile.Item.ENVELOPE)
        fetchProfile.add(UIDFolder.FetchProfileItem.UID)
        return fetchProfile
    }


}
