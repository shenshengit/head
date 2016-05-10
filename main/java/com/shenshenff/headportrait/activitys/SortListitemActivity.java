package com.shenshenff.headportrait.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.adapters.GridAdapter;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.modes.SortItem;
import com.shenshenff.headportrait.utils.JsonUtil;
import com.shenshenff.headportrait.utils.ToastUtil;
import com.shenshenff.headportrait.utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 弃用
 * Created by shenshen on 16/3/18.
 */
public class SortListitemActivity extends Activity implements AdapterView.OnItemClickListener {

    private TextView title;
    private GridView gridView;
    private SortItem currentSortitem;
    private String titlename;
    private List<String> imgUrls;
    private GridAdapter adapter;
    private int imgHeight;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortlistitem);
        initDatas();
        initViews();
    }

    private void initDatas() {
        imgUrls = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            currentSortitem = (SortItem) intent.getSerializableExtra("sortItem");
            titlename = currentSortitem.getName();
            new ContentJsonAsyncTask().execute(currentSortitem.getContentUrl());
        }
    }

    private void initViews() {
        imgHeight = (int) ((Util.getScreenWidth(this) - Util.dpChangepx(this, 4)) / 3);
        title = (TextView) findViewById(R.id.id_title);
        if (titlename != null) {
            title.setText(titlename);
        }
        gridView = (GridView) findViewById(R.id.id_sort_item_grid);
        gridView.setOnItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sswiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        new ContentJsonAsyncTask().execute(currentSortitem.getContentUrl());
                    }
                }, 2000);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewPagerActivity.class);
        intent.putStringArrayListExtra("imgUrls", (ArrayList<String>) imgUrls);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    class ContentJsonAsyncTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {
            String allUrl = Consts.URL_HEAD + params[0];
            String jsonString = null;
            try {
                jsonString = readStream(new URL(allUrl).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JsonUtil.parseImageUrlWithJSONString(jsonString);
        }

        @Override
        protected void onPostExecute(List<String> urls) {
            super.onPostExecute(urls);
            imgUrls = urls;
            if (urls != null) {
                adapter = new GridAdapter(SortListitemActivity.this, urls, imgHeight);
                gridView.setAdapter(adapter);
            } else {
                ToastUtil.showNoRepeat(SortListitemActivity.this, "请检查网络", Toast.LENGTH_SHORT);
            }
        }
    }

    /**
     * 输入流转字符
     */
    private String readStream(InputStream is) {
        InputStreamReader isr = null;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
