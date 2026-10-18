package com.example.furniture.models;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String expiresAt;
    private User user;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public User getUser() {
        return user;
    }

    public static class User {
        private int userId;
        private String email;
        private String fullName;
        private String phone;
        private int roleId;
        private String gender;
        private String address;
        private String avatar;
        private String createdAt;

        public int getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPhone() {
            return phone;
        }

        public int getRoleId() {
            return roleId;
        }

        public String getGender() {
            return gender;
        }

        public String getAddress() {
            return address;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
