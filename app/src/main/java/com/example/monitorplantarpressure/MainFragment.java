package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    public static MainFragment newInstance(String sectionNumber){
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
//        TextView textView = (TextView) view.findViewById(R.id.index_bottom_bar_fragment_example);
//        textView.setText(getString(R.string.fragment_example_string, getArguments().getString(SECTION_STRING)));
        return view;
    }
//    LineChart lineChart = findViewById(R.id.lineChart);

//        List<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(1, 10));
//        entries.add(new Entry(2, 20));
//        entries.add(new Entry(3, 15));
    // Add more entries as needed

//        LineDataSet dataSet = new LineDataSet(entries, "Label");
//        dataSet.setColor(Color.BLUE);
//        dataSet.setValueTextColor(Color.BLACK);
//
//        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);

    // Customize chart settings
//        XAxis xAxis = lineChart.getXAxis();
//        YAxis yAxisLeft = lineChart.getAxisLeft();
//        YAxis yAxisRight = lineChart.getAxisRight();
//        Legend legend = lineChart.getLegend();

    // Configure other settings as needed

//        lineChart.invalidate();
}
