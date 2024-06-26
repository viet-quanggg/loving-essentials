package com.example.loving_essentials.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.loving_essentials.Domain.Entity.Category;
import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserDTO;
import com.example.loving_essentials.Domain.Services.IService.IUserService;
import com.example.loving_essentials.Domain.Services.Service.UserService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.CategoryAdapter;
import com.example.loving_essentials.UI.Adapter.UserAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementFragment extends Fragment {
    List<UserDTO> users;
    RecyclerView rclUsers;
    UserAdapter userAdapter;
    IUserService userService;

    public UserManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_management, container, false);
        userService = UserService.getUserService();
        rclUsers = root.findViewById(R.id.rclUsers);
        rclUsers.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        users = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), users, (position, user) -> {
            // Handle the click event here
            Toast.makeText(getContext(), "User clicked: " + user.getName(), Toast.LENGTH_SHORT).show();
            // You can navigate to another fragment or activity here
        });
        rclUsers.setAdapter(userAdapter);

        fetchUsers();

        return root;
    }

    private void fetchUsers() {
        Call<UserDTO[]> call = userService.getAllUsers();
        call.enqueue(new Callback<UserDTO[]>() {
            @Override
            public void onResponse(Call<UserDTO[]> call, Response<UserDTO[]> response) {
                UserDTO[] usersList = response.body();
                if (usersList != null) {
                    Log.d("UserManagementFragment", "Number of users received: " + usersList.length);
                    users.clear();  // Clear the list before adding new items
                    users.addAll(Arrays.asList(usersList));
                    userAdapter.notifyDataSetChanged();  // Notify the adapter about data change
                } else {
                    Log.e("UserManagementFragment", "Received null response for users");
                }
            }

            @Override
            public void onFailure(Call<UserDTO[]> call, Throwable t) {
                Log.e("UserManagementFragment", "User get error", t);
            }
        });
    }
}

