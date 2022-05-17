package com.example.magazininstrumente.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.magazininstrumente.fragments.ComandaFragment;
import com.example.magazininstrumente.fragments.InfoFragment;
import com.example.magazininstrumente.fragments.ProductsFragment;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.fragments.ShoppingCartFragment;

public class HomeActivity extends AppCompatActivity {

    private ImageView imgPersonalInfo;
    private ImageView imgHome;
    private ImageView imgShoppingCart;
    private Button btnComanda;

    private ProductsFragment productsFragment;
    private InfoFragment infoFragment;
    private ShoppingCartFragment shoppingCartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imgPersonalInfo = findViewById(R.id.imgPersonalInfo);
        imgHome = findViewById(R.id.imgHome);
        imgShoppingCart = findViewById(R.id.imgShoppingCart);
        btnComanda = findViewById(R.id.btnComanda);

        productsFragment = new ProductsFragment();
        infoFragment = new InfoFragment();
        shoppingCartFragment= new ShoppingCartFragment();
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
        imgShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(shoppingCartFragment);
            }
        });


    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.productsFrame,fragment);
        fragmentTransaction.commit();
    }


}