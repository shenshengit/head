package com.shenshenff.headportrait.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;


/**
 * Created by shenshen on 16/3/10.
 */
public class Util {
    /**
     * 获取屏幕的宽度方法
     */
    public static int getScreenWidth(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);// this指当前activity
        return dm.widthPixels ;
    }

    /**
     * 获取屏幕的高度方法
     */
    public static int getScreenHeight(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);// this指当前activity
        return dm.heightPixels ;
    }

    /**
     * dp单位转成px单位
     */
    public static float dpChangepx (Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * 全部URL取出文件名
     * @param url
     */
    public static final String imgName (String url) {
        String imgFileName = url.substring(url.lastIndexOf("/") + 1);
        return imgFileName;
    }

    /**
     * 圆形Bitmap
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap, boolean isRound) {

        //圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //取最短边做边长
        int r = Math.min(width, height);

        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, r, r); //挖去一个正方形的图
        //创建一个宽width，高height的 新位图。
        Bitmap backgroundBmp = Bitmap.createBitmap( r, r, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBmp); //new一个Canvas，在backgroundBmp上画图
        Paint paint = new Paint();
        paint.setAntiAlias(true);//设置边缘光滑，去掉锯齿
        RectF rect = new RectF(0, 0, r, r); //宽高相等，即正方形
        if (isRound) {
            canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        } else {
            canvas.drawRoundRect(rect, 22, 22, paint);//第二个参数是x半径，第三个参数是y半径
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉

        canvas.drawBitmap(bm, null, rect, paint);  //canvas将bitmap画在backgroundBmp上
        return backgroundBmp;
    }



}
