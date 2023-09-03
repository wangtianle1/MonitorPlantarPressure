package com.example.monitorplantarpressure;
import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MainFragment2 extends Fragment {
    private TextView tv_name;
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
    private CharSequence[] charSequences = {"休闲模式", "运动模式"};
    private int item = 0;
    private LinearLayout ll_gotime;
    public boolean isSprot = true;
    private int sportTime;
    private boolean dataVisiable = true;
    private String name = "小王同学";
    private ImageView iv_userIcon;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private SharedPreferences sharedPreferences;
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
        lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataVisiable) {
                    dynamicLineChartManager.setFullTextSize(0f);
                    dataVisiable = false;
                } else {
                    dynamicLineChartManager.setFullTextSize(8f);
                    dataVisiable = true;
                }

            }
        });
        dynamicLineChartManager.setDescription("");
        dynamicLineChartManager.setYAxis(100, 0, 10);
        sportTime = SPUtils.getInt("ST", 1, getActivity());
        tv_countHour.setText(sportTime / 3600 + " 小时");
        tv_countMin.setText(sportTime % 3600 / 60 + " 分钟");
        tv_countSec.setText(sportTime % 3600 % 60 + " 秒");

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
        tv_name = view.findViewById(R.id.tv_name);
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
        iv_userIcon = view.findViewById(R.id.iv_userIcon);
        String uri = SPUtils.getString("avatar_image",null,getActivity());
        tv_name.setText(SPUtils.getString("name","小王同学", getActivity()));
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 修改姓名
                 * */
                final EditText editText = new EditText(getActivity());
                editText.setSingleLine();
                editText.setHint("用户名");
                editText.requestFocus();
                editText.setFocusable(true);
                AlertDialog.Builder nameDalog = new AlertDialog.Builder(getActivity())
                        .setTitle("此处修改你的用户名\n")
                        .setView(editText)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        name = editText.getText().toString().trim();
                                        if (name.isEmpty()) {
                                            Toast.makeText(getActivity(), "修改名不能为空", Toast.LENGTH_SHORT).show();

                                        }else {
                                                tv_name.setText(name);
                                                SPUtils.putString("name",name,getActivity());
                                        }
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                nameDalog.create().show();
            }
        });
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
        sharedPreferences =getActivity(). getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        iv_userIcon.setOnClickListener(v ->openImagePickerDialog());

    }

    private void openImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("")
                .setItems(new String[]{"拍照", "相册"}, (dialog, which) -> {
                    if (which == 0) {
                        // 打开相机
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
                    } else if (which == 1) {
                        // 打开相册
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_GALLERY);
                    }
                });
        builder.create().show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA && data != null) {
                // 从相机获取照片
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                setAvatarImage(imageBitmap);
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                // 从相册获取照片
                Uri selectedImage = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImage);
                    setAvatarImage(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void setAvatarImage(Bitmap imageBitmap) {
        iv_userIcon.setImageBitmap(imageBitmap);
        saveImageToSharedPreferences(imageBitmap);
    }

    private void saveImageToSharedPreferences(Bitmap imageBitmap) {
        // 将 Bitmap 转换为 Base64 编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // 将头像保存到 SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("avatar_image", encodedImage);
        editor.apply();
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