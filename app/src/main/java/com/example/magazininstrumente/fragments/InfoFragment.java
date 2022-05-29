package com.example.magazininstrumente.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends Fragment {
    private DatabaseReference databaseReference;
    private static final String CLIENT_REFERENCE = "clienti";
    private List<Client> clienti;
    private List<Client> clienti2;
    private TextView tvNume;
    private TextView tvPrenume;
    private TextView tvEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(),"parola");


        databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
        tvNume = view.findViewById(R.id.clientName);
        tvPrenume = view.findViewById(R.id.clientPrenume);
        tvEmail = view.findViewById(R.id.clientEmail);
        clienti = new ArrayList<>();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Client client = data.getValue(Client.class);
                    if(client!=null){
                        clienti.add(client);
                        if(client.getEmail().equals(user.getEmail())){
                            tvNume.setText(client.getNume());
                            tvPrenume.setText(client.getPrenume());
                            tvEmail.setText(client.getEmail());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        modificareDateCallback();

        return view;
    }

    private Callback<List<Client>> modificareDateCallback() {
        return new Callback<List<Client>>() {
            @Override
            public void rulareRezultatPeUI(List<Client> rezultat) {
                if (rezultat != null) {
                    clienti.clear();
                    clienti.addAll(rezultat);
                    //notificareListViewProductAdapter();
                }
            }
        };
    }

    private List<Client> addClient (Client client){
        clienti.add(client);
        return clienti;
    }
}