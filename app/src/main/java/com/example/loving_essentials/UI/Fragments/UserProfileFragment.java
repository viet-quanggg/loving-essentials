package com.example.loving_essentials.UI.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserProfileDTO;
import com.example.loving_essentials.Domain.Services.IService.IUserService;
import com.example.loving_essentials.Domain.Services.Service.UserService;
import com.example.loving_essentials.R;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    int userId;
    EditText edUsername, edEmail, edPhoneNumber;
    Button btnSave;

    private static final String CHANNEL_ID = "example_channel";
    private static final String CHANNEL_NAME = "Example Channel";
    private static final String CHANNEL_DESCRIPTION = "This is an example notification channel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);

        edUsername = view.findViewById(R.id.edUsername);
        edEmail = view.findViewById(R.id.edEmail);
        edPhoneNumber = view.findViewById(R.id.edPhone);
        btnSave = view.findViewById(R.id.btnSave);

        // Assuming userId is passed as a fragment argument
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1);
        }

        fetchUserProfile(userId);

        btnSave.setOnClickListener(v -> {
            UserProfileDTO userProfileDTO = new UserProfileDTO();
            userProfileDTO.setId(userId);
            userProfileDTO.setName(edUsername.getText().toString());
            userProfileDTO.setEmail(edEmail.getText().toString());
            userProfileDTO.setPhoneNumber(edPhoneNumber.getText().toString());

            IUserService userService = UserService.getUserService();
            Call<UserProfileDTO> call = userService.updateProfile(userProfileDTO);

            call.enqueue(new Callback<UserProfileDTO>() {
                @Override
                public void onResponse(Call<UserProfileDTO> call, Response<UserProfileDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        checkAndRequestNotificationPermission("User Profile", "Update profile successfully!");
                    } else {
                        Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserProfileDTO> call, Throwable t) {
                    Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void fetchUserProfile(int userId) {
        IUserService userService = UserService.getUserService();
        Call<UserProfileDTO> call = userService.getUserProfile(userId);

        call.enqueue(new Callback<UserProfileDTO>() {
            @Override
            public void onResponse(Call<UserProfileDTO> call, Response<UserProfileDTO> response) {
                if (response.isSuccessful()) {
                    UserProfileDTO userProfile = response.body();
                    if (userProfile != null) {
                        edUsername.setText(userProfile.getName());
                        edEmail.setText(userProfile.getEmail());
                        edPhoneNumber.setText(userProfile.getPhoneNumber());
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch user profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
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
}
