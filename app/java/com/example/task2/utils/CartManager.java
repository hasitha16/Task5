package com.example.task2.utils;

import com.example.task2.models.CartItem;
import com.example.task2.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private DatabaseReference cartRef;
    private String userId;

    private CartManager() {
        cartItems = new ArrayList<>();
        initializeFirebase();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    private void initializeFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
            loadCartFromFirebase();
        }
    }

    public void addToCart(Product product) {
        if (userId == null) return;

        // Check if item already exists in cart
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                updateCartItemInFirebase(item);
                return;
            }
        }

        // Add new item to cart
        CartItem newItem = new CartItem(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                1
        );
        cartItems.add(newItem);
        addCartItemToFirebase(newItem);
    }

    public void removeFromCart(String productId) {
        if (userId == null) return;

        cartItems.removeIf(item -> item.getProductId().equals(productId));
        cartRef.child(productId).removeValue();
    }

    public void updateQuantity(String productId, int quantity) {
        if (userId == null) return;

        if (quantity <= 0) {
            removeFromCart(productId);
            return;
        }

        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                updateCartItemInFirebase(item);
                break;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        if (userId == null) return;

        cartItems.clear();
        cartRef.removeValue();
    }

    private void addCartItemToFirebase(CartItem item) {
        if (cartRef != null) {
            cartRef.child(item.getProductId()).setValue(item);
        }
    }

    private void updateCartItemInFirebase(CartItem item) {
        if (cartRef != null) {
            cartRef.child(item.getProductId()).setValue(item);
        }
    }

    private void loadCartFromFirebase() {
        if (cartRef != null) {
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    cartItems.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        CartItem item = itemSnapshot.getValue(CartItem.class);
                        if (item != null) {
                            cartItems.add(item);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    public void refreshCart() {
        initializeFirebase();
    }
}