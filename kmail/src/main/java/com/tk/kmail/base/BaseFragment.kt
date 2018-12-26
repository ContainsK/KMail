package com.tk.kmail.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

/**
 * Created by TangKai on 2018/12/17.
 */
abstract class BaseFragment : Fragment(), IBaseView {
    val mContentView: View? by lazy {
        var v: View? = getContentView()
        if (v == null && getLayoutId() > 0) {
            v = LayoutInflater.from(getContext()).inflate(getLayoutId(), FrameLayout(getContext()), false)
        }
        v
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
        recycler()
        println("onDestroy")
    }

    override fun getContext(): Context? {
        return super.getActivity()
    }
}