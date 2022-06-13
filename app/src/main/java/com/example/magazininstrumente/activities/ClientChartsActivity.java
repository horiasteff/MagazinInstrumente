package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientChartsActivity extends AppCompatActivity {

    private String emailClient;
    private String idClient;
    private static List<Order> comenzi;

    Map<String, Integer> categorii = new HashMap<>();
    PieChart pieChart;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReferenceClienti;
    DatabaseReference databaseReferenceComenzi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_charts);

        emailClient = getIntent().getExtras().getString("email");
        databaseReferenceClienti = FirebaseDatabase.getInstance().getReference(getString(R.string.CLIENTI_REFERENCE));
        databaseReferenceComenzi = FirebaseDatabase.getInstance().getReference(getString(R.string.COMENZI_REFERENCE));
        comenzi = new ArrayList<>();

        categorii.put("Clape", 0);
        categorii.put("Suflat", 0);
        categorii.put("Corzi", 0);
        categorii.put("Percutie", 0);
        pieChart = findViewById(R.id.specificPie);

        databaseReferenceClienti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Client clientTemp = data.getValue(Client.class);
                    if (clientTemp != null) {
                        if (clientTemp.getEmail().equals(emailClient)){
                            idClient = clientTemp.getId();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceComenzi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if(data.getKey().equals(idClient)){
                        for(DataSnapshot data2 :  data.getChildren()){
                            Order order = data2.getValue(Order.class);
                            if(order!=null){
                                comenzi.add(order);
                            }
                        }
                    }

                }
                List<Product> produseTemp = new ArrayList<>();
                for (Order o: comenzi) {
                    produseTemp.addAll(o.getProduse());
                }
                for (Product p: produseTemp) {
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
                for (Map.Entry<String,Integer> entry : categorii.entrySet()){
                    entries.add(new PieEntry(entry.getValue(),entry.getKey()));
                }

                ArrayList<Integer> colors = new ArrayList<>();
                for(int color : ColorTemplate.MATERIAL_COLORS){
                    colors.add(color);
                }
                for(int color: ColorTemplate.VORDIPLOM_COLORS){
                    colors.add(color);
                }

                PieDataSet dataset = new PieDataSet(entries, "CATEGORII");
                dataset.setColors(colors);

                PieData pieData = new PieData(dataset);
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter(pieChart));
                pieData.setValueTextSize(12f);
                pieData.setValueTextColor(Color.BLACK);

                pieChart.setData(pieData);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}