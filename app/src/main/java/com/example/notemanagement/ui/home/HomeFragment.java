package com.example.notemanagement.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notemanagement.Entity.PointChart;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.userstore.UserLocalStore;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RoomDB roomDB;

    private Context context;
    private UserLocalStore userLocalStore;

    public static int userIdCurrent;

    private ArrayList<PointChart> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        PieChart pieChart = root.findViewById(R.id.piechartDashBoard);
        context = root.getContext();
        userLocalStore = new UserLocalStore(context);

        data = new ArrayList<PointChart>();
        if (userLocalStore.getLoginUser() != null) {
            userIdCurrent = userLocalStore.getLoginUser().getId();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setUpPieChart(pieChart);
                }
            }).start();
        }
        return root;
    }

    public void setUpPieChart(PieChart pieChart) {
        pieChart.setDrawCenterText(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.WHITE);
        //Permit rotate
        pieChart.setRotationEnabled(true);
        pieChart.getDescription().setEnabled(false);
        //Hole center
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleAlpha(0);
        //Hidden legend of default Pie chart
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        //Add data
        addDataSet(pieChart);
    }

    private void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> value = new ArrayList<>();
        Cursor cursor = roomDB.getDatabase(context).noteDAO().getNameAndCountNoteByStatus(userIdCurrent);
        //Sum for calculate percent
        int sumNote = 0;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            //Set locate for value
            int countNote = cursor.getInt(1);
            String nameType = cursor.getString(0);
            sumNote += countNote;
            PointChart chart = new PointChart(countNote, nameType);
            data.add(chart);
        }
        int len = data.size() - 1;
        //Round result
        DecimalFormat twoDot = new DecimalFormat("#.##");
        for (int i = 0; i <= len; i++) {
            Float percent = Float.valueOf(twoDot.format((float) (data.get(i).getCountNote()) / sumNote));
            //Add value for chart
            value.add(new PieEntry(percent, data.get(i).getStatus() + ": " + String.valueOf(percent * 100) + "%"));
        }

        PieDataSet pieDataSet = new PieDataSet(value, "Statistics");
        //Space between each slice
        pieDataSet.setSliceSpace(2);
        //Hidden default percent
        pieDataSet.setValueTextColor(Color.TRANSPARENT);
        //Add color
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        //More color
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        pieDataSet.setColors(colors);

        //Set data
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}