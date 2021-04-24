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

import com.example.notemanagement.DAO.StatusDAO;
import com.example.notemanagement.Entity.Account;
import com.example.notemanagement.Entity.Chart;
import com.example.notemanagement.R;
import com.example.notemanagement.RoomDB;
import com.example.notemanagement.userstore.UserLocalStore;
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
import java.util.List;

public class HomeFragment extends Fragment {

    private StatusDAO statusDAO;
    private  Context context;
    private UserLocalStore userLocalStore;
    private Account currentAcc;
    List<Chart>  chartInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
         PieChart pieChart = root.findViewById(R.id.piechartDashBoard);
         context= root.getContext();

        userLocalStore= new UserLocalStore(context);
        if (userLocalStore.getLoginUser()!=null)
        {
            currentAcc= userLocalStore.getLoginUser();
        }

        setUpPieChart(pieChart);

//        pieChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);

        return root;
    }

    public void setUpPieChart(PieChart pieChart){
        pieChart.setRotationEnabled(true);//  Cho phép xoay Pie Chart
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(0);//Tạo vòng tròn ở tâm
        pieChart.setTransparentCircleAlpha(0);
//        pieChart.setCenterText("Statistics");//Tạo text cho vòng tròn ở tâm
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(false);
        addDataSet(pieChart);
    }

    private  void addDataSet(PieChart pieChart) {


//        RoomDB.databaseWriteExecutor.execute(()->{
//            statusDAO= RoomDB.getDatabase(context).statusDAO();
//             chartInfo= statusDAO.getStatusNoteById(currentAcc.getID());
//        });
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
        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.BLUE);

        pieDataSet.setColors(colors);

//        Legend legend=pieChart.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }


}