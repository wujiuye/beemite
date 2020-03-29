package com.wujiuye.beemite.util;

/**
 * 排除一些包
 *
 * @author wujiuye 2020/03/29
 */
public class ClassSearchUtils {

    private final static String[] EXCLUDE_PACKAGE = new String[]{
            "java.",
            "org.springframework.",
            "org.apache.",
            "org.slf4j.",
    };

    public static boolean match(Class<?> aClass) {
        return match(aClass.getName());
    }

    public static boolean match(String className) {
        for (String s : EXCLUDE_PACKAGE) {
            if (className.startsWith(s)) {
                return false;
            }
        }
        return true;
    }

}
