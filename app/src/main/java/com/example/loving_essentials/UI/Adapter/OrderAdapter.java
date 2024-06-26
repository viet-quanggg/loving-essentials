package com.example.loving_essentials.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
import com.example.loving_essentials.R;

import java.sql.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderDTO> list;
    private Context context;

    public OrderAdapter(Context context, List<OrderDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.created.setText(String.valueOf(list.get(position).getCreated()));
        holder.updated.setText(String.valueOf(list.get(position).getUpdated()));
        holder.shipper.setText(String.valueOf(list.get(position).getShipperId()));
        holder.price.setText(String.valueOf(list.get(position).getTotalPrice()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView created, updated, quantity ,price, shipper;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView.findViewById(R.id.imageViewProduct);
            created.findViewById(R.id.textViewCreateDate);
            updated.findViewById(R.id.textViewUpdateDate);
            quantity.findViewById(R.id.textViewQuantity);
            shipper.findViewById(R.id.textViewShipper);
            price.findViewById(R.id.textViewTotalPrice);
        }


    }
}
