package com.hui.ffu365;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by hui on 2016/8/22.
 * 描述：显示网页链接Activity
 */
public class DetailLinkActivity extends AppCompatActivity{

    private WebView mWebView;
    private WebSettings mWebSettings;

    private String mUrl;

    public final static String URL_KEY = "URL_KEY";

    public void onCreate(Bundle savedInstanceStatus){
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_detail_link);
        mWebView = (WebView) findViewById(R.id.web_view);

        // 1.获取上个页面传递过来的url
        mUrl = getIntent().getStringExtra(URL_KEY);


        // 2.设置WebView的一些参数
        mWebSettings = mWebView.getSettings();// 获取WebView参数设置
        mWebSettings.setUseWideViewPort(false);  // 将图片调整到适合webview的大小
        mWebSettings.setJavaScriptEnabled(true); // 支持js
        mWebSettings.setLoadsImagesAutomatically(true);  // 支持自动加载图片

        // 3.利用WebView直接加载网页链接
        // 每次启动这个activity 所加载的url网页路径肯定是不一样的 ， Intent传值
        mWebView.loadUrl(mUrl);
    }
}
