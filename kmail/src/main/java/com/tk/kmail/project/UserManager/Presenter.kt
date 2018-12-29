package com.tk.kmail.project.UserManager

import com.kmail.greendao.gen.UserBeanDao
import com.tk.kmail.App
import com.tk.kmail.base.BasePresenter
import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.mvp.UserManager
import com.tk.kmail.mvp.base.ResultBean

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(mView: UserManager.View) : BasePresenter<UserManager.View>(mView), UserManager.Presenter {
    val daoBean: UserBeanDao = App.daoSession.userBeanDao
    override fun refreshUserList() {
        mView.refreshUserList(getUserList())
    }

    override fun deleteUser(user: UserBean) {
        daoBean.delete(user)
        mView.callResult(ResultBean(UserManager.TYPE_DELETE, result = user))
    }

    override fun getUserList(): List<UserBean> {
        return daoBean.loadAll()
    }

    override fun addUser(user: UserBean) {
        daoBean.save(user)
        mView.callResult(ResultBean(UserManager.TYPE_ADD, result = user))
    }
}