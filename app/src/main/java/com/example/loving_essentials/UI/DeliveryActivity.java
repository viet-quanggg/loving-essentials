package com.example.loving_essentials.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderResponse;
import com.example.loving_essentials.Domain.Services.IService.IOrderService;
import com.example.loving_essentials.Domain.Services.Service.OrderService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.OrderShippingAdapter;
import com.example.loving_essentials.UI.Fragments.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderShippingAdapter orderAdapter;
    private List<OrderResponse> orderList = new ArrayList<>();
    private EditText txtSearch;
    private TextView txtNoData;
    private int shipperId;
    private String shipperName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        LinearLayout linearMyDeliveries = findViewById(R.id.linearMyDeliveries);
        LinearLayout linearLogout = findViewById(R.id.linearLogout);
        linearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        linearMyDeliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyDeliveryActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        shipperName = sharedPreferences.getString("name", null);
        shipperId = sharedPreferences.getInt("id", 0);

//        String shipperName = getIntent().getStringExtra("name");
        TextView txtShipper = findViewById(R.id.txtShipper);
        txtShipper.setText("Hi, " + shipperName + "!");

        recyclerView = findViewById(R.id.rycViewJobs);
        orderAdapter = new OrderShippingAdapter(orderList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

        txtSearch = findViewById(R.id.txtSearch);
        txtNoData = findViewById(R.id.txtNoData);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchOrders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        fetchOrders("");
    }

    private void fetchOrders(String searchText) {
//        int shipperId = getIntent().getIntExtra("id", 0);
        IOrderService orderService = OrderService.getOrderService();
        Call<List<OrderResponse>> call = orderService.getOrdersByShipper(shipperId, 2, searchText, "");
        Log.d("Test", "fetchOrders: " + call.request().url());
        call.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful()) {
                    List<OrderResponse> orders = response.body();
                    if (orders != null && !orders.isEmpty()) {
                        orderList.clear();
                        orderList.addAll(orders);
                        orderAdapter.notifyDataSetChanged();
                        txtNoData.setVisibility(View.GONE);
                    } else {
                        orderList.clear();
                        orderAdapter.notifyDataSetChanged();
                        txtNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(DeliveryActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    orderList.clear();
                    orderAdapter.notifyDataSetChanged();
                    txtNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Toast.makeText(DeliveryActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}