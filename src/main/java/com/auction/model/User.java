package com.auction.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String username;
    private String password;
    private double balance;

    public User(String userId, String username, String password, double balance) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "User{\" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}