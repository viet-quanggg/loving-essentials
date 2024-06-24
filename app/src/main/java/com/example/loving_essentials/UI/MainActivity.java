package com.example.loving_essentials.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.loving_essentials.Domain.Entity.DTOs.UserDTO.UserProfileDTO;
import com.example.loving_essentials.Domain.Services.IService.IUserService;
import com.example.loving_essentials.Domain.Services.Service.UserService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Fragments.HomeFragment;
import com.example.loving_essentials.UI.Fragments.UserProfileFragment;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView emailTextView, usernameTextView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    Fragment homeFragment, userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // Initialize the menu variable
        menu = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);
        emailTextView = headerView.findViewById(R.id.tvEmail);
        usernameTextView = headerView.findViewById(R.id.tvUsername);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("id", 0);
        fetchUserProfile(userId);

        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(homeFragment);
        } else if (id == R.id.nav_products) {
            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_profile).setVisible(true);

            menu.findItem(R.id.nav_login).setVisible(false);
        } else if (id == R.id.nav_logout) {
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
        } else if (id == R.id.nav_profile) {
            Intent intent = getIntent();
            int userId = intent.getIntExtra("id", 0);

            Bundle args = new Bundle();
            args.putInt("userId", userId);

            userProfileFragment = new UserProfileFragment();
            userProfileFragment.setArguments(args);
            loadFragment(userProfileFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                        usernameTextView.setText(userProfile.getName());
                        emailTextView.setText(userProfile.getEmail());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch user profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileDTO> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
