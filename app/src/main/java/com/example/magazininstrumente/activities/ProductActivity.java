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
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    Button btnPlay;
    Button btnPause;

    StorageReference storageReference;
    StorageReference storageReferenceAudio;
    DatabaseReference databaseReferenceProduse;
    DatabaseReference databaseReferenceClienti;
    DatabaseReference databaseReferenceCos;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String idClient;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        storageReference = FirebaseStorage.getInstance().getReference("produse");
        storageReferenceAudio = FirebaseStorage.getInstance().getReference("audio");
        databaseReferenceProduse = FirebaseDatabase.getInstance().getReference("produse");
        databaseReferenceClienti = FirebaseDatabase.getInstance().getReference(getString(R.string.CLIENTI_REFERENCE));
        databaseReferenceCos = FirebaseDatabase.getInstance().getReference("cumparaturi");

        imageView = findViewById(R.id.choosePhoto);
        denumire = findViewById(R.id.denumireProd);
        pret = findViewById(R.id.pretProd);
        categorie = findViewById(R.id.categorieProd);
        descriere = findViewById(R.id.descriereProd);
        progressBar = findViewById(R.id.progressBar);
        btnCos = findViewById(R.id.btnCos);

        btnPlay = findViewById(R.id.btnPlay);
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30,0);

        denumire.setText(getIntent().getExtras().getString("denumireProd"));
        pret.setText(getIntent().getExtras().getString("pretProd"));
        categorie.setText(getIntent().getExtras().getString("categorieProd"));
        descriere.setText(getIntent().getExtras().getString("descriereProd"));

        Product product = new Product(denumire.getText().toString(), pret.getText().toString(), categorie.getText().toString(), descriere.getText().toString(), null,null);

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

        //StorageReference storageReference2 = FirebaseStorage.getInstance().getReferenceFromUrl(getIntent().getExtras().getString("cantecProd"));



        databaseReferenceClienti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Client clientceva = data.getValue(Client.class);
                    if (clientceva != null) {
                        if (clientceva.getEmail().equals(user.getEmail())) {
                            idClient = clientceva.getId();
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
                databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            Product produsTemporar = data.getValue(Product.class);
                            if(produsTemporar.getDenumire().equals(product.getDenumire())){
                                product.setId(data.getKey());
                                String prodId = product.getId();
                                databaseReferenceCos.child(idClient).child(prodId).setValue(product);
                                Toast.makeText(getApplicationContext(), "Adaugat in cos", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnCos.setMovementMethod(new ScrollingMovementMethod());
        try {
            mediaPlayer.setDataSource(getIntent().getExtras().getString("cantecProd"));
            mediaPlayer.prepare();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    btnPlay.setText("Pause");
                    Drawable img = btnPlay.getContext().getResources().getDrawable( R.drawable.ic_baseline_pause_24 );
                    btnPlay.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                }else {
                    mediaPlayer.pause();
                    btnPlay.setText("Play");
                    Drawable img = btnPlay.getContext().getResources().getDrawable( R.drawable.ic_baseline_play_arrow );
                    btnPlay.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                }

            }
        });

    }


}