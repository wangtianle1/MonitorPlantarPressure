package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.Random;

public class MainFragment extends Fragment {
    private static final String SECTION_STRING = "main";

    private LineChart lineChart;
    private LineDataSet dataSet;
    private LineData lineData;
    private Handler handler;
    private static final int MSG_UPDATE_CHART = 1;
    private Random random = new Random();


    public static MainFragment newInstance(String sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        lineChart = view.findViewById(R.id.lineChart);
        dataSet = new LineDataSet(new ArrayList<>(), "Real-time Data");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // 配置 LineChart 样式和交互等
        // ...

        // 初始化 Handler 用于定时更新数据
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_UPDATE_CHART) {
                    float newValue = generateRandomValue(); // 生成随机数据
                    updateChart(newValue); // 更新图表
                    sendEmptyMessageDelayed(MSG_UPDATE_CHART, 200); // 1秒后再次发送消息
                }
            }
        };

        // 开始定时更新数据
        handler.sendEmptyMessage(MSG_UPDATE_CHART);
        return view;
    }
    // 随机生成一个假数据值
    private float generateRandomValue() {
        return random.nextFloat() * 100;
    }

    // 更新图表数据
    private void updateChart(float value) {
        Entry newEntry = new Entry(dataSet.getEntryCount(), value);
        lineData.addEntry(newEntry, 0);  // 添加数据点到 LineData
        lineData.notifyDataChanged();     // 通知 LineData 数据已更改
        lineChart.notifyDataSetChanged(); // 通知 LineChart 数据已更改
        lineChart.invalidate();          // 刷新图表显示
    }
}
