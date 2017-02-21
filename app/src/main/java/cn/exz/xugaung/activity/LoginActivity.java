package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.app.LocalService;
import cn.exz.xugaung.activity.app.RectifyRemoteService;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by pc on 2016/7/15.
 * 登录
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_login)
    private TextView tv_login;

    @ViewInject(R.id.tv_register)
    private TextView tv_register;

    @ViewInject(R.id.tv_forget_password)
    private TextView tv_forget_password;

    @ViewInject(R.id.ed_phonet_num)
    private EditText ed_phonet_num;

    @ViewInject(R.id.ed_ed_password)
    private TextView ed_password;

    private Context c = LoginActivity.this;

    private String checkResult = "";


    @Override
    public void initView() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.LOCATION_HARDWARE, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }

        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);

    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(SPutils.getString(c, "name"))) {
            ed_phonet_num.setText(SPutils.getString(c, "name"));

        }
        if (!TextUtils.isEmpty(SPutils.getString(c, "password"))) {
            ed_password.setText(SPutils.getString(c, "password"));
        }
//        if (!TextUtils.isEmpty(SPutils.getString(c, "name"))&&!TextUtils.isEmpty(SPutils.getString(c, "password"))) {
//            isPassCheck(SPutils.getString(c, "name"), SPutils.getString(c, "password"));
//        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_login:
                LocationManager locationManager = (LocationManager) this
                        .getSystemService(Context.LOCATION_SERVICE);
                // 判断GPS模块是否开启，如果没有则开启
                if (!locationManager
                        .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    break;
                }

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    String phone = ed_phonet_num.getText().toString().trim();
                    String password = ed_password.getText().toString().trim();
                    if (Utils.textIsEmpty(phone) == false) {
                        Intent intent = new Intent(c, DiaLogActivity.class)
                                .putExtra("message", "请输入手机号码!");
                        startActivity(intent);
                        return;
                    }

                    if (Utils.textIsEmpty(password) == false) {
                        Intent intent = new Intent(c, DiaLogActivity.class)
                                .putExtra("message", "请输入手机号码!");
                        startActivity(intent);
                        return;
                    }

                    isPassCheck(phone, password);

                } else {
                    Toast.makeText(c, "没有开启定位,请开启定位", Toast.LENGTH_SHORT).show();
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.LOCATION_HARDWARE, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            0);


                }
                break;

            case R.id.tv_register://注册
                Utils.startActivity(LoginActivity.this, RegisterActivity.class);
                break;

            case R.id.tv_forget_password://忘记密码
                Utils.startActivityForResult(LoginActivity.this, ForgetPasswordActivity.class);
                break;


        }
    }



 /*
    * 审核结果接口
    *
    * checkState'-1:未提交审核信息 0未审核(审核中) 1审核通过 2拒绝 3禁用
    * */

    private void isPassCheck(final String phone, final String password) {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.IS_PASS_CHECK);
        params.addBodyParameter("mobile", phone);
        params.addBodyParameter("password", password);
        xUtilsApi.sendUrl(c, "post", params, false, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject optJSONObject = result.optJSONObject("info");
                    ConstantValue.DRIVERID = optJSONObject.optString("driverId");
                    Intent intent = new Intent(c, DiaLogActivity.class);
                    switch (optJSONObject.optString("checkState")) {
                        case "-1"://未提交审核信息
                            intent.putExtra("message", "亲~未提交审核信息!");
                            startActivityForResult(intent, 210);
                            break;

                        case "0"://0未审核(审核中)
                            intent.putExtra("message", "亲~正在审核中请耐心等待哦!");
                            startActivity(intent);
                            break;
                        case "2"://审核未通过
                            checkResult = optJSONObject.optString("checkResult");
                            intent.putExtra("message", "亲~审核信息未通过哦!");
                            startActivityForResult(intent, 210);
                            break;

                        case "1":
                            sumbintLogin();
                            break;

                    }
                    SPutils.save(c, "name", phone);
                    SPutils.save(c, "password", password);

                } else {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });
    }

    /*
    * 登录
    * */
    private void sumbintLogin() {
        final String phonet_num = ed_phonet_num.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();
        if (Utils.textIsEmpty(phonet_num) == false) {
            Intent intent = new Intent(c, DiaLogActivity.class)
                    .putExtra("message", "请输入手机号码!");
            startActivity(intent);
            return;
        }

        if (Utils.textIsEmpty(password) == false) {
            Intent intent = new Intent(c, DiaLogActivity.class)
                    .putExtra("message", "请输入手机号码!");
            startActivity(intent);
            return;
        }

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.LOGIN);
        params.addBodyParameter("mobile", phonet_num);
        params.addBodyParameter("password", password);
        params.addBodyParameter("deviceType", "1"); //1 Android 2 是 苹果
        params.addBodyParameter("registrationID", JPushInterface.getRegistrationID(getApplicationContext()));
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    EventBus.getDefault().post(new MainSendEvent("Update"));
                    JSONObject optJSONObject = result.optJSONObject("info");
                    ConstantValue.DRIVERID = optJSONObject.optString("driverId");
                    ConstantValue.nickName = optJSONObject.optString("nickName");
                    ConstantValue.driverImg = optJSONObject.optString("driverImg");
                    ConstantValue.PLATE_NUM = optJSONObject.optString("plateNum");//车牌号
                    Utils.startActivity(c, MainActivity.class);
                    startService(new Intent(LoginActivity.this, LocalService.class));
                    startService(new Intent(LoginActivity.this, RectifyRemoteService.class));
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
            case 100:
                if (!TextUtils.isEmpty(SPutils.getString(c, "name")) && !TextUtils.isEmpty(SPutils.getString(c, "password"))) {
                    ed_phonet_num.setText(SPutils.getString(c, "name"));
                    ed_password.setText(SPutils.getString(c, "password"));
                }
                break;
            case 210:
                Intent t = new Intent(c, FilInfoActivity.class);//完善信、信息
                t.putExtra("checkResult", checkResult);
                startActivity(t);
                break;
        }
    }


}
