package com.shenshenff.headportrait.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.activitys.ViewPagerActivity;
import com.shenshenff.headportrait.adapters.ForSdcardAdapter;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.utils.Util;
import com.shenshenff.headportrait.views.HeaderGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;

/**
 * Created by shenshen on 16/3/9.
 * <p/>
 * 读取SD卡里的图片
 * 加载到GridView
 */
public class CollectFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    //宫格高度
    private int imgHeight;
    private HeaderGridView gridview;
    private View header;
    private ForSdcardAdapter adapter;
    private TextView currentNumber;
    private View collection_bg;
    private TextView kongImage;
    private Button headBtn1, headBtn2;

    //数据
    //所有文件的路径
    private String[] fileImagePaths = null;
    private List<String> fileImagePathLists = null;
    private File[] currentFiles;

    //记录有几个图 来判断是否要刷新视图
    int oldNumber = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        fileImagePathLists = new ArrayList<>();
        if (readFileImagePaths() > 0) {
            loadFileData();
            oldNumber = fileImagePaths.length;
        }

    }

    private void initView(View view) {
        imgHeight = (int) ((Util.getScreenWidth(getActivity()) - Util.dpChangepx(getActivity(), 4)) / 4);
        header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_gridview_header, null);

        gridview = (HeaderGridView) view.findViewById(R.id.headergridview);
        gridview.addHeaderView(header);

        if (fileImagePaths != null) {
            adapter = new ForSdcardAdapter(getActivity(), fileImagePaths, imgHeight, gridview);
            gridview.setAdapter(adapter);
        }
        gridview.setOnItemClickListener(this);
        currentNumber = (TextView) header.findViewById(R.id.id_number);
        collection_bg = view.findViewById(R.id.collection_bg);
        kongImage = (TextView) view.findViewById(R.id.kong_image);

        headBtn1 = (Button) header.findViewById(R.id.head_btn_01);
        headBtn2 = (Button) header.findViewById(R.id.head_btn_02);
        headBtn1.setOnClickListener(this);
        headBtn2.setOnClickListener(this);



    }

    @Override
    public void onStart() {
        super.onStart();

        if (oldNumber == readFileImagePaths()) {
            //LogUtil.i("没有改变");
        } else {
            // LogUtil.i("发生了改变");
            loadFileData();
            adapter = new ForSdcardAdapter(getActivity(), fileImagePaths, imgHeight, gridview);
            gridview.setAdapter(adapter);
            oldNumber = fileImagePaths.length;
        }
        currentNumber.setText(oldNumber + "");
        if (oldNumber == 0) {
            kongImage.setVisibility(View.VISIBLE);
            collection_bg.setBackgroundColor(Color.parseColor("#f5f5f5"));
        }
        if (oldNumber > 0) {
            kongImage.setVisibility(View.GONE);
            collection_bg.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    public int readFileImagePaths() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard, Consts.DIRNAME);
                if (dir.exists()) {
                    currentFiles = dir.listFiles();
                    return currentFiles.length;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void loadFileData() {
        fileImagePaths = new String[currentFiles.length];

        fileImagePathLists.clear();
        for (int i = 0; i < currentFiles.length; i++) {
            LogUtil.i("循环======================" + i);
            String imgpath = currentFiles[i].getAbsolutePath();
            fileImagePaths[i] = imgpath;
            fileImagePathLists.add(imgpath);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
        LogUtil.i("fileImagePathLists" + fileImagePathLists.size());
        intent.putStringArrayListExtra("imgUrls", (ArrayList<String>) fileImagePathLists);
        intent.putExtra("position", position - 4);
        intent.putExtra("tag", true);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_btn_01:

                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdcard, Consts.CACHEDIRNAME);
                    if (!dir.exists()) {
                        dir.mkdirs();
                        LogUtil.i("创建临时文件夹");
                    }
                }
                break;
        }
    }
}

