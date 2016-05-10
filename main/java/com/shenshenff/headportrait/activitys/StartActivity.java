package com.shenshenff.headportrait.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.utils.FileUtil;
import com.shenshenff.headportrait.utils.HttpUtil;
import com.shenshenff.headportrait.utils.LogUtil;

/**
 * Created by shenshen on 16/4/6.
 */
public class StartActivity extends Activity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        initData();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }

    private void initData() {


        HttpUtil.sendHttpRequest(Consts.URL_SORT, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (response != null){
                    FileUtil.saveJSONString(StartActivity.this, Consts.FILENAME_FEILEI, response);
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e("网络请求"+Consts.FILENAME_FEILEI+"失败！");
            }
        });

        HttpUtil.sendHttpRequest(Consts.URL_MEINV, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (response != null){
                    FileUtil.saveJSONString(StartActivity.this, Consts.FILENAME_MEINV, response);
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e("网络请求"+Consts.FILENAME_MEINV+"失败！");
            }
        });

        HttpUtil.sendHttpRequest(Consts.URL_SHUAIGE, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (response != null){
                    FileUtil.saveJSONString(StartActivity.this, Consts.FILENAME_SHUAIGE, response);
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e("网络请求"+Consts.FILENAME_SHUAIGE+"失败！");
            }
        });


        //裁切增加名称文件
        SharedPreferences  sharedPreferences = getSharedPreferences(Consts.CAIJIAN_CACHE_FILENAME, MODE_PRIVATE);
        int n = sharedPreferences.getInt("caiqian", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("caiqian", n);
        editor.commit();
//        FileOutputStream fos = null;
//        BufferedWriter writer = null;
//        try {
//            fos = openFileOutput(Consts.CAIJIAN_CACHE_FILENAME, Context.MODE_PRIVATE);
//            writer = new BufferedWriter(new OutputStreamWriter(fos));
//            try {
//                writer.write("image_0001");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }  finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        //FileUtil.saveJSONString(this, Consts.CAIJIAN_CACHE_FILENAME, "image_001");
    }
}
