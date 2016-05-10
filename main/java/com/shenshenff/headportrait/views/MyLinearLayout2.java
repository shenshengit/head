package com.shenshenff.headportrait.views;

import android.content.Context;
import android.util.AttributeSet;
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
public class MyLinearLayout2 extends LinearLayout {


    public TextView titleText;
    public ImageView img1,img2,img3,img4,img5,img6;
    public Button moveBtn;

    private int imgHeight;
    private boolean isOne = true;

    public MyLinearLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_home_02, this, true);
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


    }




}
