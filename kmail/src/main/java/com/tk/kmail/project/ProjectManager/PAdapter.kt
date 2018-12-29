package com.tk.kmail.project.ProjectManager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tk.kmail.R
import com.tk.kmail.base.BaseViewHolder
import com.tk.kmail.databinding.LayoutItemMessagesBinding
import com.tk.kmail.model.utils.Evs
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


//                it.context.startActivity(Intent(it.context, View::class.java))
            }
            tv_title.text = list[p1].name


        }
//                                p0.itemView.apply { tv_title.text = "2" }
    }

}