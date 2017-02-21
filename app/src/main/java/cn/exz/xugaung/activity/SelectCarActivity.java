package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.exz.xugaung.activity.adapter.SelectCarAdapter;
import cn.exz.xugaung.activity.bean.CarInfoBean;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.XUtilsApi;
import cn.exz.xugaung.activity.view.NoScrollListView;

/**
 * Created by pc on 2016/7/18.
 * 车辆信息
 */
@ContentView(R.layout.activity_carinfo)
public class SelectCarActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.sw)
    private SwipeRefreshLayout mSwipeLayout;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;


    @ViewInject(R.id.lv)
    private NoScrollListView lv;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    private SelectCarAdapter<CarInfoBean> adapter;

    private Context c = SelectCarActivity.this;

    private final int SELECT_CAR_CALLBACK = 0x101;
    private List<CarInfoBean> list;

    @Override
    public void initView() {
        tv_title.setText("选择车辆");
        adapter = new SelectCarAdapter<CarInfoBean>(SelectCarActivity.this);
        lv.setAdapter(adapter);
        ll_back.setOnClickListener(this);
        mSwipeLayout.setEnabled(false);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getIsCheck().equals("1")) {
                    Intent intent = new Intent();
                    intent.putExtra("plateNum", list.get(position).getPlateNum());
                    intent.putExtra("truckId", list.get(position).getTruckId());
                    setResult(SELECT_CAR_CALLBACK, intent);
                    finish();

                }

            }
        });
    }


    @Override
    public void initData() {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.TRUCKINFO);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        params.addBodyParameter("checkState", "1");
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    String info = result.optString("info");
                    list = JSON.parseArray(info, CarInfoBean.class);
                    adapter.addendData(list, true);
                    adapter.updateAdapter();

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

        }
    }


}
