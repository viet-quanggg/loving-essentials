package com.example.loving_essentials.UI.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.loving_essentials.Domain.Entity.Cart;
import com.example.loving_essentials.Domain.Entity.DTOs.CartItemDTO;
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Services.IService.ICartService;
import com.example.loving_essentials.Domain.Services.Service.CartService;
import com.example.loving_essentials.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<ProductDTO> productItems;
    private List<CartItemDTO> productQuantities;
    private Context context;
    private OnQuantityChangedListener onQuantityChangedListener;

    public interface OnQuantityChangedListener {
        void onQuantityChanged(int id);
    }
    public CartAdapter(List<CartItemDTO> productQuantities, Context context, OnQuantityChangedListener onQuantityChangedListener) {
        this.productQuantities = productQuantities;
        this.context = context;
        this.productItems = new ArrayList<>();
        this.onQuantityChangedListener = onQuantityChangedListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductDTO product = productQuantities.get(position).getProduct();
        int quantity = productQuantities.get(position).getQuantity();
        Log.d("CartAdapter", "Binding view holder at position " + position);
        Glide.with(context)
                .load(product.getimageURL())
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
                .into(holder.imgproduct);


        holder.txtproductName.setText(product.getName());
        holder.txtproductPrice.setText(String.valueOf(product.getPrice()));
        holder.txtQuantity.setText(String.valueOf(quantity));

        holder.btnIncreaseQuantity.setOnClickListener(v -> {
            if (product.getQuantity() <= quantity){
                Toast.makeText(context,"Max quantity of product",Toast.LENGTH_SHORT).show();
                return;
            }
           updateQuantity(product.getId(),1,holder);

        });

        holder.btnDecreaseQuantity.setOnClickListener(v -> {
            deleteQuantity(product.getId(),1,holder);
        });
        holder.btnclearCart.setOnClickListener(v -> {
            clearCart(product.getId(),holder);
        });
    }

    @Override
    public int getItemCount() {
        return productQuantities.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgproduct;
        TextView txtproductName, txtproductPrice, txtQuantity;
        TextView btnIncreaseQuantity, btnDecreaseQuantity, btnclearCart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.imgProductcart);
            txtproductName = itemView.findViewById(R.id.txtProductNamecart);
            txtproductPrice = itemView.findViewById(R.id.txtProductPricecart);
            txtQuantity = itemView.findViewById(R.id.txtQuantityitem);
            btnIncreaseQuantity = itemView.findViewById(R.id.btnIncreaseQuantityitem);
            btnDecreaseQuantity = itemView.findViewById(R.id.btnDecreaseQuantityitem);
            btnclearCart = itemView.findViewById(R.id.btnClearCart);
        }
    }
    private void updateQuantity(int productId, int newQuantity, CartViewHolder holder) {
        ICartService cartService = CartService.getICartService();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            int id = sharedPreferences.getInt("id", -1);

            Call<Cart> call = cartService.addProductsToCart(id, productId, newQuantity);
            call.enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        // Update UI or handle success scenario
                        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                        if (sharedPreferences.contains("id")) {
                            int id = sharedPreferences.getInt("id", -1);
                            onQuantityChangedListener.onQuantityChanged(id);

                        }
                    } else {
                        Log.e("CartAdapter", "Failed to update quantity: " + response.message());
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Log.e("CartAdapter", "Failed to update quantity: " + t.getMessage());
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteQuantity(int productId, int newQuantity, CartViewHolder holder) {
        ICartService cartService = CartService.getICartService();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            int id = sharedPreferences.getInt("id", -1);


            Call<Cart> call = cartService.removeProductFromCart(id, productId, newQuantity);
            call.enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        // Update UI or handle success scenario
                        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                        if (sharedPreferences.contains("id")) {
                            int id = sharedPreferences.getInt("id", -1);
                            // use the id value
                            onQuantityChangedListener.onQuantityChanged(id);
                        } else {
                            // the id key is not present in the SharedPreferences
                        }


                    } else {
                        Log.e("CartAdapter", "Failed to update quantity: " + response.message());
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Log.e("CartAdapter", "Failed to update quantity: " + t.getMessage());
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
}
    private void clearCart(int productId, CartViewHolder holder) {
        ICartService cartService = CartService.getICartService();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            int id = sharedPreferences.getInt("id", -1);


            Call<Cart> call = cartService.clearProductFromCart(id, productId);
            call.enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    if (response.isSuccessful()) {
                        // Update UI or handle success scenario
                        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                        if (sharedPreferences.contains("id")) {
                            int id = sharedPreferences.getInt("id", -1);
                            // use the id value
                            onQuantityChangedListener.onQuantityChanged(id);
                        } else {
                            // the id key is not present in the SharedPreferences
                        }


                    } else {
                        Log.e("CartAdapter", "Failed to update quantity: " + response.message());
                        Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Log.e("CartAdapter", "Failed to update quantity: " + t.getMessage());
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}