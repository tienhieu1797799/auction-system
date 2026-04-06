import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private AuctionServer server;

    public ClientHandler(Socket socket, AuctionServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());

            processClientMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void processClientMessages() throws IOException {
        Object message;
        while ((message = input.readObject()) != null) {
            processMessage(message);
        }
    }

    private void processMessage(Object message) {
        if (message instanceof LoginMessage) {
            handleLogin((LoginMessage) message);
        } else if (message instanceof AuctionRequest) {
            handleAuctionRetrieval((AuctionRequest) message);
        } else if (message instanceof BidPlacement) {
            handleBidPlacement((BidPlacement) message);
        }
    }

    private void handleLogin(LoginMessage login) {
        // Handle client login logic here
    }

    private void handleAuctionRetrieval(AuctionRequest auctionRequest) {
        // Handle auction retrieval logic here
    }

    private void handleBidPlacement(BidPlacement bidPlacement) {
        // Handle bid placement logic here
    }

    private void closeConnections() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}