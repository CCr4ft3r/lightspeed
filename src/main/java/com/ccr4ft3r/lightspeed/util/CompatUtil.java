package com.ccr4ft3r.lightspeed.util;

public class CompatUtil {

    public static boolean existsClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}