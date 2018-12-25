package com.tk.kmail.view.fragment

import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import com.tk.kmail.R
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.databinding.LayoutSettingBinding
import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.UserManager
import com.tk.kmail.view.activity.Presenter
import com.tk.kmail.view.adapter.UserAdapter
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.layout_setting.*

/**
 * Created by TangKai on 2018/12/21.
 */
class UserManage : BaseFragment(), UserManager.View {
    override fun refreshUserList(arr: List<UserBean>) {
        var adapter: UserAdapter = recyclerView.adapter as UserAdapter
        adapter.list = arr.toMutableList()
        adapter.notifyDataSetChanged()
    }

    override fun addResult(isOk: Boolean) {
        if (isOk) {
            binding.user = UserBean()
            ToastUtils.show("添加成功..")
        }
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
        binding.user = UserBean()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = UserAdapter(mPresenter.getUserList().toMutableList(), mPresenter)
        til_password.isPasswordVisibilityToggleEnabled = true
        swipeRefresh.setOnRefreshListener {
            mPresenter.refreshUserList()
            swipeRefresh.isRefreshing = false
        }
        binding.floatingActionButton.setOnClickListener {
            mPresenter.addUser(binding.user!!)
            mPresenter.refreshUserList()

        }

//        println(mContentView)
//        App.daoSession.userBeanDao.loadAll()
    }


}



