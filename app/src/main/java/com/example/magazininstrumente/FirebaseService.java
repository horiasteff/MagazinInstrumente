package com.example.magazininstrumente;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private final DatabaseReference databaseReferenceShop;
    private final DatabaseReference databaseReferenceOrder;
    public static final String CLIENT_REFERENCE = "clienti";
    public static final String PRODUCT_REFERENCE = "produse";
    public static final String SHOP_REFERENCE = "cumparaturi";
    public static final String HISTORY_REFERENCE = "comenzi";

    private static FirebaseService firebaseService;
    private FirebaseAuth mAuth;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public FirebaseService(){
        databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
        databaseReferenceProducts = FirebaseDatabase.getInstance().getReference(PRODUCT_REFERENCE);
        databaseReferenceShop = FirebaseDatabase.getInstance().getReference(SHOP_REFERENCE);
        databaseReferenceOrder = FirebaseDatabase.getInstance().getReference(HISTORY_REFERENCE);
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

    public void notificareEventListenerProduseCart(Callback<List<Product>> callback){
        List<Product> produseTotale = new ArrayList<>();
        List<Client> clientiTotali = new ArrayList<>();
        final String[] uid = {""};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Client client = data.getValue(Client.class);
                    if(client!=null){
                        clientiTotali.add(client);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Clientul nu este disponibil");
            }
        });
        databaseReferenceProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Product product = data.getValue(Product.class);
                    if(product!=null){
                        produseTotale.add(product);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReferenceShop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for(Client c : clientiTotali){
                    if(c.getEmail().equals(user.getEmail())){
                        uid[0] = c.getId();
                    }
                }
                for(DataSnapshot data : snapshot.getChildren()){
                    if(data.getKey().equals(uid[0]))
                    for(DataSnapshot data2 : data.getChildren()){
                        Product product = data2.getValue(Product.class);
                        if(product!=null){
                            for(Product p : produseTotale){
                                if(p.getDenumire().equals(product.getDenumire())){
                                    product.setUrlImagine(p.getUrlImagine());
                                }
                            }
                            products.add(product);
                        }
                    }
                    callback.rulareRezultatPeUI(products);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void notificareEventListenerOrder(Callback<List<Order>> callback){
        List<Client> clientiTotali = new ArrayList<>();
        final String[] uid = {""};

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Client client = data.getValue(Client.class);
                    if(client!=null){
                        clientiTotali.add(client);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseService", "Clientul nu este disponibil");
            }
        });

        databaseReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(Client c : clientiTotali){
                    if(c.getEmail().equals(user.getEmail())){
                        uid[0] = c.getId();
                    }
                }
                List<Order> comenzi = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    if(data.getKey().equals(uid[0])){
                        for(DataSnapshot data2 : data.getChildren()){
                            Order order = data2.getValue(Order.class);
                            comenzi.add(order);
                        }
                    }
                }
                callback.rulareRezultatPeUI(comenzi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
