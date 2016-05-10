package com.shenshenff.headportrait.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.views.HeaderGridView;

/**
 * Created by shenshen on 16/3/14.
 */
public class ForSdcardAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private float itemHeight;
    private LinearLayout.LayoutParams lp;
    private LayoutInflater inflater;

    private String[] mDatas;
    private HeaderGridView mGridView;


    //缓存
    private LruCache<String, Bitmap> mCaches;


    public ForSdcardAdapter(Context context, String[] imagepaths, float itemHeight, HeaderGridView gridView) {

        this.inflater = LayoutInflater.from(context);
        if (imagepaths.length == 0) {
            LogUtil.i("数据为空");
            imagepaths = new String[]{"", "", "", ""};
            this.mDatas = imagepaths;
        } else {
            this.mDatas = imagepaths;
        }

        this.itemHeight = itemHeight;
        this.mGridView = gridView;

        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) itemHeight);
        mGridView.setOnScrollListener(this);

        //缓存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

    }

    @Override
    public int getCount() {
        return mDatas.length;
    }

    @Override
    public Object getItem(int position) {
        return mDatas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_reaommend_gridview, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setLayoutParams(lp);
        viewHolder.imageView.setTag(mDatas[position]);
        showImageByAsyncTask(viewHolder.imageView, mDatas[position]);
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }


    public void setImageView(ImageView imageView, String imagePath) {
        Bitmap bitmap = null;
        bitmap = getBitmapFromFile(imagePath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.img_bg);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            // LogUtil.i("停止");
        } else {
            // LogUtil.i("滑动");
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//        mFirstVisibleItem = firstVisibleItem;
//        mVisibleItemCount = visibleItemCount;
//
//        if (isFirstEnter && visibleItemCount > 0) {
//            LogUtil.e("mFirstVisibleItem" + mFirstVisibleItem + "mVisibleItemCount" +mVisibleItemCount);
//            loadBitmap(firstVisibleItem, visibleItemCount);
//            isFirstEnter =false;
//        }

    }

    private void loadBitmap(int firstVisibleItem, int visibleItemCount) {
//        LogUtil.i("loadBitmap方法执行了");
//        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
//            String imgpath = datas.get(i);
//            LogUtil.i("循环开启任务" + i);
//            BitmapWorkerTask task = new BitmapWorkerTask();
//            taskCollection.add(task);
//            task.execute(imgpath);
//        }

    }

    public void showImageByAsyncTask(ImageView imageView, String filepath) {

        Bitmap bitmap = getBitmapFromCache(filepath);
        if (bitmap == null) {
            new BitmapWorkerTask().execute(filepath);
        } else {
            imageView.setImageBitmap(bitmap);
        }


    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private String imgpath;

        @Override
        protected Bitmap doInBackground(String... params) {
            imgpath = params[0];
            Bitmap bitmap = getBitmapFromFile(imgpath);
            if (bitmap != null) {
                addBitmapToCache(imgpath, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mGridView.findViewWithTag(imgpath);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            //taskCollection.remove(this);
        }


    }

    private Bitmap getBitmapFromFile(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateInSampleSize(options, (int) itemHeight, (int) itemHeight);// 调用上面定义的方法计算inSampleSize值
        //LogUtil.i("原始图片大小=" + options.outWidth + "X" + options.outHeight);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options);// 使用获取到的inSampleSize值再次解析图片
        //LogUtil.i("压缩之后=" + bm.getWidth() + "X" + bm.getHeight());
        int i = 0;
        if (bm != null) {
            if (bm.getWidth() <= bm.getHeight()) {
                i = bm.getWidth();
            } else {
                i = bm.getHeight();
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, i, i);
        //LogUtil.i("去正方形=" + bitmap.getWidth() + "X" + bitmap.getHeight());
        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    // 缓存 ---  添加到缓存
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null) {
            mCaches.put(url, bitmap);
        }
    }

    // 缓存 ---  从缓存中获取
    public Bitmap getBitmapFromCache(String url) {
        return mCaches.get(url);
    }


}
