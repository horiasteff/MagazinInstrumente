package com.example.magazininstrumente.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.magazininstrumente.fragments.InfoFragment;
import com.example.magazininstrumente.fragments.ProductsFragment;
import com.example.magazininstrumente.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView imgPersonalInfo;
    private ImageView imgHome;

    private ProductsFragment productsFragment;
    private InfoFragment infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imgPersonalInfo = findViewById(R.id.imgPersonalInfo);
        imgHome = findViewById(R.id.imgHome);
        productsFragment = new ProductsFragment();
        infoFragment = new InfoFragment();
        setFragment(productsFragment);

        imgPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(infoFragment);
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(productsFragment);
            }
        });

    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.productsFrame,fragment);
        fragmentTransaction.commit();
    }


}