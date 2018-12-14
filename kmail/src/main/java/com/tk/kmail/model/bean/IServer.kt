package com.tk.kmail.model.bean

/**
 * Created by TangKai on 2018/12/4.
 */

interface IServer {

    val e_username: String

    val e_password: String

    val e_host: String

    val e_prot: String

    companion object {
        val TYPE_POP3 = "pop3"
        val TYPE_SMTP = "smtp"
        val TYPE_IMAP = "imap"
    }
}
