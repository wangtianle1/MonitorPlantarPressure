package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    public static final String SECTION_STRING = "home";

    public static HomeFragment newInstance(String sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    private WebView wb_home;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        wb_home = view.findViewById(R.id.wb_home);
        // 设置 WebView 允许执行 JavaScript 脚本
        wb_home.getSettings().setJavaScriptEnabled(true);
        // 确保跳转到另一个网页时仍然在当前 WebView 中显示
        // 而不是调用浏览器打开
        wb_home.setWebViewClient(new WebViewClient());
            String url = "https://www.espn.com/";
        wb_home.loadUrl(url);
        wb_home.setOnKeyListener(new View.OnKeyListener() {
            // 设置 WebView 的按键监听器，覆写监听器的 onKey 函数，对返回键作特殊处理
            // 当 WebView 可以返回到上一个页面时回到上一个页面
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && wb_home.canGoBack()) {
                    wb_home.goBack();
                    return true;
                }
                return false;
            }
        });
        return view;
    }
}
