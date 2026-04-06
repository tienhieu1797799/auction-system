import java.io.*;
import java.net.*;
import java.util.*;

public class AuctionServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static List<Auction> auctions = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Auction Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream input = socket.getInputStream();
                out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(input));

                String msg;
                while ((msg = in.readLine()) != null) {
                    processMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }

        private void processMessage(String msg) {
            if (msg.startsWith("BID")) {
                String[] parts = msg.split(" ");
                String auctionId = parts[1];
                double bidAmount = Double.parseDouble(parts[2]);
                handleBid(auctionId, bidAmount);
            }
            broadcast(msg);
        }

        private void handleBid(String auctionId, double bidAmount) {
            // Here we would handle the logic for the bid, such as verifying and updating auction status
            System.out.println("Bid received for auction " + auctionId + ": " + bidAmount);
        }

        private void broadcast(String msg) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(msg);
                }
            }
        }
    }

    private static class Auction {
        private String auctionId;
        private String item;
        private double startingPrice;
        private double highestBid;

        public Auction(String auctionId, String item, double startingPrice) {
            this.auctionId = auctionId;
            this.item = item;
            this.startingPrice = startingPrice;
            this.highestBid = startingPrice;
        }

        // Add getters and additional methods as needed.
    }
}