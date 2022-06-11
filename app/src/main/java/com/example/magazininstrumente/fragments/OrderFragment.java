package com.example.magazininstrumente.fragments;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OrderFragment extends Fragment {
    private ListView shoppingCart;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseService firebaseService = FirebaseService.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
    DatabaseReference databaseReferenceCos = FirebaseDatabase.getInstance().getReference("cumparaturi");
    DatabaseReference databaseReferenceComanda = FirebaseDatabase.getInstance().getReference("comenzi");
    ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();

    private DatePickerDialog datePickerDialog;
    private EditText etNumeComanda;
    private EditText etPrenumeComanda;
    private EditText etEmailComanda;
    private EditText etAdresaComanda;
    private EditText etTelefonComanda;
    private TextView tvCostTotal;
    private Button btnPlaseazaComanda;
    private Button btnDatePicker;
   // private Date currentTime;
    private String currentDate;
    private Switch switchPlata;

    private String idClient;
    private List<Product> produseComanda;
    private Order order;
    private TextView tvTotalPrice;
    private String tipPlata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        //btnDatePicker = view.findViewById(R.id.datePickerButton);
        //currentTime = Calendar.getInstance().getTime();
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //currentDate = new Date(String.valueOf(Locale.getDefault()));
        //initDatePicker();
        produseComanda = new ArrayList<>();

        //firebaseService.notificareEventListenerProduseCart(modificareDateCallback());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    Client clientReferinta = data.getValue(Client.class);
                    if(clientReferinta.getEmail().equals(user.getEmail())){
                        idClient = clientReferinta.getId();
                        Log.e("client", idClient);
                        Log.e("lista", shoppingCart.toString());
                        databaseReferenceCos.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data : snapshot.getChildren()){
                                    if(data.getKey().equals(idClient)){
                                        for(DataSnapshot data2 : data.getChildren()){
                                            produseComanda.add(data2.getValue(Product.class));
                                        }
                                    }
                                }

                                int sum = 0;
                                for(Product p : produseComanda){
                                    sum+=Integer.parseInt(p.getPret());
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
                                if (isChecked){
                                    tipPlata = "Card";
                                }else {
                                    tipPlata = "Cash";
                                }
                            }
                        });
                       // btnDatePicker.setText(getTodaysDate());

//                        btnDatePicker.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //openDatePicker(view);
//                                datePickerDialog.show();
//                            }
//                        });

                        btnPlaseazaComanda.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                order = new Order(etNumeComanda.getText().toString(),etPrenumeComanda.getText().toString(),etEmailComanda.getText().toString(),etAdresaComanda.getText().toString(),etTelefonComanda.getText().toString(),tvCostTotal.getText().toString(), tipPlata, currentDate,produseComanda);
                                String idComanda = databaseReferenceComanda.push().getKey();
                                databaseReferenceComanda.child(idClient).child(idComanda).setValue(order);
                                databaseReferenceCos.child(idClient).removeValue();
                                //shoppingCartFragment.notificareListViewProductAdapter();
//                                produseComanda.clear();
//                                ProductAdapter adapter = new ProductAdapter(shoppingCart.getContext(), R.layout.product_list_item, produseComanda,getLayoutInflater());
//                                shoppingCart.setAdapter(adapter);
                            }
                        });
                    }
//
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

//    private String getTodaysDate()
//    {
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        month = month + 1;
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        return makeDateString(day, month, year);
//    }
//
//    private void initDatePicker()
//    {
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
//        {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day)
//            {
//                month = month + 1;
//                String date = makeDateString(day, month, year);
//                btnDatePicker.setText(date);
//            }
//        };
//
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//
//        int style = AlertDialog.THEME_HOLO_LIGHT;
//
//        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
//        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//    }
//
//    private String makeDateString(int day, int month, int year)
//    {
//        return getMonthFormat(month) + " " + day + " " + year;
//    }
//
//    private String getMonthFormat(int month)
//    {
//        if(month == 1)
//            return "JAN";
//        if(month == 2)
//            return "FEB";
//        if(month == 3)
//            return "MAR";
//        if(month == 4)
//            return "APR";
//        if(month == 5)
//            return "MAY";
//        if(month == 6)
//            return "JUN";
//        if(month == 7)
//            return "JUL";
//        if(month == 8)
//            return "AUG";
//        if(month == 9)
//            return "SEP";
//        if(month == 10)
//            return "OCT";
//        if(month == 11)
//            return "NOV";
//        if(month == 12)
//            return "DEC";
//
//        //default should never happen
//        return "JAN";
//    }
//
//    public void openDatePicker(View view)
//    {
//        datePickerDialog.show();
//    }

//    private void notificareListViewProductAdapter(){
//        ProductAdapter adapter = (ProductAdapter) shoppingCart.getAdapter();
//        adapter.notifyDataSetChanged();
//    }

//    private Callback<List<Product>> modificareDateCallback() {
//        return new Callback<List<Product>>() {
//            @Override
//            public void rulareRezultatPeUI(List<Product> rezultat) {
//                if (rezultat != null) {
//                    int sum = 0;
//                    produseComanda.clear();
//                    produseComanda.addAll(rezultat);
//                    for(Product p : produseComanda){
//                        sum+=Integer.parseInt(p.getPret());
//                    }
//                    tvTotalPrice.setText(String.valueOf(sum));
//                    notificareListViewProductAdapter();
//                }
//            }
//        };
//    }
}