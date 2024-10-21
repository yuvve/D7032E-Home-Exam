package networking.impl;

import exceptions.NetworkingException;
import networking.ControlProtocol;
import networking.IClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class Client implements IClient {
    private boolean isConnected;
    private boolean isServerListening;
    public BufferedReader inFromServer;
    public BufferedWriter outToServer;
    private Socket socket;

    public Client() {
        isConnected = false;
        isServerListening = false;
    }

    @Override
    public void connectToServer(String ip, int port) throws NetworkingException {
        if (isConnected) {
            System.out.println("Already connected to server");
            return;
        }

        try {
            socket = new Socket(ip, port);
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            isConnected = true;
            System.out.println("Connected to server");
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            closeConnection();
        }
    }

    @Override
    public void sendMessage(String msg) throws NetworkingException{
        isServerListening = false;

        if (!isConnected) {
            throw new NetworkingException("Not connected to server");
        }

        try {
            outToServer.write(msg);
            outToServer.newLine();
            outToServer.flush();
        } catch (IOException e) {
            throw new NetworkingException("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public String receiveMessage() throws NetworkingException{
        if (!isConnected) {
            throw new NetworkingException("Not connected to server");
        }
        try {
            String fromServer = inFromServer.readLine();
            if (Objects.equals(fromServer, ControlProtocol.TRANSMISSION_OVER.getValue())) {
                isServerListening = true;
                return "Your input: ";
            }
            if (Objects.equals(fromServer, ControlProtocol.GAME_OVER.getValue())) {
                closeConnection();
                return "Game over, closing connection!";
            }
            return fromServer;
        } catch (IOException e) {
            throw new NetworkingException("Error receiving message: " + e.getMessage());
        }
    }

    @Override
    public boolean serverWaitingForInput() {
        return isServerListening;
    }

    @Override
    public boolean isConnectionAlive() {
        return isConnected;
    }

    private void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                if (inFromServer != null) inFromServer.close();
                if (outToServer != null) outToServer.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            } finally {
                isConnected = false;
            }
        }
    }
}
