package com.shenshenff.headportrait.utils;

import android.util.Log;

/**
 * Created by shenshen on 16/3/10.
 */
public class LogUtil {

    private static final String tag = "shenLog";

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTTHING = 6;

    //改变赋值来控制日志打印
    public static final int LEVEL = VERBOSE;

    public static void v (String tag, String mas) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, mas);
        }
    }

    public static void d (String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i (String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w (String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e (String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void v (String mas) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, mas);
        }
    }

    public static void d (String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i (String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w (String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e (String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

}
