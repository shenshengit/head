package com.shenshenff.headportrait.utils;

import com.shenshenff.headportrait.modes.SortItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 2016/4/22.
 */
public class JsonUtil {

    /**
     *  Json字符串 解析成 图片链接集合 集合
     */
    public static List<String> parseImageUrlWithJSONString(String jsonData) {
        if (jsonData != null) {
            List<String>  imgurls = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0, length = jsonArray.length(); i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String url = jsonObject.getString("img_url");
                    imgurls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return imgurls;
        } else {
            return null;
        }

    }

    /**
     * 解析的JSON 【分类】和【推荐】
     */
    public static List<SortItem> parseSortWithJSONFileString(String jsonData, String tag) {
        if (jsonData != null) {
            List<SortItem> sortItems = new ArrayList<>();
            SortItem sortItem ;
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray(tag);
                for (int i = 0, length = jsonArray.length(); i < length; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");                            //id
                    String name = jsonObject.getString("sortname");              //类型名称
                    String coverImgUrl = jsonObject.getString("coverimg_url");   //封面图片
                    String contentUrl = jsonObject.getString("content_url");     //分类的连接
                    sortItem = new SortItem(id, name, coverImgUrl, contentUrl);
                    //LogUtil.i("aaa",sortItem.toString());
                    sortItems.add(sortItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sortItems;
        }
        return null;
    }





}
