package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
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
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/18.
 * 验证手机号码
 */
@ContentView(R.layout.activity_verify_phone)
public class VerifyPhoneActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.tv_sumbnt_next)
    private TextView tv_sumbnt_next;

    @ViewInject(R.id.code)
    private TextView code;

    @ViewInject(R.id.ed_phone)
    private EditText ed_phone;
    @ViewInject(R.id.ed_code)
    private EditText ed_code;

    private String getCode = "";

    private Context c = VerifyPhoneActivity.this;


    @Override
    public void initView() {
        title.setText("验证手机");
        ll_back.setOnClickListener(this);
        tv_sumbnt_next.setOnClickListener(this);
        code.setOnClickListener(this);
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

            case R.id.code:
                String phone = ed_phone.getText().toString().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~请输入手机号哦!");
                    startActivity(intent);
                    return;
                }
                senCode(phone);
                break;


            case R.id.tv_sumbnt_next:
                if (!ed_code.getText().toString().trim().equals(getCode)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~验证码不正确哦!");
                    startActivity(intent);
                    return;
                }
                Intent intent=new Intent(VerifyPhoneActivity.this, AddCarActivity.class);
                intent.putExtra("TruckId",getIntent().getStringExtra("TruckId"));
                startActivity(intent);
                break;
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
        params.addBodyParameter("useType", "2"); //1：用于注册   2：用于已注册
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject optJSONObject = result.optJSONObject("info");
                    getCode = optJSONObject.optString("code");
                    ed_code.setText(getCode);
                } else {

                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }
}