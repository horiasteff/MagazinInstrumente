package com.example.magazininstrumente.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.adapters.RecyclerViewProductAdapter;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ProductFragmentRecycler extends Fragment {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mCategories = new ArrayList<>();
    private ArrayList<String> mQuantities = new ArrayList<>();
    private ArrayList<String> mDescriptions = new ArrayList<>();
    private ArrayList<String> mSongUrls = new ArrayList<>();
    private ArrayList<String> mPrices = new ArrayList<>();
    DatabaseReference databaseReferenceProduse;
    private SearchView searchView;
    private TextView tvCorzi;
    private TextView tvSuflat;
    private TextView tvPercutie;
    private TextView tvClape;
    private Button btnAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_recycler, container, false);
        tvCorzi = view.findViewById(R.id.tvFilterCorzi);
        tvClape = view.findViewById(R.id.tvFilterClape);
        tvSuflat = view.findViewById(R.id.tvFilterSuflat);
        tvPercutie = view.findViewById(R.id.tvFilterPercutie);
        searchView = view.findViewById(R.id.searchViewList);
        btnAll = view.findViewById(R.id.btnAll);
        databaseReferenceProduse = FirebaseDatabase.getInstance().getReference(getString(R.string.PRODUSE_REFERENCE));

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2,LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerViewProductAdapter[] adapter = new RecyclerViewProductAdapter[1];

        databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    if(product!=null){
                        mNames.add(product.getDenumire());
                        mImageUrls.add(product.getUrlImagine());
                        mCategories.add(product.getCategorie());
                        mQuantities.add(product.getCantitate());
                        mDescriptions.add(product.getDescriere());
                        mSongUrls.add(product.getUrlCantec());
                        mPrices.add(product.getPret());
                    }
                }

                adapter[0] = new RecyclerViewProductAdapter(getContext(), mNames, mImageUrls, mCategories,mPrices,mDescriptions,mQuantities,mSongUrls);
                recyclerView.setAdapter(adapter[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanArrays(mNames,mImageUrls,mCategories,mQuantities,mDescriptions,mSongUrls,mPrices);

                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product product = data.getValue(Product.class);
                            if(product!=null){
                                mNames.add(product.getDenumire());
                                mImageUrls.add(product.getUrlImagine());
                                mCategories.add(product.getCategorie());
                                mQuantities.add(product.getCantitate());
                                mDescriptions.add(product.getDescriere());
                                mSongUrls.add(product.getUrlCantec());
                                mPrices.add(product.getPret());
                            }
                        }
                        adapter[0].notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        tvCorzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanArrays(mNames,mImageUrls,mCategories,mQuantities,mDescriptions,mSongUrls,mPrices);

                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product product = data.getValue(Product.class);
                            if(product.getCategorie().equals("Corzi")){
                                mNames.add(product.getDenumire());
                                mImageUrls.add(product.getUrlImagine());
                                mCategories.add(product.getCategorie());
                                mQuantities.add(product.getCantitate());
                                mDescriptions.add(product.getDescriere());
                                mSongUrls.add(product.getUrlCantec());
                                mPrices.add(product.getPret());
                            }
                        }
                        adapter[0].notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tvClape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanArrays(mNames,mImageUrls,mCategories,mQuantities,mDescriptions,mSongUrls,mPrices);

                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product product = data.getValue(Product.class);
                            if(product.getCategorie().equals("Clape")){
                                mNames.add(product.getDenumire());
                                mImageUrls.add(product.getUrlImagine());
                                mCategories.add(product.getCategorie());
                                mQuantities.add(product.getCantitate());
                                mDescriptions.add(product.getDescriere());
                                mSongUrls.add(product.getUrlCantec());
                                mPrices.add(product.getPret());
                            }
                        }
                        adapter[0].notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tvSuflat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanArrays(mNames,mImageUrls,mCategories,mQuantities,mDescriptions,mSongUrls,mPrices);

                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product product = data.getValue(Product.class);
                            if(product.getCategorie().equals("Suflat")){
                                mNames.add(product.getDenumire());
                                mImageUrls.add(product.getUrlImagine());
                                mCategories.add(product.getCategorie());
                                mQuantities.add(product.getCantitate());
                                mDescriptions.add(product.getDescriere());
                                mSongUrls.add(product.getUrlCantec());
                                mPrices.add(product.getPret());
                            }
                        }
                        adapter[0].notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tvPercutie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cleanArrays(mNames,mImageUrls,mCategories,mQuantities,mDescriptions,mSongUrls,mPrices);

                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product product = data.getValue(Product.class);
                            if(product.getCategorie().equals("Percutie")){
                                mNames.add(product.getDenumire());
                                mImageUrls.add(product.getUrlImagine());
                                mCategories.add(product.getCategorie());
                                mQuantities.add(product.getCantitate());
                                mDescriptions.add(product.getDescriere());
                                mSongUrls.add(product.getUrlCantec());
                                mPrices.add(product.getPret());
                            }
                        }
                        adapter[0].notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                cleanArrays(mNames,mImageUrls,mCategories,mQuantities,mDescriptions,mSongUrls,mPrices);
                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product product = data.getValue(Product.class);
                            if(product.getDenumire().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))){
                                mNames.add(product.getDenumire());
                                mImageUrls.add(product.getUrlImagine());
                                mCategories.add(product.getCategorie());
                                mQuantities.add(product.getCantitate());
                                mDescriptions.add(product.getDescriere());
                                mSongUrls.add(product.getUrlCantec());
                                mPrices.add(product.getPret());
                            }
                        }
                        adapter[0].notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return false;
            }
        });

        return  view;
    }

    private void cleanArrays(ArrayList<String> names,ArrayList<String> images,ArrayList<String> categories,ArrayList<String> quantites,ArrayList<String> descriptions,ArrayList<String> songs,ArrayList<String> prices ){
        names.clear();
        images.clear();
        categories.clear();
        quantites.clear();
        descriptions.clear();
        songs.clear();
        prices.clear();
    }
}