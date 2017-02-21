package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.Utils;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/18.
 * 注册
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.code)
    private TextView snedCode;

    @ViewInject(R.id.ed_phone)
    private EditText ed_phone;

    @ViewInject(R.id.ed_code)
    private EditText ed_code;

    @ViewInject(R.id.ed_password)
    private EditText ed_password;

    @ViewInject(R.id.tv_sumbnt_register)
    private TextView tv_sumbnt_register;
    @ViewInject(R.id.cb)
    private CheckBox cb;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    private Context c = RegisterActivity.this;


    private String getCode = "";

    private boolean isExecute = false;

    @Override
    public void initView() {
        title.setText("注册");
        ll_back.setOnClickListener(this);
        snedCode.setOnClickListener(this);
        tv_sumbnt_register.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        String phone = ed_phone.getText().toString().trim();
        String code = ed_code.getText().toString().trim();
        String password = ed_password.getText().toString().trim();
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.code: //发送验证码
                checkPhone("0");

                break;

            case R.id.tv_sumbnt_register:
                if (!cb.isChecked()) {
                    Utils.toast(c, "请同意安至物流司机端用户服务协议!");
                    return;
                }
                checkPhone("1");

                break;
        }
    }

    private void checkPhone(String state) {
        final String phone = ed_phone.getText().toString().trim();
        String code = ed_code.getText().toString().trim();
        final String password = ed_password.getText().toString().trim();
        if (Utils.textIsEmpty(phone) == false) {
            Intent intent = new Intent(RegisterActivity.this, DiaLogActivity.class)
                    .putExtra("message", "请输入手机号码!");
            startActivity(intent);
            return;
        }

        if (phone.length() > 12 || phone.length() < 6) {
            Intent intent = new Intent(RegisterActivity.this, DiaLogActivity.class)
                    .putExtra("message", "请填写正确的手机号码!");
            startActivity(intent);
            return;
        }

        if (state.equals("0")) {
            senCode(phone);
        }

        if (state.equals("1")) {
            if (Utils.textIsEmpty(code) == false) {
                Intent intent = new Intent(RegisterActivity.this, DiaLogActivity.class)
                        .putExtra("message", "请输入验证码!");
                startActivity(intent);
                return;
            }
            if (Utils.textIsEmpty(password) == false) {
                Intent intent = new Intent(RegisterActivity.this, DiaLogActivity.class)
                        .putExtra("message", "请输入密码!");
                startActivity(intent);
                return;
            }
            XUtilsApi xUtilsApi = new XUtilsApi();
            RequestParams params = new RequestParams(Constant.REGISTER);
            params.addBodyParameter("mobile", phone);
            params.addBodyParameter("password", password);
            params.addBodyParameter("verifyCode", ed_code.getText().toString().trim()); //验证码
            xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

                @Override
                public void OnSuccess(NewEntity content, JSONObject result) {
                    if (content.getResult().equals(ConstantValue.RESULT)) {
                        JSONObject optJSONObject = result.optJSONObject("info");
                        ConstantValue.DRIVERID = optJSONObject.optString("driverId");
                        SPutils.save(c, "name", phone);
                        SPutils.save(c, "password", password);
                        Utils.startActivity(c, FilInfoActivity.class);//完善信、信息
                    } else {
                        Intent intent = new Intent(c, DiaLogActivity.class)
                                .putExtra("message", content.getMessage());
                        startActivity(intent);
                    }
                }
            });

        }
    }


    /*
    *
    * 发送验证码
    * */
    private void senCode(String phone) {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.SEND_CODE);
        params.addBodyParameter("mobile", phone);
        params.addBodyParameter("useType", "1"); //1：用于注册   2：用于找回密码
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject optJSONObject = result.optJSONObject("info");
                    Intent intent = new Intent(RegisterActivity.this, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                    getCode = optJSONObject.optString("code");
//                    ed_code.setText(getCode);
                } else {

                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }
}
