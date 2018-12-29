package com.tk.kmail.project.ProjectManager

import com.tk.kmail.App
import com.tk.kmail.mvp.ProjectMng
import io.reactivex.Observable
import javax.mail.Folder

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: ProjectMng.View) : ProjectMng.Presenter {
    override fun createFolder(name: String) {
    }

    override fun deleteFolder(name: String) {
    }



    override fun refreshList() {
        val email = App.mails!!
        Observable.just(1)
                .runUI {
                    mView.showWaitingDialog()
                }.runIO {
                    email.getFolderList()
                }
                .runUI {
                    mView.refreshList(it.toMutableList())
                }.subscribe({}, {
                    it.printStackTrace()
                    mView.hideWaitingDialog()
                }, {
                    mView.hideWaitingDialog()
                })
    }


}