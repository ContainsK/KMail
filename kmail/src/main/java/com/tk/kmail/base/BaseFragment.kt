package com.tk.kmail.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tk.kmail.mvp.base.IBase

/**
 * Created by TangKai on 2018/12/17.
 */
abstract class BaseFragment<T : IBase.View<*>> : Fragment(), IBaseView<T> {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("onCreateView")
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated")
        initView()
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        println("onAttach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("onDestroyView")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate")

    }

    override fun onDetach() {
        super.onDetach()
        println("onDetach")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("onActivityCreated")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
        mViewP?.callRecycler()
        mViewP?.mPresenter.callRecycler()
        recycler()

    }


}