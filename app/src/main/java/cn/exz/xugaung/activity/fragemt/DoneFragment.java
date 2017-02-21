package cn.exz.xugaung.activity.fragemt;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.exz.xugaung.activity.DiaLogActivity;
import cn.exz.xugaung.activity.R;
import cn.exz.xugaung.activity.adapter.OrderAdapter;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.bean.OrderBean;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.MainSendEvent;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/7/14.
 * 已完成
 */
@ContentView(R.layout.fragment_underwa)
public class DoneFragment extends BaseFragment implements XRecyclerView.LoadingListener {


    @ViewInject(R.id.recyclerview)
    private XRecyclerView mRecyclerView;
    private OrderAdapter adapter;
    private int page = 1;
    @ViewInject(R.id.ll_is_view)
    private LinearLayout ll_is_view;

    private boolean isRefresh = true;

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshing(true);
        adapter = new OrderAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {
    }

    /*
     *
     * 获取订单列表
     * */
    private void getOrderList() {
        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.ORDER_LIST);
        params.addBodyParameter("driverId", ConstantValue.DRIVERID);
        params.addBodyParameter("orderState", "2");//订单状态  0:全部  1：进行中  2:已完成
        params.addBodyParameter("page", page + "");
        params.addBodyParameter("pageType", page + "");//分页类型 （1：第page页的数据   2：从第1页到第page页的数据）
        xUtilsApi.sendUrl(getActivity(), "POST", params, false, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    String info = result.optString("info");
                    List<OrderBean> orderBeen = JSON.parseArray(info, OrderBean.class);
                    adapter.addendData(orderBeen, isRefresh);
                    adapter.updateAdapter();
                    if (adapter.getAdapterData().size() <= 0) {
                        ll_is_view.setVisibility(View.VISIBLE);
                    } else {
                        ll_is_view.setVisibility(View.GONE);

                    }

                } else {
                    Intent intent = new Intent(getActivity(), DiaLogActivity.class)
                            .putExtra("message", content.getMessage());
                    startActivity(intent);
                }
            }
        });

    }

    //主线程接收消息
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMainThread(MainSendEvent event) {
        if (event != null) {
            int t = (int) event.getT();
            if (t == 5) {
                getOrderList();
            }
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        getOrderList();
        mRecyclerView.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        page++;
        isRefresh = false;
        getOrderList();
        mRecyclerView.loadMoreComplete();

    }
}

