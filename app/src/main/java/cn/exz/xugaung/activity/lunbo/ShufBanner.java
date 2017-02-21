package cn.exz.xugaung.activity.lunbo;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.R;

/**
 * 图片无限轮播View
 * Created by mdw on 2016/3/24.
 */
public class ShufBanner extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * 需要加载的图片地址
     */
    private List<String> mImages;

    /**
     * 显示的ViewPager
     */
    private ViewPager mVp;

    /**
     * ImageView
     */
    private List<ImageView> mImageViews;

    /**
     * 图片地址
     */
    private ShufBannerAdapter mAdapter;

    /**
     * 导航点的位置
     */
    private LinearLayout mNavigationLayout;

    /**
     * 是否开启轮播
     */
    private boolean isStartShuf = false;


    /**
     * 点击事件的位置
     */
    private int position = -1;

    /**
     * 点击事件的接口回掉
     */
    private ShufBannerClickListener mClickListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int showPonit = mVp.getCurrentItem();

            showPonit = showPonit + 1;

            //getChildCount()获取的是当前存在的子类，销毁机制
/*
            if (showPonit >= mImageViews.size()) {

                showPonit = 1;
            }*/


            //获得的是正常位置
            mVp.setCurrentItem(showPonit);

            if (isStartShuf) {
                sendEmptyMessageDelayed(1, 3000);
            }
        }
    };

    public ShufBanner(Context context) {
        super(context, null);
    }

    public ShufBanner(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.widget_shufbanner, this);

        initImageLoader();

        initViewPager();

    }

    public void startShuf(List<String> urls, boolean isStartShuf) {

        if (urls.size() == 0 || urls == null) {
            return;
        }
        /**
         * 清楚数据
         */
        clearAllData();


        mImages.addAll(urls);
        /**
         * 保存图片地址
         */
        saveUrl();


        /**
         * 根据图片url创建imgView
         */
        createImageView();

        /**
         * 生成导航点
         */
        addPoint2Navigation();


        //开始刷新
        mAdapter.notifyDataSetChanged();
        mVp.setCurrentItem(1);

        //发送循环请求
        this.isStartShuf = isStartShuf;
        if (this.isStartShuf && mImages.size() > 3) {
            handler.sendEmptyMessageAtTime(1, 2000);
        }
    }

    /**
     * 添加导航点到布局中
     */
    private void addPoint2Navigation() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int pointPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        params.setMargins(pointPadding, 0, pointPadding, 0);
        for (int i = 0; i < mImageViews.size() - 2; i++) {
            ImageView point = new ImageView(getContext());
            point.setLayoutParams(params);
            point.setImageResource(R.drawable.select_navgation_point);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            mNavigationLayout.addView(point);
        }
    }

    /**
     * 清除图片数据，停止轮播
     */
    public void clearAllData() {
        mImages.clear();
        mImageViews.clear();
        mNavigationLayout.removeAllViews();

        handler.removeMessages(1);
        mAdapter.notifyDataSetChanged();
        position = -1;
        isStartShuf = false;
    }

    /**
     * 创建布局
     */
    private void createImageView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        for (String url : mImages) {
            ImageView imgView = new ImageView(getContext());
            imgView.setLayoutParams(params);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(url, imgView);
            imgView.setOnClickListener(this);
            mImageViews.add(imgView);
        }
    }

    private void saveUrl() {

        String startImageUrl = mImages.get(0);
        String endImageUrl = mImages.get(mImages.size() - 1);

        mImages.add(0, endImageUrl);
        mImages.add(mImages.size(), startImageUrl);
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mVp = ((ViewPager) findViewById(R.id.vp));

        mImages = new ArrayList<>();

        mImageViews = new ArrayList<>();

        mNavigationLayout = (LinearLayout) findViewById(R.id.ll_navigation);

        mAdapter = new ShufBannerAdapter(getContext(), mImageViews);

        mVp.setAdapter(mAdapter);

        mVp.addOnPageChangeListener(this);
        mVp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PointF downP = new PointF();
                PointF curP = new PointF();
                int act = event.getAction();
                if (act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_MOVE || act == MotionEvent.ACTION_UP) {
                    ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                    if (downP.x == curP.x && downP.y == curP.y) {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    WipeLayoutEnabled setWipeLayoutEnabled;

    public void setWipeLayoutEnabled(WipeLayoutEnabled setWipeLayoutEnabled) {
        this.setWipeLayoutEnabled = setWipeLayoutEnabled;
    }


    public interface WipeLayoutEnabled {
        void onWipeLayoutEnabled(boolean isEnbled);
    }

    /**
     * 初始化imageLoader
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        if (positionOffset == 0) {
            if (position == 0) {
                mVp.setCurrentItem(mImageViews.size() - 2, false);
            }

            if (position == mImageViews.size() - 1) {
                mVp.setCurrentItem(1, false);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        /**
         * 修改当前点击点的位置
         */
        changePosition(position);

        /**
         * 修改点的状态
         */
        changePointState(position);
    }

    /**
     * 改变当前点击的点
     *
     * @param position
     */
    private void changePosition(int position) {
        if (position == 0) {
            position = mImageViews.size() - 2;
        }
        if (position == mImageViews.size() - 1) {
            position = 1;
        }

        position--;
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == 1) {
            setWipeLayoutEnabled.onWipeLayoutEnabled(false);
        } else if (state == 0) {
            setWipeLayoutEnabled.onWipeLayoutEnabled(true);
        }
    }

    /**
     * 改变导航点的状态
     *
     * @param position
     */
    private void changePointState(int position) {

        if (position == 0) {
            position = mImageViews.size() - 2;
        }
        if (position == mImageViews.size() - 1) {
            position = 1;
        }

        position = position - 1;

        for (int i = 0; i < mNavigationLayout.getChildCount(); i++) {
            if (i == position) {
                mNavigationLayout.getChildAt(i).setEnabled(true);

            } else {
                mNavigationLayout.getChildAt(i).setEnabled(false);
            }
        }
    }

    public void setItemClcikListener(ShufBannerClickListener l) {
        mClickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {

            mClickListener.onClick(position);
        }
    }
}