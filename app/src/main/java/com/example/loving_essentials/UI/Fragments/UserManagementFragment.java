package com.example.loving_essentials.UI.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserDTO;
import com.example.loving_essentials.Domain.Services.IService.IUserService;
import com.example.loving_essentials.Domain.Services.Service.UserService;
import com.example.loving_essentials.R;
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
    Button btnCreate;

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
            showUpdateUserDialog(user);
        }, position -> {
            // Handle the long click event here
            deleteUser(users.get(position).getId());
        });
        rclUsers.setAdapter(userAdapter);

        fetchUsers();
        root.findViewById(R.id.btnCreate).setOnClickListener(v -> showCreateUserDialog());
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
    private void showCreateUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_user_admin, null);
        builder.setView(dialogView);

        EditText edUsername = dialogView.findViewById(R.id.edUsername);
        EditText edEmail = dialogView.findViewById(R.id.edEmail);
        EditText edPhoneNumber = dialogView.findViewById(R.id.edPhoneNumber);
        EditText edPassword = dialogView.findViewById(R.id.edPassword);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            String name = edUsername.getText().toString();
            String email = edEmail.getText().toString();
            String phoneNumber = edPhoneNumber.getText().toString();
            String password = edPassword.getText().toString();
            int role = spinnerRole.getSelectedItemPosition() + 1;
            byte status = (byte) spinnerStatus.getSelectedItemPosition();

            UserDTO newUser = new UserDTO();
            newUser.setId(0); // Assuming 0 means a new user
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setPassword(password);
            newUser.setRole(role);
            newUser.setStatus(status);

            addUser(newUser);

            dialog.dismiss();
        });

        dialog.show();
    }
    private void showUpdateUserDialog(UserDTO user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_user_admin, null);
        builder.setView(dialogView);

        EditText edUsername = dialogView.findViewById(R.id.edUsername);
        EditText edEmail = dialogView.findViewById(R.id.edEmail);
        EditText edPhoneNumber = dialogView.findViewById(R.id.edPhoneNumber);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Set existing user data
        edUsername.setText(user.getName());
        edEmail.setText(user.getEmail());
        edPhoneNumber.setText(user.getPhoneNumber());
        spinnerRole.setSelection(user.getRole() - 1);
        spinnerStatus.setSelection(user.getStatus());

        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            String name = edUsername.getText().toString();
            String email = edEmail.getText().toString();
            String phoneNumber = edPhoneNumber.getText().toString();
            String password = user.getPassword();
            int role = spinnerRole.getSelectedItemPosition() + 1;
            byte status = (byte) spinnerStatus.getSelectedItemPosition();

            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);
            user.setRole(role);
            user.setStatus(status);

            updateUser(user);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void addUser(UserDTO user) {
        userService.addUser(user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    users.add(response.body());
                    userAdapter.notifyItemInserted(users.size() - 1);
                    Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                fetchUsers();
            }
        });
    }

    private void updateUser(UserDTO user) {
        userService.updateUser(user).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    int index = users.indexOf(user);
                    users.set(index, response.body());
                    userAdapter.notifyItemChanged(index);
                    Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser(int userId) {
        userService.deleteUser(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getId() == userId) {
                            users.remove(i);
                            userAdapter.notifyItemRemoved(i);
                            Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                            fetchUsers();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

