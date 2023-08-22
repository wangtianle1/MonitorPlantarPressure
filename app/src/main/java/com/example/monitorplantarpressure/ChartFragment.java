package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;

public class ChartFragment extends Fragment {
    public static final String SECTION_STRING = "chart";
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
        View view = inflater.inflate(R.layout.chart_layout,container,false);
        LineChart lineCharta = view.findViewById(R.id.lineCharta);
        LineChart lineChartb = view.findViewById(R.id.lineChartb);
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {

    }
});

        return view;


    }

}
