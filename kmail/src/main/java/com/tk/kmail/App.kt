package com.tk.kmail

import android.app.Application
import com.kmail.greendao.gen.ClassBeanDao
import com.kmail.greendao.gen.DaoMaster
import com.kmail.greendao.gen.DaoSession
import com.kmail.greendao.gen.MsgBeanDao
import com.sun.mail.imap.IMAPFolder
import com.tk.kmail.model.db_bean.Flag
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.mails.ServerConfig
import com.tk.kmail.model.utils.NetUtils
import com.tk.kmail.model.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.mail.search.HeaderTerm

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
            subscribeKeep = Observable.interval(0, 2, TimeUnit.SECONDS)
//                    .map { println("${mails!!.store.defaultFolder.exists()}  .... keep ...") }.subscribeOn(Schedulers.io()).subscribe()
                    .map {

                        println(".... keep ...")
                        if (!NetUtils.isNetworkAvailable())
                            return@map
                        val mails = mails!!
                        if (!mails.isConnected() && !mails.connected())
                            return@map

                        // TODO  检测是否有待删除、待添加 的数据，更新到服务器以后修改本地flag


                        daoSession.msgBeanDao.queryBuilder().where(MsgBeanDao.Properties.Flag.notEq(Flag.FLAG_DEFAULT)).list()
                                .forEach {
                                    if (it.flag == Flag.FLAG_CREATE) {
                                        println("添加msg $it")
                                        val folder = mails.openFolder(it.className)
                                        if (folder.exists()) {
                                            val b = mails.sendMessage(folder, it)
                                            b.flag = Flag.FLAG_DEFAULT
                                            App.daoSession.msgBeanDao.save(b)
                                        } else {
                                            App.daoSession.msgBeanDao.delete(it)
                                        }
                                    } else if (it.flag == Flag.FLAG_DELETE) {
                                        println("删除msg $it")
                                        val folder = mails.openFolder(it.className, false)
                                        if (folder.exists()) {
                                            if (mails.deleteMessage((folder as IMAPFolder).getMessageByUID(it.uid))) {
                                                App.daoSession.msgBeanDao.delete(it)
                                            }
                                        }

                                    }
                                }

                        daoSession.classBeanDao.queryBuilder().where(ClassBeanDao.Properties.Flag.notEq(Flag.FLAG_DEFAULT)).list()
                                .forEach {
                                    val f = mails.openFolder(it.name)
                                    if (it.flag == Flag.FLAG_CREATE) {
                                        println("添加f $it")
                                        if (f.exists())
                                            App.daoSession.classBeanDao.save(it.apply { flag = Flag.FLAG_DEFAULT })
                                    } else if (it.flag == Flag.FLAG_DELETE) {
                                        println("删除f $it")
                                        var b = true
                                        if (f.exists())
                                            b = f.delete(true)
                                        if (b)
                                            App.daoSession.classBeanDao.delete(it)
                                    }
                                }


                    }.subscribeOn(Schedulers.io()).subscribe()
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
        RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }
    }

    private fun initDao() {
        val devOpenHelper = DaoMaster.DevOpenHelper(this, "app_data")
        daoSession = DaoMaster(devOpenHelper.writableDb).newSession()
    }


}