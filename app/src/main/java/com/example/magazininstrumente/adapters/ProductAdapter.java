package com.example.magazininstrumente.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<Product> products;

    public ProductAdapter(@NonNull Context context, int resource, List<Product> products, LayoutInflater inflater) {
        super(context, resource, products);
        this.context = context;
        this.inflater = inflater;
        this.resource = resource;
        this.products = products;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource,parent,false);
        Product product = products.get(position);
        if(product!=null){
            adaugareDenumire(view, product.getDenumire());
            adaugarePret(view, product.getPret());
            adaugareCategorie(view, product.getCategorie());
            adaugareImagine(view, product.getUrlImagine());
            adaugareCantitate(view, product.getCantitate());
        }else{
            Log.e("produs", "NU AI PRODUSUL");
        }
        return view;
    }

    private void adaugareImagine(View view, String urlImagine) {
        ImageView imageView = view.findViewById(R.id.productImage);
        ProgressBar progressBar = view.findViewById(R.id.progressBarList);
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(urlImagine);
        try {
            File temp = File.createTempFile("file","jpeg");
            storageReference.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void adaugareCategorie(View view, String categorie) {
        TextView textView = view.findViewById(R.id.productCategory);
        populareContinut(textView,categorie);
    }

    private void adaugarePret(View view, String pret) {
        TextView textView = view.findViewById(R.id.productPrice);
        populareContinut(textView,pret);
    }

    private void adaugareDenumire(View view, String denumire) {
        TextView textView = view.findViewById(R.id.productName);
        populareContinut(textView,denumire);
    }

    private void adaugareCantitate(View view, String cantitate) {
        TextView textView = view.findViewById(R.id.productQuantity);
        populareContinut(textView,cantitate);
    }

    private void populareContinut(TextView textView, String valoare){
        if(valoare!=null && !valoare.isEmpty()){
            textView.setText(valoare+ " ");
        }else{
            textView.setText(R.string.none);
        }
    }
}
