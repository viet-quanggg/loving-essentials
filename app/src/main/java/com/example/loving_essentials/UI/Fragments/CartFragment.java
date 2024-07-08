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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.Cart;
import com.example.loving_essentials.Domain.Entity.DTOs.CartItemDTO;
import com.example.loving_essentials.Domain.Services.IService.ICartService;
import com.example.loving_essentials.Domain.Services.Service.CartService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.CartAdapter;
import com.example.loving_essentials.UI.ProductListActivity;
import com.example.loving_essentials.UI.UserView.CheckoutView.CheckoutFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdapter.OnQuantityChangedListener {
    private List<Cart> carts;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView txtPrice, txtItem;
    private Button btnCon, btnOrder;
    private Double totalPrice;
    private int id;
    private int cartId;
    private Fragment checkoutFragment;
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
        txtItem = view.findViewById(R.id.IteminCart);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("id")) {
            id = sharedPreferences.getInt("id", -1);
            loadCartData(id);
        } else {
            // the id key is not present in the SharedPreferences
        }


        btnCon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductListActivity.class);
            startActivity(intent);
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutFragment = new CheckoutFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", id);
                bundle.putInt("cartId", cartId);
                bundle.putDouble("total", totalPrice);
                checkoutFragment.setArguments(bundle);
                loadFragment(checkoutFragment);
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.home_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
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
                     totalPrice = (double) 0;
                    List<Integer> quantity = new ArrayList<>();
                    List<CartItemDTO> productQuantities = new ArrayList<>();
                    Integer item = 0;
                    for (Cart cart : carts) {
                        cartId = cart.getId();
                        totalPrice += cart.getPrice();
                        for (CartItemDTO items : cart.getProducts()) {
                            item += items.getQuantity();
                        }
                        productQuantities.addAll(cart.getProducts()); // Add all CartItemDTOs to the list
                    }
                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    /*if (totalPrice>1000){
                        totalPrice = totalPrice/1000;
                        txtPrice.setText("Total: $" + totalPrice+"K");
                    }else if (totalPrice>1000000){
                        totalPrice = totalPrice/1000000;
                        txtPrice.setText("Total: $" + totalPrice+"M");
                    }
                    else {
                        txtPrice.setText("Total: $" + totalPrice);
                    }*/
                    txtPrice.setText("Total: $" + decimalFormat.format(totalPrice));
                    if (item>1){
                        txtItem.setText("Value of : "+item+" items");
                    }else {
                        txtItem.setText("Value of : "+item+" item");
                    }

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
