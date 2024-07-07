package com.example.loving_essentials.UI.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderResponse;
import com.example.loving_essentials.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderShippingAdapter extends RecyclerView.Adapter<OrderShippingAdapter.OrderViewHolder> {

    private List<OrderResponse> orderList;
    private Context context;

    public OrderShippingAdapter(List<OrderResponse> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ongoing_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);

        OrderResponse order = orderList.get(position);
        holder.txtTotalPrice.setText(String.valueOf(order.getTotalPrice()));
        holder.txtShipperName.setText(name); // Replace with actual shipper name
        holder.txtUsername.setText(order.getBuyers().getName()); // Replace with actual username

        String productName = order.getOrderDetails().get(0).getProducts().getName();
        holder.txtProductName.setText(productName);
        TooltipCompat.setTooltipText(holder.txtProductName, productName);

        String dateStr = order.getCreated(); // Get the date string from your OrderResponse object
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); // Replace with the actual date format of your date string
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Replace with your desired date format
        try {
            Date date = parser.parse(dateStr);
            String formattedDate = formatter.format(date);
            holder.txtDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String imgUrl = order.getOrderDetails().get(0).getProducts().getimageURL(); // Get the image URL from your OrderResponse object
        Glide.with(context).load(imgUrl).into(holder.imgProduct);

        if (holder.button != null) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Xử lý sự kiện click ở đây
                }
            });
        }

        int status = order.getStatus();
        String statusStr;
        switch (status) {
            case 1:
                statusStr = "Pending";
                break;
            case 2:
                statusStr = "Processing";
                break;
            case 3:
                statusStr = "Shipped";
                break;
            case 4:
                statusStr = "Delivered";
                break;
            case 5:
                statusStr = "Cancelled";
                break;
            default:
                statusStr = "Unknown";
        }
        holder.txtStatus.setText(statusStr);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtTotalPrice, txtShipperName, txtUsername, txtDate, txtStatus;
        ImageView imgProduct;
        Button button;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtShipperName = itemView.findViewById(R.id.txtShipperName);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            button = itemView.findViewById(R.id.button);
        }
    }
}