package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.exz.xugaung.activity.adapter.AfficheInfoAdapter;
import cn.exz.xugaung.activity.adapter.WebActivity;
import cn.exz.xugaung.activity.bean.AfficheInfoBean;
import cn.exz.xugaung.activity.bean.NewEntity;
import cn.exz.xugaung.activity.utils.Constant;
import cn.exz.xugaung.activity.utils.ConstantValue;
import cn.exz.xugaung.activity.utils.XUtilsApi;

/**
 * Created by pc on 2016/8/5.
 * <p>
 * 公告消息
 */
@ContentView(R.layout.activity_afficheinfo)
public class AfficheInfoActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.ll_no_afficher)
    private LinearLayout ll_no_afficher;


    @ViewInject(R.id.lv)
    private ListView lv;

    private AfficheInfoAdapter<AfficheInfoBean> adapter;

    private  List<AfficheInfoBean> afficheInfoBeen;
    private Context c = AfficheInfoActivity.this;


    @Override
    public void initView() {
        tv_title.setText("公告消息");
        ll_back.setOnClickListener(this);
        adapter = new AfficheInfoAdapter<AfficheInfoBean>(c);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(c,WebActivity.class);
                intent.putExtra("name",afficheInfoBeen.get(position).getTitle());
                intent.putExtra("info",afficheInfoBeen.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

        XUtilsApi xUtilsApi = new XUtilsApi();
        RequestParams params = new RequestParams(Constant.AFFICHER_INFO);
        xUtilsApi.sendUrl(c, "post", params, false, new XUtilsApi.URLSuccessListenter() {

            @Override
            public void OnSuccess(NewEntity content, JSONObject result) {
                if (content.getResult().equals(ConstantValue.RESULT)) {
                    String optJSONObject = result.optString("info");
                    afficheInfoBeen = JSON.parseArray(optJSONObject, AfficheInfoBean.class);
                    if (afficheInfoBeen.size() > 0) {
                        adapter.addendData(afficheInfoBeen, true);
                        adapter.updateAdapter();
                    } else {
                        ll_no_afficher.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    }

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
