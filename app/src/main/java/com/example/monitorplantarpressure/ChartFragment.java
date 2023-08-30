package com.example.monitorplantarpressure;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment  {
    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private List<Integer> lista = new ArrayList<>(); //数据集合
    private List<Integer> listb = new ArrayList<>(); //数据集合
    private List<String> namesa = new ArrayList<>(); //折线名字集合
    private List<String> namesb = new ArrayList<>(); //折线名字集合
    private List<Integer> coloura = new ArrayList<>();//折线颜色集合
    private List<Integer> colourb = new ArrayList<>();//折线颜色集合
    private int[] sprotsOff = {25};
    private int[] sprotsOn = {75};
    private boolean sportMode = false;
    public static final String SECTION_STRING = "chart";
    private SharedViewModel viewModel;
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
        namesa.add("左脚掌");
        coloura.add(Color.CYAN);
        namesb.add("右脚掌");
        colourb.add(Color.GREEN);
        dynamicLineChartManager1 = new DynamicLineChartManager(lineCharta, namesa, coloura,10);
        dynamicLineChartManager2 = new DynamicLineChartManager(lineChartb, namesb, colourb,10);
        dynamicLineChartManager1.setDescription("");
        dynamicLineChartManager2.setDescription("");
        dynamicLineChartManager1.setYAxis(100, 0, 10);
        dynamicLineChartManager2.setYAxis(100, 0, 10);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                sportMode = aBoolean;
            }
        });
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
                          if (sportMode){
                              lista.add((int) (Math.random() * sprotsOff[0] + 10));
                              listb.add((int) (Math.random() * sprotsOff[0] + 10));
                          }else {
                              lista.add((int) (Math.random() * sprotsOn[0] + 10));
                              listb.add((int) (Math.random() * sprotsOn[0] + 10));
                          }

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

}
