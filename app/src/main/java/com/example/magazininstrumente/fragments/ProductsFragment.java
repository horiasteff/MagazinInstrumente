package com.example.magazininstrumente.fragments;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;
import static com.example.magazininstrumente.FirebaseService.PRODUCT_REFERENCE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.activities.ProductActivity;
import com.example.magazininstrumente.adapters.ProductAdapter;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ProductsFragment extends Fragment {
private ListView lvProduse;
private  List<Product > produse = new ArrayList<>();
private  DatabaseReference databaseReferenceProduct =  FirebaseDatabase.getInstance().getReference(PRODUCT_REFERENCE);
private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products, container, false);
        View view2= inflater.inflate(R.layout.product_list_item, container, false);
        TextView tv = view.findViewById(R.id.productsFrame);

        //Button btnAdd = view2.findViewById(R.id.btnAdd);
        searchView = view.findViewById(R.id.searchViewList);

        FirebaseService firebaseService = FirebaseService.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);

        lvProduse = view.findViewById(R.id.productListView);
        adaugarelistViewProdusAdapter();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseService.notificareEventListenerProduseFiltered(modificareDateCallback(), s);
                return false;
            }
        });


       firebaseService.notificareEventListenerProduse(modificareDateCallback());


       lvProduse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               String denumireProdus = adapterView.getItemAtPosition(position).toString();
               Intent intent = new Intent(getActivity(), ProductActivity.class);
               intent.putExtra("imageProd", produse.get(position).getUrlImagine());
               intent.putExtra("denumireProd", produse.get(position).getDenumire());
               intent.putExtra("pretProd", produse.get(position).getPret());
               intent.putExtra("categorieProd", produse.get(position).getCategorie());
               intent.putExtra("descriereProd", produse.get(position).getDescriere());
               startActivity(intent);
           }
       });


//       btnAdd.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               Toast.makeText(getContext(),"apasat", Toast.LENGTH_LONG).show();
//               ShoppingCartFragment fragment = new ShoppingCartFragment();
//               Bundle bundle = new Bundle();
//               bundle.putString("denumire", "ceva");
//               fragment.setArguments(bundle);
//               Intent intent = new Intent(getActivity(), ShoppingCartFragment.class);
//               startActivity(intent);
//           }
//       });


        return view;
    }

    private void adaugarelistViewProdusAdapter() {
        ProductAdapter adapter = new ProductAdapter(getContext(), R.layout.product_list_item, produse,getLayoutInflater());
        lvProduse.setAdapter(adapter);
    }

    private void notificareListViewProductAdapter(){
        ProductAdapter adapter = (ProductAdapter) lvProduse.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Product>> modificareDateCallback() {
        return new Callback<List<Product>>() {
            @Override
            public void rulareRezultatPeUI(List<Product> rezultat) {
                if (rezultat != null) {
                    produse.clear();
                    produse.addAll(rezultat);
                    notificareListViewProductAdapter();
                }
            }
        };
    }




}