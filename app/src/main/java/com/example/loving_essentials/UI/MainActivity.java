package com.example.loving_essentials.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.loving_essentials.UI.Fragments.CartFragment;
import com.example.loving_essentials.UI.Fragments.HomeFragment;
import com.example.loving_essentials.UI.Fragments.LoginActivity;
import com.example.loving_essentials.UI.Fragments.MyOrderFragment;
import com.example.loving_essentials.UI.Fragments.ProductListFragment;
import com.example.loving_essentials.UI.Fragments.UserProfileFragment;
import com.example.loving_essentials.UI.UserView.AddressView.ShippingInformation;
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
    Fragment myOrderFragment;
    Fragment homeFragment, userProfileFragment, myAddressFragment;
    ImageView addcart;

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
        addcart = findViewById(R.id.addCartmain);
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

        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CartFragment());
            }
        });
    }

    public void loadFragment(Fragment fragment) {
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
            ProductListFragment productListFragment = new ProductListFragment();
            loadFragment(productListFragment);
        } /*else if (id == R.id.nav_login) {
            ProductListFragment productListFragment = new ProductListFragment();
            loadFragment(productListFragment);
        } else if (id == R.id.nav_login) {
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_profile).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_address).setVisible(true);
        }*/ else if (id == R.id.nav_logout) {
            /*menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);*/

            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);

        } else if (id == R.id.nav_profile) {
            Intent intent = getIntent();
            int userId = intent.getIntExtra("id", 0);

            Bundle args = new Bundle();
            args.putInt("userId", userId);

            userProfileFragment = new UserProfileFragment();
            userProfileFragment.setArguments(args);
            loadFragment(userProfileFragment);
        }else if(id == R.id.nav_address){
            myAddressFragment = new ShippingInformation();
            Intent intent = getIntent();
            int userId = intent.getIntExtra("id", 0);

            Bundle args = new Bundle();
            args.putInt("userId", userId);
            myAddressFragment.setArguments(args);
            loadFragment(myAddressFragment);
        }
        else if (id == R.id.nav_myorder) {
            Intent intent = getIntent();
            int userId = intent.getIntExtra("id", 0);

            Bundle args = new Bundle();
            args.putInt("userId", userId);

            myOrderFragment = new MyOrderFragment();
            myOrderFragment.setArguments(args);
            loadFragment(myOrderFragment);
        }
        else if (id == R.id.nav_phone) {
            Uri number = Uri.parse("tel:0898980731");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        }
        else if (id == R.id.nav_message) {
            String phoneNumber = "0898980731";
            String message = "Hello, I want to buy milk, can you advise me?";

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);

            // Check if there is an app that can handle this intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
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
