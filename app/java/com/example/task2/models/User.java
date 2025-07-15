package com.example.task2.models;

public class User {
    private String uid;
    private String username;
    private String email;
    private String profileImageUrl;
    private String phone;           // ✅ New field
    private String bio;             // ✅ New field
    private long createdAt;
    private long lastLoginAt;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.lastLoginAt = System.currentTimeMillis();
    }

    // Getters and setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getPhone() { return phone; }                    // ✅ Getter
    public void setPhone(String phone) { this.phone = phone; }    // ✅ Setter

    public String getBio() { return bio; }                        // ✅ Getter
    public void setBio(String bio) { this.bio = bio; }            // ✅ Setter

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(long lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
