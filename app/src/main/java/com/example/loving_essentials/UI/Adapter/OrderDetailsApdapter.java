package com.example.loving_essentials.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
import com.example.loving_essentials.Domain.Entity.DTOs.OrderDetailsDTO;
import com.example.loving_essentials.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderDetailsApdapter extends RecyclerView.Adapter<OrderDetailsApdapter.ViewHolder>{
    private List<OrderDetailsDTO> list;
    private Context context;

    public OrderDetailsApdapter(Context context, List<OrderDetailsDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderDetailsApdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDetailsApdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_orderdetail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsApdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getUrl())
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GLIDE_TAG", "Image Load Error", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);
        String name = list.get(position).getProductName();
        if(name.length() > 10){
            name = name.substring(0, 10) + "...";
        }
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.productName.setText(name);
        holder.price.setText(String.valueOf(list.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView quantity, price, productName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            price = itemView.findViewById(R.id.textViewPrice);
            productName = itemView.findViewById(R.id.textViewProductName);
            quantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }
}
