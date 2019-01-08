package com.tk.kmail.model.bean

/**
 * Created by TangKai on 2019/1/8.
 */
class TagBean(val tag: String, var text: String = "") {
    var isDeleted: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TagBean

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }


}