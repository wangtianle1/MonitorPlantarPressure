package com.example.monitorplantarpressure;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MainFragment2 extends Fragment {
    public static final String SECTION_STRING = "main";
    private LineChart lineChart;
    private DynamicLineChartManager dynamicLineChartManager;
    public List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合
    private int CollectTime = 1500;
    private CardView cv_warming, cv_booking, cv_gotime;
    private TextView tv_onTimeValue, tv_countHour, tv_countMin, tv_countSec;
    private int[] sportsMode = {20, 30};
    private int[] sprotsOff = {20, 30}, sprotsOn = {60, 90};
    private int sprot = 0;
    private CharSequence[] charSequences = {"休闲模式", "运动模式"};
    private int item = 0;
    private LinearLayout ll_gotime;
    public boolean isSprot = true;
    private int sportTime;
    private SharedViewModel viewModel;
    public static MainFragment2 newInstance(String sectionNumber) {
        MainFragment2 fragment = new MainFragment2();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        initview(view);
        tv_onTimeValue = view.findViewById(R.id.tv_onTimeValue);//实时压力
        tv_countHour = view.findViewById(R.id.tv_countHour);//运动时分秒
        tv_countMin = view.findViewById(R.id.tv_countMin);
        tv_countSec = view.findViewById(R.id.tv_countSec);
        lineChart = view.findViewById(R.id.lineChart);
        names.add("左脚掌");
        names.add("右脚掌");
        names.add("综合压力");
        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);
        dynamicLineChartManager = new DynamicLineChartManager(lineChart, names, colour, 3);
        dynamicLineChartManager.setDescription("");
        dynamicLineChartManager.setYAxis(100, 0, 10);
        sportTime = SPUtils.getInt("ST", 1, getActivity());
        tv_countHour.setText(sportTime / 3600 + " 小时");
        tv_countMin.setText(sportTime % 3600 / 60 + " 分钟");
        tv_countSec.setText(sportTime % 3600 % 60 + " 秒");
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        lineChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //设置数据采集速率
                final EditText editText = new EditText(getActivity());
                editText.setSingleLine();
                editText.setHint("调整采集速率");
                editText.requestFocus();
                editText.setFocusable(true);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("请输入你希望的速率\n单位（毫秒）")
                        .setView(editText)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String content = editText.getText().toString();
                                        if (content.isEmpty()) {
                                            Toast.makeText(getActivity(), "请填写速率：", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        //操作
                                        if (isInteger(content)) {
                                            CollectTime = Integer.valueOf(content);
                                        } else {
                                            Toast.makeText(getActivity(), "请输入整数", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                inputDialog.create().show();

                return false;
            }
        });
        //死循环添加数据
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(CollectTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SPUtils.getInt("ST", 1, getActivity());
                    if (isSprot) {
                        SPUtils.putInt("ST", sportTime, getActivity());
                    } else if (isSprot == false) {
                        sportTime++;

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_countHour.setText(sportTime / 3600 + " 小时");
                            tv_countMin.setText(sportTime % 3600 / 60 + " 分钟");
                            tv_countSec.setText(sportTime % 3600 % 60 + " 秒");
                            list.add((int) (Math.random() * sportsMode[0]) + 10);
                            list.add((int) (Math.random() * sportsMode[1]) + 10);
                            list.add((list.get(0) + list.get(1)) / 2);
                            dynamicLineChartManager.addEntry(list);
                            tv_onTimeValue.setText(list.get(2).toString());
                            list.clear();
                        }
                    });
                }
            }
        }).start();
        return view;
    }

    private void initview(View view) {
        cv_warming = view.findViewById(R.id.cv_warming);
        cv_warming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "warming", Toast.LENGTH_SHORT).show();
            }
        });
        cv_booking = view.findViewById(R.id.cv_booking);
        cv_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "booking", Toast.LENGTH_SHORT).show();
            }
        });
        cv_gotime = view.findViewById(R.id.cv_gotime);
        ll_gotime = view.findViewById(R.id.ll_gotime);
        cv_gotime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.foot)
                        .setTitle("当前运动模式")
                        .setSingleChoiceItems(charSequences, item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                item = which;
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "你选择了" + charSequences[item], Toast.LENGTH_SHORT).show();
                                if (charSequences[item].equals(charSequences[0])) {
                                    sportsMode[0] = sprotsOff[0];
                                    isSprot = true;
                                    sportsMode[1] = sprotsOff[1];
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {//休闲模式
                                            ll_gotime.setBackgroundColor(Color.rgb(114, 191, 218));
                                        }
                                    });
                                } else if (charSequences[item].equals(charSequences[1])) {//运动模式
                                    sportsMode[0] = sprotsOn[0];
                                    isSprot = false;
                                    sportsMode[1] = sprotsOn[1];
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_gotime.setBackgroundColor(Color.rgb(195, 84, 79));
                                        }
                                    });
                                }
                                viewModel.setData(isSprot);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}