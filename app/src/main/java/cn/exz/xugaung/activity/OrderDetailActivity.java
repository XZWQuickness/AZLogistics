package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.exz.xugaung.activity.adapter.GoodsInfoAdapter;
import cn.exz.xugaung.activity.bean.GoodsListBean;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.XUtilsApi;
import cn.exz.xugaung.activity.view.MyScrollView;
import cn.exz.xugaung.activity.view.NoScrollListView;

/**
 * Created by pc on 2016/7/23.
 * 订单详情
 */
@ContentView(R.layout.activity_detail)
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_title)
    private TextView title;

    @ViewInject(R.id.tv_orderdNum)
    private TextView tv_orderdNum;


    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;


    @ViewInject(R.id.lv)
    private NoScrollListView lv;

    @ViewInject(R.id.sv)
    private MyScrollView sv;

    private GoodsInfoAdapter adapter;

    private Context c = OrderDetailActivity.this;


    @ViewInject(R.id.tv_plateNum)
    private TextView tv_plateNum;

    @ViewInject(R.id.tv_poundNum)
    private TextView tv_poundNum;

    @ViewInject(R.id.tv_createDate)
    private TextView tv_createDate;

    @ViewInject(R.id.tv_createAddress)
    private TextView tv_createAddress;


    @ViewInject(R.id.tv_finishAddress)
    private TextView tv_finishAddress;


    @ViewInject(R.id.tv_finishDate)
    private TextView tv_finishDate;




    @ViewInject(R.id.tv_location)
    private TextView tv_location;




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void initView() {
        title.setText("订单详情");

        adapter = new GoodsInfoAdapter(c);
        lv.setAdapter(adapter);
        ll_back.setOnClickListener(this);
        sv.setFocusable(false);
        sv.smoothScrollTo(0, 0);
        tv_orderdNum.setText(getIntent().getStringExtra("orderId"));
    }

    @Override
    public void initData() {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.ORDER_DETAIL);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        params.addBodyParameter("orderId", getIntent().getStringExtra("orderId"));
        xUtilsApi.sendUrl(c, "post", params, true, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    JSONArray j = result.optJSONArray("info");
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject json = j.optJSONObject(i);
                        tv_plateNum.setText(json.optString("plateNum"));
                        tv_poundNum.setText(json.optString("poundNum"));
                        tv_createAddress.setText(json.optString("createAddress"));
                        tv_createDate.setText(json.optString("createDate"));
                        tv_finishDate.setText(json.optString("finishDate"));
                        tv_finishAddress.setText(json.optString("finishAddress"));
                        String goodsList = json.optString("goodsList");
                        List<GoodsListBean> goodsListBeen = JSON.parseArray(goodsList, GoodsListBean.class);
                        adapter.addendData(goodsListBeen, true);
                        adapter.updateAdapter();
                    }


                } else {
                    Intent intent = new Intent(c, DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });
    }
}
