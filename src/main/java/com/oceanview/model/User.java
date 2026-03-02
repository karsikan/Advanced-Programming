package com.oceanview.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private Integer guestId; // Null for Admin/Staff

    public User() {
    }

    public User(int id, String username, String password, String role) {
        this(id, username, password, role, null);
    }

    public User(int id, String username, String password, String role, Integer guestId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
