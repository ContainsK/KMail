package com.tk.kmail.project.Message.Add

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.EditText
import com.tk.kmail.R
import com.tk.kmail.base.BaseActivity
import com.tk.kmail.base.IBase
import com.tk.kmail.databinding.LayoutAddMessageBinding
import com.tk.kmail.model.mails.DataBean
import com.tk.kmail.model.mails.IGetData
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.mvp.Message
import com.tk.kmail.mvp.base.ResultBean
import kotlinx.android.synthetic.main.include_appbar.*
import kotlinx.android.synthetic.main.layout_add_message.*
import org.greenrobot.eventbus.Subscribe

class View : BaseActivity<Message.AddMessageView>() {
    override fun getViewP(): Message.AddMessageView {
        return object : Message.AddMessageView, IBase.IViewDialog by getViewDialog() {
            override fun callResult(result: ResultBean) {

                Snackbar.make(mContentView!!, result.getAResult<String>()!!, Snackbar.LENGTH_SHORT).show()
                if (result.status)
                    finish()
            }

            override fun getPresenter(): Message.AddMessagePresenter {
                return Presenter(this)
            }

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_add_message
    }

    private lateinit var data: DataBean

    @Subscribe(sticky = true)
    fun setDataBean(data: DataBean) {
        Evs.a.removeStickyEvent(data)
        this.data = data
    }

    @SuppressLint("RestrictedApi")
    override fun initView() {
        setSupportActionBar(toolbar)
        Evs.reg(this)
        val isR = intent.getBooleanExtra("read", false)

        if (isR) {
            fun setReadSingle(v: EditText) {
                v.setKeyListener(null)
                v.setTextIsSelectable(true)
            }
            setReadSingle(tiet_content)
            setReadSingle(tiet_title)
            setReadSingle(tiet_dec)
            val bind = DataBindingUtil.bind<LayoutAddMessageBinding>(mContentView)
            bind.data = data
            bind.isRead = isR
            floatingActionButton.visibility = View.GONE
        }



        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)//左侧添加一个默认的返回图标
//        getSupportActionBar()?.setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener {
            finish()
        }
        floatingActionButton.setOnClickListener {
            val p = mViewP.mPresenter
            p.sendMessage(p.getFolder(), object : IGetData {

                override fun getMsgContent(): String {
                    return tiet_content.text.toString()
                }

                override fun getMsgDescribe(): String {
                    var a: String = tiet_dec.text.toString()
                    if (a.length < 1)
                        a = "Not log commit..."
                    return a
                }

                override fun getMsgTitle(): String {
                    return tiet_title.text.toString()
                }

            }, intent.getStringExtra("pass"))
        }
    }


}
