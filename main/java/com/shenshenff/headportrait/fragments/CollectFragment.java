package com.shenshenff.headportrait.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenshenff.headportrait.R;

//import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;

/**
 * Created by shenshen on 16/3/9.
 * <p/>
 * 读取SD卡里的图片
 * 加载到GridView
 */
public class CollectFragment extends Fragment {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.id_stickynavlayout_viewpager);
        mPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new CollectViewPagerFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }
}

