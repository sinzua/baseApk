package com.flurry.sdk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class lx {

    public static class a {
        private final Object a;
        private final String b;
        private Class<?> c;
        private List<Class<?>> d = new ArrayList();
        private List<Object> e = new ArrayList();
        private boolean f;
        private boolean g;

        public a(Object obj, String str) {
            this.a = obj;
            this.b = str;
            this.c = obj != null ? obj.getClass() : null;
        }

        public <T> a a(Class<T> cls, T t) {
            this.d.add(cls);
            this.e.add(t);
            return this;
        }

        public a a(Class<?> cls) {
            this.g = true;
            this.c = cls;
            return this;
        }

        public Object a() throws Exception {
            Method a = lx.a(this.c, this.b, (Class[]) this.d.toArray(new Class[this.d.size()]));
            if (this.f) {
                a.setAccessible(true);
            }
            Object[] toArray = this.e.toArray();
            return this.g ? a.invoke(null, toArray) : a.invoke(this.a, toArray);
        }
    }

    public static Method a(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException {
        while (cls != null) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
}
