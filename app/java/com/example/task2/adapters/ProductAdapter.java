package com.example.task2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2.ProductDetailActivity;
import com.example.task2.R;
import com.example.task2.models.Product;
import com.example.task2.utils.CartManager;
import com.example.task2.CategoryActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        Button btnAddToCart;

        public ProductViewHolder(View view) {
            super(view);
            productImage = view.findViewById(R.id.productImage);
            productName = view.findViewById(R.id.productName);
            productPrice = view.findViewById(R.id.productPrice);
            btnAddToCart = view.findViewById(R.id.btnAddToCart);

            // FIXED: Add to Cart click listener
            btnAddToCart.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = productList.get(position);
                    CartManager.getInstance().addToCart(product);
                    Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                }
            });


            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product selectedProduct = productList.get(position);
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("productId", selectedProduct.getId());
                        // Navigate to category page showing all products from same category
                        Intent intent1 = new Intent(context, CategoryActivity.class);
                        intent.putExtra("category", selectedProduct.getCategory());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.content_main, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.productName.setText(p.getName());
        holder.productPrice.setText("₹" + p.getPrice());

        // Load product image using drawable resource
        Glide.with(context)
                .load(p.getImageUrl())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}