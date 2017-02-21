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
 * 意见反馈
 */
@ContentView(R.layout.activity_feedback)
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;


    @ViewInject(R.id.tv_submit_feedback)
    private TextView tv_submit_feedback;



    @ViewInject(R.id.ed_title)
    private EditText ed_title;

    @ViewInject(R.id.ed_content)
    private EditText ed_content;

    @ViewInject(R.id.ed_contact)
    private EditText ed_contact;


    private Context c=FeedBackActivity.this;


    @Override
    public void initView() {
        tv_title.setText("意见反馈");
        ll_back.setOnClickListener(this);
        tv_submit_feedback.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_submit_feedback:
                String tilte=ed_title.getText().toString().trim();
                String content=ed_content.getText().toString().trim();
                String contact=ed_contact.getText().toString().trim();
                if(TextUtils.isEmpty(tilte)||TextUtils.isEmpty(content)||TextUtils.isEmpty(contact)){
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", "亲~您的填写信息不完整哦!");
                    startActivity(intent);
                    return;
                }
                XUtilsApi xUtilsApi = new XUtilsApi();
                RequestParams params = new RequestParams(Constant.FEEDBACK);
                params.addBodyParameter("driverId", ConstantValue.DRIVERID);
                params.addBodyParameter("title", tilte);
                params.addBodyParameter("content", content);
                params.addBodyParameter("contact", contact);
                xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

                    @Override
                    public void OnSuccess(NewEntity content, JSONObject result) {
                        if (content.getResult().equals(ConstantValue.RESULT)) {
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
