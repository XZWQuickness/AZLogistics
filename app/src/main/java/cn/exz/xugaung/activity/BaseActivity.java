package cn.exz.xugaung.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.xutils.x;

/**
 * Created by pc on 2016/7/14.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
    }


    public abstract void initView();

    public abstract void initData();

}
