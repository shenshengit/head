package com.shenshenff.headportrait.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenshenff.headportrait.R;

/**
 * Created by shenshen on 16/4/7.
 */
public class MyLinearLayout extends LinearLayout {


    public TextView titleText;
    public ImageView img1,img2,img3,img4,img5,img6;
    public Button moveBtn;

    private int imgHeight;
    private boolean isOne = true;

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_home_01, this, true);
        titleText = (TextView) findViewById(R.id.id_myll_titletext);
        img1 = (ImageView) findViewById(R.id.id_myll_img1);
        img2 = (ImageView) findViewById(R.id.id_myll_img2);
        img3 = (ImageView) findViewById(R.id.id_myll_img3);
        img4 = (ImageView) findViewById(R.id.id_myll_img4);
        img5 = (ImageView) findViewById(R.id.id_myll_img5);
        img6 = (ImageView) findViewById(R.id.id_myll_img6);
        moveBtn = (Button) findViewById(R.id.id_myll_btn);
    }

    public void setTitleText(String string){
        titleText.setText(string);
    }

    public void setButtonText(String string) {
        moveBtn.setText(string);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        imgHeight = img1.getWidth();
        //Log.i("onMeasure", "imgHeight" + imgHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //Log.i("onLayout", "imgHeight" + imgHeight);
        if (isOne && imgHeight != 0) {
            ViewGroup.LayoutParams lp = img1.getLayoutParams();
            lp.height = imgHeight;
            img1.setLayoutParams(lp);
            //中间的这个ImageView是带 margin 属性的 所有要在读取一次 LayoutParams
            ViewGroup.LayoutParams lp2 = img2.getLayoutParams();
            lp2.height = imgHeight;
            img2.setLayoutParams(lp2);
            img5.setLayoutParams(lp2);

            img3.setLayoutParams(lp);
            img4.setLayoutParams(lp);
            img6.setLayoutParams(lp);

            isOne = false;
            //Log.i("LayoutParams", "只执行一次" );
        }

    }
}
