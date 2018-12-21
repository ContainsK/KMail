package com.tk.kmail.model.utils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by TangKai on 2017/11/22.
 */

public class Evs {
    public static EventBus a = EventBus.getDefault();

    public static void reg(Object obj) {
        try {
            if (!a.isRegistered(obj))
                a.register(obj);
        } catch (Throwable throwable) {
//            LogUtils.e("事件未注册！", obj);
        }
    }

    public static void unreg(Object obj) {
        try {
            if (a.isRegistered(obj))
                a.unregister(obj);
        } catch (Throwable throwable) {
//            LogUtils.e("事件未注册！", obj);
        }
    }

    public static void unAllClassEvent(Class<?>... cls) {
        //typesBySubscriber
        try {
            Field typesBySubscriber = EventBus.class.getDeclaredField("typesBySubscriber");
            typesBySubscriber.setAccessible(true);
            Map<Object, List<Class<?>>> o = (Map<Object, List<Class<?>>>) typesBySubscriber.get(a);
            HashSet<Object> objects = new HashSet<>();
            objects.addAll(o.keySet());
            Iterator<Object> iterator = objects.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
//                LogUtils.e(next.getClass().getName());
                for (Class<?> c : cls) {
                    if (next.getClass() == c) {
                        unreg(next);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
