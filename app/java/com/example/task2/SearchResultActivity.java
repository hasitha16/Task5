package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2.adapters.ProductAdapter;
import com.example.task2.data.ProductData;
import com.example.task2.models.Product;
import com.example.task2.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private String searchQuery;
    private TextView cartBadge;
    private RelativeLayout cartButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        initViews();
        setupRecyclerView();
        loadSearchResults();
        setupClickListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.searchRecyclerView);
        cartBadge = findViewById(R.id.cartBadge);
        cartButton = findViewById(R.id.cartButton);
        backButton = findViewById(R.id.backButton);

        TextView headerTitle = findViewById(R.id.headerTitle);
        searchQuery = getIntent().getStringExtra("searchQuery");
        headerTitle.setText("Search: " + (searchQuery != null ? searchQuery : ""));
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }

    private void loadSearchResults() {
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            loadMatchingProducts(searchQuery);
        } else {
            Toast.makeText(this, "No search query provided", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMatchingProducts(String query) {
        List<Product> allProducts = ProductData.getAllProducts();
        productList.clear();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(query.toLowerCase())) {
                productList.add(product);
            }
        }

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products found for: " + query, Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
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
}