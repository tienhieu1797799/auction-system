package com.auction.server;

import com.auction.model.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;
    private AuctionServer server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private User currentUser;

    public ClientHandler(Socket socket, AuctionServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());

            Message message;
            while ((message = (Message) input.readObject()) != null) {
                handleMessage(message);
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            closeConnection();
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case LOGIN:
                handleLogin((String[]) message.getData());
                break;
            case GET_AUCTIONS:
                handleGetAuctions();
                break;
            case PLACE_BID:
                handlePlaceBid((String[]) message.getData());
                break;
            default:
                break;
        }
    }

    private void handleLogin(String[] credentials) {
        String username = credentials[0];
        String password = credentials[1];
        
        User user = server.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            Message response = new Message(Message.MessageType.SUCCESS, 
                "Login successful", user);
            sendMessage(response);
        } else {
            Message response = new Message(Message.MessageType.ERROR, 
                "Invalid credentials", null);
            sendMessage(response);
        }
    }

    private void handleGetAuctions() {
        Map<String, Auction> auctions = server.getAuctions();
        Message response = new Message(Message.MessageType.SUCCESS, 
            "Auctions retrieved", auctions);
        sendMessage(response);
    }

    private void handlePlaceBid(String[] bidData) {
        String auctionId = bidData[0];
        double bidAmount = Double.parseDouble(bidData[1]);

        boolean success = server.placeBid(auctionId, currentUser.getUserId(), 
            currentUser.getUsername(), bidAmount);

        Message response;
        if (success) {
            response = new Message(Message.MessageType.SUCCESS, 
                "Bid placed successfully", null);
        } else {
            response = new Message(Message.MessageType.ERROR, 
                "Bid failed. Amount too low or insufficient balance", null);
        }
        sendMessage(response);
    }

    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}