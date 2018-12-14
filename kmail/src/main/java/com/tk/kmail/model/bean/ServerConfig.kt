package com.tk.kmail.model.bean

/**
 * Created by TangKai on 2018/12/4.
 */

class ServerConfig(override val e_host: String, override val e_prot: String, override val e_username: String, override val e_password: String) : IServer {

    constructor(e_username: String, e_password: String) : this("imap.qq.com", "465", e_username, e_password)


}
