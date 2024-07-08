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
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderDTO> list;
    private Context context;

    private OnOrderClickListener listener;
    public OrderAdapter(Context context, List<OrderDTO> list, OnOrderClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        holder.created.setText(sdf.format(list.get(position).getCreated()));
        holder.updated.setText(sdf.format(list.get(position).getUpdated()));
        holder.shipper.setText(String.valueOf(list.get(position).getShipperName()));
        holder.price.setText(String.valueOf(list.get(position).getTotalPrice()));

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(list.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView created, updated, quantity, price, shipper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            created = itemView.findViewById(R.id.textViewCreateDate);
            updated = itemView.findViewById(R.id.textViewUpdateDate);
            shipper = itemView.findViewById(R.id.textViewShipper);
            price = itemView.findViewById(R.id.textViewTotalPrice);
        }
    }
}
