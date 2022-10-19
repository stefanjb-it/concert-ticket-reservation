package server;

import data.Shared;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 15500;
    private final String[] actor = {"The Rolling Stones", "AC/DC", "StMiT", "Helene Fischer", "Ed Sheeran", "Logic"};
    private final int[] cards = {4, 2, 0, 72341, 0, 2};
    private final int port;
    private final Shared shared;

    public Server(int port) {
        this.port = port;
        this.shared = new Shared();
    }

    public static void main(String[] args) {
        new Server(PORT).start();
    }

    public void start() {
        try {
            ServerSocket server = new ServerSocket(port);
            for (int i = 0; i < actor.length; i++) {
                shared.add(actor[i], cards[i]);
            }
            while (true) {
                System.out.println("Waiting ...");
                Socket socket = server.accept();
                System.out.printf("Connected %s \n", socket.getRemoteSocketAddress());
                new ServerThread(socket, shared).start();
            }
        } catch (IOException e) {
            System.err.printf("ERROR: %s", e.getMessage());
        }
    }

}
