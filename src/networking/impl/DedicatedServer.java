package networking.impl;

import exceptions.NetworkingException;
import networking.ControlProtocol;
import networking.IServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A dedicated server (it can only host a game, not participate in it).
 */
public class DedicatedServer implements IServer {
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

    protected ArrayList<Client> clients;
    private boolean isStarted;
    private ServerSocket serverSocket;

    public DedicatedServer() {
        clients = new ArrayList<>();
        isStarted = false;
    }

    @Override
    public void startServer(int port) throws NetworkingException {
        if (isStarted) {
            System.out.println("Server already started");
            return;
        }

        try {
            serverSocket = new ServerSocket(port);
            isStarted = true;
            System.out.println("Server started on port: " + port);
        } catch (IOException e) {
            closeServerSocket();
            throw new NetworkingException("Failed to start server: " + e.getMessage());
        }
    }

    @Override
    public void stopServer() {
        if (!isStarted) {
            System.out.println("Server not started");
            return;
        }
        broadcast(ControlProtocol.GAME_OVER.getValue());

        try {
            closeServerSocket();
            clients.forEach(c -> {
                try {
                    c.in.close();
                    c.out.close();
                    c.socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client: " + e.getMessage());
                }
            });
            clients.clear();
            System.out.println("Server stopped");
        } catch (NetworkingException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    @Override
    public int acceptClient() throws NetworkingException{
        int clientId = clients.size();
        return acceptClient(clientId);
    }

    @Override
    public void broadcast(String msg) throws NetworkingException{
        clients.forEach(c -> sendMsg(c, msg));
    }

    @Override
    public void sendMsg(int clientId, String msg) throws NetworkingException{
        Client client = getClientFromId(clientId);
        if (client != null) {
            sendMsg(client, msg);
        } else {
            System.err.println("Client " + clientId + " not found.");
        }
    }

    @Override
    public String getClientInput(int clientId) throws NetworkingException{
        sendMsg(clientId, ControlProtocol.TRANSMISSION_OVER.getValue());
        Client client = getClientFromId(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Invalid client ID: " + clientId);
        }

        try {
            return client.in.readLine();  // Read a single line from the client
        } catch (IOException e) {
            throw new NetworkingException("Error reading client input: " + e.getMessage());
        }
    }

    @Override
    public void removeClient(int clientId) throws NetworkingException{
        Client client = getClientFromId(clientId);
        if (client != null) {
            try {
                client.in.close();
                client.out.close();
                client.socket.close();
                clients.remove(client);
                System.out.println("Client " + clientId + " disconnected.");
            } catch (IOException e) {
                throw new NetworkingException("Error removing client: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Client " + clientId + " not found.");
        }
    }

    @Override
    public int getClientCount() {
        return clients.size();
    }

    protected int acceptClient(int clientId) throws NetworkingException{
        try {
            Socket connectionSocket = serverSocket.accept();
            Client client = new Client(clientId, connectionSocket);
            clients.add(client);
            System.out.println("Player " + clientId + " connected");
        } catch (IOException e) {
            throw new NetworkingException("Error accepting client: " + e.getMessage());
        }
        return clientId;
    }

    private void sendMsg(Client client, String msg) throws NetworkingException {
        try {
            client.out.write(msg);
            client.out.newLine();
            client.out.flush();
        } catch (IOException e) {
            throw new NetworkingException("Error sending message to client: " + e.getMessage());
        }
    }

    private Client getClientFromId(int clientId) {
        return clients.stream()
                .filter(c -> c.id == clientId)
                .findFirst()
                .orElse(null);
    }

    private void closeServerSocket() throws NetworkingException{
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new NetworkingException("Error closing server socket: " + e.getMessage());
        }
    }
}
