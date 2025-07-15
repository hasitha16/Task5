package com.example.task2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.task2.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productDescription, productPrice;
    private RatingBar productRating;
    private Button btnAddToCart;
    private ImageButton wishlistBtn;

    EditText newsletterEmail = findViewById(R.id.newsletterEmail);
    Button subscribeButton = findViewById(R.id.subscribeButton);

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        //productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        //productRating = findViewById(R.id.productRating);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        //btnBuyNow = findViewById(R.id.btnBuyNow);
        //wishlistBtn = findViewById(R.id.wishlistBtn);

        productId = getIntent().getStringExtra("productId");

        if (productId != null) {
            loadProductDetails(productId);
        } else {
            Toast.makeText(this, "No product selected", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadProductDetails(String id) {
        FirebaseDatabase.getInstance().getReference("products")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            productName.setText(product.getName());
                            productDescription.setText(product.getDescription());
                            productPrice.setText("₹" + product.getPrice());
                            productRating.setRating(product.getRating());
                            Glide.with(ProductDetailActivity.this).load(product.getImageUrl()).into(productImage);
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ProductDetailActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        subscribeButton.setOnClickListener(v -> {
            String email = newsletterEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                Toast.makeText(this, "Subscribed with: " + email, Toast.LENGTH_SHORT).show();
                // Optionally: store in Firebase under "newsletter_subscribers"
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
