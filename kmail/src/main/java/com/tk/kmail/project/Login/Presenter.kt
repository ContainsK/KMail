package com.tk.kmail.project.Login

import com.tk.kmail.App
import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.mails.ServerConfig
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.mvp.Login
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.Subscribe

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: Login.View) : Login.Presenter {
    override fun loginOut(): Observable<*> {
        if (App.mails != null) {
            return Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { mView.showWaitingDialog("正在退出...") }
                    .observeOn(Schedulers.io())
                    .map {
                        App.mails?.closeConnected() ?: println("还未登录...")
                        App.mails = null
                    }
                    .observeOn(AndroidSchedulers.mainThread()).map {
                        mView.hideWaitingDialog()
                    }
        }

        return Observable.just(1)
    }


    @Subscribe
    override fun login(bean: UserBean) {
        Evs.a.cancelEventDelivery(bean)
        loginOut()
                .map {
                    mView.showWaitingDialog("登录中，请稍候...")
                }
                .observeOn(Schedulers.io())
                .map {
                    App.userConfig = ServerConfig(bean.username, bean.password)
                    App.mails = Mails(App.userConfig!!)
                    App.mails!!.connected()
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.loginResult(bean, it)
                }, {
                    it.printStackTrace()
                    mView.loginResult(bean, false)
                }, {
                    mView.hideWaitingDialog()
                })
    }
}