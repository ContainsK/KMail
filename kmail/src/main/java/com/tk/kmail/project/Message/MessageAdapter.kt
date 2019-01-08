package com.tk.kmail.project.Message

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.tk.kmail.R
import com.tk.kmail.base.BaseViewHolder
import com.tk.kmail.databinding.LayoutItemMessagesBinding
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.utils.ActivityUtil
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.mvp.Message
import com.tk.kmail.project.Main.Main4Activity

/**
 * Created by TangKai on 2018/12/27.
 */
class MessageAdapter(var list: MutableList<DataBean>, val vp: Message.View) : RecyclerView.Adapter<BaseViewHolder<LayoutItemMessagesBinding>>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<LayoutItemMessagesBinding> {
        return BaseViewHolder(p0, R.layout.layout_item_messages)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: BaseViewHolder<LayoutItemMessagesBinding>, p1: Int) {
        p0.binding.data = list[p1]
        p0.itemView.setOnClickListener {
            Evs.a.postSticky(list[p1])
            Main4Activity.buildStart(ActivityUtil.build(it.context), vp.getPassword(), true).go()
        }
        p0.itemView.setOnLongClickListener {

            AlertDialog.Builder(it.context).apply {
                setMessage("是否删除 ${list[p1].title} ？")
                setNegativeButton("否", { msg, w ->
                    msg.dismiss()

                })
                setPositiveButton("是", { msg, w ->
                    vp.mPresenter.deleteMessage(list[p1].msg)
                    msg.dismiss()
                })


            }.show()



            true
        }

//                                p0.itemView.apply { tv_title.text = "2" }
    }

}