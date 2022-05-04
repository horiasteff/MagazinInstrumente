package com.example.magazininstrumente.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.magazininstrumente.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    List<String> produse;
TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
      //  tv = findViewById(R.id.tvShop);

    produse = new ArrayList<>();
    String produs = getIntent().getExtras().getString("denumire");
    produse.add(produs);
        for (String prod: produse) {
            tv.setText(prod);
        }

    }
}