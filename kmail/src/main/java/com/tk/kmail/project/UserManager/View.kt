package com.tk.kmail.project.UserManager

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.databinding.LayoutAddUserBinding
import com.tk.kmail.databinding.LayoutSettingBinding
import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.model.eventbus.EventBusBean
import com.tk.kmail.model.utils.BindingUtils
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.UserManager
import com.tk.kmail.mvp.base.ResultBean
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.layout_setting.*

/**
 * Created by TangKai on 2018/12/27.
 */
class View : BaseFragment<UserManager.View>(), UserManager.View {
    override fun getViewP(): UserManager.View {
        return object : UserManager.View by this {}
    }

    override fun callResult(result: ResultBean) {
        val ww = fun(a: Boolean): String = if (a) "成功" else "失败"
        val bl = result.getAResult<Any>() != null
        val str = when (result.type) {
            UserManager.TYPE_ADD -> {
                mPresenter.refreshUserList()
                "添加${ww(bl)}.."
            }
            UserManager.TYPE_DELETE -> {
                "删除${ww(bl)}.."
            }
            UserManager.TYPE_UPDATE -> {
                "修改${ww(bl)}.."
            }
            else -> {
                ""
            }
        }
        ToastUtils.show(str)

    }

    override fun refreshUserList(arr: List<UserBean>) {
        var adapter: UserAdapter = recyclerView.adapter as UserAdapter
        adapter.list = arr.toMutableList()
        adapter.notifyDataSetChanged()
    }


    lateinit var binding: LayoutSettingBinding
    override fun getPresenter(): UserManager.Presenter {
        return Presenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_setting
    }

    override fun initView() {
        binding = DataBindingUtil.bind(mContentView)
//        binding.user = UserBean()
        recyclerView.layoutManager = LinearLayoutManager(getThisContext())
        recyclerView.adapter = UserAdapter(mPresenter.getUserList().toMutableList(), mPresenter)
//        til_password.isPasswordVisibilityToggleEnabled = true
        swipeRefresh.setOnRefreshListener {
            mPresenter.refreshUserList()
            swipeRefresh.isRefreshing = false
        }
//        binding.floatingActionButton.setOnClickListener {
//            mPresenter.addUser(binding.user!!)
//            mPresenter.refreshUserList()
//
//        }
//        println(mContentView)
//        App.daoSession.userBeanDao.loadAll()

        Evs.a.post(EventBusBean.EventMenu(R.menu.add_user) {
            Dialog(getThisContext(), R.style.CsDialog).apply {
                val bind = BindingUtils.bind<LayoutAddUserBinding>(context, R.layout.layout_add_user)
                val b = UserBean()
                bind.user = b
                bind.tilPassword.isPasswordVisibilityToggleEnabled = true
                setContentView(bind.root)
                show()
                window.attributes = window.attributes.apply { width = mContentView!!.width }

                bind.floatingActionButton.setOnClickListener {
                    mViewP.mPresenter.addUser(bind.user!!)
                    dismiss()
                }

            }
        })
    }


}