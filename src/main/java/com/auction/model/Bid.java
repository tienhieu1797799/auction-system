package com.auction.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Bid implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bidId;
    private String auctionId;
    private String userId;
    private String username;
    private double bidAmount;
    private LocalDateTime bidTime;

    public Bid(String bidId, String auctionId, String userId, String username, 
               double bidAmount, LocalDateTime bidTime) {
        this.bidId = bidId;
        this.auctionId = auctionId;
        this.userId = userId;
        this.username = username;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
    }

    public String getBidId() { return bidId; }
    public String getAuctionId() { return auctionId; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public double getBidAmount() { return bidAmount; }
    public LocalDateTime getBidTime() { return bidTime; }

    @Override
    public String toString() {
        return username + " bid $" + bidAmount + " at " + bidTime;
    }
}