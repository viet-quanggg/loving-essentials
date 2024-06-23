//package com.example.loving_essentials.UI;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.loving_essentials.Domain.Entity.DTOs.LoginDTO;
//import com.example.loving_essentials.Domain.Entity.DTOs.RegisterDTO;
//import com.example.loving_essentials.Domain.Services.IService.IAuthService;
//import com.example.loving_essentials.Domain.Services.Service.AuthService;
//import com.example.loving_essentials.R;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class RegisterActivity extends AppCompatActivity {
//    private TextView tvSignIn;
//    private EditText etName, etEmail, etPassword, etPhoneNumber;
//    private Button btnRegister;
//    private IAuthService authService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_register);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        authService = AuthService.getAuthService();
//
//        etName = (EditText) findViewById(R.id.etName);
//        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
//        etEmail = (EditText) findViewById(R.id.etEmail);
//        etPassword = (EditText) findViewById(R.id.etPassword);
//        btnRegister = (Button) findViewById(R.id.btnRegister);
//        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
//
//        tvSignIn.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        });
//
//        btnRegister.setOnClickListener(v -> {
//            String name = etName.getText().toString().trim();
//            String phoneNumber = etPhoneNumber.getText().toString().trim();
//            String email = etEmail.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
//
//            if (validateInput(email, password, name, phoneNumber)) {
//                register(email, password, name, phoneNumber);
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    private boolean validateInput(String email, String password, String name, String phoneNumber) {
//        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
//            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private void register(String email, String password, String name, String phoneNumber) {
//        RegisterDTO registerDTO = new RegisterDTO();
//        registerDTO.setEmail(email);
//        registerDTO.setPassword(password);
//        registerDTO.setName(name);
//        registerDTO.setPhoneNumber(phoneNumber);
//
//        Call<RegisterDTO> call = authService.register(registerDTO);
//        call.enqueue(new Callback<RegisterDTO>() {
//            @Override
//            public void onResponse(Call<RegisterDTO> call, Response<RegisterDTO> response) {
//                if (response.isSuccessful()) {
//                    // Handle successful register
//                    Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Handle unsuccessful register
//                    Toast.makeText(RegisterActivity.this, "Register failed: " + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterDTO> call, Throwable t) {
//                // Handle failure
//                Toast.makeText(RegisterActivity.this, "Register error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}