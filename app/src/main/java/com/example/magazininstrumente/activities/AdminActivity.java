package com.example.magazininstrumente.activities;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.magazininstrumente.GridSpacingItemDecoration;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.adapters.RecyclerViewClientsAdapter;
import com.example.magazininstrumente.model.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private  List<Client> clienti = new ArrayList<>();
    DatabaseReference databaseReference;
    private Button btnGeneralChart;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.CLIENTI_REFERENCE));
        btnGeneralChart = findViewById(R.id.btnChartGeneral);

        btnGeneralChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, GeneralChartsActivity.class);
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Client client= data.getValue(Client.class);
                    if(client!=null && !client.getEmail().equals("admin@Gmail.com")){
                        clienti.add(client);
                    }
                }

                mNames = new ArrayList<>();
                mEmails = new ArrayList<>();
                for (int i = 0; i < clienti.size(); i++) {
                    mNames.add(clienti.get(i).getNume());
                    mEmails.add(clienti.get(i).getEmail());
                }

                LinearLayoutManager layoutManager = new GridLayoutManager(AdminActivity.this, 3,LinearLayoutManager.VERTICAL, false);
                RecyclerView recyclerView = findViewById(R.id.clientListRecyclerView);
                recyclerView.setLayoutManager(layoutManager);
                RecyclerViewClientsAdapter adapter = new RecyclerViewClientsAdapter(AdminActivity.this, mNames,mEmails);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(10, 20, false));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}