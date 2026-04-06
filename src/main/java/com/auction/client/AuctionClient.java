package com.auction.client;

import java.io.*;
import java.net.*;

public class AuctionClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public AuctionClient(String serverAddress, int port) throws IOException {
        connect(serverAddress, port);
    }

    private void connect(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void handleLogin(String username, String password) {
        sendMessage("LOGIN " + username + " " + password);
        try {
            String response = receiveMessage();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retrieveAuctions() {
        sendMessage("GET_AUCTIONS");
        try {
            String response = receiveMessage();
            System.out.println("Available Auctions: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placeBid(int auctionId, double bidAmount) {
        sendMessage("BID " + auctionId + " " + bidAmount);
        try {
            String response = receiveMessage();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}