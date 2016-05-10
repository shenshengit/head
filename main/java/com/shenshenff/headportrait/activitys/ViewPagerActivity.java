package com.shenshenff.headportrait.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shenshenff.headportrait.R;
import com.shenshenff.headportrait.constant.Consts;
import com.shenshenff.headportrait.utils.ImageLoad;
import com.shenshenff.headportrait.utils.LogUtil;
import com.shenshenff.headportrait.utils.ToastUtil;
import com.shenshenff.headportrait.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by shenshen on 16/3/29.
 */
public class ViewPagerActivity extends Activity implements View.OnClickListener {

    private List<String> imgUrls;
    private int currentPosition;
    private ViewPager viewPager;

    private ImageButton btnBack, btnPreview, btnDownload;
    private TextView zz;//遮罩
    private LinearLayout ll;//预览布局

    private Uri uri;//裁切的位置

    private ImageView iv_QQ, iv_wx;
    private TextView caiqie, baocun;

    private boolean isCollect = false;//是否是本地
    private boolean isCaiqie = false;//是否裁切了

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //裁切之后注册点击事件并修改字体颜色
            baocun.setOnClickListener(ViewPagerActivity.this);
            baocun.setTextColor(Color.parseColor("#000000"));
            Bitmap bitmap = (Bitmap) msg.obj;
            iv_QQ.setImageBitmap(Util.toRoundBitmap(bitmap, true));
            iv_wx.setImageBitmap(Util.toRoundBitmap(bitmap, false));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initDatas();
        ininViews();
        startBtnOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ll.getVisibility() != View.VISIBLE) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnInAnima();
                }
            }, 500);
        }
    }

    private void initDatas() {
        Intent intent = getIntent();
        imgUrls = intent.getStringArrayListExtra("imgUrls");
        currentPosition = intent.getIntExtra("position", 0);
        isCollect = intent.getBooleanExtra("tag", false);
        LogUtil.i("当前路径" + imgUrls.get(currentPosition));
        LogUtil.i("当前位置" + currentPosition);
        if (isCollect) {
            LogUtil.i("来之本地");
        } else {
            LogUtil.i("网络");
        }
    }

    private void ininViews() {
        viewPager = (ViewPager) findViewById(R.id.id_item_viewpager);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnBack = (ImageButton) findViewById(R.id.btn_1);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPreview = (ImageButton) findViewById(R.id.btn_2);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOutAnima();
                if (isCollect) {
                    iv_QQ.setImageBitmap(Util.toRoundBitmap(BitmapFactory.decodeFile(imgUrls.get(currentPosition)), true));
                    iv_wx.setImageBitmap(Util.toRoundBitmap(BitmapFactory.decodeFile(imgUrls.get(currentPosition)), false));
                    baocun.setTextColor(Color.parseColor("#EEEEEE"));
                } else {
                    new ImageLoad().showImageByThread(iv_QQ, imgUrls.get(currentPosition), true);
                    new ImageLoad().showImageByThread(iv_wx, imgUrls.get(currentPosition), false);
                }
            }
        });

        btnDownload = (ImageButton) findViewById(R.id.btn_3);
        if (isCollect) {
            btnDownload.setBackgroundResource(R.drawable.btn_c4);
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除图片
                    deleteImage(imgUrls.get(currentPosition));
                    //退出当前页面
                    ViewPagerActivity.this.finish();
                }
            });
        } else {
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MyAsyncTask(false).execute(imgUrls.get(currentPosition));
                }
            });
        }
        zz = (TextView) findViewById(R.id.zz);
        zz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOutAnina();
            }
        });
        ll = (LinearLayout) findViewById(R.id.layout);
        ll.setOnTouchListener(new View.OnTouchListener() { //屏蔽穿透点击事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        iv_QQ = (ImageView) findViewById(R.id.imageview_qq);
        iv_wx = (ImageView) findViewById(R.id.imageview_wx);

        caiqie = (TextView) findViewById(R.id.caiqie);
        baocun = (TextView) findViewById(R.id.save);
        caiqie.setOnClickListener(this);
        if (isCollect) { //本地打开，在么裁切的时候，不注册保存按钮的点击事件

        } else {
            baocun.setOnClickListener(this);
        }

    }

    //按钮事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.caiqie:
                if (isCollect) {
                    //在本地裁切
                    startCaiqie();
                } else {
                    //网络图片裁切
                    //1.把图片下载到临时文件夹
                    //2.取出下载好的图片来裁切 (注意：这里是异步操作要在Task里执行)
                    new MyAsyncTask(true).execute(imgUrls.get(currentPosition));
                }
                break;

            case R.id.save:
                if (isCollect) {
                    if (!isCaiqie) {
                        //1.本地没裁切 ==》 不能点击
                    } else {
                        layoutOutAnina();
                        copyImageToLocal();//2.本地已裁切 ==》 从临时文件夹拷贝裁切好的图片到本地
                    }
                } else {
                    layoutOutAnina();
                    if (!isCaiqie) {
                        new MyAsyncTask(false).execute(imgUrls.get(currentPosition));  //3.网络没裁切 ==》 可以点击默认下载图片
                    } else {
                        copyImageToLocal();//4.网络已裁切 ==》从临时文件夹拷贝裁切好的图片到本地
                    }
                }
                break;
        }
    }

    /**
     * 从需要裁切的图片地址拿到Uri，然后启动裁切Activity
     */
    private void startCaiqie() {

        File file;
        if (isCollect) {
            file = new File(imgUrls.get(currentPosition));
        } else {
            file = new File(Environment.getExternalStorageDirectory() + Consts.CACHEDIRNAME + "/b.jpg");
        }
        if (file.exists()) {
            Uri fileUri = Uri.fromFile(file);
            File f = null;
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard, Consts.CACHEDIRNAME);
                if (!dir.exists()) {
                    dir.mkdirs();
                    LogUtil.i("创建临时文件夹");
                }
                f = new File(dir + "/a.jpg");
                if (f.exists()) {
                    f.delete();
                    LogUtil.i("删除临时图片");
                }
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            uri = Uri.fromFile(f);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(fileUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 8);
        }
    }

    /**
     * 从临时文件加拷贝裁切好的图到本地文件夹
     */
    private void copyImageToLocal() {
        //裁切后图片保存的文件名：思路，从文件存储中读取一个值用作文件名，用过之后递增，然后覆盖保存

        //文件从临时文件夹复制到本地文件夹
        File fromFile = new File(Environment.getExternalStorageDirectory() + Consts.CACHEDIRNAME + "/a.jpg");
        String newName;
        //读取文件名
        String str = Environment.getExternalStorageDirectory() + "/" + Consts.CACHEDIRNAME;
        File dir = new File(str);

        if (!dir.exists()) {
            dir.mkdirs();
            LogUtil.i("创建文件夹");
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Consts.CAIJIAN_CACHE_FILENAME, MODE_PRIVATE);
        int n = sharedPreferences.getInt("caiqian", 0);
        newName = Environment.getExternalStorageDirectory() + Consts.DIRNAME + "/img" + String.valueOf(n) + ".jpg";
        n = n + 1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("caiqian", n);
        editor.commit();
        LogUtil.i("新名字" + newName);
        File toFile = new File(newName);
        if (fromFile.exists()) {
            String s = fromFile.getAbsolutePath();
            LogUtil.i(s);
            try {
                if (toFile.exists()) {
                    toFile.delete();
                }
                toFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(fromFile);
                fos = new FileOutputStream(toFile);
                byte[] bytes = new byte[1024];
                try {
                    while (fis.read(bytes) != -1) {
                        fos.write(bytes, 0, 1024);
                        fos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                ToastUtil.showNoRepeat(ViewPagerActivity.this, "裁剪图片保存成功", Toast.LENGTH_SHORT);
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //返回键判断
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (ll.getVisibility() == View.VISIBLE) {
                layoutOutAnina();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //加载ViewPager
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(ViewPagerActivity.this, R.layout.activity_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            if (isCollect) {
                Picasso.with(ViewPagerActivity.this).load(new File(imgUrls.get(position))).into(imageView);
            } else {
                Picasso.with(ViewPagerActivity.this).load(imgUrls.get(position)).into(imageView);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 下载图片，根据参数来判断是否是下载到【临时文件夹】还是【本地文件夹】
     */
    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        //是否下载到临时文件夹
        private boolean isLinshi = false;

        public MyAsyncTask(boolean is) {
            isLinshi = is;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/" + Consts.DIRNAME);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File shen = null;
                try {
                    if (isLinshi) {
                        String str = sdcard.getAbsolutePath() + "/" + Consts.CACHEDIRNAME;
                        File lsdir = new File(str);
                        if (!lsdir.exists()) {
                            lsdir.mkdirs();
                        }
                        shen = new File(lsdir + "/b.jpg");
                    } else {
                        String name = Util.imgName(params[0]);
                        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")){

                        } else {
                            name = name+".jpg";
                        }
                        shen = new File(dir + "/" + name);
                    }
                    if (shen.exists()) {
                        shen.delete();
                    }
                    shen.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LogUtil.i("保存图片名称" + shen.getAbsolutePath());
                URL url;
                Bitmap bitmap = null;
                FileOutputStream fos = null;
                InputStream in = null;
                try {
                    url = new URL(params[0]);
                    URLConnection connection;
                    connection = url.openConnection();
                    in = connection.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(in);
                    fos = new FileOutputStream(shen);
                    byte[] b = new byte[2 * 1024];
                    int len;
                    if (bis != null) {
                        while ((len = bis.read(b)) != -1) {
                            fos.write(b, 0, len);
                        }
                    }
                    bitmap = BitmapFactory.decodeFile(shen.getAbsolutePath());
                    return bitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                if (isLinshi) {
                    startCaiqie();
                } else {
                    ToastUtil.showNoRepeat(ViewPagerActivity.this, "收藏成功", Toast.LENGTH_SHORT);
                }
            }

        }
    }

    //动画
    /**
     * 首页进入界面，瞬间把按钮移除屏幕外
     */
    private void startBtnOut() {
        ObjectAnimator.ofFloat(btnBack, "translationY", 0f, 500f).setDuration(10).start();
        ObjectAnimator.ofFloat(btnPreview, "translationY", 0f, 500f).setDuration(10).start();
        ObjectAnimator.ofFloat(btnDownload, "translationY", 0f, 500f).setDuration(10).start();
    }

    private void btnOutAnima() {
        ObjectAnimator a1 = ObjectAnimator.ofFloat(btnBack, "translationY", 0f, 500f).setDuration(300);
        a1.setStartDelay(0);
        a1.setInterpolator(new AccelerateInterpolator());
        a1.start();
        ObjectAnimator a2 = ObjectAnimator.ofFloat(btnPreview, "translationY", 0f, 500f).setDuration(280);
        a2.setStartDelay(100);
        a2.setInterpolator(new AccelerateInterpolator());
        a2.start();
        ObjectAnimator a3 = ObjectAnimator.ofFloat(btnDownload, "translationY", 0f, 500f).setDuration(260);
        a3.setStartDelay(200);
        a3.setInterpolator(new AccelerateInterpolator());
        a3.start();
        a3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutInAnina();
                ll.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(zz, "alpha", 0f, 1f).setDuration(500).start();
                zz.setVisibility(View.VISIBLE);
            }

        });
    }

    private void btnInAnima() {
        ObjectAnimator a1 = ObjectAnimator.ofFloat(btnBack, "translationY", 500f, 0f).setDuration(300);
        a1.setStartDelay(0);
        a1.setInterpolator(new OvershootInterpolator());
        a1.start();
        ObjectAnimator a2 = ObjectAnimator.ofFloat(btnPreview, "translationY", 500f, 0f).setDuration(300);
        a2.setStartDelay(100);
        a2.setInterpolator(new OvershootInterpolator());
        a2.start();
        ObjectAnimator a3 = ObjectAnimator.ofFloat(btnDownload, "translationY", 500f, 0f).setDuration(300);
        a3.setStartDelay(200);
        a3.setInterpolator(new OvershootInterpolator());
        a3.start();
    }

    private void layoutOutAnina() {
        ObjectAnimator l1 = ObjectAnimator.ofFloat(ll, "translationY", 0f, 1500f).setDuration(400);
        ObjectAnimator.ofFloat(zz, "alpha", 1f, 0f).setDuration(300).start();
        l1.setStartDelay(0);
        l1.setInterpolator(new AccelerateInterpolator());
        l1.start();
        l1.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                zz.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                //解除保存按钮的按钮事件

                isCaiqie = false;
                btnInAnima();
            }
        });
    }

    private void layoutInAnina() {
        ObjectAnimator l1 = ObjectAnimator.ofFloat(ll, "translationY", 1500f, 0f).setDuration(200);
        l1.setStartDelay(0);
        l1.setInterpolator(new AccelerateDecelerateInterpolator());
        l1.start();
    }

    //删除图片
    private void deleteImage(String imgPath) {
        File img = new File(imgPath);
        if (img.exists()) {
            img.delete();
            Toast.makeText(ViewPagerActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    //裁切返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 8:
                if (resultCode == RESULT_OK) {
                    isCaiqie = true;
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.obj = bitmap;
                    message.what = 1211;
                    handler.sendMessage(message);
                    if (isCollect) {
                        baocun.setOnClickListener(null);
                    }
                }
                break;
        }
    }
}
