package com.example.task2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private EditText usernameEditText, emailEditText, phoneEditText, bioEditText;
    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private TextInputLayout currentPasswordLayout, newPasswordLayout, confirmPasswordLayout;
    private Button saveButton, cancelButton;
    private Uri selectedImageUri;
    private String selectedImageUrl;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private boolean hasChanges = false;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        initializeFirebase();
        setupClickListeners();
        loadCurrentUserData();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Profile");
        }

        profileImageView = findViewById(R.id.profile_image);
        usernameEditText = findViewById(R.id.username_edit);
        emailEditText = findViewById(R.id.email_edit);
        phoneEditText = findViewById(R.id.phone_edit);
        bioEditText = findViewById(R.id.bio_edit);

        // Password fields
        currentPasswordEditText = findViewById(R.id.current_password_edit);
        newPasswordEditText = findViewById(R.id.new_password_edit);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit);
        currentPasswordLayout = findViewById(R.id.current_password_layout);
        newPasswordLayout = findViewById(R.id.new_password_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);

        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }
        currentUserId = currentUser.getUid();
    }

    private void setupClickListeners() {
        profileImageView.setOnClickListener(v -> showImageOptionsDialog());

        saveButton.setOnClickListener(v -> saveProfile());

        cancelButton.setOnClickListener(v -> {
            if (hasChanges) {
                showUnsavedChangesDialog();
            } else {
                finish();
            }
        });

        setupTextChangeListeners();
    }

    private void setupTextChangeListeners() {
        usernameEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
        emailEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
        phoneEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
        bioEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
        currentPasswordEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
        newPasswordEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
        confirmPasswordEditText.addTextChangedListener(new SimpleTextWatcher(() -> hasChanges = true));
    }

    private void loadCurrentUserData() {
        databaseReference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    usernameEditText.setText(username != null ? username : "");
                    emailEditText.setText(email != null ? email : "");
                    phoneEditText.setText(phone != null ? phone : "");
                    bioEditText.setText(bio != null ? bio : "");

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(EditProfileActivity.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.ic_person_placeholder)
                                .error(R.drawable.ic_person_placeholder)
                                .into(profileImageView);
                    }
                } else {
                    // Load from Firebase Auth if no database entry
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        emailEditText.setText(firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "");
                        usernameEditText.setText(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "");
                    }
                }
                hasChanges = false;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showError("Failed to load profile: " + error.getMessage());
            }
        });
    }

    private void saveProfile() {
        if (!validateInputs()) {
            return;
        }

        // Check if password change is requested
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(currentPassword) || !TextUtils.isEmpty(newPassword)) {
            // Password change requested - validate and change password first
            changePassword(currentPassword, newPassword);
        } else {
            // No password change - just update profile
            updateProfileData();
        }
    }

    private boolean validateInputs() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Clear previous errors
        currentPasswordLayout.setError(null);
        newPasswordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return false;
        }

        if (username.length() < 3) {
            usernameEditText.setError("Username must be at least 3 characters");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            return false;
        }

        // Password validation (only if password change is requested)
        if (!TextUtils.isEmpty(currentPassword) || !TextUtils.isEmpty(newPassword) || !TextUtils.isEmpty(confirmPassword)) {
            if (TextUtils.isEmpty(currentPassword)) {
                currentPasswordLayout.setError("Current password is required");
                return false;
            }

            if (TextUtils.isEmpty(newPassword)) {
                newPasswordLayout.setError("New password is required");
                return false;
            }

            if (newPassword.length() < 6) {
                newPasswordLayout.setError("Password must be at least 6 characters");
                return false;
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmPasswordLayout.setError("Passwords do not match");
                return false;
            }

            if (currentPassword.equals(newPassword)) {
                newPasswordLayout.setError("New password must be different from current password");
                return false;
            }
        }

        return true;
    }

    private void changePassword(String currentPassword, String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            showError("User not authenticated");
            return;
        }

        // Re-authenticate user with current password
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Current password is correct, now update to new password
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        showSuccess("Password updated successfully!");
                                        // Clear password fields
                                        currentPasswordEditText.setText("");
                                        newPasswordEditText.setText("");
                                        confirmPasswordEditText.setText("");
                                        // Now update other profile data
                                        updateProfileData();
                                    } else {
                                        showError("Failed to update password: " + updateTask.getException().getMessage());
                                    }
                                });
                    } else {
                        currentPasswordLayout.setError("Current password is incorrect");
                        showError("Current password is incorrect");
                    }
                });
    }

    private void updateProfileData() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String bio = bioEditText.getText().toString().trim();

        if (selectedImageUri != null) {
            // Upload image first, then update profile
            uploadImageAndUpdateProfile(username, email, phone, bio);
        } else if (selectedImageUrl != null) {
            // Use the URL directly
            updateUserData(username, email, phone, bio, selectedImageUrl);
        } else {
            // Update profile without image
            updateUserData(username, email, phone, bio, null);
        }
    }

    private void uploadImageAndUpdateProfile(String username, String email, String phone, String bio) {
        StorageReference imageRef = storageReference.child(currentUserId + ".jpg");

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateUserData(username, email, phone, bio, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    showError("Failed to upload image: " + e.getMessage());
                });
    }

    private void updateUserData(String username, String email, String phone, String bio, String profileImageUrl) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("username", username);
        userUpdates.put("email", email);
        userUpdates.put("phone", phone);
        userUpdates.put("bio", bio);

        if (profileImageUrl != null) {
            userUpdates.put("profileImageUrl", profileImageUrl);
        }

        databaseReference.child(currentUserId).updateChildren(userUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Also update Firebase Auth email if changed
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && !email.equals(user.getEmail())) {
                            user.updateEmail(email)
                                    .addOnFailureListener(e ->
                                            showError("Profile updated but failed to update email: " + e.getMessage()));
                        }

                        showSuccess("Profile updated successfully!");
                        hasChanges = false;
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        showError("Failed to update profile: " + task.getException().getMessage());
                    }
                });
    }

    private void showImageOptionsDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Change Profile Photo")
                .setMessage("Choose how you want to add your profile photo:")
                .setPositiveButton("Upload from Device", (dialog, which) -> openImagePicker())
                .setNegativeButton("Paste Image URL", (dialog, which) -> showImageUrlDialog())
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showImageUrlDialog() {
        EditText urlEditText = new EditText(this);
        urlEditText.setHint("Enter image URL (e.g., https://example.com/image.jpg)");
        urlEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_URI);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Enter Image URL")
                .setView(urlEditText)
                .setPositiveButton("Load Image", (dialog, which) -> {
                    String imageUrl = urlEditText.getText().toString().trim();
                    if (!imageUrl.isEmpty()) {
                        loadImageFromUrl(imageUrl);
                    } else {
                        showError("Please enter a valid URL");
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadImageFromUrl(String imageUrl) {
        // Validate URL format
        if (!android.util.Patterns.WEB_URL.matcher(imageUrl).matches()) {
            showError("Please enter a valid URL");
            return;
        }

        // Load image using Glide to test if URL is valid
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_person_placeholder)
                .error(R.drawable.ic_person_placeholder)
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(com.bumptech.glide.load.engine.GlideException e, Object model,
                                                com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                                boolean isFirstResource) {
                        showError("Failed to load image from URL. Please check the URL and try again.");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model,
                                                   com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                                   com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        // Image loaded successfully
                        selectedImageUri = null; // Clear file URI since we're using URL
                        selectedImageUrl = imageUrl; // Store the URL
                        hasChanges = true;
                        showMessage("Image loaded successfully. Save profile to update.");
                        return false;
                    }
                })
                .into(profileImageView);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            selectedImageUrl = null; // Clear URL since we're using file
            hasChanges = true;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                profileImageView.setImageBitmap(bitmap);
                showMessage("Image selected. Save profile to update.");
            } catch (IOException e) {
                showError("Failed to load image");
            }
        }
    }

    private void showUnsavedChangesDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Are you sure you want to leave?")
                .setPositiveButton("Leave", (dialog, which) -> finish())
                .setNegativeButton("Stay", null)
                .show();
    }

    private void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (hasChanges) showUnsavedChangesDialog();
            else finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (hasChanges) showUnsavedChangesDialog();
        else super.onBackPressed();
    }

    private static class SimpleTextWatcher implements android.text.TextWatcher {
        private final Runnable callback;

        public SimpleTextWatcher(Runnable callback) {
            this.callback = callback;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(android.text.Editable s) { callback.run(); }
    }
}