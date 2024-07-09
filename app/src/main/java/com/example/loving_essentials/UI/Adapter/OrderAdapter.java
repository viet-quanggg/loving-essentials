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
        // Lấy giá trị trạng thái và chuyển đổi thành chuỗi
        int statusValue = list.get(position).getStatus();
        String statusText = getStatusText(statusValue);
        holder.status.setText(statusText);

        // Đặt tên shipper với điều kiện kiểm tra null
        String shipperName = list.get(position).getShipperName();
        if (shipperName != null) {
            holder.shipper.setText("Shipper: " + shipperName);
        } else {
            holder.shipper.setText("Shipper: Not yet assigned");
        }
        holder.price.setText(String.valueOf(list.get(position).getTotalPrice()));

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(list.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView created, status, quantity, price, shipper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            created = itemView.findViewById(R.id.textViewCreateDate);
            status = itemView.findViewById(R.id.textViewStatusOrder);
            shipper = itemView.findViewById(R.id.textViewShipper);
            price = itemView.findViewById(R.id.textViewTotalPrice);
        }
    }
    private String getStatusText(int status) {
        switch (status) {
            case 1:
                return "Pending";
            case 2:
                return "Processing";
            case 3:
                return "Shipped";
            case 4:
                return "Delivered";
            case 5:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }
}
