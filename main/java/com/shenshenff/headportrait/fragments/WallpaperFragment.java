package com.shenshenff.headportrait.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.shenshenff.headportrait.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenshen on 16/3/9.
 */
public class WallpaperFragment extends Fragment {

    private ViewPager mViewPager;
    private W01Fragment w01Fragment;
    private W02Fragment w02Fragment;
    private List<Fragment> mContents;
    private FragmentPagerAdapter mAdapter;

    private TextView btnMeinv,btnShuaige;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);

        initDatas();
        initViews(view);
        mViewPager.setAdapter(mAdapter);
        return view;
    }

    private void initDatas() {
        w01Fragment = new W01Fragment();
        w02Fragment = new W02Fragment();
        mContents = new ArrayList<>();
        mContents.add(w01Fragment);
        mContents.add(w02Fragment);
        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }
        };
    }

    private void initViews(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);

        btnMeinv = (TextView) view.findViewById(R.id.id_tab_meinv);
        btnMeinv.setOnClickListener(tabListener);
        btnShuaige = (TextView) view.findViewById(R.id.id_tab_shuaige);
        btnShuaige.setOnClickListener(tabListener);

        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    //监听tab点击切换
    View.OnClickListener tabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.id_tab_meinv:
                    btnMeinv.setTextColor(Color.parseColor("#e71de7"));
                    btnShuaige.setTextColor(Color.parseColor("#404040"));
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.id_tab_shuaige:
                    btnMeinv.setTextColor(Color.parseColor("#404040"));
                    btnShuaige.setTextColor(Color.parseColor("#e71de7"));
                    mViewPager.setCurrentItem(1);
                    break;
            }
        }
    };

    //监听滑屏幕来改变tab的选中
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    btnMeinv.setTextColor(Color.parseColor("#e71de7"));
                    btnShuaige.setTextColor(Color.parseColor("#404040"));

                    break;
                case 1:
                    btnMeinv.setTextColor(Color.parseColor("#404040"));
                    btnShuaige.setTextColor(Color.parseColor("#e71de7"));

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
