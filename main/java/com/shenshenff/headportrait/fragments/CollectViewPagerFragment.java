package com.shenshenff.headportrait.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.activitys.ViewPagerActivity;
import com.shenshenff.headportrait.adapters.ForSdcardAdapter;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Felix on 2016/5/13.
 */


public class CollectViewPagerFragment extends Fragment implements AdapterView.OnItemClickListener{

    private GridView gridView;   //1.view
    private List<String> SDcardPathList = new ArrayList<>(); //2.数据
    private List<File> listFiles = new ArrayList<>();
    private ForSdcardAdapter mForSdcardAdapter;//3.适配器

    private File[] currentFiles;
    private int  currentNumber;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);
        initViews(view);
        initDatas();
        initEvents();

        return view;
    }

    private void initViews(View view) {
        gridView = (GridView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
    }

    private void initDatas() {
        currentNumber = readFileImagePaths();
        loadFileData();
        int imgHeight = (int) ((Util.getScreenWidth(getActivity()) - Util.dpChangepx(getActivity(), 4)) / 4);
        mForSdcardAdapter = new ForSdcardAdapter(getActivity(), SDcardPathList,imgHeight ,gridView);
        gridView.setAdapter(mForSdcardAdapter);
    }

    private void initEvents(){
        gridView.setOnItemClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        int i ;
        if ((i = readFileImagePaths()) != currentNumber){
            LogUtil.i("刷新数据");
            loadFileData();
            mForSdcardAdapter.notifyDataSetChanged();
            currentNumber = i;
        }
    }

    public int readFileImagePaths() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard, Consts.DIRNAME);
                listFiles = getFileSort(dir.getAbsolutePath());
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
        SDcardPathList.clear();
//        for (int i = 0; i < currentFiles.length; i++) {
//            String imgpath = currentFiles[i].getAbsolutePath();
//            if (imgpath.endsWith(".jpg")||imgpath.endsWith(".png")||imgpath.endsWith(".gif")){
//                //LogUtil.i("循环======================" + i + imgpath);
//                SDcardPathList.add(imgpath);
//            }
//        }
        for (File f : listFiles){
            SDcardPathList.add(f.getAbsolutePath());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
        intent.putStringArrayListExtra("imgUrls", (ArrayList<String>) SDcardPathList);
        intent.putExtra("position", position);
        intent.putExtra("tag", true);
        startActivity(intent);
    }



    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param path
     * @return
     */
    public static List<File> getFileSort(String path) {

        List<File> list = getFiles(path, new ArrayList<File>());

        if (list != null && list.size() > 0) {

            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }

                }
            });

        }

        return list;
    }

    /**
     *
     * 获取目录下所有文件
     *
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {

        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            LogUtil.i("文件夹存在");
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    LogUtil.i("文件路径"+ file.getAbsolutePath());
                    files.add(file);
                }
            }
        } else {
            LogUtil.i("不是文件夹"+realFile.getAbsolutePath());
        }
        return files;
    }
}
