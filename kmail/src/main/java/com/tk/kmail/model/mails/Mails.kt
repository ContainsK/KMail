package com.tk.kmail.model.mails

import com.sun.mail.imap.IMAPFolder
import com.tk.kmail.model.utils.DesUtil
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
    lateinit var transport: Transport
    val KEY_HEAD_CONTENT = "MK-CONTENT"
    val KEY_HEAD_DESCRIBE = "MK-DESCRIBE"

    init {
        val properties = Properties()
        var protocol = IServer.TYPE_IMAP
        properties["mail.$protocol.host"] = protocol + ".qq.com"
        properties["mail.$protocol.port"] = "993"
        properties["mail.$protocol.ssl.enable"] = "true"
        properties["mail.$protocol.statuscachetimeout"] = -1
        properties["mail.$protocol.connectionpoolsize"] = 10
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

            this@Mails.transport = getTransport(IServer.TYPE_SMTP).apply {
                addConnectionListener(object : ConnectionListener {
                    override fun opened(e: ConnectionEvent?) {
                        println("transport opened")
                    }

                    override fun closed(e: ConnectionEvent?) {
                        println("transport closed")
                    }

                    override fun disconnected(e: ConnectionEvent?) {
                        println("transport disconnected")
                    }

                })

                addTransportListener(object : TransportListener {
                    override fun messageNotDelivered(e: TransportEvent?) {
                        println("messageNotDelivered")
                    }

                    override fun messageDelivered(e: TransportEvent?) {
                        println("messageDelivered")
                    }

                    override fun messagePartiallyDelivered(e: TransportEvent?) {
                        println("messagePartiallyDelivered")
                    }

                })
            }
        }
    }

    fun connected(): Boolean {
        try {
            if (!store.isConnected)
                store.connect()
            if (!transport.isConnected)
                transport.connect()
            return true
        } catch (a: Exception) {
            a.printStackTrace()
        }
        return false
    }

    fun closeConnected() {
        if (store.isConnected)
            store.close()
        if (transport.isConnected)
            transport.close()
    }

    fun openFolder(name: String): Folder? {
        return openFolder(store.getFolder(name))
    }

    fun openFolder(folder: Folder): Folder? {
        return openFolder(folder, true)
    }

    fun openFolder(folder: Folder, autoCreate: Boolean): Folder? {
        if (folder.exists() || (autoCreate && folder.create(Folder.HOLDS_MESSAGES))) {
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

            return folder
        }
        return null
    }


    fun closeFolder(folder: Folder) {
        if (folder.isOpen)
            folder.close(true)
    }

    fun renameFolder(folder: Folder, name: String) {
        folder.renameTo(folder.parent.getFolder(name))
    }

    fun deleteFolder(folder: Folder) {
        val message = folder.getMessages()
        message.forEach {
            deleteMessage(it)
        }
        folder.delete(true)
    }

    fun deleteMessage(msg: Message): Boolean {
        val folder = openFolder(msg.folder)
        if (folder is IMAPFolder) {
            val uid = folder.getUID(msg)
            val messageByUID = folder.getMessageByUID(uid)
            messageByUID.setFlag(Flags.Flag.DELETED, true)
            folder.expunge()
            return true
        }
        return false
    }

    fun sendMessage(folder: Folder, msg: IGetData) {
        val mimeMessage = MimeMessage(mSession).apply {
            setFrom(InternetAddress("ppx@ppx.com", "TanX"))
            //mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(username));
            setSubject(msg.msgTitle)
            setHeader(KEY_HEAD_CONTENT, MimeUtility.fold(9,
                    MimeUtility.encodeText(msg.msgContent, null, null)))
            setHeader(KEY_HEAD_DESCRIBE, MimeUtility.fold(9,
                    MimeUtility.encodeText(msg.msgDescribe, null, null)))
            setSentDate(Date())
            setContent(DesUtil.encrypt("who are you ?"), "text/application")
            //smtpTran.sendMessage(mimeMessage,new Address[]{new InternetAddress(username, "Tan33")});

        }
        folder.appendMessages(arrayOf(mimeMessage))
        println("新发送的UID：" + (folder as IMAPFolder).getUID(mimeMessage))
    }

    fun getFetchProfile(): FetchProfile {
        val fetchProfile = FetchProfile()
        fetchProfile.add("Subject")
        fetchProfile.add(KEY_HEAD_CONTENT)
        fetchProfile.add(KEY_HEAD_DESCRIBE)
        fetchProfile.add(FetchProfile.Item.FLAGS)
        fetchProfile.add(FetchProfile.Item.ENVELOPE)
        fetchProfile.add(UIDFolder.FetchProfileItem.UID)

        return fetchProfile
    }

    fun parseMessage(msg: Message): DataBean {
        val folder = msg.folder as IMAPFolder
        val dataBean = DataBean().apply {
            title = msg.subject
            content = MimeUtility.decodeText(MimeUtility.unfold(msg.getHeader(KEY_HEAD_CONTENT)[0]))
            dec = MimeUtility.decodeText(MimeUtility.unfold(msg.getHeader(KEY_HEAD_DESCRIBE)[0]))
            sendTime = msg.getReceivedDate().toString()
            this.msg = msg
        }
        return dataBean
    }
}