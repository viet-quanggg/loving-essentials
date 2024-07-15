package com.example.loving_essentials.UI.Fragments;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Patterns;
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
import java.util.Date;
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

    private static final String CHANNEL_ID = "example_channel";
    private static final String CHANNEL_NAME = "Example Channel";
    private static final String CHANNEL_DESCRIPTION = "This is an example notification channel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;

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

            if (validateUserInput(name, email, phoneNumber, password, false)) {
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
            }
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

            if (validateUserInput(name, email, phoneNumber, password, true)) {
                user.setName(name);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                user.setRole(role);
                user.setStatus(status);

                updateUser(user);

                dialog.dismiss();
            }
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
                    checkAndRequestNotificationPermission("User Management", "You are created user successfully!");
                    fetchUsers();
                } else {
                    Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                    checkAndRequestNotificationPermission("User Management", "You are created user successfully!");
                    fetchUsers();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                checkAndRequestNotificationPermission("User Management", "You are created user successfully!");
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
                    checkAndRequestNotificationPermission("User Management", "You are updated user successfully!");
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
                            checkAndRequestNotificationPermission("User Management", "You are deleted user successfully!");
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
    private boolean validateUserInput(String name, String email, String phoneNumber, String password, boolean isUpdate) {
        if (name.isEmpty() || name.length() < 4) {
            Toast.makeText(getContext(), "Username is required and must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Valid email is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10}")) {
            Toast.makeText(getContext(), "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isUpdate) {
            String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
            if (password.isEmpty() || !password.matches(passwordPattern)) {
                Toast.makeText(getContext(), "Password must contain at least one special character, one number, one uppercase character, and one lowercase character, and be at least 6 characters long", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
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

