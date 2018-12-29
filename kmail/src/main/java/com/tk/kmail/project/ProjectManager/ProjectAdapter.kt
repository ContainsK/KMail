package com.tk.kmail.project.ProjectManager

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.tk.kmail.R
import com.tk.kmail.base.BaseViewHolder
import com.tk.kmail.databinding.LayoutItemMessagesBinding
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.project.Message.Add.View

/**
 * Created by TangKai on 2018/12/27.
 */
class ProjectAdapter(var list: MutableList<DataBean>) : RecyclerView.Adapter<BaseViewHolder<LayoutItemMessagesBinding>>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<LayoutItemMessagesBinding> {
        return BaseViewHolder(p0, R.layout.layout_item_messages)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: BaseViewHolder<LayoutItemMessagesBinding>, p1: Int) {
        p0.binding.data = list[p1]
        p0.itemView.setOnClickListener {

            it.context.startActivity(Intent(it.context, View::class.java))
        }
//                                p0.itemView.apply { tv_title.text = "2" }
    }

}