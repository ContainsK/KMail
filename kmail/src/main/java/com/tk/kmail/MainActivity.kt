package com.tk.kmail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tk.kmail.model.mails.ServerConfig
import com.tk.kmail.model.mails.Mails
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    lateinit var mails: Mails
    lateinit var serverConfig: ServerConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_setting)
        val username = "fgbqahzuk46430@qq.com"
        val password = "xfdtpevgqgvpjdeg"

        serverConfig = ServerConfig(username, password)
        Observable.just(serverConfig)
                .map {
                    println("准备连接中.....")
                    mails = Mails(it)
                    mails.connected()

                    true
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    println("连接成功.....")
                    supportActionBar?.setTitle("连接服务器：" + it) ?: println("supportActionBar is null")

                }





    }
}
