    package com.example.loving_essentials.UI.Fragments;

    import static android.content.ContentValues.TAG;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.loving_essentials.Domain.Entity.DTOs.OrderDTO;
    import com.example.loving_essentials.Domain.Services.IService.IOrderService;
    import com.example.loving_essentials.Domain.Services.Service.OrderService;
    import com.example.loving_essentials.R;
    import com.example.loving_essentials.UI.Adapter.OnOrderClickListener;
    import com.example.loving_essentials.UI.Adapter.OrderAdapter;
    import com.example.loving_essentials.UI.AdminMainActivity;
    import com.example.loving_essentials.UI.AssignShipperActivity;
    import com.example.loving_essentials.UI.MainActivity;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class OrderListFragment extends Fragment implements OnOrderClickListener {
        List<OrderDTO> list;
        OrderAdapter orderAdapter;
        RecyclerView recyclerView;
        IOrderService orderService;
        int userId;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_order, container, false);

            recyclerView = view.findViewById(R.id.rv_cart_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            orderService = OrderService.getOrderService();

            list = new ArrayList<>();
            orderAdapter = new OrderAdapter(getContext(), list, this);
            recyclerView.setAdapter(orderAdapter);
            fetchOrders();

            return view;
        }
        private void fetchOrders() {
            Call<OrderDTO[]> callOrders = orderService.getOrders();
            callOrders.enqueue(new Callback<OrderDTO[]>() {
                @Override
                public void onResponse(Call<OrderDTO[]> call, Response<OrderDTO[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        OrderDTO[] orders = response.body();
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
                public void onFailure(Call<OrderDTO[]> call, Throwable t) {
                    Log.e(TAG, "API call failed", t);
                }
            });

        }
        @Override
        public void onOrderClick(int orderId) {

            Intent assignShipper = new Intent(getActivity(), AssignShipperActivity.class);
            assignShipper.putExtra("orderId", orderId);
            startActivity(assignShipper);
        }

    }

