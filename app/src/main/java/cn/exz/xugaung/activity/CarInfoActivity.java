package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import cn.exz.xugaung.activity.adapter.CarInfoAdapter;
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
public class CarInfoActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.sw)
    private SwipeRefreshLayout mSwipeLayout;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.tv_title_right)
    private TextView tv_title_right;


    @ViewInject(R.id.lv)
    private NoScrollListView lv;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    private CarInfoAdapter<CarInfoBean> adapter;

    private Context c = CarInfoActivity.this;

    private final int ADD_CAR_CALLBACK = 0x100;
    private List<CarInfoBean> list;

    @Override
    public void initView() {
        tv_title.setText("车辆信息");
        tv_title_right.setText("添加");
        adapter = new CarInfoAdapter<CarInfoBean>(CarInfoActivity.this);
        lv.setAdapter(adapter);
        ll_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.yeelow,
                R.color.blueness,
                R.color.gary);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getIsCheck().equals("1")) {
                    deleteCar(list.get(position).getTruckId(),position);
                } else if (!list.get(position).getIsCheck().equals("1")) {
                    Intent intent = new Intent(CarInfoActivity.this, AddCarActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("carInfoBean", list.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ADD_CAR_CALLBACK);
                }

            }
        });
    }

    /**
     * 删除车辆
     */
    private void deleteCar(String truckId, final int position) {
        XUtilsApi x = new XUtilsApi();
        RequestParams p = new RequestParams(Constant.DELETE_TRUCK);
        p.addBodyParameter("driverId", ConstantValue.DRIVERID);//司机id
        p.addBodyParameter("truckId", truckId);//车牌IDid
        x.sendUrl(c, "post", p, true, new XUtilsApi.URLSuccessListenter() {
            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    setResult(ADD_CAR_CALLBACK);
                    JSONObject optJSONObject = result.optJSONObject("info");
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                    adapter.deleteData(position);
                } else {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void initData() {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.TRUCKINFO);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        params.addBodyParameter("checkState", "-1");
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
            case R.id.tv_title_right://添加车辆
                addCarNum();
                break;

        }
    }

    /**
     * 添加车辆审核是否达到添加的上线
     */
    private void addCarNum() {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.ADD_TRUCK_CHECK);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);

        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    Intent intent = new Intent(CarInfoActivity.this, AddCarActivity.class);
                    startActivityForResult(intent, ADD_CAR_CALLBACK);
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

            case ADD_CAR_CALLBACK:// 添加车辆的回调
                    initData();
                break;
        }

    }

    @Override
    public void onRefresh() {
        initData();
        mSwipeLayout.setRefreshing(false);
    }
}
