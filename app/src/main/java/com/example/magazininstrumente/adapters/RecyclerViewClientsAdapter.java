package com.example.magazininstrumente.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.magazininstrumente.R;
import com.example.magazininstrumente.activities.ClientChartsActivity;

import java.util.ArrayList;

public class RecyclerViewClientsAdapter extends RecyclerView.Adapter<RecyclerViewClientsAdapter.ViewHolder> {

    private ArrayList<String> names;
    private ArrayList<String> emails;
    private Context mContext;

    public RecyclerViewClientsAdapter(Context context, ArrayList<String> names, ArrayList<String> emails) {
        this.names = names;
        this.emails = emails;
        mContext = context;
    }

    @Override
    public RecyclerViewClientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_client, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewClientsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.name.setText(names.get(position));
        holder.email.setText(emails.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClientChartsActivity.class);
                intent.putExtra("email",emails.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView email;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
        }
    }
}
