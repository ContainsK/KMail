package com.tk.kmail.project.ProjectManager

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tk.kmail.R
import com.tk.kmail.base.BaseViewHolder
import com.tk.kmail.databinding.LayoutItemMessagesBinding
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.StringUtils
import kotlinx.android.synthetic.main.layout_item_messages.view.*
import javax.mail.Folder


/**
 * Created by TangKai on 2018/12/28.
 */
class PAdapter(var list: MutableList<Folder>, val afg: View) : RecyclerView.Adapter<BaseViewHolder<LayoutItemMessagesBinding>>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<LayoutItemMessagesBinding> {

        return BaseViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.layout_item_messages, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: BaseViewHolder<LayoutItemMessagesBinding>, p1: Int) {

        p0.itemView.apply {
            setOnClickListener {

                val fm = afg.fragmentManager!!
                Evs.a.removeStickyEvent(Folder::javaClass)
                Evs.a.postSticky(list[p1])
                fm.beginTransaction()
                        .replace(R.id.layout_fragment, com.tk.kmail.project.Message.View())
                        .addToBackStack(null)
                        .commit()


//
//                AlertDialog.Builder(it.context).apply {
//                    //                    setMessage()
////                    setTitle("输入密码")
//                    setView(LayoutInflater.from(context).inflate(R.layout.include_edittext, it as ViewGroup, false).apply {
//                        kotlinx.android.synthetic.main.include_edittext.view.tiet_text.setHint("输入密码")
//                        kotlinx.android.synthetic.main.include_edittext.view.tv_title.text = "请输入密码："
//                    })
//
//                    setPositiveButton("确定") { a, b ->
//                        a.dismiss()
//                    }
//                    setNegativeButton("取消") { a, b ->
//                        a.dismiss()
//                    }
//
//                }.show()
//                it.context.startActivity(Intent(it.context, View::class.java))
            }
            val name = StringUtils.getSubString(list[p1].name, Mails.PROJECT_NAME_S, "")
            tv_title.text = name

            setOnLongClickListener {
                AlertDialog.Builder(it.context).apply {
                    setMessage("是否删除 $name ？")
                    setNegativeButton("否", { msg, w ->
                        msg.dismiss()

                    })
                    setPositiveButton("是", { msg, w ->
                        afg.mViewP.mPresenter.deleteFolder(list[p1].name)
                        msg.dismiss()
                    })


                }.show()



                true
            }
        }
//                                p0.itemView.apply { tv_title.text = "2" }
    }

}