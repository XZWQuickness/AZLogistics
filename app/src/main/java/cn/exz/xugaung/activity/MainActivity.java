package cn.exz.xugaung.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.animation.ZoomOutPageTransformer;
import cn.exz.xugaung.activity.app.LocalService;
import cn.exz.xugaung.activity.app.RemoteService;
import cn.exz.xugaung.activity.fragemt.HomeFragment;
import cn.exz.xugaung.activity.fragemt.MinFragment;
import cn.exz.xugaung.activity.fragemt.OrderFragment;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.view.MyViewPager;

/**
 * Created by pc on 2016/7/11.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener, RadioGroup
        .OnCheckedChangeListener {

    private List<Fragment> mListViews;
    @ViewInject(R.id.rg)
    private RadioGroup rg;


    @ViewInject(R.id.view_pager)
    private MyViewPager view_pager;


    @ViewInject(R.id.rb_main_1)
    private RadioButton rb_main_1;


    @ViewInject(R.id.rb_main_2)
    private RadioButton rb_main_2;

    @ViewInject(R.id.rb_main_3)
    private RadioButton rb_main_3;


    private HomeFragment home;
    private OrderFragment order;
    private MinFragment min;

    private boolean bannerIsMOVE = true;

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private Thread thread;


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, RemoteService.class));
        startService(new Intent(this, LocalService.class));
    }

    public void initView() {


        if (home == null) {
            home = new HomeFragment();
        }
        if (order == null) {
            order = new OrderFragment();
        }
        if (min == null) {
            min = new MinFragment();
        }
        mListViews = new ArrayList<Fragment>();
        mListViews.add(home);
        mListViews.add(order);
        mListViews.add(min);
        view_pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mListViews));
        view_pager.addOnPageChangeListener(new MypagerChaeLister());
        view_pager.setOffscreenPageLimit(3);
        view_pager.setPageTransformer(true, new ZoomOutPageTransformer());
        rg.setOnCheckedChangeListener(this);
    }
//    PowerManager powerManager = null;
//    PowerManager.WakeLock wakeLock = null;
    public void initData() {
//        this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
//        this.wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK , "My Lock");
//        this.wakeLock.acquire();
    }



    public void onClick(View v) {
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch (i) {
            case R.id.rb_main_1:
                view_pager.setCurrentItem(0);
                break;
            case R.id.rb_main_2:
                view_pager.setCurrentItem(1);
                break;
            case R.id.rb_main_3:
                view_pager.setCurrentItem(2);
                break;
        }


    }


    /**
     * viewpager适配器
     */
    class MyPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }


        public void setList(List<Fragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    class MypagerChaeLister implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) { //选中
            if (bannerIsMOVE == true) {
                EventBus.getDefault().post(new MainSendEvent(position));
                switch (position) {
                    case 0:
                        rb_main_1.setChecked(true);
                        break;

                    case 1:
                        rb_main_2.setChecked(true);
                        break;
                    case 2:
                        rb_main_3.setChecked(true);
                        break;
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //主线程接收消息
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBannerState(MainSendEvent event) {
        if (event != null) {
//            bannerIsMOVE = (boolean) event.getT();
//            view_pager.setScrollble(bannerIsMOVE);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
