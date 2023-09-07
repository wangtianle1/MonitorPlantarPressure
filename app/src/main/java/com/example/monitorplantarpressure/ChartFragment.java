package com.example.monitorplantarpressure;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {
    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private List<Integer> lista = new ArrayList<>(); //数据集合
    private List<Integer> listb = new ArrayList<>(); //数据集合
    private List<String> namesa = new ArrayList<>(); //折线名字集合
    private List<String> namesb = new ArrayList<>(); //折线名字集合
    private List<Integer> coloura = new ArrayList<>();//折线颜色集合
    private List<Integer> colourb = new ArrayList<>();//折线颜色集合
    private ImageView   iv_rightback,iv_rgihtfoot,iv_leftfoot;
    private Button bt_goTo3Ddata;
    private int[] sprotsOff = {25};
    private int[] sprotsOn = {75};
    public static final String SECTION_STRING = "chart";
    private DynamicLineChartManager dynamicLineChartManagera,dynamicLineChartManagerb;
    public static ChartFragment newInstance(String sectionNumber) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment,container,false);
        LineChart lineCharta = view.findViewById(R.id.lineCharta);
        LineChart lineChartb = view.findViewById(R.id.lineChartb);
        initview(view);
        namesa.add("左脚掌");
        coloura.add(Color.CYAN);
        namesb.add("右脚掌");
        colourb.add(Color.GREEN);
        dynamicLineChartManager1 = new DynamicLineChartManager(lineCharta, namesa, coloura,3);
        dynamicLineChartManager2 = new DynamicLineChartManager(lineChartb, namesb, colourb,3);
        dynamicLineChartManager1.setDescription("");
        dynamicLineChartManager2.setDescription("");
        dynamicLineChartManager1.setYAxis(100, 0, 10);
        dynamicLineChartManager2.setYAxis(100, 0, 10);

        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
              getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO 添加运动模式判断，并根据判断修改随机数
                            lista.add((int) (Math.random() * sprotsOff[0] + 10));
                            listb.add((int) (Math.random() * sprotsOff[0] + 10));
                            dynamicLineChartManager1.addEntry(lista);
                            dynamicLineChartManager2.addEntry(listb);
                            lista.clear();
                            listb.clear();
                        }
                    });
                }
            }
        }).start();



        return view;
    }

    private void initview(View view) {
        bt_goTo3Ddata = view.findViewById(R.id.bt_goTo3Ddata);
        iv_rightback = view.findViewById(R.id.iv_rightback);
        iv_rightback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GifActivity.class));

            }
        });
        bt_goTo3Ddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GifActivity.class));
            }
        });
        iv_leftfoot= view.findViewById(R.id.iv_leftfoot);
        iv_rgihtfoot = view.findViewById(R.id.iv_rightfoot);
        // 使用Glide加载第一个本地GIF图片
        Glide.with(getActivity())
                .asGif()
                .load(R.drawable.leftfoot) // 替换成您的第一个本地GIF图片资源
                .apply(RequestOptions.centerInsideTransform())
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 在第一个GIF图片加载完成后执行操作（可选）
                        return false;
                    }
                })
                .into(iv_leftfoot);

        // 使用Glide加载第二个本地GIF图片
        Glide.with(getActivity())
                .asGif()
                .load(R.drawable.rightfoot) // 替换成您的第二个本地GIF图片资源
                .apply(RequestOptions.centerInsideTransform())
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 在第二个GIF图片加载完成后执行操作（可选）
                        return false;
                    }
                })
                .into(iv_rgihtfoot);
    }
}
