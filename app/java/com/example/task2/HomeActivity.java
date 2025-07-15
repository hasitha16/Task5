package com.example.task2;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.TextView;
import android.widget.Toast;
import com.example.task2.utils.CartManager;
import com.example.task2.adapters.ProductAdapter;
import com.example.task2.data.ProductData;
import com.example.task2.models.Product;
import com.example.task2.CategoryActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private View dot1, dot2, dot3;
    private HorizontalScrollView scrollView;
    private LinearLayout sidebarIcon;
    private TextView welcomeText;

    private android.widget.EditText searchEditText;
    private ImageView searchIcon;
    private TextView cartBadge;
    private RelativeLayout cartButton;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    // Products RecyclerView
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initViews();
        setupUserWelcome();
        setupScrollListener();
        setupClickListeners();
        setupProductsRecyclerView();
        setupCategoryImageClickListeners();
    }

    private void initViews() {
        scrollView = findViewById(R.id.imageScroller);
        sidebarIcon = findViewById(R.id.sidebarIcon);
        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.searchIcon);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        welcomeText = findViewById(R.id.welcomeText);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        cartBadge = findViewById(R.id.cartBadge);
        cartButton = findViewById(R.id.cartButton);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
    }

    private void setupUserWelcome() {
        // Add null check for current user
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        welcomeText.setText("Welcome, " + username + "!");
                    } else {
                        welcomeText.setText("Welcome!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(HomeActivity.this, "Failed to fetch user", Toast.LENGTH_SHORT).show();
                }
            });

            // Refresh cart when user logs in
            // Initialize CartManager for the logged-in user
            CartManager.getInstance();
        } else {
            welcomeText.setText("Welcome!");
        }
    }

    private void setupScrollListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                float cardWidthPx = getResources().getDisplayMetrics().density * 166;
                int page = Math.round(scrollX / cardWidthPx);
                updateDots(page);
            });
        }
    }

    private void setupClickListeners() {
        searchIcon.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
                intent.putExtra("searchQuery", query);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        sidebarIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // Updated navigation listener with all menu items
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                Toast.makeText(this, "Going to Profile Page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.nav_cart) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.nav_orders) {
                Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupProductsRecyclerView() {
        // Load products from ProductData
        productList = ProductData.getAllProducts();

        // Setup RecyclerView with GridLayoutManager (2 columns)
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, productList);
        productsRecyclerView.setAdapter(productAdapter);
    }

    private void updateDots(int position) {
        dot1.setBackgroundResource(position == 0 ? R.drawable.dot_active : R.drawable.dot_inactive);
        dot2.setBackgroundResource(position == 1 ? R.drawable.dot_active : R.drawable.dot_inactive);
        dot3.setBackgroundResource(position == 2 ? R.drawable.dot_active : R.drawable.dot_inactive);
    }

    private void updateCartBadge() {
        int itemCount = CartManager.getInstance().getCartItemCount();
        if (itemCount > 0) {
            cartBadge.setVisibility(android.view.View.VISIBLE);
            cartBadge.setText(String.valueOf(itemCount));
        } else {
            cartBadge.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    private void setupCategoryImageClickListeners() {
        // Add click listeners to the category images in horizontal scroll
        findViewById(R.id.dine).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Dining");
            startActivity(intent);
        });

        findViewById(R.id.couch).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Furniture");
            startActivity(intent);
        });

        findViewById(R.id.bed).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Bedroom");
            startActivity(intent);
        });

        findViewById(R.id.chair).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Chairs");
            startActivity(intent);
        });

        findViewById(R.id.vase).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Decor");
            startActivity(intent);
        });

        findViewById(R.id.cup1).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Cupboard");
            startActivity(intent);
        });

        findViewById(R.id.cup2).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            intent.putExtra("category", "Cupboard");
            startActivity(intent);
        });
    }

}
