package com.oceanview.model;

public class User {
    private int id;
    private String name;                // full name of the user (optional)
    private String username;
    private String email;               // contact email (optional)
    private String phone;               // contact phone (optional)
    private String password;
    private String role;
    private String status;              // ACTIVE / BLOCKED
    private java.sql.Timestamp createdAt;
    private Integer guestId;

    public User() {
    }

    /**
     * Minimal constructor used by legacy sections of the app (username/password/role).
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Full constructor for records that include personal details and metadata.
     */
    public User(int id,
                String name,
                String username,
                String email,
                String phone,
                String password,
                String role,
                String status,
                java.sql.Timestamp createdAt,
                Integer guestId) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.guestId = guestId;
    }

    public User(int id, String username, String password, String role, Integer guestId) {
        this(id, username, password, role);
        this.guestId = guestId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public Integer getGuestId() {
        return guestId;
    }

    public void setGuestId(Integer guestId) {
        this.guestId = guestId;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
