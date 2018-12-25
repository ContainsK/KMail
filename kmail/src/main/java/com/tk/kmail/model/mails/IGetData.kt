package com.tk.kmail.model.mails

/**
 * Created by TangKai on 2018/12/4.
 */

interface IGetData {
    val msgId: String

    val msgContent: String

    val msgDescribe: String

    val msgTitle: String

    val msgTime: String

    val msgFrom: String
}
