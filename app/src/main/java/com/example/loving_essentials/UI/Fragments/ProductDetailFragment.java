package com.example.loving_essentials.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.loving_essentials.Domain.Entity.Cart;
import com.example.loving_essentials.Domain.Entity.ProductDetail;
import com.example.loving_essentials.Domain.Services.IService.ICartService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;
import com.example.loving_essentials.Domain.Services.Service.CartService;
import com.example.loving_essentials.Domain.Services.Service.ProductService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {

    TextView txtName, txtPrice, txtQuantity, txtDes, txtBrandname, txtCateName;
    ImageView imgPro;
    Button btnaddtocart, btnOrder;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_product_detail, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtName = view.findViewById(R.id.txtName);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtDes = view.findViewById(R.id.txtDes);
        txtBrandname = view.findViewById(R.id.txtBrand);
        txtCateName = view.findViewById(R.id.txtCate);
        imgPro = view.findViewById(R.id.imgProduct);
        btnaddtocart = view.findViewById(R.id.btnAddToCartDetail);
        btnOrder = view.findViewById(R.id.btnBuyNow);
        int id = 0;
        Bundle args = getArguments();
        if (args!= null) {
            id = args.getInt("productId");
        }
        loadProductDetail(id);
        int finalId = id;
        btnaddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ICartService cartService = CartService.getICartService();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                int buyerId = sharedPreferences.getInt("id", -1);
                Bundle args = getArguments();
                if (args!= null) {
                    int id = args.getInt("productId");
                    ;
                }
                // Assuming you have the productId and quantity values
                int productId = finalId; // Replace with the actual product ID
                int quantity = 1; // Replace with the actual quantity

                Call<Cart> call = cartService.addProductsToCart(buyerId, productId, quantity);
                call.enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        if (response.isSuccessful()) {
                            // Handle success scenario
                            Toast.makeText(getContext(), "Product added to cart successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("CartAdapter", "Failed to add product to cart: " + response.message());
                            Toast.makeText(getContext(), "Failed to add product to cart", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Log.e("CartAdapter", "Failed to add product to cart: " + t.getMessage());
                        Toast.makeText(getContext(), "Failed to add product to cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
    private void loadProductDetail(int id) {
        IProductService productService = ProductService.getProductService();
        Call<ProductDetail> call = productService.getProductbyId(id);
        call.enqueue(productDetailCallback);
    }
    private void updateUI(ProductDetail productDetail) {
        txtName.setText(productDetail.getName());
        txtPrice.setText("$ " + productDetail.getPrice());
        txtQuantity.setText("Items Available: " + productDetail.getQuantity());
        txtDes.setText(productDetail.getDescription());
        txtBrandname.setText(productDetail.getBrandName());
        txtCateName.setText(productDetail.getCategoryName());

        Glide.with(this)
                .load(productDetail.getImageURL())
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GLIDE_TAG", "Image Load Error", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgPro);
    }
    private Callback<ProductDetail> productDetailCallback = new Callback<ProductDetail>() {
        @Override
        public void onResponse(Call<ProductDetail> call, Response<ProductDetail> response) {
            if (response.isSuccessful()) {
                Log.d("ProductCardListAdapter", "API call successful");
                ProductDetail productDetail = response.body();
                updateUI(productDetail);
            } else {
                Log.e("ProductCardListAdapter", "API call failed: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<ProductDetail> call, Throwable t) {
            Log.e("ProductCardListAdapter", "API call failed: " + t.getMessage());
        }
    };




}