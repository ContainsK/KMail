package com.tk.kmail.model.eventbus

import android.view.MenuItem

/**
 * Created by TangKai on 2019/1/2.
 */
class EventBusBean private constructor() {

    open class EventMenu(val menuId: Int, val callBack: (item: MenuItem) -> Unit = {})

}