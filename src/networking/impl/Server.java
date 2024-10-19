package networking.impl;

import networking.ControlProtocol;
import networking.IServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements IServer {
    private class Client {
        private Client(int id, Socket socket, DataInputStream in, DataOutputStream out) {
            this.id = id;
            this.socket = socket;
            this.in = in;
            this.out = out;
        }
        private int id;
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
    }

    private ArrayList<Client> clients;
    private boolean isStarted = false;
    private ServerSocket serverSocket;

    @Override
    public void startServer(int port) {
        if (isStarted) {
            System.out.println("Server already started");
            return;
        }
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            isStarted = true;
            System.out.println("Server started");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (!isStarted) {
                try {
                    if (serverSocket != null) serverSocket.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    @Override
    public int acceptClient() {
        int clientId = clients.size();
        Socket connectionSocket = null;
        try {
            connectionSocket = serverSocket.accept();
            Client client = new Client(
                    clientId, connectionSocket,
                    new DataInputStream(connectionSocket.getInputStream()),
                    new DataOutputStream(connectionSocket.getOutputStream()));
            clients.add(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        sendMsg(client, msg);
    }

    @Override
    public String getClientInput(int clientId) {
        Client client = getClientFromId(clientId);
        StringBuilder msg = new StringBuilder();
        try {
            while (true) {
                String line = client.in.readUTF();
                if (line.equals(ControlProtocol.TRANSMISSION_OVER.getValue())) {
                    break;
                }
                msg.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return msg.toString();
    }

    @Override
    public void removeClient(int clientId) {
        Client client = getClientFromId(clientId);
        try {
            client.in.close();
            client.out.close();
            client.socket.close();
            clients.remove(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getClientCount() {
        return clients.size();
    }

    private void sendMsg(Client client, String msg) {
        try {
            client.out.writeUTF(msg);
            client.out.writeUTF(ControlProtocol.TRANSMISSION_OVER.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Client getClientFromId(int clientId) {
        return clients.stream()
                .filter(c -> c.id == clientId)
                .findFirst()
                .orElse(null);
    }
}
