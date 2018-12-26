package com.tk.kmail.view.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sun.mail.imap.IMAPMessage
import com.tk.kmail.App
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.databinding.LayoutItemMessagesBinding
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.Project
import com.tk.kmail.view.adapter.BaseViewHolder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_swipe_recyclerview.*

/**
 * Created by TangKai on 2018/12/25.
 */
class Project : BaseFragment(), Project.View {
    override fun refreshList() {
        if (App.mails == null) {
            println(Thread.currentThread())
            ToastUtils.show("还未登录...")
            return
        }
        getViewDialog().apply {
            Observable.just(1)
                    .runUI {
                        showWaitingDialog()
                    }.runIO {
                        mPresenter.getMessageArrs()
                    }
                    .runUI {
                        recyclerView.adapter = object : RecyclerView.Adapter<BaseViewHolder<LayoutItemMessagesBinding>>() {
                            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<LayoutItemMessagesBinding> {
                                return BaseViewHolder(p0, R.layout.layout_item_messages)
                            }

                            override fun getItemCount(): Int {
                                return it.size
                            }

                            override fun onBindViewHolder(p0: BaseViewHolder<LayoutItemMessagesBinding>, p1: Int) {
                                p0.binding.data = it[p1]
//                                p0.itemView.apply { tv_title.text = "2" }
                            }

                        }

                        hideWaitingDialog()
                    }.subscribe()
        }
    }

    override fun getPresenter(): Project.Presenter {
        return Presenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.include_swipe_recyclerview
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefresh.setOnRefreshListener {
            refreshList()
            swipeRefresh.isRefreshing = false
        }
        refreshList()


    }

}

class Presenter(override val mView: Project.View) : Project.Presenter {

    override fun getMessageArrs(): MutableList<DataBean> {
        val email = App.mails!!
        val dataBeans = mutableListOf<DataBean>()
        val abcd = email.openFolder("Abcd")
        if (abcd != null) {
            val messages = abcd.getMessages()
            abcd.fetch(messages, email.getFetchProfile())

            for (v in messages) {
                val msg = v as IMAPMessage
                println(msg.flags.toString() + " flags")
                val dataBean = email.parseMessage(msg)
                println(dataBean)
                dataBeans.add(dataBean)
            }
        }
        return dataBeans
    }

}