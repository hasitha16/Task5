package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2.adapters.ProductAdapter;
import com.example.task2.data.ProductData;
import com.example.task2.models.Product;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private TextView categoryTitle;
    private RecyclerView categoryRecyclerView;
    private ProductAdapter productAdapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initViews();
        setupCategory();
        setupRecyclerView();
    }

    private void initViews() {
        categoryTitle = findViewById(R.id.categoryTitle);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

        // Setup back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void setupCategory() {
        // Get category from intent
        category = getIntent().getStringExtra("category");
        if (category == null) {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set title
        categoryTitle.setText(category + " Products");
    }

    private void setupRecyclerView() {
        // Get products for this category
        List<Product> categoryProducts = ProductData.getProductsByCategory(category);

        // Setup RecyclerView
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, categoryProducts);
        categoryRecyclerView.setAdapter(productAdapter);

        // Show message if no products found
        if (categoryProducts.isEmpty()) {
            Toast.makeText(this, "No products found in " + category, Toast.LENGTH_SHORT).show();
        }
    }
}