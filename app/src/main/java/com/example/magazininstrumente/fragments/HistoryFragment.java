package com.example.magazininstrumente.fragments;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;
import static com.example.magazininstrumente.FirebaseService.HISTORY_REFERENCE;
import static com.example.magazininstrumente.FirebaseService.PRODUCT_REFERENCE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.adapters.OrderAdapter;
import com.example.magazininstrumente.adapters.ProductAdapter;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ListView lvProduseHistory;
    private List<Order> comenzi = new ArrayList<>();
    private DatabaseReference databaseReferenceHistory =  FirebaseDatabase.getInstance().getReference(HISTORY_REFERENCE);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        FirebaseService firebaseService = FirebaseService.getInstance();

        //firebaseService.notificareEventListenerOrder(modificareDateCallback());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);


        lvProduseHistory = view.findViewById(R.id.historyListView);
        adaugarelistViewOrderAdapter();
        firebaseService.notificareEventListenerOrder(modificareDateCallback());

        return view;
    }

    private void adaugarelistViewOrderAdapter() {
        OrderAdapter adapter = new OrderAdapter(getContext(), R.layout.history_list_item, comenzi,getLayoutInflater());
        lvProduseHistory.setAdapter(adapter);
    }

    private void notificareListViewOrderAdapter(){
        OrderAdapter adapter = (OrderAdapter) lvProduseHistory.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Order>> modificareDateCallback() {
        return new Callback<List<Order>>() {
            @Override
            public void rulareRezultatPeUI(List<Order> rezultat) {
                if (rezultat != null) {
                    comenzi.clear();
                    comenzi.addAll(rezultat);
                    notificareListViewOrderAdapter();
                }
            }
        };
    }







}