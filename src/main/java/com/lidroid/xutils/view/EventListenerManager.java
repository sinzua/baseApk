package com.lidroid.xutils.view;

import android.view.View;
import com.lidroid.xutils.util.DoubleKeyValueMap;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.event.EventBase;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class EventListenerManager {
    private static final DoubleKeyValueMap<ViewInjectInfo, Class<?>, Object> listenerCache = new DoubleKeyValueMap();

    public static class DynamicHandler implements InvocationHandler {
        private WeakReference<Object> handlerRef;
        private final HashMap<String, Method> methodMap = new HashMap(1);

        public DynamicHandler(Object handler) {
            this.handlerRef = new WeakReference(handler);
        }

        public void addMethod(String name, Method method) {
            this.methodMap.put(name, method);
        }

        public Object getHandler() {
            return this.handlerRef.get();
        }

        public void setHandler(Object handler) {
            this.handlerRef = new WeakReference(handler);
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object handler = this.handlerRef.get();
            if (handler != null) {
                method = (Method) this.methodMap.get(method.getName());
                if (method != null) {
                    return method.invoke(handler, args);
                }
            }
            return null;
        }
    }

    private EventListenerManager() {
    }

    public static void addEventMethod(ViewFinder finder, ViewInjectInfo info, Annotation eventAnnotation, Object handler, Method method) {
        try {
            View view = finder.findViewByInfo(info);
            if (view != null) {
                DynamicHandler dynamicHandler;
                EventBase eventBase = (EventBase) eventAnnotation.annotationType().getAnnotation(EventBase.class);
                Class<?> listenerType = eventBase.listenerType();
                String listenerSetter = eventBase.listenerSetter();
                String methodName = eventBase.methodName();
                boolean addNewMethod = false;
                Object listener = listenerCache.get(info, listenerType);
                if (listener != null) {
                    dynamicHandler = (DynamicHandler) Proxy.getInvocationHandler(listener);
                    addNewMethod = handler.equals(dynamicHandler.getHandler());
                    if (addNewMethod) {
                        dynamicHandler.addMethod(methodName, method);
                    }
                }
                if (!addNewMethod) {
                    dynamicHandler = new DynamicHandler(handler);
                    dynamicHandler.addMethod(methodName, method);
                    listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, dynamicHandler);
                    listenerCache.put(info, listenerType, listener);
                }
                view.getClass().getMethod(listenerSetter, new Class[]{listenerType}).invoke(view, new Object[]{listener});
            }
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
        }
    }
}
