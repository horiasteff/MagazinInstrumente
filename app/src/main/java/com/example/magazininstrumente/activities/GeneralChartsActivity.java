package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralChartsActivity extends AppCompatActivity {

    private static List<Order> comenzi;
    private Map<String, Integer> categorii = new HashMap<>();
    private Map<String, Integer> comenziLunare = new HashMap<>();
    DatabaseReference databaseReferenceClienti;
    DatabaseReference databaseReferenceComenzi;
    private PieChart pieChart;
    private BarChart barChart;
    private TextView tvNicioComanda;
    private ImageView imgSad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_charts);

        databaseReferenceClienti = FirebaseDatabase.getInstance().getReference(getString(R.string.CLIENTI_REFERENCE));
        databaseReferenceComenzi = FirebaseDatabase.getInstance().getReference(getString(R.string.COMENZI_REFERENCE));
        comenzi = new ArrayList<>();
        categorii.put("Clape", 0);
        categorii.put("Suflat", 0);
        categorii.put("Corzi", 0);
        categorii.put("Percutie", 0);

        comenziLunare.put("1", 0);
        comenziLunare.put("2", 0);
        comenziLunare.put("3", 0);
        comenziLunare.put("4", 0);
        comenziLunare.put("5", 0);
        comenziLunare.put("6", 0);
        comenziLunare.put("7", 0);

        pieChart = findViewById(R.id.generalPie);
        barChart = findViewById(R.id.generalBarChart);
        tvNicioComanda = findViewById(R.id.nicioComandaGeneralText);
        imgSad = findViewById(R.id.imgSadGeneral);

        databaseReferenceComenzi.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    for (DataSnapshot data2 : data.getChildren()) {
                        Order order = data2.getValue(Order.class);
                        if (order != null) {
                            comenzi.add(order);
                            if (comenzi.size() == 0) {
                                tvNicioComanda.setVisibility(View.VISIBLE);
                                imgSad.setVisibility(View.VISIBLE);
                                pieChart.setVisibility(View.GONE);
                                barChart.setVisibility(View.GONE);
                            } else {
                                tvNicioComanda.setVisibility(View.GONE);
                                imgSad.setVisibility(View.GONE);
                                pieChart.setVisibility(View.VISIBLE);
                                barChart.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                List<Product> produseTemp = new ArrayList<>();
                for (Order o : comenzi) {
                    produseTemp.addAll(o.getProduse());
                    LocalDate localDate = LocalDate.parse(o.getDataComanda(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    String luna = String.valueOf(localDate.getMonth().getValue());
                    int countComanda = comenziLunare.containsKey(luna) ? comenziLunare.get(luna) : 0;
                    comenziLunare.put(luna, countComanda + 1);
                }
                for (Product p : produseTemp) {
                    int count = categorii.containsKey(p.getCategorie()) ? categorii.get(p.getCategorie()) : 0;
                    categorii.put(p.getCategorie(), count + 1);
                }

                pieChart.setDrawHoleEnabled(true);
                pieChart.setUsePercentValues(true);
                pieChart.setEntryLabelTextSize(12);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setCenterText("CATEGORII");
                pieChart.setCenterTextSize(24);
                pieChart.getDescription().setEnabled(true);

                ArrayList<PieEntry> entries = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : categorii.entrySet()) {
                    entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                }

                ArrayList<Integer> colors = new ArrayList<>();
                for (int color : ColorTemplate.MATERIAL_COLORS) {
                    colors.add(color);
                }
                for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                    colors.add(color);
                }

                PieDataSet dataset = new PieDataSet(entries, "CATEGORII");
                dataset.setColors(colors);

                ArrayList<BarEntry> entriesComenzi = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : comenziLunare.entrySet()) {
                    entriesComenzi.add(new BarEntry(Float.parseFloat(entry.getKey()), entry.getValue()));
                }

                PieData pieData = new PieData(dataset);
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter(pieChart));
                pieData.setValueTextSize(12f);
                pieData.setValueTextColor(Color.BLACK);

                BarDataSet barDataSet = new BarDataSet(entriesComenzi, "COMENZI");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.animateY(2000);

                pieChart.setData(pieData);
                pieChart.animateY(2000);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}