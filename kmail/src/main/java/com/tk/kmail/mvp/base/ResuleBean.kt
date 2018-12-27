package com.tk.kmail.mvp.base

/**
 * Created by TangKai on 2018/12/27.
 */
class ResuleBean(val type: Int, private val result: Any?) {
    fun <T> getAResult(): T? {
        return result as T
    }
}