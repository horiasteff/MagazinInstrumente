package com.example.magazininstrumente.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends Fragment {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private static final String CLIENT_REFERENCE = "clienti";
    private List<Client> clienti;
    private List<Client> clienti2;
    private TextView tvNume;
    private TextView tvEmail;
    private TextView tvTelefon;
    private TextView tvAdresa;
    private TextView tvBuget;
    private Button btnResetPassword;
    private Button btnResetBuget;
    private EditText etBuget;
    private Button btnOkBuget;
    private static Client clientTemp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




        databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
        tvNume = view.findViewById(R.id.clientNume);
        tvEmail = view.findViewById(R.id.clientEmail);
        tvTelefon = view.findViewById(R.id.clientTelefon);
        tvAdresa = view.findViewById(R.id.clientAdresa);
        tvBuget = view.findViewById(R.id.clientBuget);
        etBuget = view.findViewById(R.id.etBuget);
        clienti = new ArrayList<>();
        btnResetPassword = view.findViewById(R.id.btnResetareParola);
        btnResetBuget = view.findViewById(R.id.btnReteazaBuget);
        btnOkBuget = view.findViewById(R.id.btnOkBuget);
        auth = FirebaseAuth.getInstance();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Client client = data.getValue(Client.class);
                    if(client!=null){
                        clienti.add(client);
                        if(client.getEmail().equals(user.getEmail())){
                            clientTemp = client;
                            tvNume.setText(client.getNume() + " " + client.getPrenume());
                            tvEmail.setText(client.getEmail());
                            tvTelefon.setText(client.getTelefon());
                            tvAdresa.setText(client.getAdresa());
                            tvBuget.setText(String.valueOf(client.getBuget()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        modificareDateCallback();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Verifica email pentru a reseta parola", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "Mai incearca", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        btnResetBuget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOkBuget.setVisibility(View.VISIBLE);
                etBuget.setVisibility(View.VISIBLE);
            }
        });


        btnOkBuget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(etBuget.getText().length() == 0){
                        Toast.makeText(getActivity(), "Nu ai introdus nimic", Toast.LENGTH_LONG).show();
                    }
                    else if(Integer.parseInt(etBuget.getText().toString()) < 0){
                        Toast.makeText(getActivity(), "Nu poti introduce un numar negativ", Toast.LENGTH_LONG).show();
                    }
                    else if(Integer.parseInt(etBuget.getText().toString()) < clientTemp.getBuget()){
                        Toast.makeText(getActivity(), "Ai introdus o suma mai mica decat cea existenta", Toast.LENGTH_LONG).show();
                    }
                    else {
                        databaseReference.child(clientTemp.getId()).child("buget").setValue(Float.parseFloat(etBuget.getText().toString()));
                        etBuget.setText("");
                        etBuget.setVisibility(View.INVISIBLE);
                        btnOkBuget.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Resetat cu succes", Toast.LENGTH_LONG).show();
                    }
                }catch(NumberFormatException ex){
                    Toast.makeText(getActivity(), "Acesta nu este un format valid", Toast.LENGTH_LONG).show();
                }


            }
        });


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