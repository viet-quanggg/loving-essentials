package com.example.loving_essentials.UI.Fragments;

import static com.example.loving_essentials.Domain.Services.Service.AuthService.getAuthService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.RegisterRequest;
import com.example.loving_essentials.Domain.Entity.DTOs.AuthDTO.RegisterResponse;
import com.example.loving_essentials.Domain.Services.IService.IAuthService;
import com.example.loving_essentials.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword, txtFullName, txtPhone, txtRepeatPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        txtEmail = findViewById(R.id.txt_email);
        txtFullName = findViewById(R.id.txt_fullname);
        txtPhone = findViewById(R.id.txt_phone);
        txtPassword = findViewById(R.id.txt_password);
        txtRepeatPassword = findViewById(R.id.txt_repeatPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this::registerUser);
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void registerUser(View v) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String fullName = txtFullName.getText().toString();
        String phone = txtPhone.getText().toString();
        String repeatPassword = txtRepeatPassword.getText().toString();

        Log.d("RegisterActivity", "Attempting to register with email: " + email + ", fullName: " + fullName + ", phone: " + phone);

        if (!isValidInput(email, password, fullName, phone, repeatPassword)) {
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPhoneNumber(phone);
        registerRequest.setPassword(password);
        registerRequest.setName(fullName);

        IAuthService authService = getAuthService();
        Call<RegisterResponse> call = authService.registerUser(registerRequest);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("RegisterActivity", "Attempting to register with email: " + email + ", fullName: " + fullName);
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("RegisterActivity", "Registration failed. Response code: " + response.code() + ", message: " + response.message());
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isValidInput(String email, String password, String fullName, String phone, String repeatPassword) {
        if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || fullName.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() != 10) {
            Toast.makeText(this, "Please enter a valid phone - 10 numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
