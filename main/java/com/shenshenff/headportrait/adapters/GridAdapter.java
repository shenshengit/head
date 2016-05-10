package com.shenshenff.headportrait.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shenshen on 16/3/10.
 *
 * 用于美女模块
 *
 */
public class GridAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> imageDatas;
    private float itenHeight;
    LinearLayout.LayoutParams lp;

    public GridAdapter(Context mContext, List<String> imageDatas, float itemHeight) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.imageDatas = imageDatas;
        this.itenHeight = itemHeight;
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) itenHeight);
    }

    @Override
    public int getCount() {
        return imageDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return imageDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_reaommend_gridview,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setLayoutParams(lp);
        Picasso.with(mContext).load(imageDatas.get(position)).into(viewHolder.imageView);
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }



}
