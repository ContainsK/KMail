package com.tk.kmail.view.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by TangKai on 2018/12/19.
 */
class BaseViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root) {
    constructor(group: ViewGroup, layoutId: Int) : this(DataBindingUtil.inflate<T>(LayoutInflater.from(group.context), layoutId, group, false))
}