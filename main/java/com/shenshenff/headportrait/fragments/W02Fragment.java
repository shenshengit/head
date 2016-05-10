package com.shenshenff.headportrait.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.activitys.ViewPagerActivity;
import com.shenshenff.headportrait.adapters.GridAdapter;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.utils.FileUtil;
import com.shenshenff.headportrait.utils.HttpUtil;
import com.shenshenff.headportrait.utils.JsonUtil;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenshen on 16/3/15.
 */
public class W02Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private GridView gridView;
    private List<String> imgUrls;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3302) {
                int imgHeight = (int) ((Util.getScreenWidth(getActivity()) - Util.dpChangepx(getActivity(), 4)) / 2);
                gridView.setAdapter(new GridAdapter(getActivity(), imgUrls, (int) (imgHeight * 1.2)));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_w02, null);
        initViews(view);
        initDatas();
        return view;
    }

    private void initViews(View view) {
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.w02swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                HttpUtil.sendHttpRequest(Consts.URL_SHUAIGE, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        FileUtil.saveJSONString(getActivity(), Consts.FILENAME_SHUAIGE, response);

                        imgUrls = JsonUtil.parseImageUrlWithJSONString(response);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if (imgUrls != null) {
                            Message msg = new Message();
                            msg.what = 3302;
                            handler.sendMessage(msg);
                        }
                    }
                }, 2000);
            }
        });
    }

    private void initDatas() {
        imgUrls = new ArrayList<>();
        String jsonString;
        jsonString = FileUtil.loadJSONString(getActivity(), Consts.FILENAME_SHUAIGE);
        imgUrls = JsonUtil.parseImageUrlWithJSONString(jsonString);
        if (imgUrls != null && imgUrls.size() != 0) {
            Message msg = new Message();
            msg.what = 3302;
            handler.sendMessage(msg);
            LogUtil.e("【帅哥】—— 读取本地数据");
        } else {
            HttpUtil.sendHttpRequest(Consts.URL_SHUAIGE, new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    FileUtil.saveJSONString(getActivity(), Consts.FILENAME_SHUAIGE, response);
                    imgUrls = JsonUtil.parseImageUrlWithJSONString(response);
                    if (imgUrls != null) {
                        Message msg = new Message();
                        msg.what = 3302;
                        handler.sendMessage(msg);
                    }
                }
                @Override
                public void onError(Exception e) {
//                    ToastUtil.showNoRepeat(getActivity(), "请检查网络", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
        intent.putStringArrayListExtra("imgUrls", (ArrayList<String>) imgUrls);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
