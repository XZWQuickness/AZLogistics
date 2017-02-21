package cn.exz.xugaung.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_webview)
public class WebViewActivity extends BaseActivity implements OnKeyListener {
    private TextView title_top;
    private RelativeLayout rl_top_color;
    private Context context = WebViewActivity.this;
    private String url;
    private WebView webView;
    private ImageView back;
    private int number;


    public void initData() {
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("info"))
                || !TextUtils.isEmpty(intent.getStringExtra("name"))) {
            title_top.setText(intent.getStringExtra("name"));
            url = intent.getStringExtra("info");
            webView.loadUrl(url);
            // 启用支持javascript
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            WebSettings settings = webView.getSettings();
            settings.setLoadsImagesAutomatically(true);
            settings.setJavaScriptEnabled(true);// 支持js
            // 启用支持javascript
            WebSettings webSettings = webView.getSettings();
            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setBuiltInZoomControls(true);// support zoom
            webSettings.setUseWideViewPort(true);// 这个很关键
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setDomStorageEnabled(true);// 允许DCO
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webSettings.setUseWideViewPort(false);
            webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setLoadsImagesAutomatically(true);

            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    WebBackForwardList list = view.copyBackForwardList();
                    number = list.getSize();

                    return true;
                }
            });

            webView.setOnKeyListener(this);

        }
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        title_top = (TextView) findViewById(R.id.tv_title);
        webView = (WebView) findViewById(R.id.webView);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webView.canGoBack() == true) {
                    webView.goBack(); // 后退
                } else {
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { // 表示按返回键

                webView.goBack(); // 后退

                // webview.goForward();//前进
                return true; // 已处理
            }
        }
        return false;
    }

}
