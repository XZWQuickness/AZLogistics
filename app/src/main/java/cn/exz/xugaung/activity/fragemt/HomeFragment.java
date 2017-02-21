package cn.exz.xugaung.activity.fragemt;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.AfficheInfoActivity;
import cn.exz.xugaung.activity.DiaLogActivity;
import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.StartOffActivity;
import cn.exz.xugaung.activity.ToolActivity;
import cn.exz.xugaung.activity.adapter.NewstOrderAdapter;
import cn.exz.xugaung.activity.adapter.WebActivity;
import cn.exz.xugaung.activity.bean.BannerBean;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.bean.OrderBean;
import cn.exz.xugaung.activity.bean.WeatherBean;
import cn.exz.xugaung.activity.lunbo.ShufBanner;
import cn.exz.xugaung.activity.lunbo.ShufBannerClickListener;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;

import static cn.exz.xugaung.activity.app.MyAppclication.city;


/**
 * Created by pc on 2016/7/14.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements  View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView title;

    private TextView tv_weather;

    private TextView tv_location_weather;


    @ViewInject(R.id.lv)
    private ListView lv;


    @ViewInject(R.id.iv_start_off)
    private ImageView iv_start_off;

    private LinearLayout ll_is_view,ll_affiche;

    private ShufBanner mShufBanner;

    @ViewInject(R.id.ll_back)
    private LinearLayout llBack;
    private LinearLayout ll_tool;
    private LayoutInflater inflater;
    private NewstOrderAdapter adapter;
    List<BannerBean> list;
    List<String> mImages = new ArrayList<>();

    List<String> state = new ArrayList<String>();

    @Override
    public void initView() {

        title.setText("安至物流");
        llBack.setVisibility(View.INVISIBLE);
//        getActivity().startService(new Intent(getActivity(), DaemonService.class));
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_headview, null);
        iv_start_off = (ImageView) headView.findViewById(R.id.iv_start_off);
        mShufBanner = (ShufBanner) headView.findViewById(R.id.shuf);
        tv_location_weather = (TextView) headView.findViewById(R.id.tv_location_weather);
        ll_is_view = (LinearLayout) headView.findViewById(R.id.ll_is_view);
        ll_affiche = (LinearLayout) headView.findViewById(R.id.ll_affiche);
        ll_tool = (LinearLayout) headView.findViewById(R.id.ll_tool);
        tv_weather = (TextView) headView.findViewById(R.id.tv_weather);
//        sv.setFocusable(false);
//        sv.smoothScrollTo(0, 0);
        lv.addHeaderView(headView);
        adapter = new NewstOrderAdapter(getActivity(), state);
        lv.setAdapter(adapter);
        iv_start_off.setOnClickListener(this);
//        sv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });
        ll_affiche.setOnClickListener(this);
        ll_tool.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.LOCATION_HARDWARE, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initData() {

        getBanner();//获取banner图
        mShufBanner.setWipeLayoutEnabled(new ShufBanner.WipeLayoutEnabled() {
            @Override
            public void onWipeLayoutEnabled(boolean isEnbled) {
            }
        });
        getOrderList();
        getWeather();
    }

    /*
    *
    * 获取天气
    * */
    private void getWeather() {
        RequestParams params = new RequestParams(Constant.WEATHER);
        params.addBodyParameter("city", city);
        params.addBodyParameter("key", "69d9eff357da7c380507f62e3878679c");
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optString("infocode").equals("10000")) {
                    String info = result.optString("lives");
                    List<WeatherBean> weatherBeen = JSON.parseArray(info, WeatherBean.class);
                    tv_location_weather.setText(weatherBeen.get(0).getCity());
                    String weather = weatherBeen.get(0).getWeather();//天气
                    String temperature = weatherBeen.get(0).getTemperature();//温度
                    String winddirection = weatherBeen.get(0).getWinddirection();//风向
                    String windpower = weatherBeen.get(0).getWindpower();//风力
                    tv_weather.setText(weather + "  温度: " + temperature + "℃" + "  风力:" + winddirection + "风" + windpower + "级");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /*
    *
    * 获取订单列表
    * */
    private void getOrderList() {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.LATEST_ORDER_LIST);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        xUtilsApi.sendUrl(getActivity(), "post", params, false, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    String banner = result.optString("info");
                    List<OrderBean> orderBeen = JSON.parseArray(banner, OrderBean.class);
                    state.clear();
                    for (OrderBean i : orderBeen) {

                        if (i.getOrderState().equals("进行中")) {
                            state.add(i.getOrderState());
                        }
                    }
                    adapter.addendData(orderBeen, true);
                    adapter.updateAdapter();
                    if (adapter.getAdapterData().size() <= 0) {
                        ll_is_view.setVisibility(View.VISIBLE);
                    } else {
                        ll_is_view.setVisibility(View.GONE);
                    }

                } else {
                    Intent intent = new Intent(getActivity(), DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }

    private void getBanner() {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.BANNER_LIST);
        xUtilsApi.sendUrl(getActivity(), "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    String banner = result.optString("info");
                    list = JSON.parseArray(banner, BannerBean.class);
                    for (int i = 0; i < list.size(); i++) {
                        mImages.add(list.get(i).getImgUrl());
                    }
                    mShufBanner.startShuf(mImages, true);
                } else {
                    Intent intent = new Intent(getActivity(), DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });
        mShufBanner.setItemClcikListener(new ShufBannerClickListener() {
            @Override
            public void onClick(int position) {
                Intent i = new Intent(getActivity(), WebActivity.class);
                i.putExtra("name", "banner详情");
                i.putExtra("info", list.get(position).getAdUrl());
                startActivity(i);
            }
        });
    }


    //主线程接收消息
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(MainSendEvent event) {
        if (event != null) {
            int t = (int) event.getT();
            if (t == 0) {
                getOrderList();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_start_off:
                Utils.startActivityForResult(getActivity(), StartOffActivity.class);
                break;
            case R.id.ll_affiche://公告消息
                Utils.startActivityForResult(getActivity(), AfficheInfoActivity.class);
                break;

            case R.id.ll_tool://新闻头条
                Utils.startActivityForResult(getActivity(),ToolActivity.class);
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                getOrderList();
                break;
        }
    }
}
