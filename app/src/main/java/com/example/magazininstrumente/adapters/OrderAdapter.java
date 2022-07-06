package com.example.magazininstrumente.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private LayoutInflater inflator;
    private int resource;
    private List<Order> comenzi;

    public OrderAdapter(@NonNull Context context, int resource, List<Order> comenzi, LayoutInflater inflator) {
        super(context, resource,comenzi);
        this.context = context;
        this.inflator = inflator;
        this.resource = resource;
        this.comenzi = comenzi;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflator.inflate(resource, parent, false);
        Order order = comenzi.get(position);
        if(order!=null){
            adaugareNumeComanda(view, order.getNumeComanda());
            adaugareAdresaComanda(view, order.getAdresaComanda());
            adaugareNumarProduse(view, order.getProduse());
            adaugareCostComanda(view, order.getCostTotalComanda());
        }else {
            Log.e("comanda", "NU AI COMANDA");
        }
        return view;
    }

    private void adaugareNumarProduse(View view, List<Product> produse) {
        TextView textView = view.findViewById(R.id.orderProductsNumber);
       int sum = produse.size();
        populareContinut(textView,String.valueOf(sum));
    }

    private void adaugareNumeComanda(View view, String nume){
        TextView textView = view.findViewById(R.id.orderName);
        populareContinut(textView,nume);
    }
    private void adaugareAdresaComanda(View view, String adresa){
        TextView textView = view.findViewById(R.id.orderAdress);
        populareContinut(textView,adresa);
    }
    private void adaugareCostComanda(View view, String price){
        TextView textView = view.findViewById(R.id.orderPrice);
        populareContinut(textView,price);
    }

    private void populareContinut(TextView textView, String valoare){
        if(valoare!=null && !valoare.isEmpty()){
            textView.setText(valoare+ " ");

        }else{
            textView.setText(R.string.none);
        }
    }

}
