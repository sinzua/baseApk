package org.codehaus.jackson.map.util;

import org.codehaus.jackson.map.introspect.AnnotatedMethod;

public class BeanUtil {
    public static String okNameForGetter(AnnotatedMethod am) {
        String name = am.getName();
        String str = okNameForIsGetter(am, name);
        if (str == null) {
            return okNameForRegularGetter(am, name);
        }
        return str;
    }

    public static String okNameForRegularGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("get")) {
            return null;
        }
        if ("getCallbacks".equals(name)) {
            if (isCglibGetCallbacks(am)) {
                return null;
            }
        } else if ("getMetaClass".equals(name) && isGroovyMetaClassGetter(am)) {
            return null;
        }
        return manglePropertyName(name.substring(3));
    }

    public static String okNameForIsGetter(AnnotatedMethod am, String name) {
        if (!name.startsWith("is")) {
            return null;
        }
        Class<?> rt = am.getRawType();
        if (rt == Boolean.class || rt == Boolean.TYPE) {
            return manglePropertyName(name.substring(2));
        }
        return null;
    }

    public static String okNameForSetter(AnnotatedMethod am) {
        String name = am.getName();
        if (!name.startsWith("set")) {
            return null;
        }
        name = manglePropertyName(name.substring(3));
        if (name == null) {
            return null;
        }
        if ("metaClass".equals(name) && isGroovyMetaClassSetter(am)) {
            return null;
        }
        return name;
    }

    protected static boolean isCglibGetCallbacks(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || !rt.isArray()) {
            return false;
        }
        Package pkg = rt.getComponentType().getPackage();
        if (pkg == null) {
            return false;
        }
        String pname = pkg.getName();
        if (pname.startsWith("net.sf.cglib") || pname.startsWith("org.hibernate.repackage.cglib")) {
            return true;
        }
        return false;
    }

    protected static boolean isGroovyMetaClassSetter(AnnotatedMethod am) {
        Package pkg = am.getParameterClass(0).getPackage();
        if (pkg == null || !pkg.getName().startsWith("groovy.lang")) {
            return false;
        }
        return true;
    }

    protected static boolean isGroovyMetaClassGetter(AnnotatedMethod am) {
        Class<?> rt = am.getRawType();
        if (rt == null || rt.isArray()) {
            return false;
        }
        Package pkg = rt.getPackage();
        if (pkg == null || !pkg.getName().startsWith("groovy.lang")) {
            return false;
        }
        return true;
    }

    protected static String manglePropertyName(String basename) {
        int len = basename.length();
        if (len == 0) {
            return null;
        }
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            char upper = basename.charAt(i);
            char lower = Character.toLowerCase(upper);
            if (upper == lower) {
                break;
            }
            if (sb == null) {
                sb = new StringBuilder(basename);
            }
            sb.setCharAt(i, lower);
        }
        if (sb != null) {
            return sb.toString();
        }
        return basename;
    }
}
