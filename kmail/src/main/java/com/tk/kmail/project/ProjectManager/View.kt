package com.tk.kmail.project.ProjectManager

import android.annotation.SuppressLint
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import com.tk.kmail.App
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.base.IBase
import com.tk.kmail.model.eventbus.EventBusBean
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.ProjectMng
import com.tk.kmail.mvp.base.ResultBean
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_swipe_recyclerview.*
import javax.mail.Folder

@SuppressLint("ValidFragment")
/**
 * Created by TangKai on 2018/12/27.
 */
class View : BaseFragment<ProjectMng.View>() {
    override fun getViewP(): ProjectMng.View {
        return object : ProjectMng.View, IBase.IViewDialog by getViewDialog() {
            override fun refreshList(list: MutableList<Folder>) {
                this@View.recyclerView.adapter = PAdapter(list, this@View)
            }


            override fun getPresenter(): ProjectMng.Presenter {
                return Presenter(this)
            }

            override fun callResult(result: ResultBean) {
                when (result.type) {
                    ProjectMng.TYPE_PROJECT_ADD -> {
                        Snackbar.make(mContentView!!, "创建：${result.status}", Snackbar.LENGTH_SHORT).show()
                        refresh()
                    }
                    ProjectMng.TYPE_PROJECT_DELETE -> {
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
        refresh()
        Evs.a.post(EventBusBean.EventMenu(R.menu.add_project) {
            AlertDialog.Builder(getThisContext()!!).apply {
                val v = EditText(getThisContext())
                setView(v)
                setTitle("创建新的项目：")
                setPositiveButton("确定") { a, b ->
                    a.dismiss()
                    mViewP.mPresenter.createFolder(v.text.toString())
                }
            }.show()
        })
    }

    private fun refresh() {
        if (App.mails == null) {
            ToastUtils.show("还未登录...")
            return
        }
        mViewP.mPresenter.refreshList()
    }

    override fun recycler() {
        Evs.a.removeStickyEvent(Folder::javaClass)
        super.recycler()
    }

}