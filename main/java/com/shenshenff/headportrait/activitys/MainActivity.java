package com.shenshenff.headportrait.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.fragments.CollectFragment;
import com.shenshenff.headportrait.fragments.RecommendFragment;
import com.shenshenff.headportrait.fragments.WallpaperFragment;
import com.shenshenff.headportrait.utils.FileUtil;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.utils.Networkutil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button tab1, tab3, tab4;
    private RecommendFragment mRecommendFragment;
    private WallpaperFragment mWallpaperFragment;
    private CollectFragment mCollectFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //透明状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mFragmentManager = getSupportFragmentManager();
        initView();
        setTabSelection(1);
        setTools(tab1, 1);

        if (Networkutil.isNetwork(this)) {
            //Toast.makeText(MainActivity.this, "有网络",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "无网络链接", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        tab1 = (Button) findViewById(R.id.id_tools1);
        tab3 = (Button) findViewById(R.id.id_tools3);
        tab4 = (Button) findViewById(R.id.id_tools4);
        tab1.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_tools1:
                setTools(tab1, 1);
                setTabSelection(1);
                break;
            case R.id.id_tools3:
                setTools(tab3, 2);
                setTabSelection(3);
                break;
            case R.id.id_tools4:
                setTools(tab4, 3);
                setTabSelection(4);
                break;
        }
    }

    private void setTools(Button v, int i) {
        tab1.setTextColor(Color.BLACK);
        tab1.setSelected(false);
        tab3.setTextColor(Color.BLACK);
        tab3.setSelected(false);
        tab4.setTextColor(Color.BLACK);
        tab4.setSelected(false);
        if (i == 1) {
            v.setTextColor(Color.parseColor("#3a9ff4"));
        }
        if (i == 2) {
            v.setTextColor(Color.parseColor("#6dd421"));
        }
        if (i == 3) {
            v.setTextColor(Color.parseColor("#ff8320"));
        }
        v.setSelected(true);
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 1:
                if (mRecommendFragment == null) {
                    mRecommendFragment = new RecommendFragment();
                    transaction.add(R.id.fragment_layout, mRecommendFragment);
                } else {
                    transaction.show(mRecommendFragment);
                }
                break;
            case 3:
                if (mWallpaperFragment == null) {
                    mWallpaperFragment = new WallpaperFragment();
                    transaction.add(R.id.fragment_layout, mWallpaperFragment);
                } else {
                    transaction.show(mWallpaperFragment);
                }
                break;
            case 4:
                if (mCollectFragment == null) {
                    mCollectFragment = new CollectFragment();
                    transaction.add(R.id.fragment_layout, mCollectFragment);
                } else {
                    transaction.show(mCollectFragment);

                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mRecommendFragment != null) {
            transaction.hide(mRecommendFragment);
        }
        if (mWallpaperFragment != null) {
            transaction.hide(mWallpaperFragment);
        }
        if (mCollectFragment != null) {
            transaction.hide(mCollectFragment);
            mCollectFragment.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File cacheFile = new File(Environment.getExternalStorageDirectory() + Consts.CACHEDIRNAME);
            if (cacheFile.exists()) {
                FileUtil.delete(cacheFile);
                LogUtil.i("删除临时文件夹");
            }
        }
    }


}
