package com.example.magazininstrumente.fragments;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.adapters.ProductAdapter;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {
    private ListView shoppingCart;
    private List<Product> produse = new ArrayList<>();
    FirebaseService firebaseService = FirebaseService.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        shoppingCart = view.findViewById(R.id.shoppingCartListView);
        adaugarelistViewProdusAdapter();

        firebaseService.notificareEventListenerProduseCart(modificareDateCallback());

        return view;
    }

    private void adaugarelistViewProdusAdapter() {
        ProductAdapter adapter = new ProductAdapter(getContext(), R.layout.product_list_item, produse,getLayoutInflater());
        shoppingCart.setAdapter(adapter);
    }

    private void notificareListViewProductAdapter(){
        ProductAdapter adapter = (ProductAdapter) shoppingCart.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Product>> modificareDateCallback() {
        return new Callback<List<Product>>() {
            @Override
            public void rulareRezultatPeUI(List<Product> rezultat) {
                if (rezultat != null) {
                    produse.clear();
                    produse.addAll(rezultat);
                    notificareListViewProductAdapter();
                }
            }
        };
    }
}