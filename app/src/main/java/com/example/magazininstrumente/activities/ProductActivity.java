package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.magazininstrumente.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProductActivity extends AppCompatActivity {
    ImageView imageView;

    TextView denumire;
    TextView pret;
    TextView categorie;
    TextView descriere;
    ProgressBar progressBar;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        storageReference = FirebaseStorage.getInstance().getReference("produse");
        databaseReference = FirebaseDatabase.getInstance().getReference("produse");

        imageView = findViewById(R.id.choosePhoto);
        denumire = findViewById(R.id.denumireProd);
        pret = findViewById(R.id.pretProd);
        categorie = findViewById(R.id.categorieProd);
        descriere = findViewById(R.id.descriereProd);
        progressBar = findViewById(R.id.progressBar);


        denumire.setText(getIntent().getExtras().getString("denumireProd"));
        pret.setText(getIntent().getExtras().getString("pretProd"));
        categorie.setText(getIntent().getExtras().getString("categorieProd"));
        descriere.setText(getIntent().getExtras().getString("descriereProd"));


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
    }
}