package com.example.magazininstrumente.fragments;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;
import static com.example.magazininstrumente.FirebaseService.HISTORY_REFERENCE;
import static com.example.magazininstrumente.FirebaseService.PRODUCT_REFERENCE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.activities.OrderActivity;
import com.example.magazininstrumente.activities.ProductActivity;
import com.example.magazininstrumente.adapters.OrderAdapter;
import com.example.magazininstrumente.adapters.ProductAdapter;
import com.example.magazininstrumente.adapters.RecyclerViewAdapter;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Order;
import com.example.magazininstrumente.model.Product;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HistoryFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ListView lvProduseHistory;
    private ImageView imgSad;
    private TextView tvComanda;
    private List<Order> comenzi = new ArrayList<>();
    private DatabaseReference databaseReferenceHistory = FirebaseDatabase.getInstance().getReference(HISTORY_REFERENCE);
    private Bitmap bitmap, scaledBitmap;
    private int pageWidth = 1200;
    private Random rand = new Random();
    private float total1, subtotal;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<Product> produseRecomand = new ArrayList<>();
    private Map<String, Integer> categorii = new HashMap<>();
    private String idClient;
    private String emailClient;
    private String categorieKey;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReferenceClienti;
    DatabaseReference databaseReferenceComenzi;
    DatabaseReference databaseReferenceProduse;

    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        FirebaseService firebaseService = FirebaseService.getInstance();

        //firebaseService.notificareEventListenerOrder(modificareDateCallback());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_invoice);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1200, 518, false);
        databaseReferenceClienti = FirebaseDatabase.getInstance().getReference(getString(R.string.CLIENTI_REFERENCE));
        databaseReferenceComenzi = FirebaseDatabase.getInstance().getReference(getString(R.string.COMENZI_REFERENCE));
        databaseReferenceProduse = FirebaseDatabase.getInstance().getReference(getString(R.string.PRODUSE_REFERENCE));
        mImageUrls.clear();
        mNames.clear();

        categorii.put("Clape", 0);
        categorii.put("Suflat", 0);
        categorii.put("Corzi", 0);
        categorii.put("Percutie", 0);

        lvProduseHistory = view.findViewById(R.id.historyListView);
        tvComanda = view.findViewById(R.id.nicioComandaText);
        imgSad = view.findViewById(R.id.imgSad);
        adaugarelistViewOrderAdapter();
        firebaseService.notificareEventListenerOrder(modificareDateCallback());

        lvProduseHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                createPdf(comenzi, position);
            }
        });

        databaseReferenceClienti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Client clientTemp = data.getValue(Client.class);
                    if (clientTemp != null) {
                        if (clientTemp.getEmail().equals(emailClient)) {
                            idClient = clientTemp.getId();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReferenceComenzi.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(idClient)) {
                        for (DataSnapshot data2 : data.getChildren()) {
                            Order order = data2.getValue(Order.class);
                            if (order != null) {
                                comenzi.add(order);
                            }
                        }
                    }

                }
                List<Product> produseTemp = new ArrayList<>();
                for (Order o : comenzi) {
                    produseTemp.addAll(o.getProduse());
                }
                for (Product p : produseTemp) {
                    int count = categorii.containsKey(p.getCategorie()) ? categorii.get(p.getCategorie()) : 0;
                    categorii.put(p.getCategorie(), count + 1);
                }
                categorieKey = categorii.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Product produs = data.getValue(Product.class);
                            if (produs != null && produs.getCategorie().equals(categorieKey)) {
                                produseRecomand.add(produs);
                            }
                        }
                        mImageUrls = new ArrayList<>();
                        mNames = new ArrayList<>();
                        Collections.shuffle(produseRecomand);
                        for (int i = 0; i < 5; i++) {
                            mImageUrls.add(produseRecomand.get(i).getUrlImagine());
                            mNames.add(produseRecomand.get(i).getDenumire());
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(layoutManager);
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


    private void adaugarelistViewOrderAdapter() {
        OrderAdapter adapter = new OrderAdapter(getContext(), R.layout.history_list_item, comenzi, getLayoutInflater());
        lvProduseHistory.setAdapter(adapter);
    }

    private void notificareListViewOrderAdapter() {
        OrderAdapter adapter = (OrderAdapter) lvProduseHistory.getAdapter();
        adapter.notifyDataSetChanged();
    }


    private Callback<List<Order>> modificareDateCallback() {
        return new Callback<List<Order>>() {
            @Override
            public void rulareRezultatPeUI(List<Order> rezultat) {
                if (rezultat != null) {
                    comenzi.clear();
                    comenzi.addAll(rezultat);
                    if (comenzi.size() == 0) {
                        tvComanda.setVisibility(View.VISIBLE);
                        imgSad.setVisibility(View.VISIBLE);
                    } else {
                        tvComanda.setVisibility(View.GONE);
                        imgSad.setVisibility(View.GONE);
                    }
                    notificareListViewOrderAdapter();
                }
            }
        };
    }

    private void createPdf(List<Order> orders, int position) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        PdfDocument pdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page myPage1 = pdfDocument.startPage(myPageInfo1);

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        File file = new File(pdfPath, "/Factura" + position + ".pdf");
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledBitmap, 0, 0, myPaint);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(70);


        myPaint.setColor(Color.rgb(0, 113, 188));
        myPaint.setTextSize(30f);


        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Telefon: 0724111222", 1160, 40, myPaint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        titlePaint.setTextSize(70);
        canvas.drawText("Factura", pageWidth / 2, 500, titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Nume client: " + orders.get(position).getNumeComanda(), 20, 590, myPaint);
        canvas.drawText("Telefon: " + orders.get(position).getTelefonComanda(), 20, 640, myPaint);


        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Factura nr. " + rand.nextInt(100000), pageWidth - 20, 590, myPaint);
        canvas.drawText("Data: " + orders.get(position).getDataComanda(), pageWidth - 20, 640, myPaint);
        canvas.drawText("Curier: " + orders.get(position).getCourier().getNumeCurier(), pageWidth - 20, 690, myPaint);


        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20, 720, pageWidth - 20, 860, myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Nr.", 40, 830, myPaint);
        canvas.drawText("Produs", 200, 830, myPaint);
        canvas.drawText("Pret", 700, 830, myPaint);
        canvas.drawText("Cantitate", 887, 830, myPaint);
        canvas.drawText("Total", 1050, 830, myPaint);

        canvas.drawLine(180, 790, 180, 840, myPaint);
        canvas.drawLine(680, 790, 680, 840, myPaint);
        canvas.drawLine(880, 790, 880, 840, myPaint);
        canvas.drawLine(1030, 790, 1030, 840, myPaint);

        counter = 0;
        int nr = 1;
        for (Product product : comenzi.get(position).getProduse()) {
            Log.e("produs", product.toString());
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(String.valueOf(nr), 40, 950 + (counter * 100), myPaint);
            canvas.drawText(product.getDenumire(), 200, 950 + (counter * 100), myPaint);
            int pretBuc = Integer.parseInt(product.getPret()) / Integer.parseInt(product.getCantitate());
            canvas.drawText(String.valueOf(pretBuc), 700, 950 + (counter * 100), myPaint);
            canvas.drawText(product.getCantitate(), 900, 950 + (counter * 100), myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            total1 = Float.parseFloat(product.getPret()) * Integer.parseInt(product.getCantitate());
            subtotal += total1;
            canvas.drawText(String.valueOf(product.getPret()), pageWidth - 40, 950 + (counter * 100), myPaint);
            counter++;
            nr++;
        }
        counter++;

        myPaint.setTextAlign(Paint.Align.LEFT);

        canvas.drawLine(680, 1200, pageWidth - 20, 1200, myPaint);
        canvas.drawText("Subtotal", 700, 950 + (counter * 100), myPaint);
        canvas.drawText(":", 900, 950 + (counter * 100), myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(comenzi.get(position).getCostTotalComanda()), pageWidth - 40, 950 + (counter * 100), myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Reducere", 700, 1050 + (counter * 100), myPaint);
        canvas.drawText(":", 900, 1050 + (counter * 100), myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        float reducere = (float) (Float.parseFloat(comenzi.get(position).getCostTotalComanda()) * 0.1);
        canvas.drawText(String.valueOf(reducere), pageWidth - 40, 1050 + (counter * 100), myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.rgb(247, 147, 30));
        canvas.drawRect(680, 1100 + (counter * 100), pageWidth - 20, 1150 + (counter * 100), myPaint);

        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(50f);
        myPaint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("Total", 700, 1150 + (counter * 100), myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        float costTotal = Float.parseFloat(comenzi.get(position).getCostTotalComanda()) - reducere;
        canvas.drawText(String.valueOf(costTotal), pageWidth - 40, 1150 + (counter * 100), myPaint);


        pdfDocument.finishPage(myPage1);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getContext(), "Factura creata", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}