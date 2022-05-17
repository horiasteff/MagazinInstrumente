package com.example.magazininstrumente.fragments;

import static com.example.magazininstrumente.FirebaseService.CLIENT_REFERENCE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.magazininstrumente.Callback;
import com.example.magazininstrumente.FirebaseService;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.activities.HomeActivity;
import com.example.magazininstrumente.adapters.ProductAdapter;
import com.example.magazininstrumente.model.Client;
import com.example.magazininstrumente.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {
    private ListView shoppingCart;
    private List<Product> produse = new ArrayList<>();
    FirebaseService firebaseService = FirebaseService.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CLIENT_REFERENCE);
    DatabaseReference databaseReferenceCos = FirebaseDatabase.getInstance().getReference("cumparaturi");
    DatabaseReference databaseReferenceProduse = FirebaseDatabase.getInstance().getReference("produse");
    private TextView tvTotalPrice;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String idClient;
    private Product produsSelectat;
    private String referinta;
    private Button btnComanda;
    private ComandaFragment comandaFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        shoppingCart = view.findViewById(R.id.shoppingCartListView);
        tvTotalPrice = view.findViewById(R.id.tv_totalPrice);
        btnComanda = view.findViewById(R.id.btnComanda);
        comandaFragment = new ComandaFragment();

        adaugarelistViewProdusAdapter();

        notificareListViewProductAdapter();
        firebaseService.notificareEventListenerProduseCart(modificareDateCallback());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Client clientReferinta = data.getValue(Client.class);
                    if (clientReferinta != null) {
                        if (clientReferinta.getEmail().equals(user.getEmail())) {
                            idClient = clientReferinta.getId();
                            shoppingCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_delete)
                                            .setTitle("Esti sigur?").setMessage("Vrei sa stergi din lista?")
                                            .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    produsSelectat = produse.get(position);
                                                    databaseReferenceProduse.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for(DataSnapshot data : snapshot.getChildren()){
                                                                Product unProdus = data.getValue(Product.class);
                                                                if(unProdus!= null){
                                                                    if(unProdus.getDenumire().equals(produsSelectat.getDenumire())){
                                                                        referinta = data.getKey();
                                                                        databaseReferenceCos.child(idClient).child(referinta).removeValue();
//                                                                        Log.e("produsulmeu", String.valueOf(produse.get(position).getId()));
//                                                                        Log.e("produsulmeu", idClient);
                                                                        produse.remove(produsSelectat);
                                                                        notificareListViewProductAdapter();
                                                                        int sum = 0;
                                                                        for(Product p : produse){
                                                                            sum+=Integer.parseInt(p.getPret());
                                                                        }
                                                                        tvTotalPrice.setText(String.valueOf(sum));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                }
                                            }).setNegativeButton("No", null).show();
                                    return true;
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    btnComanda.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.productsFrame,comandaFragment);
            fragmentTransaction.commit();
        }
    });
        return view;
    }

    private void adaugarelistViewProdusAdapter() {
        ProductAdapter adapter = new ProductAdapter(getContext(), R.layout.product_list_item, produse,getLayoutInflater());
        shoppingCart.setAdapter(adapter);
    }

    private void notificareListViewProductAdapter(){
        ProductAdapter adapter = (ProductAdapter) shoppingCart.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private Callback<List<Product>> modificareDateCallback() {
        return new Callback<List<Product>>() {
            @Override
            public void rulareRezultatPeUI(List<Product> rezultat) {
                if (rezultat != null) {
                    int sum = 0;
                    produse.clear();
                    produse.addAll(rezultat);
                    for(Product p : produse){
                        sum+=Integer.parseInt(p.getPret());
                    }
                    tvTotalPrice.setText(String.valueOf(sum));
                    notificareListViewProductAdapter();
                }
            }
        };
    }
}