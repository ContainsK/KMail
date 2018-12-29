package com.tk.kmail.model.mails

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by TangKai on 2018/12/4.
 */

interface IGetData {

    //    fun getMsgId(): String
    fun getMsgContent(): String

    fun getMsgDescribe(): String
    fun getMsgTitle(): String
//    fun getMsgTime(): String
//    fun getMsgFrom(): String


}
