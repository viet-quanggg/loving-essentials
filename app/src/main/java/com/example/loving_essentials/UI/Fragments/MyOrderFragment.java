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
    import com.example.loving_essentials.Domain.Services.IService.IOrderService;
    import com.example.loving_essentials.Domain.Services.Service.OrderService;
    import com.example.loving_essentials.R;
    import com.example.loving_essentials.UI.Adapter.OnOrderClickListener;
    import com.example.loving_essentials.UI.Adapter.OrderAdapter;
    import com.example.loving_essentials.UI.MainActivity;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class MyOrderFragment extends Fragment implements OnOrderClickListener {
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
            Bundle args = getArguments();
            if (args != null) {
                int userId = args.getInt("userId");
                fetchOrders(userId);
            } else {
                Log.e(TAG, "Arguments are null, cannot fetch orders");
            }

            return view;
        }
        private void fetchOrders(int userId) {
            Call<OrderDTO[]> callOrders = orderService.getOrdersByUserId(userId);
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
            Bundle bundle = new Bundle();
            bundle.putInt("orderId", orderId);

            OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
            orderDetailFragment.setArguments(bundle);

            if (getActivity() != null) {
                ((MainActivity) getActivity()).loadFragment(orderDetailFragment);
            }
        }

    }

