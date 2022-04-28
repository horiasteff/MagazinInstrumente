package com.example.magazininstrumente;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirebaseService {
    private final DatabaseReference databaseReference;
    private final DatabaseReference databaseReferenceProducts;
    public static final String CLIENT_REFERENCE = "clienti";
    public static final String PRODUCT_REFERENCE = "produse";

    private static FirebaseService firebaseService;
    private FirebaseAuth mAuth;


    public FirebaseService(){
        databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
        databaseReferenceProducts = FirebaseDatabase.getInstance().getReference(PRODUCT_REFERENCE);
    }

    public static FirebaseService getInstance(){
        if(firebaseService == null){
            synchronized (FirebaseService.class){
                if(firebaseService == null){
                    firebaseService = new FirebaseService();
                }
            }
        }
        return firebaseService;
    }

    public void insert(Client client){
        if(client == null || (client.getId() !=null && !client.getId().trim().isEmpty() )){
            return;
        }

//        mAuth = FirebaseAuth.getInstance();
//        mAuth.createUserWithEmailAndPassword(client.getEmail(), client.getParola()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    String id = databaseReference.push().getKey();
//                    client.setId(id);
//                    databaseReference.child(client.getId()).setValue(client);
//            }}else{
//
//            }
//        });

    }


    public void delete(Client client){
        if(client == null || client.getId() == null || client.getId().trim().isEmpty()){
            return;
        }
        databaseReference.child(client.getId()).removeValue();
    }

    public void notificareEventListener(Callback<List<Client>> callback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Client> clienti = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    Client client = data.getValue(Client.class);
                    if(client!=null){
                        clienti.add(client);
                    }
                }
                callback.rulareRezultatPeUI(clienti);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Clientul nu este disponibil");
            }
        });
    }

    public void notificareEventListenerProduse(Callback<List<Product>> callback){
        databaseReferenceProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> produse = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    if(product!=null){
                        produse.add(product);
                    }
                }
                callback.rulareRezultatPeUI(produse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void notificareEventListenerProduseFiltered(Callback<List<Product>> callback, String s){
        databaseReferenceProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> produse = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    if(product!=null && product.getDenumire().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))){
                        produse.add(product);
                    }
                }
                callback.rulareRezultatPeUI(produse);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
