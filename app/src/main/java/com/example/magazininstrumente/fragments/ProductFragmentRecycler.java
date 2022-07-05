package com.example.magazininstrumente.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.adapters.RecyclerViewAdapter;
import com.example.magazininstrumente.adapters.RecyclerViewAdapterProduct;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductFragmentRecycler extends Fragment {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();
    DatabaseReference databaseReferenceProduse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_recycler, container, false);

        databaseReferenceProduse = FirebaseDatabase.getInstance().getReference(getString(R.string.PRODUSE_REFERENCE));

        databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    mNames.add(product.getDenumire());
                    mImageUrls.add(product.getUrlImagine());
                    mCategories.add(product.getCategorie());
                }
//
//                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//                RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProducts);
//                recyclerView.setLayoutManager(layoutManager);
//                RecyclerViewAdapterProduct adapter = new RecyclerViewAdapterProduct(getContext(), mNames, mImageUrls, mCategories);
//                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.e("size", String.valueOf(mNames.size()));



        return  view;
    }
}