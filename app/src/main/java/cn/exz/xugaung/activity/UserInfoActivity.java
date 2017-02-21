package cn.exz.xugaung.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Base64Coder;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/18.
 * 个人信息
 */
@ContentView(R.layout.activity_userinfo)
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.tv_name)
    private TextView tv_name;

    @ViewInject(R.id.phone_num)
    private TextView phone_num;


    @ViewInject(R.id.ll_back)
    private LinearLayout back;

    @ViewInject(R.id.ll_photo)
    private LinearLayout ll_photo;

    @ViewInject(R.id.iv_photo)
    private ImageView iv_photo;


    @ViewInject(R.id.rl_nickname)
    private RelativeLayout rl_nickname;


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

    @Override
    public void initView() {
        title.setText("个人信息");
        back.setOnClickListener(this);
        ll_photo.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(Constant.URLIMG + ConstantValue.driverImg, iv_photo);
        tv_name.setText(ConstantValue.nickName);
        phone_num.setText(SPutils.getString(this,"name"));
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_photo:
                new popupwindows(UserInfoActivity.this, ll_photo);
                break;
            case R.id.rl_nickname:
                pop();
                break;


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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PIC_EDIT_REQUEST_DATA:
                    photo = data.getParcelableExtra("data");
                    if (photo != null) {
                        iv_photo.setImageBitmap(photo);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] bytes = stream.toByteArray();
                        String poto_01 = Base64Coder.encodeLines(bytes);
                        sumbintEditexUserInfo("", poto_01);


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
                        Toast.makeText(UserInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PIC_EDIT_REQUEST_DATA);
    }

    private void pop() {
        final AlertDialog dlgtwo = new AlertDialog.Builder(UserInfoActivity.this)
                .create();
        View viewtwo = LayoutInflater.from(UserInfoActivity.this).inflate(
                R.layout.dialog_shuanganniu, null);
        dlgtwo.setView(UserInfoActivity.this.getLayoutInflater().inflate(
                R.layout.dialog_shuanganniu, null));
        dlgtwo.show();
        dlgtwo.getWindow().setContentView(viewtwo);
        TextView queding = (TextView) viewtwo.findViewById(R.id.queding);
        TextView quxiao = (TextView) viewtwo.findViewById(R.id.quxiao);
        TextView titlename = (TextView) viewtwo
                .findViewById(R.id.titlename);
        final EditText ed_nickname = (EditText) viewtwo
                .findViewById(R.id.ed_nickname);
        quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dlgtwo.dismiss();

            }
        });
        queding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dlgtwo.dismiss();
                String nickname = ed_nickname.getText().toString().trim();
                if (TextUtils.isEmpty(nickname)) {
                    Intent intent = new Intent(UserInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", "亲~昵称不能为空哦!");
                    startActivity(intent);
                    return;
                }
                tv_name.setText(nickname);
                ConstantValue.nickName = nickname;
                EventBus.getDefault().post(new MainSendEvent(2));
                sumbintEditexUserInfo(nickname, "");
            }
        });
    }

    private void sumbintEditexUserInfo(String nickname, String photo) {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.EDit_USERINFO);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        if (!TextUtils.isEmpty(nickname)) {

            params.addBodyParameter("nickname", nickname);
        }
        if (!TextUtils.isEmpty(photo)) {

            params.addBodyParameter("driverImg", photo);
        }
        xUtilsApi.sendUrl(UserInfoActivity.this, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject optJSONObject = result.optJSONObject("info");
                    if (optJSONObject != null) {

                        ConstantValue.driverImg = optJSONObject.optString("driverImg");
                        EventBus.getDefault().post(new MainSendEvent(2));
                    }

                } else {
                    Intent intent = new Intent(UserInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });
    }
}
