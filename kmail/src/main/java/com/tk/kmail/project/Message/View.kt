package com.tk.kmail.project.Message

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tk.kmail.App
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.base.IBase
import com.tk.kmail.model.eventbus.EventBusBean
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.utils.ActivityUtil
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import com.tk.kmail.project.Main.Main4Activity
import kotlinx.android.synthetic.main.include_edittext.view.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_swipe_recyclerview.*

@SuppressLint("ValidFragment")
/**
 * Created by TangKai on 2018/12/27.
 */
class View : BaseFragment<Message.View>() {

    override fun getViewP(): Message.View {
        return object : Message.View, IBase.IViewDialog by getViewDialog() {
            override fun getPassword(): String {
                return pass
            }

            override fun refreshList(list: MutableList<DataBean>) {
                this@View.recyclerView.adapter = MessageAdapter(list, this)
            }

            override fun getPresenter(): Message.Presenter {
                return Presenter(this)
            }

            override fun callResult(result: ResultBean) {
                when (result.type) {
                    Message.TYPE_DELETE -> {
                        Snackbar.make(mContentView!!, "删除：${result.status}", Snackbar.LENGTH_SHORT).show()
                        refresh()
                    }
                    Message.TYPE_PASSERROR -> {
                        if (!result.status) {
                            ToastUtils.show("密码错误！")
                            fragmentManager!!.popBackStack()
                        }
                    }
                }

            }

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.include_swipe_recyclerview
    }

    private lateinit var pass: String

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(getThisContext())
        swipeRefresh.setOnRefreshListener {
            refresh()
            swipeRefresh.isRefreshing = false
        }
        Evs.a.post(EventBusBean.EventMenu(R.menu.add_message) {
            Main4Activity.buildStart(ActivityUtil.build(getThisContext()), mViewP.getPassword()).go()
        })

        AlertDialog.Builder(getThisContext()).apply {
            //                    setMessage()
//                    setTitle("输入密码")
            val v = LayoutInflater.from(context).inflate(R.layout.include_edittext, mContentView as ViewGroup, false)


            v.tiet_text.setHint("输入密码")
            v.tv_title.text = "请输入密码："
            setView(v)

            setPositiveButton("确定") { a, b ->
                pass = v.tiet_text.text.toString()
                refresh()
                a.dismiss()
            }
            setNegativeButton("取消") { a, b ->
                a.dismiss()
                fragmentManager!!.popBackStack()
            }

            setCancelable(false)

        }.show()

    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    private fun refresh() {
        if (!::pass.isInitialized)
            return
        if (App.mails == null) {
            ToastUtils.show("还未登录...")
            return
        }
        mViewP.mPresenter.refreshList(mViewP.mPresenter.getFolder())
    }

    override fun recycler() {
        Evs.a.removeStickyEvent(mViewP::javaClass)
        super.recycler()
    }

}