package com.tk.kmail.project.Message.Add

import com.tk.kmail.App
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.Subscribe
import javax.mail.Folder

/**
 * Created by TangKai on 2018/12/29.
 */
class Presenter(override val mView: Message.AddMessageView) : Message.AddMessagePresenter {
    override fun sendMessage(folder: Folder, bean: IGetData, password: String) {
        Observable.just(1)
                .runUI {
                    mView.showWaitingDialog("保存中，请稍候...")
                }.runIO {
                    val app = App.mails!!
                    app.sendMessage(app.openFolder(folder)!!, bean, password)
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.hideWaitingDialog()
                    mView.callResult(ResultBean(0, true, result = "保存成功！"))
                }, {
                    it.printStackTrace()
                    mView.hideWaitingDialog()
                    mView.callResult(ResultBean(0, false, result = "保存失败！"))
                })


    }

    private lateinit var folder: Folder

    init {
        Evs.reg(this)
    }

    override fun getFolder(): Folder {
        return folder
    }

    @Subscribe(sticky = true)
    override fun setFolder(folder: Folder) {
        this.folder = folder
    }

}