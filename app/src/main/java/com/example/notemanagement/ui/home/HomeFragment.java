package com.example.notemanagement.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagement.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
         PieChart pieChart = root.findViewById(R.id.piechartDashBoard);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        setUpPieChart(pieChart);

//        pieChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);

        return root;
    }

    public void setUpPieChart(PieChart pieChart){
        pieChart.setRotationEnabled(false);//  Cho phép xoay Pie Chart
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(0);//Tạo vòng tròn ở tâm
        pieChart.setTransparentCircleAlpha(0);
//        pieChart.setCenterText("Statistics");//Tạo text cho vòng tròn ở tâm
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(false);
        addDataSet(pieChart);
    }

    private static void addDataSet(PieChart pieChart) {

        ArrayList<String> label = new ArrayList<>();
        ArrayList<PieEntry> value = new ArrayList<>();

        String[] xData = { "Processing", "Pending", "Done" };
        float[] yData = { 30, 50, 20 };

        for (int i = 0; i < xData.length;i++) {
            label.add(xData[i]);
        }

        for (int i = 0; i < yData.length;i++){
            value.add(new PieEntry(yData[i],xData[i]));
        }

        PieDataSet pieDataSet=new PieDataSet(value,"");
        pieDataSet.setSliceSpace(2);//Đặt khoảng trống ở giữa các lát cắt
        pieDataSet.setValueTextSize(15);
        pieDataSet.setValueTextColor(Color.WHITE);
        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.BLACK);

        pieDataSet.setColors(colors);

//        Legend legend=pieChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }


}