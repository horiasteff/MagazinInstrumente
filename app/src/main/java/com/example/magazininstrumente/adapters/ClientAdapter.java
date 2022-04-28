package com.example.magazininstrumente.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Client;

import java.util.List;

public class ClientAdapter extends ArrayAdapter<Client> {

    private Context context;
    private LayoutInflater inflator;
    private int resource;
    private List<Client> clienti;


    public ClientAdapter(@NonNull Context context, int resource, List<Client> clienti, LayoutInflater inflator) {
        super(context, resource, clienti);
        this.context = context;
        this.inflator = inflator;
        this.resource = resource;
        this.clienti = clienti;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflator.inflate(resource, parent, false);
        Client client = clienti.get(position);
        if(client!= null){
            adaugareNume(view, client.getNume());
            adaugarePrenume(view, client.getPrenume());
            adaugareEmail(view, client.getEmail());
            adaugareParola(view, client.getParola());
            client.setBuget(0);
        }
        return view;
    }

    private void adaugareNume(View view, String nume){
        TextView textView = view.findViewById(R.id.tv_row_nume);
        populareContinut(textView,nume);
    }
    private void adaugarePrenume(View view, String prenume){
        TextView textView = view.findViewById(R.id.tv_row_prenume);
        populareContinut(textView,prenume);
    }
    private void adaugareEmail(View view, String email){
        TextView textView = view.findViewById(R.id.tv_row_email);
        populareContinut(textView,email);
    }
    private void adaugareParola(View view, String parola){
        TextView textView = view.findViewById(R.id.tv_row_parola);
        populareContinut(textView,parola);
    }
     private void populareContinut(TextView textView, String valoare){
        if(valoare!=null && !valoare.isEmpty()){
            textView.setText(valoare+ " ");

        }else{
            textView.setText("-");
        }
     }

}
