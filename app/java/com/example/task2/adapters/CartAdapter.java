package com.example.task2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.task2.R;
import com.example.task2.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onQuantityChanged(String productId, int newQuantity);
        void onItemRemoved(String productId);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.productPrice.setText(String.format("₹%.2f", item.getProductPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.totalPrice.setText(String.format("₹%.2f", item.getTotalPrice()));

        // Load product image
        int imageResource = context.getResources().getIdentifier(
                item.getProductImage(), "drawable", context.getPackageName());
        if (imageResource != 0) {
            holder.productImage.setImageResource(imageResource);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_placeholder);
        }

        // Quantity controls
        holder.decreaseBtn.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity >= 0) {
                listener.onQuantityChanged(item.getProductId(), newQuantity);
            }
        });

        holder.increaseBtn.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            listener.onQuantityChanged(item.getProductId(), newQuantity);
        });

        holder.removeBtn.setOnClickListener(v -> {
            listener.onItemRemoved(item.getProductId());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, decreaseBtn, increaseBtn, removeBtn;
        TextView productName, productPrice, quantity, totalPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartProductImage);
            productName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            quantity = itemView.findViewById(R.id.cartQuantity);
            totalPrice = itemView.findViewById(R.id.cartTotalPrice);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}