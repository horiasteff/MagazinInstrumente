package com.example.magazininstrumente.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.magazininstrumente.R;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {
    private List<String> produse;
    private TextView tv;
    Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        produse = new ArrayList<>();
        //tv = view.findViewById(R.id.tvShop);

//        bundle = this.getArguments();
//
//
//
//        String produs;
//        if(bundle!= null){
//            produs = bundle.getString("denumire");
//            tv.setText( this.getArguments().getString("denumire"));
//        }else{
//            tv.setText("null");
//            //tv.setText(this.getArguments().getString("denumire"));
//        }
////        produse.add(produs);
////        for (String prod: produse
////        ) {
////            tv.setText(prod);
////        }
//

        return view;
    }
}