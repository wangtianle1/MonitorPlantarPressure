package com.example.monitorplantarpressure;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private MyViewPager index_vp_fragment_list_top;
    private ImageView index_bottom_bar_home_image;
    private LinearLayout index_bottom_bar_home;
    private ImageView index_bottom_bar_dynamic_state_image;
    private LinearLayout index_bottom_bar_dynamic_state;
    private ImageView index_bottom_bar_integral_image;
    private LinearLayout index_bottom_bar_integral;
    private ImageView index_bottom_bar_me_image;
    private LinearLayout index_bottom_bar_me;
    private FrameLayout index_fl_bottom_bar;
    private ImageView index_bottom_bar_scan;
    private RelativeLayout index_rl_contain;

    private List<Fragment> mFragments;

    private FragmentIndexAdapter mFragmentIndexAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullscreen(true, true);
        setAndroidNativeLightStatusBar(this, true);
        initView();
        initData();
        initEvent();
    }
    public void startScanActivity(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("请扫描二维码");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // 用户取消扫描
            } else {
                String scanResult = result.getContents();
                // 使用扫描到的数据作为搜索查询

                // 将扫描结果附加到百度搜索引擎的查询 URL
                String searchUrl = "https://www.baidu.com/s?wd=" + Uri.encode(scanResult);

                // 通过外部浏览器打开查询 URL
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
                startActivity(browserIntent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void initEvent() {
        index_bottom_bar_home.setOnClickListener(new TabOnClickListener(0));
        index_bottom_bar_dynamic_state.setOnClickListener(new TabOnClickListener(1));
        index_bottom_bar_integral.setOnClickListener(new TabOnClickListener(2));
        index_bottom_bar_me.setOnClickListener(new TabOnClickListener(3));
        index_bottom_bar_scan.setOnClickListener(new TabOnClickListener(4));
    }
    private void initIndexFragmentAdapter() {
        mFragmentIndexAdapter = new FragmentIndexAdapter(this.getSupportFragmentManager(), mFragments);
        index_vp_fragment_list_top.setAdapter(mFragmentIndexAdapter);
        index_bottom_bar_home.setSelected(true);
        index_vp_fragment_list_top.setCurrentItem(0);
        index_vp_fragment_list_top.setOffscreenPageLimit(3);
        index_vp_fragment_list_top.addOnPageChangeListener(new TabOnPageChangeListener());
    }

    private void initData() {
        mFragments = new ArrayList<Fragment>();
        mFragments.add(HomeFragment.newInstance(getResources().getString(R.string.index_bottom_bar_home)));
        mFragments.add(ChartFragment.newInstance(getResources().getString(R.string.index_bottom_bar_dynamic_state)));
        mFragments.add(AnalyseFragment.newInstance(getResources().getString(R.string.index_bottom_bar_integral)));
        mFragments.add(MainFragment2.newInstance(getResources().getString(R.string.index_bottom_bar_me)));
        initIndexFragmentAdapter();
    }
    private void initView() {
        index_vp_fragment_list_top = (MyViewPager) findViewById(R.id.index_vp_fragment_list_top);
        index_bottom_bar_home_image = (ImageView) findViewById(R.id.index_bottom_bar_home_image);
        index_bottom_bar_home = (LinearLayout) findViewById(R.id.index_bottom_bar_home);
        index_bottom_bar_dynamic_state_image = (ImageView) findViewById(R.id.index_bottom_bar_dynamic_state_image);
        index_bottom_bar_dynamic_state = (LinearLayout) findViewById(R.id.index_bottom_bar_dynamic_state);
        index_bottom_bar_integral_image = (ImageView) findViewById(R.id.index_bottom_bar_integral_image);
        index_bottom_bar_integral = (LinearLayout) findViewById(R.id.index_bottom_bar_integral);
        index_bottom_bar_me_image = (ImageView) findViewById(R.id.index_bottom_bar_me_image);
        index_bottom_bar_me = (LinearLayout) findViewById(R.id.index_bottom_bar_me);
        index_fl_bottom_bar = (FrameLayout) findViewById(R.id.index_fl_bottom_bar);
        index_bottom_bar_scan = (ImageView) findViewById(R.id.index_bottom_bar_scan);
        index_rl_contain = (RelativeLayout) findViewById(R.id.index_rl_contain);
    }
    public void setFullscreen(boolean isShowStatusBar, boolean isShowNavigationBar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //隐藏标题栏
        // getSupportActionBar().hide();
        setNavigationStatusColor(Color.TRANSPARENT);
    }

    public void setNavigationStatusColor(int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(color);
            getWindow().setStatusBarColor(color);
        }
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    /**
     * Bottom_Bar的点击事件
     */
    public class TabOnClickListener implements View.OnClickListener {

        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            if (index == 4) {
                startScanActivity(index_bottom_bar_scan);
                // 跳转到Scan界面
//                Toast.makeText(MainActivity.this, "点击了扫描按钮", Toast.LENGTH_SHORT).show();
            } else {
                //选择某一页
                index_vp_fragment_list_top.setCurrentItem(index, false);
            }
        }

    }

    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {
        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        private void ImageChange(int drawble){
            switch (drawble ){
                case 0:
                    index_bottom_bar_home_image.setImageResource(R.drawable.home_on);
                    index_bottom_bar_dynamic_state_image.setImageResource(R.drawable.chart_off);
                    index_bottom_bar_integral_image.setImageResource(R.drawable.find_on);
                    index_bottom_bar_me_image.setImageResource(R.drawable.user_off);
                    break;
                    case 1:
                    index_bottom_bar_home_image.setImageResource(R.drawable.home_off);
                    index_bottom_bar_dynamic_state_image.setImageResource(R.drawable.chart_on);
                    index_bottom_bar_integral_image.setImageResource(R.drawable.find_on);
                    index_bottom_bar_me_image.setImageResource(R.drawable.user_off);
                    break;
                    case 2:
                    index_bottom_bar_home_image.setImageResource(R.drawable.home_off);
                    index_bottom_bar_dynamic_state_image.setImageResource(R.drawable.chart_off);
                    index_bottom_bar_integral_image.setImageResource(R.drawable.find_off);
                    index_bottom_bar_me_image.setImageResource(R.drawable.user_off);
                    break;
                    case 3:
                    index_bottom_bar_home_image.setImageResource(R.drawable.home_off);
                    index_bottom_bar_dynamic_state_image.setImageResource(R.drawable.chart_off);
                    index_bottom_bar_integral_image.setImageResource(R.drawable.find_on);
                    index_bottom_bar_me_image.setImageResource(R.drawable.user_on);
                    break;
            }
        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            resetTextView();
            switch (position) {
                case 0:
                    index_bottom_bar_home.setSelected(true);
                    break;
                case 1:
                    index_bottom_bar_dynamic_state.setSelected(true);
                    break;
                case 2:
                    index_bottom_bar_integral.setSelected(true);
                    break;
                case 3:
                    index_bottom_bar_me.setSelected(true);
                    break;
            }
            ImageChange(position);

        }
    }

    /**
     * 重置所有TextView的字体颜色
     */
    private void resetTextView() {
        index_bottom_bar_home.setSelected(false);
        index_bottom_bar_dynamic_state.setSelected(false);
        index_bottom_bar_integral.setSelected(false);
        index_bottom_bar_me.setSelected(false);
    }

}

