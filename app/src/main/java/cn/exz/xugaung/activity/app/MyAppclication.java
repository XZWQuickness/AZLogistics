package cn.exz.xugaung.activity.app;


import android.app.Application;

import com.lzy.okgo.OkGo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.x;

import cn.exz.xugaung.activity.R;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by pc on 2016/7/11.
 */
public class MyAppclication extends Application {
    public static String address = "获取地址失败,请开启定位权限!";
    public static double latitude = 0.0;// 获取纬度
    public static double longitude = 0.0;//获取经度
    public static String city = "徐州";

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);
//必须调用初始化
        OkGo.init(this);
        // 初始化ImageLoader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(20 * 1024 * 1024)//
                .discCacheFileCount(100).build();
        ImageLoader.getInstance().init(config);
    }



}
