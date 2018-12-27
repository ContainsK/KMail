package com.tk.kmail.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by TangKai on 2018/12/19.
 */
open class BaseViewHolder<T : ViewDataBinding>(v: View) : RecyclerView.ViewHolder(v) {
    constructor(group: ViewGroup, layoutId: Int) : this(DataBindingUtil.inflate<T>(LayoutInflater.from(group.context), layoutId, group, false))

    lateinit var binding: T

    constructor(binding: T) : this(binding.root) {
        this.binding = binding
    }
}