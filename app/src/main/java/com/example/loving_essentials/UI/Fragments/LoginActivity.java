package com.example.loving_essentials.UI.Fragments;

import static com.example.loving_essentials.Domain.Services.Service.AuthService.getAuthService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.LoginRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.LoginResponse;
import com.example.loving_essentials.Domain.Entity.User;
import com.example.loving_essentials.Domain.Services.IService.IAuthService;
import com.example.loving_essentials.R;
import com.google.android.gms.common.SignInButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnRegister;
    private SignInButton signInButton;
    private TextView tvForgotPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        txtEmail = findViewById(R.id.txt_email);
        txtPassword = findViewById(R.id.txt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        signInButton = findViewById(R.id.sign_in_button);
        tvForgotPW = findViewById(R.id.tvForgotPW);

        btnLogin.setOnClickListener(v -> {
            Log.d("TestLogin", "Tung Test Login button clicked");
            loginUser();
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        signInButton.setOnClickListener(v -> {
            // Handle Google Sign-In
        });

        tvForgotPW.setOnClickListener(v -> {
            // Handle forgotten password
        });
    }

    private void loginUser() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        IAuthService authService = getAuthService();
        Call<LoginResponse> call = authService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    handleSuccessfulLogin(response.body());
                } else {
                    Log.e("TestLogin", "Login failed. Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("TestLogin_Error", "Login failed with exception: " + t.getMessage(), t);
            }
        });
    }

    private void handleSuccessfulLogin(LoginResponse loginResponse) {
        User user = loginResponse.getUser();
        String token = loginResponse.getToken();

        int id = user.getId();
        String name = user.getName();
        int role = user.getRole();
        String email = user.getEmail();
        String phone = user.getPhoneNumber();

        Log.d("TestLogin", "User ID: " + id + ", Name: " + name + ", Role: " + role + ", Email: " + email + ", Phone: " + phone + ", Token: " + token);

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", id);
        editor.putString("name", name);
        editor.putString("token", token);
        editor.apply();

        if (role == 1) {
            Toast.makeText(LoginActivity.this, "Login successful with User Role", Toast.LENGTH_SHORT).show();
        } else if (role == 2) {
            // Navigate to another page
        } else if (role == 3) {
            // Navigate to another page
        }
    }
}