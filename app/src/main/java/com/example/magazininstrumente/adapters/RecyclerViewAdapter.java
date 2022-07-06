package com.example.magazininstrumente.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.magazininstrumente.R;
import com.example.magazininstrumente.activities.ProductActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> names;
    private ArrayList<String> imageUrls;
    private ArrayList<String> prices;
    private ArrayList<String> categories;
    private ArrayList<String> descriptions;
    private ArrayList<String> songUrls;
    private ArrayList<String> quantites;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls,ArrayList<String> prices,ArrayList<String> categories,ArrayList<String> descriptions,ArrayList<String> songUrls, ArrayList<String> quantites) {
        this.names = names;
        this.imageUrls = imageUrls;
        this.prices = prices;
        this.categories = categories;
        this.descriptions = descriptions;
        this.songUrls = songUrls;
        this.quantites = quantites;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(imageUrls.get(position))
                .into(holder.image);

        holder.name.setText(names.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductActivity.class);
                intent.putExtra("imageProd", imageUrls.get(position));
                intent.putExtra("denumireProd", names.get(position));
                intent.putExtra("pretProd", prices.get(position));
                intent.putExtra("categorieProd", categories.get(position));
                intent.putExtra("descriereProd", descriptions.get(position));
                intent.putExtra("cantecProd",songUrls.get(position));
                intent.putExtra("cantitateProd",quantites.get(position));
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}