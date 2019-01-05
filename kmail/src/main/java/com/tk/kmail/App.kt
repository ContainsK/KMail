package com.tk.kmail

import android.app.Application
import com.kmail.greendao.gen.DaoMaster
import com.kmail.greendao.gen.DaoSession
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.mails.ServerConfig
import com.tk.kmail.model.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by TangKai on 2018/12/18.
 */
class App : Application() {
    companion object {
        lateinit var daoSession: DaoSession
        lateinit var context: App
        var userConfig: ServerConfig? = null
        var mails: Mails? = null
        private var subscribeKeep: Disposable? = null


        fun startKeep() {
            if (mails == null)
                return
            stopKeep()
            println("开启Keep")
            subscribeKeep = Observable.interval(0, 120, TimeUnit.SECONDS)
//                    .map { println("${mails!!.store.defaultFolder.exists()}  .... keep ...") }.subscribeOn(Schedulers.io()).subscribe()
                    .map { println(".... keep ...") }.subscribeOn(Schedulers.io()).subscribe()
        }

        fun stopKeep() {
            if (subscribeKeep != null && !subscribeKeep!!.isDisposed) {
                subscribeKeep!!.dispose()
                subscribeKeep = null
                println("关闭Keep")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        ToastUtils.instance(this)
        initDao()
        context = this
    }

    private fun initDao() {
        val devOpenHelper = DaoMaster.DevOpenHelper(this, "app_data")
        daoSession = DaoMaster(devOpenHelper.writableDb).newSession()
    }


}