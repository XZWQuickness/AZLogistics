package cn.exz.xugaung.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import org.xutils.view.annotation.ContentView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by pc on 2016/7/14.
 * 启动页
 */
@ContentView(R.layout.activity_boot_page)
public class BootPage extends BaseActivity {

    @Override
    public void initView() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BootPage.this,
                        LoginActivity.class); // 从启动动画ui跳转到主ui
                startActivity(intent);
                finish();
            }
        }, 2000); // 启动动画持续

    }


    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(BootPage.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(BootPage.this);
    }

}
