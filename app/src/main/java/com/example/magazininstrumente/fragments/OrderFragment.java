package com.example.magazininstrumente.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Courier;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class OrderFragment extends Fragment {
    private ListView shoppingCart;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(String.valueOf(R.string.CLIENTI_REFERENCE));
    DatabaseReference databaseReferenceCos = FirebaseDatabase.getInstance().getReference(String.valueOf(R.string.CUMPARATURI_REFERENCE));
    DatabaseReference databaseReferenceComanda = FirebaseDatabase.getInstance().getReference(String.valueOf(R.string.COMENZI_REFERENCE));
    DatabaseReference databaseReferenceCurier = FirebaseDatabase.getInstance().getReference(String.valueOf(R.string.CURIERI_REFERENCE));

    private Random random = new Random();
    private EditText etNumeComanda;
    private EditText etPrenumeComanda;
    private EditText etEmailComanda;
    private EditText etAdresaComanda;
    private EditText etTelefonComanda;
    private TextView tvCostTotal;
    private Button btnPlaseazaComanda;

    private String currentDate;
    private Switch switchPlata;

    private String idClient;
    private List<Product> produseComanda;
    private List<Courier> curieri;
    private Order order;
    private String tipPlata = "Cash";
    private Courier courier;

    private Client clientReferinta;
    private float bugetReferinta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comanda, container, false);
        View view2 = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        shoppingCart = view2.findViewById(R.id.shoppingCartListView);
        btnPlaseazaComanda = view.findViewById(R.id.btnPlaseazaComanda);
        etNumeComanda = view.findViewById(R.id.numeFactura);
        etPrenumeComanda = view.findViewById(R.id.prenumeFactura);
        etEmailComanda = view.findViewById(R.id.emailFactura);
        etAdresaComanda = view.findViewById(R.id.adresaFactura);
        etTelefonComanda = view.findViewById(R.id.telefonFactura);
        tvCostTotal = view.findViewById(R.id.costTotalFactura);
        switchPlata = view.findViewById(R.id.switchPlata);
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        produseComanda = new ArrayList<>();
        curieri = new ArrayList<>();

        databaseReferenceCurier.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Courier courier = data.getValue(Courier.class);
                    if (courier != null) {
                        curieri.add(courier);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    clientReferinta = data.getValue(Client.class);
                    if (clientReferinta.getEmail().equals(user.getEmail())) {
                        idClient = clientReferinta.getId();
                        bugetReferinta = clientReferinta.getBuget();
                        databaseReferenceCos.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.getKey().equals(idClient)) {
                                        for (DataSnapshot data2 : data.getChildren()) {
                                            produseComanda.add(data2.getValue(Product.class));
                                        }
                                    }
                                }
                                int sum = 0;
                                for (Product p : produseComanda) {
                                    sum += Integer.parseInt(p.getPret());
                                }
                                tvCostTotal.setText(String.valueOf(sum));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        switchPlata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if (isChecked) {
                                    tipPlata = "Card";

                                } else {
                                    tipPlata = "Cash";
                                }

                            }
                        });

                        btnPlaseazaComanda.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                courier = curieri.get(random.nextInt(4));
                                order = new Order(etNumeComanda.getText().toString(), etPrenumeComanda.getText().toString(), etEmailComanda.getText().toString(), etAdresaComanda.getText().toString(), etTelefonComanda.getText().toString(), tvCostTotal.getText().toString(), tipPlata, currentDate, produseComanda, courier);
                                String idComanda = databaseReferenceComanda.push().getKey();
                                if (tipPlata.equals("Card")) {
                                    if (Float.parseFloat(order.getCostTotalComanda()) < bugetReferinta) {
                                        float rez = bugetReferinta - Float.parseFloat(order.getCostTotalComanda());
                                        databaseReference.child(idClient).child("buget").setValue(rez);
                                        databaseReferenceComanda.child(idClient).child(idComanda).setValue(order);
                                        databaseReferenceCos.child(idClient).removeValue();
                                        Toast.makeText(getContext(), "Comanda plasata cu succes!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(), "Fonduri insuficiente", Toast.LENGTH_LONG).show();
                                    }
                                } else if (tipPlata.equals("Cash")) {
                                    databaseReferenceComanda.child(idClient).child(idComanda).setValue(order);
                                    databaseReferenceCos.child(idClient).removeValue();
                                    Toast.makeText(getContext(), "Comanda plasata cu succes!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnPlaseazaComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}