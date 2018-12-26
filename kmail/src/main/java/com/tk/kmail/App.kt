package com.tk.kmail

import android.app.Application
import com.kmail.greendao.gen.DaoMaster
import com.kmail.greendao.gen.DaoSession
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.mails.ServerConfig
import com.tk.kmail.model.utils.ToastUtils

/**
 * Created by TangKai on 2018/12/18.
 */
class App : Application() {
    companion object {
        lateinit var daoSession: DaoSession
        lateinit var context: App
        var userConfig: ServerConfig? = null
        var mails: Mails? = null
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