package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task2.models.CartItem;
import com.example.task2.models.Order;
import com.example.task2.utils.CartManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView totalAmountText;
    private EditText addressEditText, phoneEditText, emailEditText;
    private RadioGroup paymentMethodGroup;
    private RadioButton creditCardRadio, debitCardRadio, upiRadio, codRadio;
    private EditText cardNumberEditText, expiryEditText, cvvEditText, upiIdEditText;
    private Button placeOrderButton;
    private ImageView backButton;

    private List<CartItem> cartItems;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initViews();
        loadCartData();
        setupClickListeners();
        setupPaymentMethodListener();
    }

    private void initViews() {
        totalAmountText = findViewById(R.id.totalAmountText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        creditCardRadio = findViewById(R.id.creditCardRadio);
        debitCardRadio = findViewById(R.id.debitCardRadio);
        upiRadio = findViewById(R.id.upiRadio);
        codRadio = findViewById(R.id.codRadio);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryEditText = findViewById(R.id.expiryEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        upiIdEditText = findViewById(R.id.upiIdEditText);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        backButton = findViewById(R.id.backButton);
    }

    private void loadCartData() {
        cartItems = CartManager.getInstance().getCartItems();
        totalAmount = CartManager.getInstance().getCartTotal();
        totalAmountText.setText(String.format("Total: ₹%.2f", totalAmount));
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        placeOrderButton.setOnClickListener(v -> {
            if (validateInputs()) {
                placeOrder();
            }
        });
    }

    private void setupPaymentMethodListener() {
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Hide all payment fields first
            cardNumberEditText.setVisibility(android.view.View.GONE);
            expiryEditText.setVisibility(android.view.View.GONE);
            cvvEditText.setVisibility(android.view.View.GONE);
            upiIdEditText.setVisibility(android.view.View.GONE);

            // Show relevant fields based on selection
            if (checkedId == R.id.creditCardRadio || checkedId == R.id.debitCardRadio) {
                cardNumberEditText.setVisibility(android.view.View.VISIBLE);
                expiryEditText.setVisibility(android.view.View.VISIBLE);
                cvvEditText.setVisibility(android.view.View.VISIBLE);
            } else if (checkedId == R.id.upiRadio) {
                upiIdEditText.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private boolean validateInputs() {
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter shipping address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedPaymentId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate payment method specific fields
        if (selectedPaymentId == R.id.creditCardRadio || selectedPaymentId == R.id.debitCardRadio) {
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String expiry = expiryEditText.getText().toString().trim();
            String cvv = cvvEditText.getText().toString().trim();

            if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Please fill all card details", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (selectedPaymentId == R.id.upiRadio) {
            String upiId = upiIdEditText.getText().toString().trim();
            if (upiId.isEmpty()) {
                Toast.makeText(this, "Please enter UPI ID", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void placeOrder() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = "ORDER_" + System.currentTimeMillis();
        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String address = addressEditText.getText().toString().trim();

        // Get selected payment method
        int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
        String paymentMethod = "";
        if (selectedPaymentId == R.id.creditCardRadio) {
            paymentMethod = "Credit Card";
        } else if (selectedPaymentId == R.id.debitCardRadio) {
            paymentMethod = "Debit Card";
        } else if (selectedPaymentId == R.id.upiRadio) {
            paymentMethod = "UPI";
        } else if (selectedPaymentId == R.id.codRadio) {
            paymentMethod = "Cash on Delivery";
        }

        // Create order object
        Order order = new Order(orderId, userId, cartItems, totalAmount, orderDate, "Confirmed", paymentMethod, address);

        // Save order to Firebase
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(userId).child(orderId);
        ordersRef.setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Clear cart after successful order
                CartManager.getInstance().clearCart();

                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();

                // Navigate to order confirmation or home
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}