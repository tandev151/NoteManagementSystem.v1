package com.example.notemanagement.ui.home;

import android.content.Context;
import android.database.Cursor;
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
import androidx.lifecycle.LiveData;
import androidx.room.Room;


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

import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import javax.sql.DataSource;

public class HomeFragment extends Fragment {
    private RoomDB roomDB;

    private  Context context;
    private UserLocalStore userLocalStore;

    public static int userIdCurrent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
         PieChart pieChart = root.findViewById(R.id.piechartDashBoard);
         context= root.getContext();
        userLocalStore= new UserLocalStore(context);
        if (userLocalStore.getLoginUser()!=null)
        {
            userIdCurrent = userLocalStore.getLoginUser().getId();
            new Thread(new Runnable() {
                @Override
                public void run() {
                   setUpPieChart(pieChart);
                }

            }).start();

        }


//        pieChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);

        return root;
    }

    public void setUpPieChart(PieChart pieChart){
        pieChart.setDrawCenterText(true);
       // pieChart.setEntryLabelTextSize(9);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setRotationEnabled(true);//  Cho phép xoay Pie Chart
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(1);//Tạo vòng tròn ở tâm
        pieChart.setTransparentCircleAlpha(0);
//        pieChart.setCenterText("Statistics");//Tạo text cho vòng tròn ở tâm
        pieChart.setCenterTextSize(10);
       // pieChart.setDrawEntryLabels(false);

        addDataSet(pieChart);
    }

    private  void addDataSet(PieChart pieChart) {


//        RoomDB.databaseWriteExecutor.execute(()->{
//            statusDAO= RoomDB.getDatabase(context).statusDAO();
//             chartInfo= statusDAO.getStatusNoteById(currentAcc.getID());
//        });

        ArrayList<PieEntry> value = new ArrayList<>();
        Cursor cursor =  roomDB.getDatabase(context).noteDAO().getNameAndCountNoteByStatus(userIdCurrent);
//        LiveData<List<Status>>[] xData = {RoomDB.getDatabase(context).statusDAO().getStatusByAccountId(userIdCurrent)};

        for (int i = 0; i<cursor.getCount(); i++)
        {
           cursor.moveToNext();
           value.add(new PieEntry(cursor.getFloat(1),cursor.getString(0)));

        }


//
//
//
//        for (int i = 0; i < xData.length;i++) {
//            label.add(xData[i]);
//        }

//        for (int i = 0; i < yData.length;i++){
//            value.add(new PieEntry(yData[i],xData[i]));
//        }

        PieDataSet pieDataSet=new PieDataSet(value,"Statisfics");

        pieDataSet.setSliceSpace(2);//Đặt khoảng trống ở giữa các lát cắt
        pieDataSet.setValueTextSize(9);
        pieDataSet.setValueTextColor(Color.WHITE);
        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.RED);
        colors.add(Color.BLUE);

        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }


        pieDataSet.setColors(colors);

      Legend legend=pieChart.getLegend();
      legend.setEnabled(false);
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        pieDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(value)+" ";
            }
        });


        PieData pieData=new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieData.setValueTextSize(9f);
        pieData.setValueTextColor(Color.BLACK);


        pieChart.setData(pieData);
        pieChart.invalidate();
    }


}