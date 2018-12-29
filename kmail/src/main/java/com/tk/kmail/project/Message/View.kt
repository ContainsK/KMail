package com.tk.kmail.project.Message

import android.annotation.SuppressLint
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.tk.kmail.App
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.base.IBase
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_swipe_recyclerview.*

@SuppressLint("ValidFragment")
/**
 * Created by TangKai on 2018/12/27.
 */
class View : BaseFragment<Message.View>() {

    override fun getViewP(): Message.View {
        return object : Message.View, IBase.IViewDialog by getViewDialog() {
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
                }

            }

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.include_swipe_recyclerview
    }

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(getThisContext())
        swipeRefresh.setOnRefreshListener {
            refresh()
            swipeRefresh.isRefreshing = false
        }

    }

    override fun onStart() {
        super.onStart()
        refresh()
    }

    private fun refresh() {
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