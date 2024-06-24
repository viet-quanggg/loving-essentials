    package com.example.loving_essentials.UI.Fragments;

    import android.os.Bundle;
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
    import com.example.loving_essentials.UI.Adapter.OrderAdapter;
    import com.example.loving_essentials.UI.Adapter.ProductCardListAdapter;
    import com.example.loving_essentials.UI.ProductListActivity;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class MyOrderFragment extends Fragment {
        List<OrderDTO> list;
        OrderAdapter orderAdapter;
        RecyclerView recyclerView;
        IOrderService orderService;
        public MyOrderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_order, container, false);

            recyclerView = view.findViewById(R.id.rv_cart_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            orderService = OrderService.getOrderService();

            list = new ArrayList<>();
            orderAdapter = new OrderAdapter(getContext(), list);
            recyclerView.setAdapter(orderAdapter);

            Call<OrderDTO[]> callOrders = orderService.getOrders();
            callOrders.enqueue(new Callback<OrderDTO[]>() {
                @Override
                public void onResponse(Call<OrderDTO[]> call, Response<OrderDTO[]> response) {
                    OrderDTO[] orders = response.body();
                    if (orders == null) {
                        return;
                    }

                    list.addAll(Arrays.asList(orders));
                    orderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<OrderDTO[]> call, Throwable t) {

                }
            });
            orderAdapter.notifyDataSetChanged();

            return view;
        }
    }
