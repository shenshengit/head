package com.shenshenff.headportrait.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.activitys.SortListitemActivity;
import com.shenshenff.headportrait.activitys.ViewPagerActivity;
import com.shenshenff.headportrait.adapters.RecyclerAdapter;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.modes.SortItem;
import com.shenshenff.headportrait.utils.FileUtil;
import com.shenshenff.headportrait.utils.HttpUtil;
import com.shenshenff.headportrait.utils.JsonUtil;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.views.MyLinearLayout;
import com.shenshenff.headportrait.views.MyLinearLayout2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenshen on 16/3/9.
 */
public class RecommendFragment extends Fragment {

    private SortItem sortItem; //单个分类的实体类
    private List<SortItem> sortLists; //recyclerView的数据,分类的数据源
    private List<SortItem> tuijianLists; //推荐的bean对象集合

    private RecyclerView recyclerView; //首页的分类
    private RecyclerAdapter recyclerAdapter; //recyclerView适配器

    private MyLinearLayout mb1, mb2, mb3, mb4;
    private MyLinearLayout2 ql;  //情侣

    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Handler 用户网络请求到数据加载给UI的
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1100:
                    jiazaishouju(0, mb1);
                    break;
                case 1101://情侣是特殊的
                    jiazaishouju2(1, ql);
                    break;
                case 1102:
                    jiazaishouju(2, mb2);
                    break;
                case 1103:
                    jiazaishouju(3, mb3);
                    break;
                case 1104:
                    jiazaishouju(4, mb4);
                    break;
                case 2201:

                    recyclerAdapter = new RecyclerAdapter(getActivity(), sortLists);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (sortLists == null || sortLists.size() == 0) {
                                return;
                            } else {
                                startActivityforFenlei(sortLists.get(position));
                            }
                        }
                    });
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        sortLists = new ArrayList<>();
        tuijianLists = new ArrayList<>();
        String jsonStringFeilei = FileUtil.loadJSONString(getActivity(), Consts.FILENAME_FEILEI);   //获取 分类 的数据
        sortLists = JsonUtil.parseSortWithJSONFileString(jsonStringFeilei, "fenlei");     //从分类js 解析成 分类bean对象

        tuijianLists = JsonUtil.parseSortWithJSONFileString(jsonStringFeilei, "tuijian"); //从分类js 解析成 推荐bean对象
        getSortContentWithContentUrl();         //从对象中网络请求每个单独的分类 json字符串
    }

    private void initView(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.top_rectckerview);

        mb1 = (MyLinearLayout) view.findViewById(R.id.id_sl_rm1);
        mb2 = (MyLinearLayout) view.findViewById(R.id.id_sl_rm2);
        mb3 = (MyLinearLayout) view.findViewById(R.id.id_sl_rm3);
        mb4 = (MyLinearLayout) view.findViewById(R.id.id_sl_rm4);

        ql = (MyLinearLayout2) view.findViewById(R.id.id_sl_ql);

        Message msg = new Message();
        msg.what = 2201;
        handler.sendMessage(msg);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Message msg = new Message();
                        msg.what = 2201;
                        handler.sendMessage(msg);
                    }
                }, 3000);

            }
        });
    }


    /**
     * 在mainActivity调用，来控制ScrollView滑动的位置
     */
    public void setScrollViewtoTop() {
        //scrollView.smoothScrollTo(0, scrollView.getScrollY());
    }

    /**
     * 进入分类
     */
    private void startActivityforFenlei(SortItem sortItem) {
        if (sortItem != null) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), SortListitemActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("sortItem", sortItem);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 进入单个分类里
     *
     * @param imageUrls 单个分类所有的图片的链接
     * @param position  点击是那张图片
     */
    private void startActivityToSortItem(List<String> imageUrls, int position) {
        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
        intent.putStringArrayListExtra("imgUrls", (ArrayList<String>) imageUrls);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * 从分类的对象中得到每条分类中的内容json字符串
     */
    private void getSortContentWithContentUrl( ) {
        if (tuijianLists != null) {
            for (int i = 0; i < tuijianLists.size(); i++) {
                final String url = Consts.URL_HEAD + tuijianLists.get(i).getContentUrl();
                final int finalI = i;
                HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        tuijianLists.get(finalI).setContentJsonStr(response);
                        LogUtil.i(tuijianLists.get(finalI).toString());

                        //存储分类的每个对象
                        //FileUtil.saveObject(getActivity(), tuijian.getContentUrl(), tuijian);
                        //加载第一个模板的数据
                        if (response != null) {
                            Message msg = new Message();
                            msg.what = 1100 + finalI;
                            handler.sendMessage(msg);
                            LogUtil.i("发送请求第" + (1100 + finalI));
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        LogUtil.e("读取分类的" + tuijianLists.get(finalI).getName() + "条错误");
                    }
                });
            }
        }

    }

    private void jiazaishouju(final int poistion, MyLinearLayout view) {

        final int poistionA;
        if (poistion > 5 || poistion < 0) {
            poistionA = 0;
        } else {
            poistionA = poistion;
        }

        final List<String> urls = JsonUtil.parseImageUrlWithJSONString(tuijianLists.get(poistionA).getContentJsonStr());
        view.titleText.setText(tuijianLists.get(poistionA).getName());
        view.moveBtn.setText("更多" + tuijianLists.get(poistionA).getName());
        if (urls != null) {
            Picasso.with(getActivity()).load(urls.get(0)).into(view.img1);
            Picasso.with(getActivity()).load(urls.get(1)).into(view.img2);
            Picasso.with(getActivity()).load(urls.get(2)).into(view.img3);
            Picasso.with(getActivity()).load(urls.get(3)).into(view.img4);
            Picasso.with(getActivity()).load(urls.get(4)).into(view.img5);
            Picasso.with(getActivity()).load(urls.get(5)).into(view.img6);

            view.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 0);
                }
            });
            view.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 1);
                }
            });
            view.img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 2);
                }
            });
            view.img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 3);
                }
            });
            view.img5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 4);
                }
            });
            view.img6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 5);
                }
            });
            view.moveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityforFenlei(tuijianLists.get(poistionA));
                }
            });
        }
    }

    private void jiazaishouju2(final int poistion, MyLinearLayout2 view) {

        final int poistionA;
        if (poistion > 5 || poistion < 0) {
            poistionA = 0;
        } else {
            poistionA = poistion;
        }
        final List<String> urls = JsonUtil.parseImageUrlWithJSONString(tuijianLists.get(poistionA).getContentJsonStr());
        view.titleText.setText(tuijianLists.get(poistionA).getName());
        view.moveBtn.setText("更多" + tuijianLists.get(poistionA).getName());
        if (urls != null) {
            Picasso.with(getActivity()).load(urls.get(0)).into(view.img1);
            Picasso.with(getActivity()).load(urls.get(1)).into(view.img2);
            Picasso.with(getActivity()).load(urls.get(2)).into(view.img3);
            Picasso.with(getActivity()).load(urls.get(3)).into(view.img4);
            Picasso.with(getActivity()).load(urls.get(4)).into(view.img5);
            Picasso.with(getActivity()).load(urls.get(5)).into(view.img6);

            view.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 0);
                }
            });
            view.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 1);
                }
            });
            view.img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 2);
                }
            });
            view.img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 3);
                }
            });
            view.img5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 4);
                }
            });
            view.img6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityToSortItem(urls, 5);
                }
            });
            view.moveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityforFenlei(tuijianLists.get(poistionA));
                }
            });
        }
    }


}
