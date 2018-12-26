package com.tk.kmail.view

import com.tk.kmail.App.Companion.mails
import com.tk.kmail.App.Companion.userConfig
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
 * Created by TangKai on 2018/12/24.
 */
interface LoginView : Login.View {
    override fun getPresenter(): Login.Presenter {
        return LoginPresenter(this)
    }
}

class LoginPresenter(override val mView: Login.View) : Login.Presenter {
    override fun loginOut(): Observable<*> {
        if (mails != null) {
            return Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { mView.showWaitingDialog("正在退出...") }
                    .observeOn(Schedulers.io())
                    .map {
                        mails?.closeConnected() ?: println("还未登录...")
                        mails = null
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
                    userConfig = ServerConfig(bean.username, bean.password)
                    mails = Mails(userConfig!!)
                    mails!!.connected()
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