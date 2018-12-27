package com.tk.kmail.project.ProjectManager

import android.support.v7.widget.LinearLayoutManager
import com.tk.kmail.App
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.ProjectMng
import com.tk.kmail.mvp.base.ResuleBean
import io.reactivex.Observable
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_swipe_recyclerview.*

/**
 * Created by TangKai on 2018/12/27.
 */
class View : BaseFragment(), ProjectMng.View {
    override fun callResult(result: ResuleBean) {
    }

    override fun refreshList(list: MutableList<DataBean>) {
        recyclerView.adapter = ProjectAdapter(list)
    }

    override fun getPresenter(): ProjectMng.Presenter {
        return Presenter(this)
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
        refresh()
    }

    private fun refresh() {
        if (App.mails == null) {
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
                        refreshList(it)
                    }.subscribe({}, {
                        it.printStackTrace()
                        hideWaitingDialog()
                    }, {
                        hideWaitingDialog()
                    })
        }
    }

}