package com.example.magazininstrumente.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.activities.ProductActivity;

import java.util.ArrayList;


public class RecyclerViewProductAdapter extends RecyclerView.Adapter<RecyclerViewProductAdapter.ViewHolder> {

    private ArrayList<String> names;
    private ArrayList<String> imageUrls;
    private ArrayList<String> categories;
    private ArrayList<String> prices;
    private ArrayList<String> descriptions;
    private ArrayList<String> quantites;
    private ArrayList<String> songUrls;

    private Context mContext;

    public RecyclerViewProductAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<String> categories, ArrayList<String> prices, ArrayList<String> descriptions, ArrayList<String> quantites, ArrayList<String> songUrls) {
        this.names = names;
        this.imageUrls = imageUrls;
        this.categories = categories;
        this.prices = prices;
        this.descriptions = descriptions;
        this.quantites = quantites;
        this.songUrls = songUrls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(imageUrls.get(position))
                .into(holder.image);

        holder.name.setText(names.get(position));
        holder.price.setText(prices.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductActivity.class);
                intent.putExtra("imageProd", imageUrls.get(position));
                intent.putExtra("denumireProd", names.get(position));
                intent.putExtra("pretProd", prices.get(position));
                intent.putExtra("categorieProd", categories.get(position));
                intent.putExtra("descriereProd", descriptions.get(position));
                intent.putExtra("cantecProd", songUrls.get(position));
                intent.putExtra("cantitateProd", quantites.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        ImageView image;
        TextView name;
        TextView category;
        TextView description;
        TextView songUrl;
        TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            category = itemView.findViewById(R.id.category);
            description = itemView.findViewById(R.id.description);
            songUrl = itemView.findViewById(R.id.songUrl);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}