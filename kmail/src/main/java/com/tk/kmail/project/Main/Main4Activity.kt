package com.tk.kmail.project.Main

import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.tk.kmail.R
import com.tk.kmail.base.BaseActivity
import com.tk.kmail.base.IBase
import com.tk.kmail.model.bean.MessageTagBean
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.model.utils.ActivityUtil
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.GsonUtils
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import com.tk.kmail.project.Message.Presenter
import kotlinx.android.synthetic.main.include_appbar.view.*
import kotlinx.android.synthetic.main.layout_new_add_message.view.*
import org.greenrobot.eventbus.Subscribe

class Main4Activity : BaseActivity<Message.View>() {
    companion object {
        fun buildStart(build: ActivityUtil.Build, pass: String, isRead: Boolean = false): ActivityUtil.Build {
            return build.setClass(Main4Activity::class.java).apply {
                intent.putExtra("read", isRead)
                intent.putExtra("pass", pass)
            }
        }

    }

    val KEY_TEXT_TITLE = "名称"
    val KEY_TEXT_DEC = "备注"
    val arrText = arrayListOf(KEY_TEXT_TITLE, KEY_TEXT_DEC)
    val tagList = mutableListOf<MessageTagBean>()
    //    val linkedHashMap: LinkedHashMap<String, MessageTagBean> = java.util.LinkedHashMap()
    override fun getViewP(): Message.View {
        return object : Message.View, IBase.IViewDialog by getViewDialog() {
            override fun refreshList(list: MutableList<DataBean>) {
            }

            override fun getPassword(): String {
                return intent.getStringExtra("pass")
            }

            override fun getPresenter(): Message.Presenter {
                return Presenter(this)
            }

            override fun callResult(result: ResultBean) {
                //TODO 这一块需要 与本地联调，已达到编辑效果。
                when (result.type) {
                    Message.TYPE_SEND -> {
                        if (result.status) {
                            ToastUtils.show(result.getAResult())
                            if (!::data.isInitialized) {
                                finish()
                                return
                            }
                            mViewP.mPresenter.deleteMessage(data.msg)
                        }

                    }
                    Message.TYPE_DELETE -> {
                        if (result.status) {
                            //删除旧数据以后才替换新数据
//                            ToastUtils.show(result.getAResult())
                            finish()
                        }

                    }
                }


            }

        }
    }

    private lateinit var data: DataBean

    @Subscribe(sticky = true)
    fun setDataBean(data: DataBean) {
        Evs.a.removeStickyEvent(data)
        this.data = data
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_new_add_message
    }

    private var ed_name: TextInputLayout? = null
    private var ed_dec: TextInputLayout? = null

    private var isRead: Boolean = false

    override fun initView() {
        Evs.reg(this)
        isRead = intent.getBooleanExtra("read", false)
        setSupportActionBar(mContentView!!.toolbar)
        if (isRead) {
            println(data.content)
            tagList.addAll(GsonUtils.build(data.content).getClassType(Array<MessageTagBean>::class.java))
            tagList.forEach {
                mContentView!!.linearLayout.addView(createView(it, true))

            }
            return
        }

        (mContentView as ViewGroup).apply {
            arrText.forEach {
                linearLayout.addView(createW(MessageTagBean(it)))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_new_message, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_add_send -> {
                if (isRead) {
                    ToastUtils.show("只读状态，保存失败...")
                    return true
                }


                val vg = mContentView!!.linearLayout
                val coun = vg.childCount
                for (ind in 1..coun) {
                    val edline = vg.getChildAt(ind - 1) as TextInputLayout
                    val edit = edline.editText!!
                    val bean = edline.tag as MessageTagBean
                    bean.text = edit.text.toString()
                }

                mViewP.mPresenter.sendMessage(mViewP.mPresenter.getFolder(), object : IGetData {
                    override fun getMsgContent(): String {
                        return GsonUtils.gson().toJson(tagList)
                    }

                    override fun getMsgDescribe(): String {
                        return ed_dec!!.editText!!.text.toString()
                    }

                    override fun getMsgTitle(): String {
                        return ed_name!!.editText!!.text.toString()
                    }
                })

            }
            R.id.action_add_tag -> {
                if (isRead) {
                    ToastUtils.show("只读状态，不可以添加...")
                    return true
                }
                showDialog()
            }
            R.id.action_edit -> {
                if (!isRead) {
                    return true
                }
                ToastUtils.show("可以编辑了！")
                isRead = false
                mContentView!!.linearLayout.removeAllViews()
                tagList.forEach {
                    mContentView!!.linearLayout.addView(createView(it, false))
                }

            }

        }


        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val ed = createView(MessageTagBean("标签名"), false)
        android.support.v7.app.AlertDialog.Builder(getThisContext()!!).apply {
            val view = LinearLayout(context)
            view.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val l = (view.layoutParams as ViewGroup.MarginLayoutParams)
                l.setMargins(330, 330, 330, 330)
                view.layoutParams = l
            }

            view.addView(ed)
            setView(view)
            setPositiveButton("添加", null)
            setNegativeButton("取消", null)



            setCancelable(false)
        }.create().apply {
            show()
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val v = createW(MessageTagBean(ed.editText!!.text.toString()))
                if (v != null) {
                    mContentView!!.linearLayout.addView(v)
                    dismiss()
                } else {
                    ToastUtils.show("已存在该标签！")
                }
            }

        }


    }

    fun createView(tag: MessageTagBean, isRead: Boolean): TextInputLayout {
        val ctx = getThisContext()
        return TextInputLayout(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val edit = TextInputEditText(ctx).apply {
                hint = tag.tag
                if (isRead)
                    setRead(this)
                setText(tag.text)
            }
            isCounterEnabled = true
            if (!isRead) {
                setOnLongClickListener {
                    if (tag.isDeleted) {
                        (parent as ViewGroup).removeView(it)
                        tagList.remove(tag)
                    }
                    true
                }
            }
            addView(edit, layoutParams)
            when (tag.tag) {
                KEY_TEXT_TITLE -> {
                    ed_name = this
                }
                KEY_TEXT_DEC -> {
                    ed_dec = this
                }
            }
            this.tag = tag
        }
    }


//    private fun createA(hintText: String, tag: MessageTagBean): TextInputLayout {
//        return createA(hintText, tag, true)
//    }
//
//    private fun createA(hintText: String, tag: MessageTagBean, isRead: Boolean): TextInputLayout {
//        //TextInputLayout
//        //TextInputEditText
//        val ctx = getThisContext()
//        return TextInputLayout(ctx).apply {
//            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//
//            val edit = TextInputEditText(ctx).apply {
//                hint = hintText
//                if (isRead)
//                    setRead(this)
//                setText(tag.text)
//            }
//            isCounterEnabled = true
//            setOnLongClickListener {
//                (parent as ViewGroup).removeView(it)
//                linkedHashMap.remove(hintText)
//
//                true
//            }
//            addView(edit, layoutParams)
//            when (hintText) {
//                KEY_TEXT_TITLE -> {
//                    ed_name = this
//                }
//                KEY_TEXT_DEC -> {
//                    ed_dec = this
//                }
//            }
//        }
//    }

    private fun createW(tag: MessageTagBean): TextInputLayout? {
        val ind = tagList.indexOf(tag)
        return if (ind > -1) {
            null
        } else {
            tagList.add(tag)
            createView(tag, false)
        }
    }

    private fun setRead(v: EditText) {
        v.keyListener = null
        v.setTextIsSelectable(true)

    }


}
