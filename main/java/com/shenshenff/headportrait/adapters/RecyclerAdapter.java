package com.shenshenff.headportrait.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.modes.SortItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenshen on 16/3/19.
 * 用于首页分类
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<SortItem> mDatas;


    public RecyclerAdapter(Context mContext, List<SortItem> datas) {
        this.mContext = mContext;
        if (datas.size() == 0 || datas == null) {
            //当数据没过来，也可以显示5个空白的
            this.mDatas = new ArrayList<>();
            for(int i =0; i<5; i++){
                mDatas.add(new SortItem());
            }
        } else {
            this.mDatas = datas;
        }
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview_imageview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Picasso.with(mContext).load(mDatas.get(position).getCoverImgUrl()).into(holder.imageview);
        holder.textView.setText(mDatas.get(position).getName());
       // LogUtil.i(mDatas.get(position).getName());
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.item_iv);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }

    //点击事件监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //成员变量
    private OnItemClickListener onItemClickListener;

    //公共方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
