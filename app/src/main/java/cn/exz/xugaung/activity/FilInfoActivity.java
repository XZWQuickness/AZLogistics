package cn.exz.xugaung.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Base64Coder;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/20.
 * 完善信息
 */
@ContentView(R.layout.activity_fill_info)
public class FilInfoActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @ViewInject(R.id.tv_title)
    private TextView title;
    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

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

    private String trueName = ""; //    真实姓名
    private String contactInfo = "";//    联系方式
    private String IDCardNum = "";//    身份证号
    private String drivingLicenseNum = "";//    驾驶证号
    private String IDCardFrontImg = "";//    身份证正面照片（数据流）
    private String IDCardBackImg = "";//    身份证背面照片（数据流）
    private String holdIDCardImg = "";//    手持身份证照片（数据流）
    private String drivingLicenseImg = "";//    驾驶证照片（数据流）
    private String plateNum = "";//     车牌号
    private String truckImg = "";//     车辆照片
    private String travelLicenseImg = "";//    行驶证照片
    private String emergencyContactPerson = "";//     紧急联系人
    private String emergencyContactNumber = "";//    紧急联系方式
    //=================1是审核通过==2未通过=======================
    private String trueNameState = ""; //    真实姓名状态
    private String contactInfoState = "";//    联系方式状态
    private String IDCardNumState = "";//    身份证号状态
    private String drivingLicenseNumState = "";//    驾驶证号状态
    private String IDCardFrontImgState = "";//    身份证正面照片（数据流）状态
    private String IDCardBackImgState = "";//    身份证背面照片（数据流）状态
    private String holdIDCardImgState = "";//    手持身份证照片（数据流）状态
    private String drivingLicenseImgState = "";//    驾驶证照片（数据流）状态
    private String plateNumState = "";//     车牌号状态
    private String truckImgState = "";//     车辆照片状态
    private String travelLicenseImgState = "";//    行驶证照片状态
    private String emergencyContactPersonState = "";//     紧急联系人状态
    private String emergencyContactNumberState = "";//    紧急联系方式状态

    @ViewInject(R.id.ed_true_name)
    private EditText ed_true_name;

    @ViewInject(R.id.ed_contact_mode)
    private EditText ed_contactInfo;

    @ViewInject(R.id.ed_IDCar_num)
    private EditText ed_IDCardNum;

    @ViewInject(R.id.ed_drivingLicenseNum)
    private EditText ed_drivingLicenseNum;

    @ViewInject(R.id.iv_IDCardFrontImg)
    private ImageView iv_IDCardFrontImg;

    @ViewInject(R.id.iv_IDCardBackImg)
    private ImageView iv_IDCardBackImg;

    @ViewInject(R.id.iv_holdIDCardImg)
    private ImageView iv_holdIDCardImg;

    @ViewInject(R.id.iv_drivingLicenseImg)
    private ImageView iv_drivingLicenseImg;

    @ViewInject(R.id.ed_plateNum)
    private EditText ed_plateNum;

    @ViewInject(R.id.iv_truckImg)
    private ImageView iv_truckImg;

    @ViewInject(R.id.iv_travelLicenseImg)
    private ImageView iv_travelLicenseImg;

    @ViewInject(R.id.ed_emergencyContactPerson)
    private EditText ed_emergencyContactPerson;//     紧急联系人

    @ViewInject(R.id.ed_emergencyContactNumber)
    private EditText ed_emergencyContactNumber;//    紧急联系方式


    @ViewInject(R.id.tv_sumbint_info)
    private TextView tv_sumbint_info;


    private String state = "";

    private int widthPixels, heightPixels;

    @Override
    public void initView() {

        // 得到屏幕宽高
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        List<ImageView> views = new ArrayList<ImageView>();
        views.add(iv_IDCardFrontImg);
        views.add(iv_IDCardBackImg);
        views.add(iv_holdIDCardImg);
        views.add(iv_drivingLicenseImg);
        views.add(iv_truckImg);
        views.add(iv_travelLicenseImg);

        for (int i = 0; i < views.size(); i++) {
            // 动态改变大小
            changeWidthAndHeight(views.get(i));
        }
        title.setText("完善信息");
        ll_back.setOnClickListener(this);
        iv_IDCardFrontImg.setOnClickListener(this);
        iv_IDCardBackImg.setOnClickListener(this);
        iv_holdIDCardImg.setOnClickListener(this);
        iv_drivingLicenseImg.setOnClickListener(this);
        iv_truckImg.setOnClickListener(this);
        iv_travelLicenseImg.setOnClickListener(this);
        tv_sumbint_info.setOnClickListener(this);

    }

    private void changeWidthAndHeight(ImageView tv) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.width = widthPixels / 2 - 10;
        layoutParams.height = widthPixels / 4 - 10;
        tv.setLayoutParams(layoutParams);
    }

    @Override
    public void initData() {

        if (!TextUtils.isEmpty(getIntent().getStringExtra("checkResult"))) {
            XUtilsApi x = new XUtilsApi();
            try {
                JSONObject json = new JSONObject(getIntent().getStringExtra("checkResult"));
                JSONObject trueName = json.optJSONObject("trueName");
                JSONObject contactInfo = json.optJSONObject("contactInfo");
                JSONObject IDCardNum = json.optJSONObject("IDCardNum");
                JSONObject plateNum = json.optJSONObject("plateNum");
                JSONObject truckImg = json.optJSONObject("truckImg");
                JSONObject travelLicenseImg = json.optJSONObject("travelLicenseImg");
                JSONObject emergencyContactPerson = json.optJSONObject("emergencyContactPerson");
                JSONObject emergencyContactNumber = json.optJSONObject("emergencyContactNumber");
                JSONObject drivingLicenseNum = json.optJSONObject("drivingLicenseNum");
                JSONObject IDCardFrontImg = json.optJSONObject("IDCardFrontImg");
                JSONObject IDCardBackImg = json.optJSONObject("IDCardBackImg");
                JSONObject holdIDCardImg = json.optJSONObject("holdIDCardImg");
                JSONObject drivingLicenseImg = json.optJSONObject("drivingLicenseImg");

                ed_true_name.setText(trueName.optString("value"));
                this.trueName = trueName.optString("value");
                ed_contactInfo.setText(contactInfo.optString("value"));
                this.contactInfo = contactInfo.optString("value");
                ed_IDCardNum.setText(IDCardNum.optString("value"));
                this.IDCardNum = IDCardNum.optString("value");

                ed_drivingLicenseNum.setText(drivingLicenseNum.optString("value"));
                this.drivingLicenseNum = drivingLicenseNum.optString("value");


                ed_plateNum.setText(plateNum.optString("value"));
                this.plateNum = plateNum.optString("value");

                ed_emergencyContactPerson.setText(emergencyContactPerson.optString("value"));
                this.emergencyContactPerson = emergencyContactPerson.optString("value");
                ed_emergencyContactNumber.setText(emergencyContactNumber.optString("value"));
                this.emergencyContactNumber = emergencyContactNumber.optString("value");

                trueNameState = trueName.optString("check");
                contactInfoState = contactInfo.optString("check");
                IDCardNumState = IDCardNum.optString("check");
                drivingLicenseNumState = drivingLicenseNum.optString("check");
                IDCardFrontImgState = IDCardFrontImg.optString("check");
                IDCardBackImgState = IDCardBackImg.optString("check");
                holdIDCardImgState = holdIDCardImg.optString("check");
                drivingLicenseImgState = drivingLicenseImg.optString("check");
                plateNumState = plateNum.optString("check");
                truckImgState = truckImg.optString("check");
                travelLicenseImgState = travelLicenseImg.optString("check");
                emergencyContactPersonState = emergencyContactPerson.optString("check");
                emergencyContactNumberState = emergencyContactNumber.optString("check");

                if (trueNameState.equals("2")) {
                    ed_true_name.setTextColor(Color.parseColor("#FF290C"));
                }

                if (contactInfoState.equals("2")) {
                    ed_contactInfo.setTextColor(Color.parseColor("#FF290C"));
                }

                if (IDCardNumState.equals("2")) {
                    ed_IDCardNum.setTextColor(Color.parseColor("#FF290C"));
                }

                if (drivingLicenseNumState.equals("2")) {
                    ed_drivingLicenseNum.setTextColor(Color.parseColor("#FF290C"));
                }

                if (plateNumState.equals("2")) {
                    ed_plateNum.setTextColor(Color.parseColor("#FF290C"));
                }

                if (emergencyContactPersonState.equals("2")) {
                    ed_emergencyContactPerson.setTextColor(Color.parseColor("#FF290C"));
                }
                if (emergencyContactNumberState.equals("2")) {
                    ed_emergencyContactNumber.setTextColor(Color.parseColor("#FF290C"));
                }
                if (IDCardFrontImgState.equals("1")) {
                    x.imageLoad(iv_IDCardFrontImg, IDCardFrontImg.optString("value"));

                }

                if (IDCardBackImgState.equals("1")) {
                    x.imageLoad(iv_IDCardBackImg, IDCardBackImg.optString("value"));
                }
                if (holdIDCardImgState.equals("1")) {
                    x.imageLoad(iv_holdIDCardImg, holdIDCardImg.optString("value"));
                }
                if (drivingLicenseImgState.equals("1")) {
                    x.imageLoad(iv_drivingLicenseImg, drivingLicenseImg.optString("value"));
                }
                if (truckImgState.equals("1")) {
                    x.imageLoad(iv_truckImg, truckImg.optString("value"));
                }
                if (travelLicenseImgState.equals("1")) {
                    x.imageLoad(iv_travelLicenseImg, travelLicenseImg.optString("value"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ed_true_name.addTextChangedListener(this);
        ed_contactInfo.addTextChangedListener(this);
        ed_IDCardNum.addTextChangedListener(this);
        ed_drivingLicenseNum.addTextChangedListener(this);
        ed_plateNum.addTextChangedListener(this);
        ed_emergencyContactPerson.addTextChangedListener(this);
        ed_emergencyContactNumber.addTextChangedListener(this);
    }

    /* 保存界面状态，如果activity意外被系统killed，返回时可以恢复状态值 */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState); // 实现父类方法 放在最后
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.iv_IDCardFrontImg: //    身份证正面照片（数据流）
                state = "1";
                new popupwindows(FilInfoActivity.this, iv_IDCardFrontImg);
                break;

            case R.id.iv_IDCardBackImg: //    身份证背面照片（数据流）
                state = "2";
                new popupwindows(FilInfoActivity.this, iv_IDCardBackImg);
                break;

            case R.id.iv_holdIDCardImg: //   手持身份证（数据流）
                state = "3";
                new popupwindows(FilInfoActivity.this, iv_holdIDCardImg);
                break;

            case R.id.iv_drivingLicenseImg: //  驾驶证（数据流）
                state = "4";
                new popupwindows(FilInfoActivity.this, iv_drivingLicenseImg);
                break;


            case R.id.iv_truckImg: //  车辆照片（数据流）
                state = "5";
                new popupwindows(FilInfoActivity.this, iv_truckImg);
                break;

            case R.id.iv_travelLicenseImg: //  行驶证照片
                state = "6";
                new popupwindows(FilInfoActivity.this, iv_travelLicenseImg);
                break;

            case R.id.tv_sumbint_info: //提交信息
                trueName = ed_true_name.getText().toString().trim();
                contactInfo = ed_contactInfo.getText().toString().trim();
                IDCardNum = ed_IDCardNum.getText().toString().trim();
                drivingLicenseNum = ed_drivingLicenseNum.getText().toString().trim();
                plateNum = ed_plateNum.getText().toString().trim();
                emergencyContactPerson = ed_emergencyContactPerson.getText().toString().trim();
                emergencyContactNumber = ed_emergencyContactNumber.getText().toString().trim();
                if (TextUtils.isEmpty(getIntent().getStringExtra("checkResult"))) {
                    if (TextUtils.isEmpty(trueName) || TextUtils.isEmpty(contactInfo) || TextUtils.isEmpty(drivingLicenseNum)
                            || TextUtils.isEmpty(plateNum) || TextUtils.isEmpty(truckImg) || TextUtils.isEmpty(travelLicenseImg) || TextUtils.isEmpty(holdIDCardImg) || TextUtils.isEmpty(drivingLicenseImg)) {
                        Intent intent = new Intent(FilInfoActivity.this, DiaLogActivity.class)
                                .putExtra("message", "亲~信息要填写完整哦!");
                        startActivity(intent);
                        return;
                    }
                }


                if (trueNameState.equals("2") || contactInfoState.equals("2") || drivingLicenseNumState.equals("2")
                        || plateNumState.equals("2") || truckImgState.equals("2") ||
                        travelLicenseImgState.equals("2") || holdIDCardImgState.equals("2") ||
                        drivingLicenseImgState.equals("2")) {
                    Intent intent = new Intent(FilInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", "亲~您还没有修改未通过的信息哦!");
                    startActivity(intent);
                    return;
                }

                sumbintInfo(trueName, contactInfo, IDCardNum, drivingLicenseNum, IDCardFrontImg, IDCardBackImg, holdIDCardImg, drivingLicenseImg, plateNum, truckImg, travelLicenseImg, emergencyContactPerson, emergencyContactNumber);

                break;


        }
    }

    /*
     *         真实姓名
     *         联系方式
     *         身份证号
     *         驾驶证号
     *         身份证正面照片（数据流）
     *         身份证背面照片（数据流）
     *         持身份证照片（数据流）
     *         驾驶证照片（数据流）
     *         车牌号
     *         车辆照片
     *         行驶证照片
     *         紧急联系人
     *         紧急联系方式
     *
     */
    private void sumbintInfo(String trueName, String contactInfo, String idCardNum, String drivingLicenseNum, String idCardFrontImg, String idCardBackImg, String holdIDCardImg, String drivingLicenseImg, String plateNum, String truckImg, String travelLicenseImg, String emergencyContactPerson, String emergencyContactNumber) {

        XUtilsApi xUtilsApi = new XUtilsApi();
        String url = "";
        if (TextUtils.isEmpty(getIntent().getStringExtra("checkResult"))) {
            url = Constant.SUBINT_CHECK_INFO;
        } else {
            url = Constant.EDit_CHECK_INFO;

        }
        RequestParams params = new RequestParams(url);

        if (TextUtils.isEmpty(getIntent().getStringExtra("checkResult"))) {
            params.addBodyParameter("driverId", ConstantValue.DRIVERID);
            params.addBodyParameter("trueName", trueName);
            params.addBodyParameter("contactInfo", contactInfo);
//            params.addBodyParameter("IDCardNum", IDCardNum);
            params.addBodyParameter("drivingLicenseNum", drivingLicenseNum);
            params.addBodyParameter("IDCardFrontImg", IDCardFrontImg);
            params.addBodyParameter("IDCardBackImg", IDCardBackImg);
            params.addBodyParameter("holdIDCardImg", holdIDCardImg);
            params.addBodyParameter("drivingLicenseImg", drivingLicenseImg);
            params.addBodyParameter("plateNum", plateNum);
            params.addBodyParameter("truckImg", truckImg);
            params.addBodyParameter("travelLicenseImg", travelLicenseImg);
            params.addBodyParameter("emergencyContactPerson", emergencyContactPerson);
            params.addBodyParameter("emergencyContactNumber", emergencyContactNumber);
        } else {
            params.addBodyParameter("driverId", ConstantValue.DRIVERID);
            if (trueNameState.equals("3")) {

                params.addBodyParameter("trueName", trueName);
            }
            if (contactInfoState.equals("3")) {

                params.addBodyParameter("contactInfo", contactInfo);
            }
//            if (IDCardNumState.equals("3")) {
//
//                params.addBodyParameter("IDCardNum", IDCardNum);
//            }
            if (drivingLicenseNumState.equals("3")) {

                params.addBodyParameter("drivingLicenseNum", drivingLicenseNum);
            }

            if (IDCardFrontImgState.equals("3")) {

                params.addBodyParameter("IDCardFrontImg", IDCardFrontImg);
            }

            if (IDCardBackImgState.equals("3")) {

                params.addBodyParameter("IDCardBackImg", IDCardBackImg);
            }

            if (holdIDCardImgState.equals("3")) {

                params.addBodyParameter("holdIDCardImg", holdIDCardImg);
            }

            if (drivingLicenseImgState.equals("3")) {

                params.addBodyParameter("drivingLicenseImg", drivingLicenseImg);
            }

            if (plateNumState.equals("3")) {

                params.addBodyParameter("plateNum", plateNum);
            }

            if (truckImgState.equals("3")) {

                params.addBodyParameter("truckImg", truckImg);
            }

            if (travelLicenseImgState.equals("3")) {

                params.addBodyParameter("travelLicenseImg", travelLicenseImg);
            }

            if (emergencyContactPersonState.equals("3")) {

                params.addBodyParameter("emergencyContactPerson", emergencyContactPerson);
            }

            if (emergencyContactNumberState.equals("3")) {

                params.addBodyParameter("emergencyContactNumber", emergencyContactNumber);
            }

        }
        xUtilsApi.sendUrl(FilInfoActivity.this, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject optJSONObject = result.optJSONObject("info");
                    Utils.startActivity(FilInfoActivity.this, LoginActivity.class);
                    Intent intent = new Intent(FilInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Intent intent = new Intent(FilInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!trueName.equals(ed_true_name.getText().toString()) && trueNameState.equals("2")) {
            ed_true_name.setTextColor(Color.parseColor("#808080"));
            trueNameState = "3";
        }
        if (!contactInfo.equals(ed_contactInfo.getText().toString()) && contactInfoState.equals("2")) {
            ed_contactInfo.setTextColor(Color.parseColor("#808080"));
            contactInfoState = "3";
        }
        if (!IDCardNum.equals(ed_IDCardNum.getText().toString()) && IDCardNumState.equals("2")) {
            ed_IDCardNum.setTextColor(Color.parseColor("#808080"));
            IDCardNumState = "3";
        }
        if (!drivingLicenseNum.equals(ed_drivingLicenseNum.getText().toString()) && drivingLicenseNumState.equals("2")) {
            ed_drivingLicenseNum.setTextColor(Color.parseColor("#808080"));
            drivingLicenseNumState = "3";
        }
        if (!plateNum.equals(ed_plateNum.getText().toString()) && plateNumState.equals("2")) {
            ed_plateNum.setTextColor(Color.parseColor("#808080"));
            plateNumState = "3";
        }
        if (!emergencyContactPerson.equals(ed_emergencyContactPerson.getText().toString()) && emergencyContactPersonState.equals("2")) {
            ed_emergencyContactPerson.setTextColor(Color.parseColor("#808080"));
            emergencyContactPersonState = "3";
        }
        if (!emergencyContactNumber.equals(ed_emergencyContactNumber.getText().toString()) && emergencyContactNumberState.equals("2")) {
            ed_emergencyContactNumber.setTextColor(Color.parseColor("#808080"));
            emergencyContactNumberState = "3";
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

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

                        switch (state) {

                            case "1": //身份正
                                IDCardFrontImg = Base64Coder.encodeLines(bytes);
                                iv_IDCardFrontImg.setImageBitmap(photo);
                                IDCardFrontImgState = "3";
                                break;
                            case "2"://身份背面
                                IDCardBackImg = Base64Coder.encodeLines(bytes);
                                iv_IDCardBackImg.setImageBitmap(photo);
                                IDCardBackImgState = "3";
                                break;
                            case "3"://手持身份
                                holdIDCardImg = Base64Coder.encodeLines(bytes);
                                iv_holdIDCardImg.setImageBitmap(photo);
                                holdIDCardImgState = "3";
                                break;
                            case "4"://驾驶证
                                drivingLicenseImg = Base64Coder.encodeLines(bytes);
                                iv_drivingLicenseImg.setImageBitmap(photo);
                                drivingLicenseImgState = "3";
                                break;
                            case "5"://车辆
                                truckImg = Base64Coder.encodeLines(bytes);
                                iv_truckImg.setImageBitmap(photo);
                                truckImgState = "3";
                                break;
                            case "6"://行驶证
                                travelLicenseImg = Base64Coder.encodeLines(bytes);
                                iv_travelLicenseImg.setImageBitmap(photo);
                                travelLicenseImgState = "3";
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
                        Toast.makeText(FilInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
