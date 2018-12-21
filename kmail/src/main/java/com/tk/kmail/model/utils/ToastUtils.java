package com.tk.kmail.model.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by TangKai on 2017/11/2.
 */

public class ToastUtils {
    private static Toast toast;

    public static Toast instance(Context context) {
        if (context != null && toast == null)
            synchronized (ToastUtils.class) {
                if (toast == null)
                    toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            }
        return toast;
    }

    private static Toast instance() {
        return instance(null);
    }

    public static void show(String text, int d) {
        Toast instance = instance();
        instance.setDuration(d);
        instance.setGravity(Gravity.BOTTOM, 0, 50);
        instance.setText(text);
        instance.show();
    }

    public static void show(String text) {
        show(text, Toast.LENGTH_SHORT);
    }
}
