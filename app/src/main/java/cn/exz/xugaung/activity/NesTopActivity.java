package cn.exz.xugaung.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;
import java.util.List;

import cn.exz.xugaung.activity.adapter.NewsTopAdapter;
import cn.exz.xugaung.activity.bean.NewsTopBean;
import cn.exz.xugaung.activity.utils.Constant;

/**
 * Created by pc on 2016/8/17.
 * 历史上的今天
 */
@ContentView(R.layout.activity_newstop)
public class NesTopActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.lv)
    private ListView lv;


    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;


    private NewsTopAdapter<NewsTopBean> adapter;
    private  int month,day;

    @Override
    public void initView() {
        tv_title.setText("历史上的今天");
        adapter = new NewsTopAdapter<NewsTopBean>(NesTopActivity.this);
        lv.setAdapter(adapter);
        ll_back.setOnClickListener(this);
        System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        month++;
        day= calendar.get(calendar.DAY_OF_MONTH);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams(Constant.LISHI_DAY);
        params.addBodyParameter("v", "1.0");
        params.addBodyParameter("month", month+"");
        params.addBodyParameter("day", day+"");
        params.addBodyParameter("key", "c98b9115bc97c12779a5693ad96b9526");
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                String jsonObject = result.optString("result");
                List<NewsTopBean> newsTopBeen = JSON.parseArray(jsonObject, NewsTopBean.class);
                adapter.addendData(newsTopBeen, true);
                adapter.updateAdapter();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

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
