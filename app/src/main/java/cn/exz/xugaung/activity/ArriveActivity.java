package cn.exz.xugaung.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.adapter.ArriveAdapter;
import cn.exz.xugaung.activity.adapter.GoodsInfoAdapter;
import cn.exz.xugaung.activity.bean.GoodsListBean;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.bean.PhotoBean;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.GlideImageLoader;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;
import cn.exz.xugaung.activity.utils.imagepicke.ImagePicker;
import cn.exz.xugaung.activity.view.MyScrollView;
import cn.exz.xugaung.activity.view.NoScrollListView;
import cn.jpush.android.api.JPushInterface;

import static com.lzy.imagepicker.ui.ImageGridActivity.REQUEST_PERMISSION_STORAGE;

/**
 * Created by pc on 2016/7/22.
 * 到达目的地
 */
@ContentView(R.layout.activity_arrive)
public class ArriveActivity extends BaseActivity implements View.OnClickListener, AMapLocationListener {
    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;

    @ViewInject(R.id.tv_plateNum)
    private TextView tv_plateNum;

    @ViewInject(R.id.tv_poundNum)
    private TextView tv_poundNum;

    @ViewInject(R.id.tv_createDate)
    private TextView tv_createDate;

    @ViewInject(R.id.tv_createAddress)
    private TextView tv_createAddress;

    @ViewInject(R.id.tv_location)
    private TextView tv_location;


    @ViewInject(R.id.tv_orderNum)
    private TextView tv_orderNum;


    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.lv)
    private NoScrollListView lv;

    @ViewInject(R.id.sv)
    private MyScrollView sv;

    private GoodsInfoAdapter adapter;

    private Context c = ArriveActivity.this;

    /**
     * 相册请求参数
     */
    private static final int PHOTO_PICKED_REQUEST_DATA = 3021;
    private File tempFile = null;

    /**
     * 相机请求参数
     */
    private static final int CAMERA_REQUEST_DATA = 3023;
    /**
     * 图片编辑请求参数
     */
    private static final int PIC_EDIT_REQUEST_DATA = 3025;
    private Bitmap photo;

    private String images = "";

    private List<PhotoBean> listPhoto = new ArrayList<PhotoBean>();
    private int tag = 0;
    ArriveAdapter arriveAdapter;
    @Override
    public void initView() {
        title.setText("到达目的地");
        ActivityCompat.requestPermissions(ArriveActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_STORAGE);
        sv.setFocusable(false);
        sv.smoothScrollTo(0, 0);
        initImagePicker();
        adapter = new GoodsInfoAdapter(c);
        lv.setAdapter(adapter);
        tv_orderNum.setText(getIntent().getStringExtra("orderId"));

        tv_title_right.setText("确定");
        tv_title_right.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        arriveAdapter=new ArriveAdapter(this);
        mRecyclerView.setAdapter(arriveAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        arriveAdapter.setOnClick(new ArriveAdapter.onClick() {
            @Override
            public void onClick() {
                // 是否有拍照权限。
                if(AndPermission.hasPermission(ArriveActivity.this, android.Manifest.permission.CAMERA)) {
                    List adapterData = adapter.getAdapterData();
                    if (adapterData.size() > 5) {
                        Intent intent = new Intent(c, DiaLogActivity.class)
                                .putExtra("message", "亲~最多上传四张图片哦!");
                        startActivity(intent);
                        return;
                    } else {
                        if (ContextCompat.checkSelfPermission(ArriveActivity.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ArriveActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_STORAGE);
                        } else {

                            imagePicker.takePicture(ArriveActivity.this, imagePicker.REQUEST_CODE_TAKE);
                        }
                    }
                } else {
                    // 申请权限。
                    AndPermission.with(ArriveActivity.this)
                            .requestCode(100)
                            .permission(android.Manifest.permission.CAMERA, android.Manifest.permission.CAMERA)
                            .send();
                }



            }
        });

        initLocation();
    }
    String address;
    double longitude;
    double latitude;
    private void initLocation() {
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
        locationOption.setHttpTimeOut(2000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            if(requestCode==imagePicker.REQUEST_CODE_TAKE){

                imagePicker.takePicture(ArriveActivity.this,imagePicker.REQUEST_CODE_TAKE);
            }else{

                tv_location.setText(address);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {

        }
    };
    private ImagePicker imagePicker;

    /**
     * 初始化相机
     */
    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        //显示相机
        imagePicker.setShowCamera(true);
        //是否裁剪
        imagePicker.setCrop(true);
        //是否按矩形区域保存裁剪图片
        imagePicker.setSaveRectangle(true);
        //圖片緩存
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);//单选
        //矩形尺寸
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(width);
        //圖片輸出尺寸
        imagePicker.setOutPutX((int) (getResources().getDisplayMetrics().widthPixels * 0.16));
        imagePicker.setOutPutY((int) (getResources().getDisplayMetrics().heightPixels * 0.16));
    }

    @Override
    public void initData() {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.ORDER_DETAIL);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        params.addBodyParameter("orderId", getIntent().getStringExtra("orderId"));
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONArray j = result.optJSONArray("info");
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject json = j.optJSONObject(i);
                        tv_plateNum.setText(json.optString("plateNum")); //车牌
                        tv_poundNum.setText(json.optString("poundNum"));  //磅单号
                        tv_createAddress.setText(json.optString("createAddress"));
                        tv_createDate.setText(json.optString("createDate"));
                        String goodsList = json.optString("goodsList");
                        List<GoodsListBean> goodsListBeen = JSON.parseArray(goodsList, GoodsListBean.class);
                        adapter.addendData(goodsListBeen, true);
                        adapter.updateAdapter();
                    }


                } else {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_title_right:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }
                if (longitude == 0.0 || latitude == 0.0||address.equals("获取地址失败,请开启定位权限!")) {
                    Toast.makeText(c, "没有获取到当前经纬度,请开启定位权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(images)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~至少上传一张照片哦!");
                    startActivity(intent);
                    return;
                }
                String image = images.substring(0, images.length() - 1);

                XUtilsApi xUtilsApi = new XUtilsApi();
                RequestParams params = new RequestParams(Constant.ORDER_FINISH);
                params.addBodyParameter("driverId", ConstantValue.DRIVERID);
                params.addBodyParameter("orderId", getIntent().getStringExtra("orderId"));
                params.addBodyParameter("finishAddress", address);
                params.addBodyParameter("finishLongitude", longitude + "");
                params.addBodyParameter("finishLatitude", latitude + "");
                params.addBodyParameter("images", image);
                params.addBodyParameter("finishDeviceType", "1");
                params.addBodyParameter("finishRegistrationID", JPushInterface.getRegistrationID(ArriveActivity.this));
                xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

                    @Override
                    public void OnSuccess(NewEntity content, JSONObject result) {
                        if (content.getResult().equals(ConstantValue.RESULT)) {
                            EventBus.getDefault().post(new MainSendEvent("Update"));
                            EventBus.getDefault().post(new MainSendEvent(4));
                            EventBus.getDefault().post(new MainSendEvent(0));
                            Intent intent = new Intent(c, DiaLogActivity.class)
                                    .putExtra("message", content.getMessage());
                            startActivity(intent);
                            finish();

                        } else {
                            Intent intent = new Intent(c, DiaLogActivity.class)
                                    .putExtra("message", content.getMessage());
                            startActivity(intent);
                        }
                    }
                });

                break;


        }
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc && loc.getLocationType() != 0 && loc.getLocationType() != 2) {
            address = loc.getAddress();
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            tv_location.setText(address);
        }
    }


    class popupwindows extends PopupWindow {

        public popupwindows(final Context context, View parent) {
            View view = View.inflate(context, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.fade_ins));
            final LinearLayout pop = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            pop.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt3.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case KeyEvent.ACTION_DOWN:
                            dismiss();
                            break;
                    }
                    return false;
                }
            });
            bt2.setOnTouchListener(new View.OnTouchListener() { // 从相册中选取

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case KeyEvent.ACTION_DOWN:

                            Intent intentx = new Intent(Intent.ACTION_PICK);
                            intentx.setDataAndType(
                                    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                                    "image/*");
                            startActivityForResult(intentx,
                                    PHOTO_PICKED_REQUEST_DATA);
                            dismiss();
                            break;
                    }
                    return false;
                }
            });
            bt1.setOnTouchListener(new View.OnTouchListener() { // 拍照

                @SuppressWarnings("static-access")
                @SuppressLint({"ClickableViewAccessibility", "ShowToast"})
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case KeyEvent.ACTION_DOWN:
                            Intent intent = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            // 判断存储卡是否可以用，可用进行存储
                            if (hasSdcard()) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                        .fromFile(new File(Environment
                                                .getExternalStorageDirectory(),
                                                "feendback_photo.jpg")));
                            }
                            startActivityForResult(intent, CAMERA_REQUEST_DATA);
                            dismiss();
                            break;
                    }
                    return false;
                }
            });
        }
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
            //发送广播通知图片增加了
            ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
            ImageItem imageItem = new ImageItem();
            imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
            try {
                images += Utils.encodeBase64File( imageItem.path) + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
            PhotoBean bean = new PhotoBean();
            bean.setPhoto( imageItem.path);
            listPhoto.add(bean);
            arriveAdapter.addendData(listPhoto, true);
            arriveAdapter.updateAdapter();

        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            ArrayList<ImageItem> d = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            try {
                images += Utils.encodeBase64File(d.get(0).path) + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
            PhotoBean bean = new PhotoBean();
            bean.setPhoto(d.get(0).path);
            listPhoto.add(bean);
            arriveAdapter.addendData(listPhoto, true);
            arriveAdapter.updateAdapter();

        }

    }

}
