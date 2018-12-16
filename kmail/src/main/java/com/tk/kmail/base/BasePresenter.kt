package com.tk.kmail.base

import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
open class BasePresenter<T : IBase.View<*>>(override val mView: T) : IBase.Presenter<T>