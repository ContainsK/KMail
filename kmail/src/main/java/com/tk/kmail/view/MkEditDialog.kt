package com.tk.kmail.view

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.tk.kmail.R

/**
 * Created by TangKai on 2019/1/7.
 */
class MkEditDialog(context: Context) : AlertDialog.Builder(context, R.style.CsDialog) {
    val view: ViewGroup
    private val titleView: TextView

    init {
        view = LinearLayout(context)
        val l = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        titleView = TextView(context)
        view.addView(titleView, l)
        setView(view)
    }

    fun setTitle(v: String) {
        titleView.text = v
    }


    companion object {
        fun createA(ctx: Context, hintText: String, text: String): View {
            return createA(ctx, hintText, text, true)
        }

        fun createA(ctx: Context, hintText: String, content: String = "", isRead: Boolean): View {
            //TextInputLayout
            //TextInputEditText
            return TextInputLayout(ctx).apply {
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                val edit = TextInputEditText(ctx).apply {
                    hint = hintText
                    if (isRead)
                        setRead(this)
                    setText(content)
                }
                isCounterEnabled = true
                addView(edit, layoutParams)
            }

        }

        fun setRead(v: EditText) {
            v.keyListener = null
            v.setTextIsSelectable(true)
        }
    }

}