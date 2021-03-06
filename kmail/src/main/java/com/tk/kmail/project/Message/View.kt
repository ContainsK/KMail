package com.tk.kmail.project.Message

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.base.BaseViewHolder
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
    val count = 10
    var start = 1
    lateinit var adapter: MessageAdapter
    override fun getViewP(): Message.View {
        return object : Message.View, IBase.IViewDialog by getViewDialog() {


            override fun getPassword(): String {
                return pass
            }

            override fun refreshList(list: MutableList<DataBean>) {

                if (!::adapter.isInitialized) {
                    adapter = MessageAdapter(list, this)
                    adapter.setOnViewTachedListener(object : MessageAdapter.Companion.OnViewTachedListener {

                        override fun onViewDetached(adapter: RecyclerView.Adapter<*>, holder: BaseViewHolder<*>) {
                        }

                        override fun onViewAttached(adapter: RecyclerView.Adapter<*>, holder: BaseViewHolder<*>) {
                            if (holder.layoutPosition > adapter.itemCount - 2) {
                                refresh(true)
                            }
                        }

                    })
                    this@View.recyclerView.adapter = adapter
                } else {
                    if (adapter.itemCount == 0) {
                        adapter.list.addAll(list)
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter.list.addAll(list)
                        adapter.notifyItemRangeChanged(start, count)
                    }


                }

                start += list.size
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
//        recyclerView.onFlingListener = object : RecyclerView.OnFlingListener() {
//            override fun onFling(p0: Int, p1: Int): Boolean {
//                print("$p0 - $p1")
//                return true
//            }
//
//        }
//        recyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
//            override fun onChildViewDetachedFromWindow(p0: View) {
//                println("onChildViewDetachedFromWindow $p0")
//            }
//
//            override fun onChildViewAttachedToWindow(p0: View) {
//                println("onChildViewAttachedToWindow $p0")
//            }
//
//        })

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
            v.tiet_text.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            v.til_layout.isPasswordVisibilityToggleEnabled = true

            v.til_layout.layoutParams = v.til_layout.layoutParams
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

    private fun refresh(isMore: Boolean = false) {
        println("refresh")
        if (!::pass.isInitialized)
            return
//        if (App.mails == null) {
//            ToastUtils.show("还未登录...")
//            return
//        }
        if (!isMore) {
            if (::adapter.isInitialized)
                adapter.list.clear()
            start = 1
        }
        mViewP.mPresenter.refreshList(mViewP.mPresenter.getClassBean().name, start, count)
    }

    override fun recycler() {
        Evs.a.removeStickyEvent(mViewP::javaClass)
        super.recycler()
    }

}