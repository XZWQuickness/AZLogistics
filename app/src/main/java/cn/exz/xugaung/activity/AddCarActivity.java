package cn.exz.xugaung.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.exz.xugaung.activity.bean.CarInfoBean;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Base64Coder;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/19.
 * 车辆资料
 */
@ContentView(R.layout.activity_cardate)
public class AddCarActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;

    @ViewInject(R.id.tv_login)
    private TextView tv_login;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.ed_plateNum)
    private EditText ed_plateNum;

    private CarInfoBean carInfoBean;

    private String plateNum = "", truckImg = "", travelLicenseImg = "";

    @ViewInject(R.id.iv_truckImg)
    private ImageView iv_truckImg;//车辆照片

    @ViewInject(R.id.iv_travelLicenseImg)
    private ImageView iv_travelLicenseImg;//行驶证照片

    private int pohotoState;

    private Context c = AddCarActivity.this;

    private RequestParams p;
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
    private final int ADD_CAR_CALLBACK = 0x100;

    @Override
    public void initView() {
        title.setText("车辆资料");
        tv_title_right.setText("删除");
        ll_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        iv_truckImg.setOnClickListener(this);
        iv_travelLicenseImg.setOnClickListener(this);

    }

    @Override
    public void initData() {
        if (getIntent().getSerializableExtra("carInfoBean") != null) {
            carInfoBean = (CarInfoBean) getIntent().getSerializableExtra("carInfoBean");
            ed_plateNum.setText(carInfoBean.getPlateNum());
            XUtilsApi.ImageLoad(iv_truckImg, Constant.URL+carInfoBean.getTruckImg(), R.drawable.truckim);
            XUtilsApi.ImageLoad(iv_travelLicenseImg, Constant.URL+carInfoBean.getDrivingLicenseImg(), R.drawable.travellicenseimg);
            if(carInfoBean.getIsCheck().equals("2")){
                Intent intent = new Intent(c, DiaLogActivity.class)
                        .putExtra("message", carInfoBean.getMessage());//审核未通过原因
                startActivity(intent);
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_title_right://删除车辆
                deleteCar();
                break;

            case R.id.tv_login://提交车辆信息
                sumbintCarInfo();
                break;

            case R.id.iv_truckImg: //车辆照片
                pohotoState = 1;
                new popupwindows(c, iv_truckImg);
                break;

            case R.id.iv_travelLicenseImg: //行驶证照片
                pohotoState = 2;
                new popupwindows(c, iv_travelLicenseImg);
                break;


        }
    }

    /**
     * 删除车辆
     */
    private void deleteCar() {
        XUtilsApi x = new XUtilsApi();
        p = new RequestParams(Constant.DELETE_TRUCK);
        p.addBodyParameter("driverId", ConstantValue.DRIVERID);//司机id
        p.addBodyParameter("truckId", carInfoBean.getTruckId());//车牌IDid
        x.sendUrl(c, "post", p, true, new XUtilsApi.URLSuccessListenter() {
            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    setResult(ADD_CAR_CALLBACK);
                    JSONObject optJSONObject = result.optJSONObject("info");
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

    }

    /**
     * 提交车辆信息
     */
    private void sumbintCarInfo() {
        plateNum = ed_plateNum.getText().toString().trim();

        if (TextUtils.isEmpty(plateNum)) {
            Toast.makeText(this, "请输入车牌号!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (carInfoBean == null) {//添加车辆才会执行 编辑不执行
            if (TextUtils.isEmpty(truckImg)) {
                Toast.makeText(this, "请添加车辆照片!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(travelLicenseImg)) {
                Toast.makeText(this, "请添加行驶证照片!", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            if(carInfoBean.getPlateNum().equals(ed_plateNum.getText().toString())&&truckImg.equals("")&&travelLicenseImg.equals("")){
                Toast.makeText(this, "信息未作修改!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        XUtilsApi x = new XUtilsApi();
        if (carInfoBean == null) { //添加车辆
            p = new RequestParams(Constant.ADD_TRUCK);
            p.addBodyParameter("truckImg", truckImg);//车辆照片
            p.addBodyParameter("travelLicenseImg", travelLicenseImg);//行驶证照片
        } else {//编辑车辆
            p = new RequestParams(Constant.EDIT_TRUCK);
            p.addBodyParameter("truckId", carInfoBean.getTruckId());//车牌IDid
            if (!TextUtils.isEmpty(truckImg)) {
                p.addBodyParameter("truckImg", truckImg);//车辆照片
            }
            if (!TextUtils.isEmpty(truckImg)) {
                p.addBodyParameter("travelLicenseImg", travelLicenseImg);//行驶证照片
            }
        }

        p.addBodyParameter("driverId", ConstantValue.DRIVERID);//司机id
        p.addBodyParameter("plateNum", plateNum);//车牌号

        x.sendUrl(c, "post", p, true, new XUtilsApi.URLSuccessListenter() {
            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    setResult(ADD_CAR_CALLBACK);
                    JSONObject optJSONObject = result.optJSONObject("info");
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PIC_EDIT_REQUEST_DATA:
                    photo = data.getParcelableExtra("data");
                    if (photo != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] bytes = stream.toByteArray();

                        switch (pohotoState) {

                            case 1: //身份正
                                truckImg = Base64Coder.encodeLines(bytes);
                                iv_truckImg.setImageBitmap(photo);
                                break;
                            case 2://身份背面
                                travelLicenseImg = Base64Coder.encodeLines(bytes);
                                iv_travelLicenseImg.setImageBitmap(photo);
                                break;
                        }
                    }

                    break;
                /**
                 * 相机拍照
                 */
                case CAMERA_REQUEST_DATA:
                    if (hasSdcard()) {
                        tempFile = new File(
                                Environment.getExternalStorageDirectory(),
                                "feendback_photo.jpg");
                        crop(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(AddCarActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                /**
                 * 相册选择
                 */
                case PHOTO_PICKED_REQUEST_DATA:
                    if (data != null) {
                        Uri uri = data.getData();
                        crop(uri);
                    }
                    break;
            }
        }
    }

    private void crop(Uri uri) {
        // TODO Auto-generated method stub
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PIC_EDIT_REQUEST_DATA);
    }
}
