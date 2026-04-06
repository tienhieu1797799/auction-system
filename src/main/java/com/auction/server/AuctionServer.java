package com.auction.server;

import com.auction.model.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuctionServer {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private List<ClientHandler> connectedClients = new CopyOnWriteArrayList<>();
    private Map<String, Auction> auctions = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private List<Bid> bidHistory = new ArrayList<>();

    public AuctionServer() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        users.put("user1", new User("user1", "Alice", "pass123", 5000));
        users.put("user2", new User("user2", "Bob", "pass123", 3000));
        users.put("user3", new User("user3", "Charlie", "pass123", 4000));

        LocalDateTime now = LocalDateTime.now();
        auctions.put("auction1", new Auction("auction1", "Laptop", 
            "Dell XPS 13", 500, now, now.plusHours(2)));
        auctions.put("auction2", new Auction("auction2", "Phone", 
            "iPhone 15", 800, now, now.plusHours(3)));
        auctions.put("auction3", new Auction("auction3", "Headphones", 
            "Sony WH-1000XM5", 200, now, now.plusHours(1)));
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("🟢 Auction Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("📱 New client connected: " + clientSocket.getInetAddress());
                
                ClientHandler handler = new ClientHandler(clientSocket, this);
                connectedClients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean placeBid(String auctionId, String userId, 
                                        String username, double bidAmount) {
        Auction auction = auctions.get(auctionId);
        User user = users.get(userId);

        if (auction == null || !auction.isActive()) {
            return false;
        }

        if (bidAmount <= auction.getCurrentPrice() || bidAmount > user.getBalance()) {
            return false;
        }

        auction.setCurrentPrice(bidAmount);
        auction.setHighestBidder(username);

        String bidId = "bid_" + System.currentTimeMillis();
        Bid bid = new Bid(bidId, auctionId, userId, username, bidAmount, LocalDateTime.now());
        bidHistory.add(bid);

        broadcastAuctionUpdate(auction);

        System.out.println("✅ Bid placed: " + username + " bid $" + bidAmount + " on " + auction.getItemName());
        return true;
    }

    public void broadcastAuctionUpdate(Auction auction) {
        Message msg = new Message(Message.MessageType.AUCTION_UPDATE, 
            "Auction updated", auction);
        
        for (ClientHandler client : connectedClients) {
            client.sendMessage(msg);
        }
    }

    public Map<String, Auction> getAuctions() {
        return new HashMap<>(auctions);
    }

    public User authenticateUser(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void removeClient(ClientHandler handler) {
        connectedClients.remove(handler);
        System.out.println("❌ Client disconnected");
    }

    public static void main(String[] args) {
        new AuctionServer().start();
    }
}