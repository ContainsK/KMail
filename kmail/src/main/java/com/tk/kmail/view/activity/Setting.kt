package com.tk.kmail.view.activity

import com.tk.kmail.R
import com.tk.kmail.base.BaseActivity
import com.tk.kmail.base.BasePresenter
import com.tk.kmail.mvp.Setting

class Setting : BaseActivity(), Setting.View {
    override fun getPresenter(): Setting.Presenter {
        return Presenter(this)
    }

    override fun getLayoutId(): Int {

        return R.layout.layout_setting
    }

    override fun initView() {



    }



}

class Presenter(mView: Setting.View) : BasePresenter<Setting.View>(mView), Setting.Presenter {
    override fun saveUser(username: String, password: String) {
    }

}
