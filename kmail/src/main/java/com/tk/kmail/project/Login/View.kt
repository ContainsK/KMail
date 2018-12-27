package com.tk.kmail.project.Login

import com.tk.kmail.mvp.Login

/**
 * Created by TangKai on 2018/12/27.
 */
abstract class View : Login.View {
    override fun getPresenter(): Login.Presenter {
        return Presenter(this)
    }
}