package com.tk.kmail.project.ProjectManager

import com.tk.kmail.App
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.mvp.ProjectMng
import com.tk.kmail.mvp.ProjectMng.Companion.TYPE_PROJECT_ADD
import com.tk.kmail.mvp.ProjectMng.Companion.TYPE_PROJECT_DELETE
import com.tk.kmail.mvp.base.ResultBean

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: ProjectMng.View) : ProjectMng.Presenter {
    override fun createFolder(name: String) {
        mView.runDialog("创建项目中...") {
            App.mails!!.openFolder(Mails.PROJECT_NAME_S + name) != null
        }.doOnNext { mView.callResult(ResultBean(TYPE_PROJECT_ADD, it, null)) }
                .doOnError {
                    it.printStackTrace()
                    mView.callResult(ResultBean(TYPE_PROJECT_ADD, false, null))
                }.subscribe()
    }

    override fun deleteFolder(name: String) {
        mView.runDialog("删除项目中...") {
            val f = App.mails!!.openFolder(name)
            if (f != null) {
                App.mails!!.deleteFolder(f)
            }
            true
        }.doOnNext { mView.callResult(ResultBean(TYPE_PROJECT_DELETE, it, null)) }
                .doOnError {
                    it.printStackTrace()
                    mView.callResult(ResultBean(TYPE_PROJECT_DELETE, false, null))
                }.subscribe()
    }


    override fun refreshList() {
        val email = App.mails!!
        mView.runDialog {
            val vs = email.isConnected()
            println(vs)
            email.getFolderList()
        }.subscribe {
            mView.refreshList(it.filter {
                println(it.name)
                it.name.startsWith(Mails.PROJECT_NAME_S)
            }.toMutableList())
        }

    }


}