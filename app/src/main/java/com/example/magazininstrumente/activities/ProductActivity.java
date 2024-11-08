package com.example.magazininstrumente.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazininstrumente.R;
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

public class ProductActivity extends AppCompatActivity {
    ImageView imageView;

    private TextView denumire;
    private TextView pret;
    private TextView categorie;
    private TextView descriere;
    private ProgressBar progressBar;
    private Button btnCos;
    private Button btnPlay;
    private Button btnMinus;
    private Button btnPlus;
    private EditText etCantitate;

    StorageReference storageReference;
    StorageReference storageReferenceAudio;
    DatabaseReference databaseReferenceProduse;
    DatabaseReference databaseReferenceClienti;
    DatabaseReference databaseReferenceCos;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String idClient;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        storageReference = FirebaseStorage.getInstance().getReference(getString(R.string.PRODUSE_REFERENCE));
        storageReferenceAudio = FirebaseStorage.getInstance().getReference(getString(R.string.AUDIO_REFERENCE));
        databaseReferenceProduse = FirebaseDatabase.getInstance().getReference(getString(R.string.PRODUSE_REFERENCE));
        databaseReferenceClienti = FirebaseDatabase.getInstance().getReference(getString(R.string.CLIENTI_REFERENCE));
        databaseReferenceCos = FirebaseDatabase.getInstance().getReference(getString(R.string.CUMPARATURI_REFERENCE));

        imageView = findViewById(R.id.choosePhoto);
        denumire = findViewById(R.id.denumireProd);
        pret = findViewById(R.id.pretProd);
        categorie = findViewById(R.id.categorieProd);
        descriere = findViewById(R.id.descriereProd);
        progressBar = findViewById(R.id.progressBar);
        btnCos = findViewById(R.id.btnCos);
        btnMinus = findViewById(R.id.minusCantitate);
        btnPlus = findViewById(R.id.plusCantitate);
        etCantitate = findViewById(R.id.etCantitate);

        btnPlay = findViewById(R.id.btnPlay);
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30,0);

        denumire.setText(getIntent().getExtras().getString("denumireProd"));
        pret.setText(getIntent().getExtras().getString("pretProd"));
        categorie.setText(getIntent().getExtras().getString("categorieProd"));
        descriere.setText(getIntent().getExtras().getString("descriereProd"));
        etCantitate.setText(getIntent().getExtras().getString("cantitateProd"));

        Product product = new Product(denumire.getText().toString(), pret.getText().toString(), categorie.getText().toString(), descriere.getText().toString(), etCantitate.getText().toString(),null,null);

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
                                product.setCantitate(etCantitate.getText().toString());
                                int sumTotal = Integer.parseInt(product.getPret())*Integer.parseInt(product.getCantitate());
                                product.setPret(String.valueOf(sumTotal));
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

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantitatePlus = Integer.parseInt(etCantitate.getText().toString());
                cantitatePlus++;
                etCantitate.setText(String.valueOf(cantitatePlus));
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantitateMinus = Integer.parseInt(etCantitate.getText().toString());
                if(cantitateMinus>1){
                    cantitateMinus--;
                }
                etCantitate.setText(String.valueOf(cantitateMinus));
            }
        });

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
                    btnPlay.setText("Pauza");
                    Drawable img = btnPlay.getContext().getResources().getDrawable( R.drawable.ic_baseline_pause_24 );
                    btnPlay.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                }else {
                    mediaPlayer.pause();
                    btnPlay.setText("Asculta");
                    Drawable img = btnPlay.getContext().getResources().getDrawable( R.drawable.ic_baseline_play_arrow );
                    btnPlay.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                }
            }
        });
    }
}