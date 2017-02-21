package cn.exz.xugaung.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.XUtilsApi;

import static cn.exz.xugaung.activity.R.id.ed_nickname;

/**
 * Created by pc on 2016/7/19.
 * 司机信息
 */
@ContentView(R.layout.activity_driver_info)
public class DriverInfoActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView title;


    @ViewInject(R.id.tv_true_name)
    private TextView tv_true_name;

    @ViewInject(R.id.tv_IDCardNum)
    private TextView tv_IDCardNum;

    @ViewInject(R.id.tv_drivingLicenseNum)
    private TextView tv_drivingLicenseNum;

    @ViewInject(R.id.tv_contactInfo)
    private TextView tv_contactInfo;


    @ViewInject(R.id.tv_emergencyContactPerson)
    private TextView tv_emergencyContactPerson;

    @ViewInject(R.id.tv_emergencyContactNumber)
    private TextView tv_emergencyContactNumber;

    @ViewInject(R.id.rl_contactInfo)
    private RelativeLayout rl_contactInfo;

    @ViewInject(R.id.rl_emergencyContactPerson)
    private RelativeLayout rl_emergencyContactPerson;

    @ViewInject(R.id.rl_emergencyContactNumber)
    private RelativeLayout rl_emergencyContactNumber;


    @ViewInject(R.id.ll_back)
    private LinearLayout back;

    private Context c = DriverInfoActivity.this;


    @Override
    public void initView() {
        title.setText("司机信息");
        back.setOnClickListener(this);
        rl_contactInfo.setOnClickListener(this);
        rl_emergencyContactPerson.setOnClickListener(this);
        rl_emergencyContactNumber.setOnClickListener(this);
    }

    @Override
    public void initData() {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.DriverInfo);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONObject info = result.optJSONObject("info");
                    tv_true_name.setText(info.optString("trueName"));
//                    String IDCardNum = info.optString("IDCardNum");
                    String drivingLicenseNum = info.optString("drivingLicenseNum");
//                    String headIDCardNum = IDCardNum.substring(IDCardNum.length()-10,IDCardNum.length()-4);
                    String headdrivingLicenseNum = drivingLicenseNum.substring(drivingLicenseNum.length()-10,drivingLicenseNum.length()-4);


//                    tv_IDCardNum.setText(IDCardNum.replace(headIDCardNum,"******"));
                    tv_drivingLicenseNum.setText(drivingLicenseNum .replace(headdrivingLicenseNum,"******"));
                    tv_contactInfo.setText(info.optString("contactInfo"));
                    tv_emergencyContactPerson.setText(info.optString("emergencyContactPerson"));
                    tv_emergencyContactNumber.setText(info.optString("emergencyContactNumber"));

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
            case R.id.rl_contactInfo:
                pop("修改联系方式");
                break;
            case R.id.rl_emergencyContactPerson:
                pop("修改紧急联系人");
                break;
            case R.id.rl_emergencyContactNumber:
                pop("修改紧急联系方式");
                break;
        }
    }

    private void pop(final String title) {
        final AlertDialog dlgtwo = new AlertDialog.Builder(DriverInfoActivity.this)
                .create();
        View viewtwo = LayoutInflater.from(DriverInfoActivity.this).inflate(
                R.layout.dialog_shuanganniu, null);
        dlgtwo.setView(DriverInfoActivity.this.getLayoutInflater().inflate(
                R.layout.dialog_shuanganniu, null));
        dlgtwo.show();
        dlgtwo.getWindow().setContentView(viewtwo);
        TextView queding = (TextView) viewtwo.findViewById(R.id.queding);
        TextView quxiao = (TextView) viewtwo.findViewById(R.id.quxiao);
        TextView titlename = (TextView) viewtwo
                .findViewById(R.id.titlename);
        titlename.setText(title);
        final EditText editext = (EditText) viewtwo
                .findViewById(ed_nickname);
        editext.setHint("请输入" + title);
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
                String editextInfo = editext.getText().toString().trim();
                if (TextUtils.isEmpty(editextInfo)) {
                    Intent intent = new Intent(DriverInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", "亲~输入内容不能为空哦!");
                    startActivity(intent);
                    return;

                } else {
                    switch (title) {

                        case "修改联系方式":
                            sumbintChangeDriverInfo(editextInfo, "", "");
                            tv_contactInfo.setText(editextInfo);
                            break;

                        case "修改紧急联系人":
                            sumbintChangeDriverInfo("", editextInfo, "");
                            tv_emergencyContactPerson.setText(editextInfo);
                            break;

                        case "修改紧急联系方式":
                            sumbintChangeDriverInfo("", "", editextInfo);
                            tv_emergencyContactNumber.setText(editextInfo);
                            break;

                    }
                }

            }
        });
    }

    private void sumbintChangeDriverInfo(String contactInfo, String emergencyContactPerson, String emergencyContactNumber) {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.CHANGE_DRIVER_INFO);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        if (!TextUtils.isEmpty(contactInfo)) {

            params.addBodyParameter("contactInfo", contactInfo);
        }
        if (!TextUtils.isEmpty(emergencyContactPerson)) {

            params.addBodyParameter("emergencyContactPerson", emergencyContactPerson);
        }
        if (!TextUtils.isEmpty(emergencyContactNumber)) {

            params.addBodyParameter("emergencyContactNumber", emergencyContactNumber);
        }
        xUtilsApi.sendUrl(DriverInfoActivity.this, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {

                    Intent intent = new Intent(DriverInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DriverInfoActivity.this, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });


    }
}
