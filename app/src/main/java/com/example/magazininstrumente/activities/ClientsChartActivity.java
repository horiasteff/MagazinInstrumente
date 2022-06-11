package com.example.magazininstrumente.activities;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.adapters.ClientAdapter;
import com.example.magazininstrumente.adapters.ProductAdapter;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ClientsChartActivity extends AppCompatActivity {

    private ListView lvClienti;
    private  List<Client> clienti = new ArrayList<>();
    FirebaseService firebaseService = FirebaseService.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
    private Button btnGeneralChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_chart);

        lvClienti = findViewById(R.id.clientsListView);
        adaugarelistViewClientAdapter();
        btnGeneralChart = findViewById(R.id.btnChartGeneral);

        firebaseService.notificareEventListenerClienti(modificareDateCallback());


        lvClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ClientsChartActivity.this, ClientChartsActivity.class);
                intent.putExtra("email",clienti.get(position).getEmail());
                startActivity(intent);
            }
        });

        btnGeneralChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientsChartActivity.this, GeneralChartsActivity.class);
                startActivity(intent);
            }
        });


    }


    private void adaugarelistViewClientAdapter() {
        ClientAdapter adapter = new ClientAdapter(getApplicationContext(), R.layout.client_list_item, clienti,getLayoutInflater());
        lvClienti.setAdapter(adapter);

    }

    private void notificareListViewClientAdapter(){
        ClientAdapter adapter = (ClientAdapter) lvClienti.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Client>> modificareDateCallback() {
        return new Callback<List<Client>>() {
            @Override
            public void rulareRezultatPeUI(List<Client> rezultat) {
                if (rezultat != null) {
                    clienti.clear();
                    clienti.addAll(rezultat);
                    notificareListViewClientAdapter();
                    Log.e("adaugat", String.valueOf(clienti.size()));
                }
            }
        };
    }
}