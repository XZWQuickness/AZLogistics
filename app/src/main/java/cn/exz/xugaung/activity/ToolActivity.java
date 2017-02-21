package cn.exz.xugaung.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.utils.Utils;

/**
 * Created by pc on 2016/8/23.
 * 使用工具
 */
@ContentView(R.layout.activity_tool)
public class ToolActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;

    @ViewInject(R.id.rl_history)
    private RelativeLayout rl_history;

    @ViewInject(R.id.rl_weizhang)
    private RelativeLayout rl_weizhang;




    @Override
    public void initView() {
        tv_title.setText("实用工具");
        ll_back.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        rl_weizhang.setOnClickListener(this);

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


            case R.id.rl_history://历史上的今天
                Utils.startActivity(ToolActivity.this,NesTopActivity.class);
                break;


            case R.id.rl_weizhang://违章查询
                Intent intent = new Intent(ToolActivity.this, DiaLogActivity.class)
                        .putExtra("message","亲~不要着急正在开发中!");
                startActivity(intent);
                break;


        }
    }
}
