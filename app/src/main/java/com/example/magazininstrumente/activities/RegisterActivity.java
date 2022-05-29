package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final String CLIENT_REFERENCE = "clienti";
    private Button btnSave;
    //private ListView lvClienti;

    private EditText etNume;
    private EditText etPrenume;
    private EditText etEmail;
    private EditText etParola;

    private TextView tvLogin;
    private TextView tvLogin2;

    private FirebaseService firebaseService;
    private DatabaseReference databaseReference;
    private int indexClientSelectat = -1;
    private List<Client> clienti = new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }catch (Exception e){}
        setContentView(R.layout.activity_register);

        btnSave = findViewById(R.id.btnSalvareClient);

        etNume = findViewById(R.id.etNume);
        etPrenume = findViewById(R.id.etPrenume);
        etEmail = findViewById(R.id.etEmail);
        etParola = findViewById(R.id.etParola);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin2 = findViewById(R.id.tvLogin2);

        //lvClienti = findViewById(R.id.main_lv_clienti);
        //adaugareListViewClientAdapter();
        btnSave.setOnClickListener(salvareClient());

        firebaseService = FirebaseService.getInstance();
        //firebaseServiceClienti.notificareEventListener(modificareDateCallBack());
        databaseReference =  FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);

        tvLogin.setOnClickListener(clickLogin());
        tvLogin2.setOnClickListener(clickLogin());
    }

    private View.OnClickListener clickLogin(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(RegisterActivity.this, MainActivity.class);
                RegisterActivity.this.startActivity(intent);
            }
        };
    }

    private Callback<List<Client>> modificareDateCallBack(){
        return new Callback<List<Client>>() {
            @Override
            public void rulareRezultatPeUI(List<Client> rezultat) {
                if(rezultat != null){
                    clienti.clear();
                    clienti.addAll(rezultat);
                    //notificareListViewClientAdapter();
                    curatareFields();
                }
            }
        };
    }

    private void curatareFields() {

        etNume.setText(null);
        etPrenume.setText(null);
        etEmail.setText(null);
        etParola.setText(null);
        indexClientSelectat = -1;
    }

    private View.OnClickListener salvareClient(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(esteValid()){
                    if(indexClientSelectat == -1){
                        Client client = adaugareClientDinView(null);
                        if(client.getNume().isEmpty()){
                            etNume.setError("Numele este invalid");
                        }
                        else if(client.getPrenume().isEmpty()){
                            etPrenume.setError("Prenumele este invalid");
                        }
                        else if(client.getEmail().isEmpty()){
                            etEmail.setError("Emailul este invalid");
                        }
                        else if(client.getParola().isEmpty()){
                            etParola.setError("Parola este invalida");
                        }
                        else if(client.getParola().length() < 6){
                            etParola.setError("Parola este prea scurta");
                        }else{
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.createUserWithEmailAndPassword(client.getEmail(), client.getParola()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        String id = databaseReference.push().getKey();
                                        client.setId(id);
                                        databaseReference.child(client.getId()).setValue(client);
                                        curatareFields();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_LONG).show();
                                    }}
                            });
                        }

                    }
                }
            }
        };
    }

    private boolean esteValid(){
        if(etNume.getText() == null || etPrenume.getText() == null || etEmail.getText() == null ||
                 etParola.getText() == null){
            return false;
        }
        return true;
    }

    private Client adaugareClientDinView(String id){
        Client client = new Client();
        client.setId(id);
        client.setNume(etNume.getText().toString());
        client.setPrenume(etPrenume.getText().toString());
        client.setEmail(etEmail.getText().toString());
        client.setParola(etParola.getText().toString());
        return client;
    }

//    private void notificareListViewClientAdapter(){
//        ClientAdapter adapter = (ClientAdapter) lvClienti.getAdapter();
//        adapter.notifyDataSetChanged();
//    }
//    private void adaugareListViewClientAdapter() {
//        ClientAdapter adapter = new ClientAdapter(getApplicationContext(),
//                R.layout.lv_row_register, clienti, getLayoutInflater());
//        lvClienti.setAdapter(adapter);
//    }



}