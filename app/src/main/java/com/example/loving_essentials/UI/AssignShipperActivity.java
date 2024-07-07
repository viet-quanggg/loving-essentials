package com.example.loving_essentials.UI;

import static java.util.Locale.filter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.OrderStatusUpdateRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.ShipperDTO.Shipper;
import com.example.loving_essentials.Domain.Services.IService.IOrderService;
import com.example.loving_essentials.Domain.Services.IService.IUserService;
import com.example.loving_essentials.Domain.Services.Service.OrderService;
import com.example.loving_essentials.Domain.Services.Service.UserService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.ShipperAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignShipperActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShipperAdapter adapter;
    private int selectedUserId = -1;
    private List<Shipper> allShippers;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_shipper);

        recyclerView = findViewById(R.id.rycViewJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShipperAdapter(this); // Pass the recyclerView to the adapter
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchEditText); // Assume you have a EditText for search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adapter.setOnShipperSelectedListener(userId -> {
            selectedUserId = userId;
            Log.d("AssignShipperActivity", "Selected shipper: " + userId);
        });

        Button btnCancel = findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(v -> finish());

        Button btnAssign = findViewById(R.id.buttonAssign);
        btnAssign.setOnClickListener(v -> {
            if (selectedUserId != -1) {
                // Chỗ Intent này nhớ truyền OrderId từ bên màn OrderDetail qua nhen
                int orderId = getIntent().getIntExtra("OrderId", -1);
                if (orderId != -1) {
                    updateOrderStatus(selectedUserId, orderId, 2);
                } else {
                    Toast.makeText(this, "Invalid OrderId", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No shipper selected", Toast.LENGTH_SHORT).show();
            }
        });

        loadShippers();
    }

    private void filter(String text) {
        List<Shipper> filteredShippers = new ArrayList<>();
        for (Shipper shipper : allShippers) {
            // Assuming you want to search by shipper's name
            if (shipper.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredShippers.add(shipper);
            }
        }
        adapter.setShippers(filteredShippers);
    }

    private void loadShippers() {
        IUserService service = UserService.getUserService();
        Call<List<Shipper>> call = service.getShippers();
        call.enqueue(new Callback<List<Shipper>>() {
            @Override
            public void onResponse(Call<List<Shipper>> call, Response<List<Shipper>> response) {
                if (response.isSuccessful()) {
                    allShippers = response.body(); // Save all shippers
                    if (allShippers != null && !allShippers.isEmpty()) {
                        adapter.setShippers(new ArrayList<>(allShippers)); // Show all shippers initially
                    } else {
                        Toast.makeText(AssignShipperActivity.this, "No shippers available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AssignShipperActivity.this, "Failed to load shippers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Shipper>> call, Throwable t) {
                Toast.makeText(AssignShipperActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(int shipperId, int orderId, int status) {
        IOrderService service = OrderService.getOrderService();
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setShipperId(shipperId);
        request.setOrderId(orderId);
        request.setNewStatus(status);
        Call<Void> call = service.updateOrderStatus(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AssignShipperActivity.this, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AssignShipperActivity.this, "Failed to update order status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AssignShipperActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}