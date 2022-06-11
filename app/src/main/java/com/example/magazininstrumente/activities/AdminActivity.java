package com.example.magazininstrumente.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.magazininstrumente.R;

public class AdminActivity extends AppCompatActivity {

    private Button btnCharts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnCharts = findViewById(R.id.btnCharts);

        btnCharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ClientsChartActivity.class);
                startActivity(intent);
            }
        });


    }
}