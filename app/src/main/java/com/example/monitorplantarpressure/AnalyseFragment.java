package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnalyseFragment extends Fragment {
    public static final String SECTION_STRING = "analyse";

    public static AnalyseFragment newInstance(String sectionNumber) {
        AnalyseFragment analyseFragment = new AnalyseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SECTION_STRING, sectionNumber);
        analyseFragment.setArguments(bundle);
        return analyseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.anaylse_fragment, container, false);

        return view;
    }
}
