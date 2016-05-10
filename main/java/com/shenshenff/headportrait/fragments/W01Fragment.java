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
public class W01Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private GridView gridView;
    private List<String> imgUrls;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3301) {
                LogUtil.i("请求刷新");
                int imgHeight = (int) ((Util.getScreenWidth(getActivity()) - Util.dpChangepx(getActivity(), 36)) / 2);
                gridView.setAdapter(new GridAdapter(getActivity(), imgUrls, (int) (imgHeight * 1.2)));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_w01, null);
        initViews(view);
        initDatas();
        return view;
    }

    private void initViews(View view) {

        int ScreenHeight = Util.getScreenHeight(getActivity());

        gridView = (GridView) view.findViewById(R.id.gridview1);

        ViewGroup.LayoutParams lp = gridView.getLayoutParams();
        lp.height = ScreenHeight + 500;
        gridView.setLayoutParams(lp);

        gridView.setOnItemClickListener(this);

        //用于定位 当在里面看完返回列表是 可以定位里面看到的位置
        //gridView.setSelection(50);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.w01swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                HttpUtil.sendHttpRequest(Consts.URL_MEINV, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        FileUtil.saveJSONString(getActivity(), Consts.FILENAME_MEINV, response);
                        imgUrls = JsonUtil.parseImageUrlWithJSONString(response);
                    }
                    @Override
                    public void onError(Exception e) {
                        //ToastUtil.showNoRepeat(getActivity(), "请检查网络", Toast.LENGTH_SHORT);
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if (imgUrls != null) {
                            Message msg = new Message();
                            msg.what = 3301;
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
        jsonString = FileUtil.loadJSONString(getActivity(), Consts.FILENAME_MEINV);
        imgUrls = JsonUtil.parseImageUrlWithJSONString(jsonString);
        if (imgUrls != null && imgUrls.size() != 0) {

            Message msg = new Message();
            msg.what = 3301;
            handler.sendMessage(msg);
            LogUtil.e("【美女】—— 读取本地数据");
        } else {
            LogUtil.i("网络请求");
            HttpUtil.sendHttpRequest(Consts.URL_MEINV, new HttpUtil.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    FileUtil.saveJSONString(getActivity(), Consts.FILENAME_MEINV, response);

                    imgUrls = JsonUtil.parseImageUrlWithJSONString(response);
                    if (imgUrls != null) {
                        Message msg = new Message();
                        msg.what = 3301;
                        handler.sendMessage(msg);
                    }
                }

                @Override
                public void onError(Exception e) {

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
