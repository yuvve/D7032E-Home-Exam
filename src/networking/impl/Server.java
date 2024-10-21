package networking.impl;

import networking.IServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements IServer {
    private class Client {
        private int id;
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        private Client(int id, Socket socket) throws IOException {
            this.id = id;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
    }

    private ArrayList<Client> clients = new ArrayList<>();  // Initialize the client list
    private boolean isStarted = false;
    private ServerSocket serverSocket;

    @Override
    public void startServer(int port) {
        if (isStarted) {
            System.out.println("Server already started");
            return;
        }

        try {
            serverSocket = new ServerSocket(port);
            isStarted = true;
            System.out.println("Server started on port: " + port);
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            closeServerSocket();
        }
    }

    @Override
    public int acceptClient() {
        int clientId = clients.size();
        try {
            Socket connectionSocket = serverSocket.accept();
            Client client = new Client(clientId, connectionSocket);
            clients.add(client);
            System.out.println("Player " + clientId + " connected");
        } catch (IOException e) {
            throw new RuntimeException("Error accepting client: " + e.getMessage(), e);
        }
        return clientId;
    }

    @Override
    public void broadcast(String msg) {
        clients.forEach(c -> sendMsg(c, msg));
    }

    @Override
    public void sendMsg(int clientId, String msg) {
        Client client = getClientFromId(clientId);
        if (client != null) {
            sendMsg(client, msg);
        } else {
            System.err.println("Client " + clientId + " not found.");
        }
    }

    @Override
    public String getClientInput(int clientId) {
        Client client = getClientFromId(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Invalid client ID: " + clientId);
        }

        try {
            return client.in.readLine();  // Read a single line from the client
        } catch (IOException e) {
            throw new RuntimeException("Error reading client input: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeClient(int clientId) {
        Client client = getClientFromId(clientId);
        if (client != null) {
            try {
                client.in.close();
                client.out.close();
                client.socket.close();
                clients.remove(client);
                System.out.println("Client " + clientId + " disconnected.");
            } catch (IOException e) {
                throw new RuntimeException("Error removing client: " + e.getMessage(), e);
            }
        } else {
            System.err.println("Client " + clientId + " not found.");
        }
    }

    @Override
    public int getClientCount() {
        return clients.size();
    }

    private void sendMsg(Client client, String msg) {
        try {
            client.out.write(msg);
            client.out.newLine();
            client.out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending message to client " + client.id + ": " + e.getMessage(), e);
        }
    }

    private Client getClientFromId(int clientId) {
        return clients.stream()
                .filter(c -> c.id == clientId)
                .findFirst()
                .orElse(null);
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }
}
