package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.fragments.ShoppingCartFragment;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ProductActivity extends AppCompatActivity {
    ImageView imageView;

    TextView denumire;
    TextView pret;
    TextView categorie;
    TextView descriere;
    ProgressBar progressBar;
    Button btnCos;

    StorageReference storageReference;
    DatabaseReference databaseReferenceProduse;
    DatabaseReference databaseReferenceClienti;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String ceimitrebuie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        storageReference = FirebaseStorage.getInstance().getReference("produse");
        databaseReferenceProduse = FirebaseDatabase.getInstance().getReference("produse");
        databaseReferenceClienti = FirebaseDatabase.getInstance().getReference("clienti");

        imageView = findViewById(R.id.choosePhoto);
        denumire = findViewById(R.id.denumireProd);
        pret = findViewById(R.id.pretProd);
        categorie = findViewById(R.id.categorieProd);
        descriere = findViewById(R.id.descriereProd);
        progressBar = findViewById(R.id.progressBar);
        btnCos = findViewById(R.id.btnCos);


        denumire.setText(getIntent().getExtras().getString("denumireProd"));
        pret.setText(getIntent().getExtras().getString("pretProd"));
        categorie.setText(getIntent().getExtras().getString("categorieProd"));
        descriere.setText(getIntent().getExtras().getString("descriereProd"));

        Product product = new Product(denumire.getText().toString(), pret.getText().toString(), categorie.getText().toString(), descriere.getText().toString(), null);

        StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(getIntent().getExtras().getString("imageProd"));
        try {
            File temp = File.createTempFile("file", "jpeg");
            storageReference1.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(temp.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("eroare", e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseReferenceClienti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Client clientceva = data.getValue(Client.class);
                    if (clientceva != null) {
                        if (clientceva.getEmail().equals(user.getEmail())) {
                            ceimitrebuie = clientceva.getId();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prodId = databaseReferenceClienti.push().getKey();
                databaseReferenceClienti.child(ceimitrebuie).child("cos").child(prodId).setValue(product);
            }
        });

    }
    }