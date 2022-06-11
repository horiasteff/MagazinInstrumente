package com.example.magazininstrumente.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.example.magazininstrumente.R;

import java.util.Arrays;

public class GeneralChartsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_charts);


     //  HIChartView chartView = findViewById(R.id.hc);
//
//        HIOptions options = new HIOptions();
//
//        HIChart chart = new HIChart();
//        chart.setType("pie");
//        chart.setOptions3d(new HIOptions3d());
//        chart.getOptions3d().setEnabled(true);
//        chart.getOptions3d().setAlpha(45);
//        options.setChart(chart);
//
//        HITitle title = new HITitle();
//        title.setText("Contents of Highsoft's weekly fruit delivery");
//        options.setTitle(title);
//
//        HISubtitle subtitle = new HISubtitle();
//        subtitle.setText("3D donut in Highcharts");
//        options.setSubtitle(subtitle);
//
//        HIPlotOptions plotOptions = new HIPlotOptions();
//        plotOptions.setPie(new HIPie());
//        plotOptions.getPie().setInnerSize(100);
//        plotOptions.getPie().setDepth(45);
//        options.setPlotOptions(plotOptions);
//
//        HIPie series1 = new HIPie();
//        series1.setName("Delivered amount");
//        Object[] object1 = new Object[] { "Bananas", 8};
//        Object[] object2 = new Object[] { "Mixed nuts", 1};
//        Object[] object3 = new Object[] { "Kiwi", 3 };
//        Object[] object4 = new Object[] { "Oranges", 6 };
//        Object[] object5 = new Object[] { "Apples", 8 };
//        Object[] object6 = new Object[] { "Pears", 4 };
//        Object[] object7 = new Object[] { "Clementines", 4 };
//        Object[] object8 = new Object[] { "Reddish (bag)", 1 };
//        Object[] object9 = new Object[] { "Grapes (bunch)", 1 };
//        Object[] object10 = new Object[] { "Kiwi", 3 };
//        Object[] object11 = new Object[] { "Kiwi", 3 };
//        series1.setData(new ArrayList<>(Arrays.asList(object1, object2, object3, object4, object5, object6, object7, object8, object9, object10, object11)));
//        options.setSeries(new ArrayList<>(Arrays.asList(series1)));
//
//        chartView.setOptions(options);
    }
}