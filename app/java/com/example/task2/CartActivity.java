package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2.adapters.CartAdapter;
import com.example.task2.models.CartItem;
import com.example.task2.utils.CartManager;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalPriceText, emptyCartText;
    private ImageView backButton;
    private List<CartItem> cartItems;
    private Button proceedToCheckoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupRecyclerView();
        loadCartItems();
        setupClickListeners();
    }

    private void initViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        emptyCartText = findViewById(R.id.emptyCartText);
        backButton = findViewById(R.id.backButton);
        proceedToCheckoutButton = findViewById(R.id.proceedToCheckoutButton);
    }

    private void setupRecyclerView() {
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(String productId, int newQuantity) {
                CartManager.getInstance().updateQuantity(productId, newQuantity);
                updateTotal();

                if (newQuantity == 0) {
                    loadCartItems(); // Refresh the list
                }
            }

            @Override
            public void onItemRemoved(String productId) {
                CartManager.getInstance().removeFromCart(productId);
                loadCartItems();
                Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        });

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void loadCartItems() {
        cartItems.clear();
        cartItems.addAll(CartManager.getInstance().getCartItems());
        cartAdapter.notifyDataSetChanged();

        if (cartItems.isEmpty()) {
            emptyCartText.setVisibility(android.view.View.VISIBLE);
            cartRecyclerView.setVisibility(android.view.View.GONE);
            totalPriceText.setText("Total: ₹0.00");
        } else {
            emptyCartText.setVisibility(android.view.View.GONE);
            cartRecyclerView.setVisibility(android.view.View.VISIBLE);
            updateTotal();
        }
    }

    private void updateTotal() {
        double total = CartManager.getInstance().getCartTotal();
        totalPriceText.setText(String.format("Total: ₹%.2f", total));
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        proceedToCheckoutButton.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }
}