package com.example.loving_essentials.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.Cart;
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Services.IService.ICartService;
import com.example.loving_essentials.Domain.Services.Service.CartService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.CartAdapter;
import com.example.loving_essentials.UI.ProductListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdapter.OnQuantityChangedListener {
    private List<Cart> carts;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView txtPrice;
    private Button btnCon, btnOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        txtPrice = view.findViewById(R.id.totalPrice);
        btnCon = view.findViewById(R.id.btnContinueShopping);
        btnOrder = view.findViewById(R.id.btnCheckout);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            int id = sharedPreferences.getInt("id", -1);
            loadCartData(id);
        } else {
            // the id key is not present in the SharedPreferences
        }


        btnCon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductListActivity.class);
            startActivity(intent);
        });

        return view;
    }
    private void loadCartData(int id) {
        ICartService cartService = CartService.getICartService();
        Call<Cart[]> call = cartService.getUserCart(id);

        call.enqueue(new Callback<Cart[]>() {
            @Override
            public void onResponse(Call<Cart[]> call, Response<Cart[]> response) {
                if (response.isSuccessful()) {
                    Cart[] cartsResult = response.body();
                    carts = new ArrayList<>();
                    carts.clear();
                    carts.addAll(Arrays.asList(cartsResult));
                    Double totalPrice = (double) 0;
                    List<Integer> quantity = new ArrayList<>();
                    Map<ProductDTO, Integer> productQuantities = new HashMap<>();
                    for (Cart cart : carts) {
                        for (Map.Entry<Integer, ProductDTO> entry : cart.Products.entrySet()) {
                            productQuantities.put(entry.getValue(), entry.getKey()); // key is quantity, value is product
                        }
                        totalPrice += cart.getPrice();
                    }

                    txtPrice.setText("Total: $" + totalPrice);
                    cartAdapter = new CartAdapter(productQuantities,getContext(),CartFragment.this);
                    recyclerViewCart.setAdapter(cartAdapter);
                } else {
                    Log.e("ProductCardListAdapter", "API call failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Cart[]> call, Throwable t) {
                // Handle failure
            }
        });
    }
    @Override
    public void onQuantityChanged(int id) {
        loadCartData(id);
    }
}
