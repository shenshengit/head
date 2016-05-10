package com.shenshenff.headportrait.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Felix on 2016/4/15.
 */
public class ToastUtil {
    public static Toast sToast;

    public static void showNoRepeat(Context context, String text, int duration) {
        if (context == null) {
            return;
        }
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, text, duration);
        sToast.show();
    }
}
