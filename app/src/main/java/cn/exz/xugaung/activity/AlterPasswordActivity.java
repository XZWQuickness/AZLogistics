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
import cn.exz.xugaung.activity.utils.SPutils;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/18.
 * 修改密码
 */
@ContentView(R.layout.activity_alter_password)
public class AlterPasswordActivity extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.tv_finish_login)
    private TextView tv_finish_login;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.ed_former_password)
    private EditText ed_former_password;

    @ViewInject(R.id.ed_password)
    private EditText ed_password;

    @ViewInject(R.id.ed_new_password)
    private EditText ed_new_password;


    private Context c = AlterPasswordActivity.this;

    @Override
    public void initView() {
        title.setText("修改密码");
        ll_back.setOnClickListener(this);
        tv_finish_login.setOnClickListener(this);
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

            case R.id.tv_finish_login:
                String formerPassword = ed_former_password.getText().toString().trim();
                final String password = ed_password.getText().toString().trim();
                String newPassword = ed_new_password.getText().toString().trim();
                if (TextUtils.isEmpty(formerPassword) || TextUtils.isEmpty(password)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~输入项不能为空哦!");
                    startActivity(intent);
                    return;
                }
                if (!SPutils.getString(c, "password").equals(formerPassword)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~您输入旧密码不正确哦!");
                    startActivity(intent);
                    return;
                }

                if (!password.equals(newPassword)) {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~您两次输入的密码不一致哦!");
                    startActivity(intent);
                    return;
                }
                XUtilsApi xUtilsApi = new XUtilsApi();
                RequestParams params = new RequestParams(Constant.MODIFY_PASSWORD);
                params.addBodyParameter("driverId", ConstantValue.DRIVERID);
                params.addBodyParameter("password", formerPassword);
                params.addBodyParameter("newPassword", password);
                xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

                    @Override
                    public void OnSuccess(NewEntity content, JSONObject result) {
                        if (content.getResult().equals(ConstantValue.RESULT)) {
                            SPutils.save(c, "password", password);
                            Intent intent = new Intent(c, DiaLogActivity.class)
                                    .putExtra("message", content.getMessage());
                            startActivity(intent);
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
}
