    package com.example.loving_essentials.UI.Fragments;

    import static android.content.ContentValues.TAG;

    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.content.Context;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Handler;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import androidx.core.app.NotificationCompat;
    import androidx.core.content.ContextCompat;
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
    import java.util.Date;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class MyOrderFragment extends Fragment implements OnOrderClickListener {
        private static final long POLLING_INTERVAL = 30000; // 30 seconds
        List<OrderDTO> list;
        OrderAdapter orderAdapter;
        RecyclerView recyclerView;
        IOrderService orderService;
        int userId;
        Handler handler = new Handler();
        Runnable pollOrdersRunnable;

        private static final String CHANNEL_ID = "example_channel";
        private static final String CHANNEL_NAME = "Example Channel";
        private static final String CHANNEL_DESCRIPTION = "This is an example notification channel";
        private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

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
                userId = args.getInt("userId");
                fetchOrders(userId);
            } else {
                Log.e(TAG, "Arguments are null, cannot fetch orders");
            }

            // Start polling for order updates
            pollOrdersRunnable = new Runnable() {
                @Override
                public void run() {
                    fetchOrders(userId);
                    handler.postDelayed(this, POLLING_INTERVAL);
                }
            };
            handler.post(pollOrdersRunnable);

            return view;
        }

        private void fetchOrders(int userId) {
            Call<OrderDTO[]> callOrders = orderService.getOrdersByUserId(userId);
            callOrders.enqueue(new Callback<OrderDTO[]>() {
                @Override
                public void onResponse(Call<OrderDTO[]> call, Response<OrderDTO[]> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<OrderDTO> updatedOrders = Arrays.asList(response.body());
                        checkForStatusUpdates(updatedOrders);
                        list.clear();
                        list.addAll(updatedOrders);
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

        private void checkForStatusUpdates(List<OrderDTO> updatedOrders) {
            for (OrderDTO updatedOrder : updatedOrders) {
                for (OrderDTO existingOrder : list) {
                    if (updatedOrder.getId() == existingOrder.getId() &&
                            updatedOrder.getStatus() == 4 &&
                            existingOrder.getStatus() != 4) {
                        // Order status changed to Delivered
                        checkAndRequestNotificationPermission("Order Delivered", "Your order has been delivered successfully!");
                    }
                }
            }
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

        private void checkAndRequestNotificationPermission(String title, String contentText) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
                } else {
                    createNotificationChannel();
                    sendNotify(title, contentText);
                }
            } else {
                createNotificationChannel();
                sendNotify(title, contentText);
            }
        }

        private void sendNotify(String title, String contentText) {
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(contentText)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(bitmap)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    int num = getNotificationId();
                    notificationManager.notify(num, builder.build());
                    Log.d("CheckoutFragment", "Notification sent with ID: " + num);
                }
            } catch (Exception ex) {
                Log.e("CheckoutFragment", "Error sending notification: ", ex);
                ex.printStackTrace();
            }
        }

        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription(CHANNEL_DESCRIPTION);

                NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                    Log.d("CheckoutFragment", "Notification channel created: " + CHANNEL_NAME);
                } else {
                    Toast.makeText(getContext(), "Error creating notification channel!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private int getNotificationId() {
            return (int) new Date().getTime();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Notification Permission Granted", Toast.LENGTH_SHORT).show();
                    createNotificationChannel();
                    sendNotify("Notification", "Order created successfully!");  // Default message
                } else {
                    Toast.makeText(getContext(), "Notification Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            // Stop polling when the view is destroyed
            handler.removeCallbacks(pollOrdersRunnable);
        }
    }
