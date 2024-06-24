package com.example.loving_essentials.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserProfileDTO;
import com.example.loving_essentials.Domain.Services.IService.IUserService;
import com.example.loving_essentials.Domain.Services.Service.UserService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    int userId;
    EditText edUsername, edEmail, edPhoneNumber;
    Button btnSave;

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
}
