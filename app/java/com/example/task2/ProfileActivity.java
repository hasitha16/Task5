package com.example.task2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.View;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.task2.models.User;
import com.example.task2.utils.FirebaseManager;
import com.example.task2.utils.NetworkUtils;
import com.example.task2.utils.LoadingDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage, backButton;
    private TextView usernameText, emailText;
    private LinearLayout settingsButton, editProfileButton, languagesButton, locationButton, logoutButton;
    private Uri imageUri;

    private FirebaseManager firebaseManager;
    private LoadingDialog loadingDialog;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        initializeFirebase();
        setupClickListeners();
        loadUserData();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profile_image);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);

        // Button references
        settingsButton = findViewById(R.id.settings_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        languagesButton = findViewById(R.id.languages_button);
        locationButton = findViewById(R.id.location_button);
        logoutButton = findViewById(R.id.logout_button);

        loadingDialog = new LoadingDialog(this);
    }

    private void initializeFirebase() {
        firebaseManager = FirebaseManager.getInstance();

        // Check if user is logged in
        if (firebaseManager.getCurrentUser() == null) {
            redirectToLogin();
            return;
        }
    }

    private void setupClickListeners() {
        // Profile image click - open image picker
        profileImage.setOnClickListener(v -> {
            if (!NetworkUtils.isNetworkAvailable(this)) {
                showNetworkError();
                return;
            }
            openImagePicker();
        });

        // Settings button
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, 100); // Request code 100 for edit profile
        });

        // Languages button
        languagesButton.setOnClickListener(v -> {
            showMessage("Languages feature coming soon!");
        });

        // Location button
        locationButton.setOnClickListener(v -> {
            openLocationActivity();
        });

        // Logout button - Enhanced with confirmation dialog
        logoutButton.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void openLocationActivity() {
        Intent intent = new Intent(ProfileActivity.this, LocationActivity.class);
        startActivityForResult(intent, 200); // Request code 200 for location
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void loadUserData() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNetworkError();
            loadCachedUserData();
            return;
        }

        loadingDialog.show("Loading profile...");

        String uid = firebaseManager.getCurrentUserId();
        if (uid == null) {
            loadingDialog.dismiss();
            redirectToLogin();
            return;
        }

        firebaseManager.getUserData(uid, new FirebaseManager.UserDataCallback() {
            @Override
            public void onSuccess(User user) {
                loadingDialog.dismiss();
                currentUser = user;
                updateUI(user);
                cacheUserData(user);
            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                showError("Failed to load profile: " + error);
                loadCachedUserData();
            }
        });
    }

    private void updateUI(User user) {
        if (user != null) {
            String username = user.getUsername();
            String email = user.getEmail();

            usernameText.setText(username != null ? username : "Username");
            emailText.setText(email != null ? email :
                    (firebaseManager.getCurrentUser() != null ?
                            firebaseManager.getCurrentUser().getEmail() : "Email"));

            // Load profile image if available
            if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(user.getProfileImageUrl())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(profileImage);
            }
        } else {
            usernameText.setText("Username");
            emailText.setText(firebaseManager.getCurrentUser() != null ?
                    firebaseManager.getCurrentUser().getEmail() : "Email");
        }
    }

    private void performLogout() {
        loadingDialog.show("Signing out...");

        try {
            // Update last login time before logout (if user data exists)
            if (currentUser != null && NetworkUtils.isNetworkAvailable(this)) {
                currentUser.setLastLoginAt(System.currentTimeMillis());
                firebaseManager.updateUserData(currentUser.getUid(), currentUser,
                        new FirebaseManager.UpdateCallback() {
                            @Override
                            public void onSuccess() {
                                completeLogout();
                            }

                            @Override
                            public void onError(String error) {
                                // Continue with logout even if update fails
                                completeLogout();
                            }
                        });
            } else {
                // If no network or user data, proceed directly to logout
                completeLogout();
            }
        } catch (Exception e) {
            // Handle any unexpected errors during logout
            e.printStackTrace();
            completeLogout();
        }
    }

    private void completeLogout() {
        try {
            // Dismiss loading dialog
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }

            // Sign out from Firebase
            if (firebaseManager != null) {
                firebaseManager.signOut();
            }

            // Clear all cached data
            clearCachedData();

            // Clear any session data
            clearSessionData();

            // Show success message
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to login with proper flags
            redirectToLogin();

        } catch (Exception e) {
            e.printStackTrace();
            // Even if there's an error, still redirect to login
            Toast.makeText(this, "Logout completed", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        }
    }

    private void clearSessionData() {
        // Clear any additional session-related data
        getSharedPreferences("app_session", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Clear any other app-specific preferences
        getSharedPreferences("user_preferences", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImageToFirebase() {
        if (imageUri != null && currentUser != null) {
            loadingDialog.show("Uploading image...");
            // Simulate upload delay
            new android.os.Handler().postDelayed(() -> {
                loadingDialog.dismiss();
                showMessage("Image upload feature needs Firebase Storage setup");
            }, 2000);
        }
    }

    private void redirectToLogin() {
        try {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            // Clear the entire activity stack and start fresh
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            // Finish current activity
            finish();

            // Add transition animation (optional)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: just finish current activity
            finish();
        }
    }

    private void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Retry", v -> loadUserData())
                .show();
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showNetworkError() {
        Snackbar.make(findViewById(android.R.id.content),
                        "No internet connection", Snackbar.LENGTH_LONG)
                .setAction("Retry", v -> loadUserData())
                .show();
    }

    // Cache methods for offline support
    private void cacheUserData(User user) {
        try {
            getSharedPreferences("user_cache", MODE_PRIVATE)
                    .edit()
                    .putString("username", user.getUsername())
                    .putString("email", user.getEmail())
                    .putString("profileImageUrl", user.getProfileImageUrl())
                    .putLong("cached_at", System.currentTimeMillis())
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCachedUserData() {
        try {
            var prefs = getSharedPreferences("user_cache", MODE_PRIVATE);
            String username = prefs.getString("username", null);
            String email = prefs.getString("email", null);
            String profileImageUrl = prefs.getString("profileImageUrl", null);

            if (username != null) {
                usernameText.setText(username + " (Offline)");
            }
            if (email != null) {
                emailText.setText(email);
            }
            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                Glide.with(this)
                        .load(profileImageUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(profileImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCachedData() {
        try {
            // Clear user cache
            getSharedPreferences("user_cache", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Clear any other cached data
            getSharedPreferences("app_cache", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle result from EditProfileActivity (request code 100)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("profile_updated", false)) {
                loadUserData();
                showMessage("Profile updated successfully!");
            }
        }

        // Handle result from LocationActivity (request code 200)
        else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            String address = data.getStringExtra("address");

            showMessage("Location saved: " + address);

            // You can save this to user profile or display it somewhere
            // updateUserLocation(latitude, longitude, address);
        }

        // Handle image picker result (request code 1)
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                uploadImageToFirebase();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to load image");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is still logged in
        if (firebaseManager != null && firebaseManager.getCurrentUser() == null) {
            redirectToLogin();
            return;
        }

        // Refresh data when returning to activity
        if (NetworkUtils.isNetworkAvailable(this)) {
            loadUserData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up loading dialog
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        // Override back button to go to main activity instead of login
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}