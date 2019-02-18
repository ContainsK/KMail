package com.tk.kmail.model.utils

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

/**
 * Created by TangKai on 2018/12/24.
 */
class BindingUtils {
    companion object {
        fun <T : ViewDataBinding> bind(ctx: Context, resId: Int): T {
            return DataBindingUtil.inflate(LayoutInflater.from(ctx), resId, LinearLayout(ctx), false)
        }

        fun <T : ViewDataBinding> bind(v: View): T {
            return DataBindingUtil.bind(v)
        }
    }

}
