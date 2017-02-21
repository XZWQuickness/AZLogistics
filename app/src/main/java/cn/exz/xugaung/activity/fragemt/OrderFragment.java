package cn.exz.xugaung.activity.fragemt;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.utils.MainSendEvent;

/**
 * Created by pc on 2016/7/14.
 */
@ContentView(R.layout.fragment_order)
public class OrderFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView title;


    @ViewInject(R.id.tv_underway)
    private TextView tv_underway;//进行中

    @ViewInject(R.id.ll_back)
    private LinearLayout llBack;
    @ViewInject(R.id.tv_done)
    private TextView tv_done;//已完成

    @ViewInject(R.id.v_01)
    private View v_01;//线一

    @ViewInject(R.id.v_02)
    private View v_02;//线二

    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;

    private List<Fragment> mListViews;


    private List<View> listView;

    private UnderwaFragment underwa;
    private DoneFragment doneFragment;
    @Override
    public void initView() {
        title.setText("订单");
        llBack.setVisibility(View.INVISIBLE);
        v_02.setVisibility(View.INVISIBLE);
        selectColor(0);
        tv_underway.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        if (underwa == null) {
            underwa = new UnderwaFragment();
        }
        if (doneFragment == null) {
            doneFragment = new DoneFragment();
        }
        mListViews = new ArrayList<Fragment>();
        mListViews.add(underwa);
        mListViews.add(doneFragment);
        viewpager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager(), mListViews));
        viewpager.addOnPageChangeListener(new MypagerChaeLister());

//        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void selectColor(int pos) {
        switch (pos) {
            case 0:
                tv_underway.setTextColor(Color.parseColor("#FFFF290C"));
                tv_done.setTextColor(Color.parseColor("#626262"));
                v_01.setVisibility(View.VISIBLE);
                v_02.setVisibility(View.INVISIBLE);
                break;

            case 1:
                tv_done.setTextColor(Color.parseColor("#FFFF290C"));
                tv_underway.setTextColor(Color.parseColor("#626262"));
                v_01.setVisibility(View.INVISIBLE);
                v_02.setVisibility(View.VISIBLE);
                break;


        }

    }


    @Override
    public void initData() {

    }

    //主线程接收消息
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(MainSendEvent event) {
        if (event != null) {
            int t = (int) event.getT();
            if (t == 1) {

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_underway: //进行中
                selectColor(0);
                viewpager.setCurrentItem(0);
                break;
            case R.id.tv_done://已完成
                selectColor(1);
                viewpager.setCurrentItem(1);
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

            switch (position) {
                case 0:
                   selectColor(0);
                    EventBus.getDefault().post(new MainSendEvent(4));
                    break;

                case 1:
                    selectColor(1);
                    EventBus.getDefault().post(new MainSendEvent(5));
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
