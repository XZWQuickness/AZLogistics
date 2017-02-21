package cn.exz.xugaung.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;
import cn.jpush.android.api.JPushInterface;

import static cn.exz.xugaung.activity.app.MyAppclication.address;


/**
 * Created by pc on 2016/7/22.
 * 立即出发
 */
@ContentView(R.layout.activity_statr_off)
public class StartOffActivity extends BaseActivity implements View.OnClickListener, AMapLocationListener {

    String addressStr;
    double longitude;
    double latitude;
    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.tv_plateNum)
    private TextView tv_plateNum;


    @ViewInject(R.id.tv_createAddress)
    private TextView tv_createAddress;

    @ViewInject(R.id.ed_poundNum)
    private EditText ed_poundNum;


    @ViewInject(R.id.tv_sumint_start_off)
    private TextView tv_sumint_start_off;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    private Context c = StartOffActivity.this;
    private final int SELECT_CAR_CALLBACK = 0x101;

    private String truckId = "", plateNum = "";

    @Override
    public void initView() {
        title.setText("立即出发");
        ll_back.setOnClickListener(this);
        tv_sumint_start_off.setOnClickListener(this);
        tv_createAddress.setText(address);
        tv_plateNum.setOnClickListener(this);
    }

    @Override
    public void initData() {
        AMapLocationClient locationClient = null;
        AMapLocationClientOption locationOption = null;
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);

        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        int alarmInterval = 5;
        locationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        locationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        locationOption.setInterval(10000);//可选，设置定位间隔。默认为2秒
        locationOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        locationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        locationOption.setOnceLocationLatest(true);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,
        // 会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        locationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        locationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        locationOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_plateNum: //选择车辆
                Intent selectCarIntent = new Intent(c, SelectCarActivity.class);
                selectCarIntent.putExtra("plateNum", plateNum);
                startActivityForResult(selectCarIntent, SELECT_CAR_CALLBACK);
                break;

            case R.id.tv_sumint_start_off: //立即出发
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }
                if (longitude == 0.0 || latitude == 0.0) {
                    Toast.makeText(c, "没有获取到当前经纬度,请开启定位权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(plateNum) || TextUtils.isEmpty(truckId)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~您没有选择车牌号哦!");
                    startActivity(intent);
                    return;
                }

                if (TextUtils.isEmpty(ed_poundNum.getText().toString().trim())) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~您没有输入磅单号哦!");
                    startActivity(intent);
                    return;
                }

                sumbintinfo();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tv_createAddress.setText(addressStr);
    }

    private void sumbintinfo() {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.CREATE_ORDER);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        params.addBodyParameter("plateNum", plateNum);
        params.addBodyParameter("truckId", truckId);//车牌号的id
        params.addBodyParameter("poundNum", ed_poundNum.getText().toString().trim());
        params.addBodyParameter("createAddress", addressStr); //详细地址
        params.addBodyParameter("createLongitude", longitude + ""); //经度
        params.addBodyParameter("createLatitude", latitude + "");//纬度
        params.addBodyParameter("createDeviceType", "1"); //1 Android 2 是 苹果
        params.addBodyParameter("createRegistrationID", JPushInterface.getRegistrationID(getApplicationContext()));
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    EventBus.getDefault().post(new MainSendEvent("Update"));
                    setResult(100);
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                    Utils.startActivity(c, MainActivity.class);
                    finish();
                } else {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_CAR_CALLBACK:
                if (data != null && !TextUtils.isEmpty(data.getStringExtra("plateNum")) && !TextUtils.isEmpty(data.getStringExtra("truckId"))) {
                    plateNum = data.getStringExtra("plateNum");
                    truckId = data.getStringExtra("truckId");
                    tv_plateNum.setText(plateNum);
                }

                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc && loc.getLocationType() != 0 && loc.getLocationType() != 2) {
            addressStr = loc.getAddress();
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
        }
    }
}
