package com.tk.kmail.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/14.
 */
abstract class BaseActivity<T : IBase.View<*>> : AppCompatActivity(), IBaseView<T> {
    val mContentView: View? by lazy {
        var v: View? = getContentView()
        if (v == null && getLayoutId() > 0) {
            v = LayoutInflater.from(getThisContext()).inflate(getLayoutId(), FrameLayout(getThisContext()), false)
        }
        v
    }
    val mViewP: T by lazy {
        getViewP()
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mContentView)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewP?.callRecycler()
        mViewP?.mPresenter.callRecycler()
        recycler()
    }


}




