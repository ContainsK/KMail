package com.tk.kmail.model.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by TangKai on 2018/5/23.
 */

public class ActivityUtil {
    public static class Build {
        private final Context context;
        private Intent intent;

        public Build(Context context) {
            this.context = context;
            intent = new Intent();
        }


        public Intent getIntent() {
            return intent;
        }

        public Build setIntent(Intent intent) {
            this.intent = intent;
            return this;
        }

        public Build setClass(Class<? extends Activity> activityClass) {
            intent.setClass(context, activityClass);
            return this;
        }

        public void go(Class<? extends Activity> activityClass) {
            setClass(activityClass);
            context.startActivity(intent);
        }

        public void goResult(Class<? extends Activity> activityClass, int requestCode) {
            if (context instanceof Activity) {
                setClass(activityClass);
                ((Activity) context).startActivityForResult(intent, requestCode);
            } else {
                throw new RuntimeException("context not is Activity");
            }

        }

        public void go() {
            context.startActivity(intent);
        }
    }

    public static Build build(Context context) {
        return new Build(context);
    }


}
