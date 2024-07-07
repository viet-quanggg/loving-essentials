    package com.example.loving_essentials.UI.Fragments;

    import static android.content.ContentValues.TAG;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
    import com.example.loving_essentials.Domain.Entity.DTOs.OrderDetailsDTO;
    import com.example.loving_essentials.Domain.Services.IService.IOrderService;
    import com.example.loving_essentials.Domain.Services.Service.OrderService;
    import com.example.loving_essentials.R;
    import com.example.loving_essentials.UI.Adapter.OrderAdapter;
    import com.example.loving_essentials.UI.Adapter.OrderDetailsApdapter;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class OrderDetailFragment extends Fragment {
        List<OrderDetailsDTO> list;
        OrderDetailsApdapter orderAdapter;
        RecyclerView recyclerView;
        IOrderService orderService;
        int orderId;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_orderdetail, container, false);

            recyclerView = view.findViewById(R.id.rv_orderdetail_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            orderService = OrderService.getOrderService();

            list = new ArrayList<>();
            orderAdapter = new OrderDetailsApdapter(getContext(), list);
            recyclerView.setAdapter(orderAdapter);
            Bundle args = getArguments();
            if (getArguments() != null) {
                orderId = getArguments().getInt("orderId");
                fetchOrders(orderId);
            } else {
                Log.e(TAG, "Arguments are null, cannot fetch orders");
            }

            return view;
        }
        private void fetchOrders(int orderid) {
            Call<OrderDetailsDTO[]> callOrders = orderService.getOrderDetailById(orderid);
            callOrders.enqueue(new Callback<OrderDetailsDTO[]>() {
                @Override
                public void onResponse(Call<OrderDetailsDTO[]> call, Response<OrderDetailsDTO[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        OrderDetailsDTO[] orders = response.body();
                        list.addAll(Arrays.asList(orders));
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Response unsuccessful or body is null. Code: " + response.code() + ", Message: " + response.message());
                        if (response.errorBody() != null) {
                            try {
                                Log.e(TAG, "Error body: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderDetailsDTO[]> call, Throwable t) {
                    Log.e(TAG, "API call failed", t);
                }
            });
        }
    }
