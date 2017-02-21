package cn.exz.xugaung.activity.adapter;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.exz.xugaung.activity.BaseActivity;
import cn.exz.xugaung.activity.R;

/**
 * Created by pc on 2016/7/25.
 */
@ContentView(R.layout.activity_webview)
public class WebActivity extends BaseActivity {
    @ViewInject(R.id.webView)
    private WebView webView;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    @ViewInject(R.id.ll_back)
    private LinearLayout ll_back;


    @Override
    public void initView() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack() == true) {
                    webView.goBack(); // 后退
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("name"))) {

            webView.clearCache(true);
            tv_title.setText(getIntent().getStringExtra("name"));
            webView.loadUrl(getIntent().getStringExtra("info"));
            // 启用支持javascript
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            WebSettings settings = webView.getSettings();
            settings.setLoadsImagesAutomatically(true);
            settings.setJavaScriptEnabled(true);// 支持js
            // 启用支持javascript
            WebSettings webSettings = webView.getSettings();
            webSettings.setSupportZoom(true);
            webSettings.setDefaultTextEncodingName("utf-8");// 设置字符编码
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setBuiltInZoomControls(true);// support zoom
            webSettings.setUseWideViewPort(true);// 这个很关键
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDomStorageEnabled(true);// 允许DCO
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setUseWideViewPort(false);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setLoadsImagesAutomatically(true);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        }
    }
}
