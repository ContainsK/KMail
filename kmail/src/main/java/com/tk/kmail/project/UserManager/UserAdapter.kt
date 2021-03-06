package com.tk.kmail.project.UserManager

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.tk.kmail.R
import com.tk.kmail.base.BaseViewHolder
import com.tk.kmail.databinding.LayoutItemUsersBinding
import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.mvp.UserManager

/**
 * Created by TangKai on 2018/12/19.
 */
class UserAdapter(var list: MutableList<UserBean>, val p: UserManager.Presenter) : RecyclerView.Adapter<BaseViewHolder<LayoutItemUsersBinding>>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<LayoutItemUsersBinding> {
        return BaseViewHolder(layoutId = R.layout.layout_item_users, group = p0)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: BaseViewHolder<LayoutItemUsersBinding>, p1: Int) {
        p0.binding.user = list.get(p1)

        p0.binding.btnDelete.setOnClickListener {
            p.deleteUser(p0.binding.user!!)
            list.removeAt(p0.layoutPosition)
            notifyItemRemoved(p0.layoutPosition)

        }
        p0.binding.btnLogin.setOnClickListener {
            Evs.a.post(p0.binding.user)
        }
    }


}


