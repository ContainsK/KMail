package com.tk.kmail.model.bean

/**
 * 该类是消息内容标签，将它格式化json传给MessageDataBean
 * Created by TangKai on 2019/1/8.
 */
class MessageTagBean(val tag: String, var text: String = "") {
    var isDeleted: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageTagBean

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }


}