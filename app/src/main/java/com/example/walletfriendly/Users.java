package com.example.walletfriendly;

public class Users {
    private int id;
    private String username;
    private String email;
    private String password;

    public Users(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter for the ID field
    public int getId() {
        return id;
    }

    // Setter for the ID field
    public void setId(int id) {
        this.id = id;
    }

    // Getter for the username field
    public String getUsername() {
        return username;
    }

    // Setter for the username field
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for the email field
    public String getEmail() {
        return email;
    }

    // Setter for the email field
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for the password field
    public String getPassword() {
        return password;
    }

    // Setter for the password field
    public void setPassword(String password) {
        this.password = password;
    }
}
