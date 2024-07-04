package com.example.loving_essentials.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderStatus;
import com.example.loving_essentials.Domain.Services.IService.IOrderService;
import com.example.loving_essentials.Domain.Services.Service.OrderService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.DeliveryAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDeliveryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeliveryAdapter orderAdapter;
    private List<OrderResponse> orderList = new ArrayList<>();
    private EditText txtSearch;
    private TextView txtNoData;
    private OrderStatus selectedStatus = OrderStatus.Processing;
    private int statusValue;
    private String currentSearchText = "";
    private TextView txtActiveDeliveries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_delivery);

        LinearLayout linearMyDeliveries = findViewById(R.id.linearMyDeliveries);
        LinearLayout linearHome = findViewById(R.id.linearHome);


        linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeliveryActivity.class);
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


        Spinner spinnerStatus = findViewById(R.id.spinnerStatus);
        ArrayAdapter<OrderStatus> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, OrderStatus.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        int defaultPosition = adapter.getPosition(OrderStatus.Processing);
        spinnerStatus.setSelection(defaultPosition);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = (OrderStatus) parent.getItemAtPosition(position);
                statusValue = selectedStatus.getValue();
                // Fetch orders with the selected status and current search text
                fetchOrders(currentSearchText, statusValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        String shipperName = getIntent().getStringExtra("name");

        orderAdapter = new DeliveryAdapter(orderList, this);
        recyclerView = findViewById(R.id.rycViewJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

        TextView txtActiveDeliveries = findViewById(R.id.txtActiveDeliveries); // Replace with your actual TextView id
        int count = orderAdapter.getCount();
        txtActiveDeliveries.setText(String.valueOf(count) + " Active Deliveries");


        txtSearch = findViewById(R.id.txtSearch);
        txtNoData = findViewById(R.id.txtNoData);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchText = s.toString();
                // Fetch orders with the current search text and the current selected status
                fetchOrders(currentSearchText, statusValue);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        // Fetch orders with the default search text and status
        Log.d("Status","Status:"+ statusValue);
        fetchOrders(currentSearchText, statusValue);
    }

    private void fetchOrders(String searchText, int status) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int shipperId = sharedPreferences.getInt("id", 0);
        Log.d("MyDeliveryActivity", "fetchOrders: shipperId: " + shipperId + ", status: " + status + ", searchText: " + searchText);
        IOrderService orderService = OrderService.getOrderService();
        Call<List<OrderResponse>> call = orderService.getOrdersByShipper(shipperId, status, searchText, "");
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
                    txtActiveDeliveries = findViewById(R.id.txtActiveDeliveries);
                    int count = orderAdapter.getCount();
                    txtActiveDeliveries.setText(String.valueOf(count) + " Active Deliveries");
                } else {
                    Toast.makeText(MyDeliveryActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    orderList.clear();
                    orderAdapter.notifyDataSetChanged();
                    txtNoData.setVisibility(View.VISIBLE);
                    txtActiveDeliveries.setText("0 Active Deliveries");
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Toast.makeText(MyDeliveryActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage(), t);
            }
        });
    }
}