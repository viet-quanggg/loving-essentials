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
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.R;

import java.util.List;

public class ProductCardListAdapter extends RecyclerView.Adapter<ProductCardListAdapter.ViewHolder> {

    List<ProductDTO> productList;
    Context context;

    public ProductCardListAdapter(List<ProductDTO> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    // ViewHolder inner class here

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProductDTO product = productList.get(position);
        String name = productList.get(position).getName();
        if(name.length() > 10){
            name = name.substring(0, 10) + "...";
        }
        holder.name.setText(name);
        String desc = productList.get(position).getDescription();
        if(desc.length() > 15){
            desc = desc.substring(0, 15) + "...";
        }
        holder.description.setText(desc);
        holder.price.setText(String.valueOf(productList.get(position).getPrice()));
        Glide.with(context)
                .load(productList.get(position).getimageURL())
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
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        public TextView price;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvProductCardName);
            description = (TextView) itemView.findViewById(R.id.tvProductCardDescription);
            price = (TextView) itemView.findViewById(R.id.tvProductCardPrice);
            image = (ImageView) itemView.findViewById(R.id.imgProductCard);
        }
    }
}